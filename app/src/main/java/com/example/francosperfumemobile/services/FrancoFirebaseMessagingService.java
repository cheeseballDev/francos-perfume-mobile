package com.example.francosperfumemobile.services;

import androidx.annotation.NonNull;

import com.example.francosperfumemobile.helpers.FcmTokenHelper;
import com.example.francosperfumemobile.helpers.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Receives push messages from Firebase.
 *
 * The backend should send a data payload like:
 *   { "title": "...", "body": "...", "type": "REQUEST_APPROVED", "referenceId": "12" }
 * "type" containing REQUEST / DELIVERY / INVENTORY decides which screen opens when tapped
 * (same rules as the website's notification list).
 */
public class FrancoFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Firebase rotated the token: tell the backend, if someone is logged in
        FcmTokenHelper.registerToken(getApplicationContext(), token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Map<String, String> data = message.getData();

        String title = data.get("title");
        String body = data.get("body");
        if (body == null) body = data.get("message");
        if (message.getNotification() != null) {
            if (title == null) title = message.getNotification().getTitle();
            if (body == null) body = message.getNotification().getBody();
        }
        if (body == null) body = "";

        String type = data.get("type");
        String referenceId = data.get("referenceId");
        if (referenceId == null) referenceId = data.get("reference_id");

        NotificationHelper.show(getApplicationContext(), title, body, type, referenceId);
    }
}
