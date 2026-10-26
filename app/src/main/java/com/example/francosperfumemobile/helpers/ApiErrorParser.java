package com.example.francosperfumemobile.helpers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import retrofit2.Response;

/**
 * Turns a failed response into one readable sentence.
 * Same idea as the website's getAPIError(): use the backend's "message" when there is one.
 *
 * Handles:
 *  - ServiceResult JSON          {"success": false, "message": "..."}
 *  - ASP.NET validation errors   {"title": "...", "errors": {"Email": ["..."]}}
 *  - plain text bodies           "Invalid email or password"
 *  - network problems            no internet, timeouts
 */
public final class ApiErrorParser {

    public static final String DEFAULT_ERROR = "Something went wrong. Please try again.";

    private ApiErrorParser() {
    }

    @NonNull
    public static String fromResponse(@Nullable Response<?> response) {
        return fromResponse(response, DEFAULT_ERROR);
    }

    @NonNull
    public static String fromResponse(@Nullable Response<?> response, @NonNull String fallback) {
        if (response == null) return fallback;
        String raw = null;
        try {
            if (response.errorBody() != null) raw = response.errorBody().string();
        } catch (IOException ignored) {
        }
        return fromBody(raw, response.code(), fallback);
    }

    @NonNull
    public static String fromBody(@Nullable String raw, int code, @NonNull String fallback) {
        String message = extractMessage(raw);
        if (message != null) return message;
        return forStatusCode(code, fallback);
    }

    @NonNull
    public static String fromThrowable(@Nullable Throwable t) {
        if (t instanceof SocketTimeoutException) {
            return "The server took too long to respond. Please try again.";
        }
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            return "Can't reach the server. Check your internet connection and try again.";
        }
        if (t instanceof IOException) {
            return "Connection problem. Check your internet connection and try again.";
        }
        return "Unexpected response from the server. Please try again.";
    }

    @Nullable
    private static String extractMessage(@Nullable String raw) {
        if (raw == null) return null;
        String body = raw.trim();
        if (body.isEmpty() || body.startsWith("<")) return null; // empty or an HTML error page

        if (body.startsWith("{") || body.startsWith("[") || body.startsWith("\"")) {
            try {
                return fromJson(JsonParser.parseString(body));
            } catch (Exception ignored) {
                return null;
            }
        }
        // Plain text from the backend, e.g. Unauthorized("Invalid email or password")
        return body.length() <= 300 ? body : null;
    }

    @Nullable
    private static String fromJson(JsonElement element) {
        if (element == null || element.isJsonNull()) return null;
        if (element.isJsonPrimitive()) {
            String text = element.getAsString().trim();
            return text.isEmpty() ? null : text;
        }
        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            return array.size() > 0 ? fromJson(array.get(0)) : null;
        }

        JsonObject object = element.getAsJsonObject();
        for (String key : new String[]{"message", "Message", "error", "detail"}) {
            if (object.has(key) && object.get(key).isJsonPrimitive()) {
                String text = object.get(key).getAsString().trim();
                if (!text.isEmpty()) return text;
            }
        }

        // ASP.NET model validation: take the first field error
        if (object.has("errors") && object.get("errors").isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : object.getAsJsonObject("errors").entrySet()) {
                String text = fromJson(entry.getValue());
                if (text != null) return text;
            }
        }
        if (object.has("title") && object.get("title").isJsonPrimitive()) {
            return object.get("title").getAsString();
        }
        return null;
    }

    @NonNull
    private static String forStatusCode(int code, @NonNull String fallback) {
        if (code == 400) return "Please check your input and try again.";
        if (code == 401) return "Your session has expired. Please log in again.";
        if (code == 403) return "You don't have permission to do that.";
        if (code == 404) return "We couldn't find that. It may have been removed.";
        if (code == 409) return "This was already changed by someone else. Refresh and try again.";
        if (code == 429) return "Too many attempts. Please wait a moment and try again.";
        if (code >= 500) return "The server ran into a problem. Please try again in a moment.";
        return fallback;
    }
}
