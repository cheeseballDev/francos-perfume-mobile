package com.example.francosperfumemobile.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Retrofit callback that
 *  - does nothing if the screen is already closed,
 *  - hides the given loading view,
 *  - shows the backend's message (not raw JSON) when a call fails.
 *
 * Override onError(...) to show the error somewhere else (e.g. under a text field),
 * and onFinish() to re-enable buttons whatever the outcome.
 * A 401 is handled globally by RetrofitClient/SessionEvents, so no toast is shown for it.
 */
public abstract class SafeCallback<T> implements Callback<T> {

    private Fragment fragment;
    private Activity activity;
    private final View progressView;

    public SafeCallback(@NonNull Fragment fragment, @Nullable View progressView) {
        this.fragment = fragment;
        this.progressView = progressView;
    }

    public SafeCallback(@NonNull Activity activity, @Nullable View progressView) {
        this.activity = activity;
        this.progressView = progressView;
    }

    public SafeCallback(@NonNull Activity activity) {
        this(activity, null);
    }

    public SafeCallback(@NonNull Fragment fragment) {
        this(fragment, null);
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (!isContextSafe()) return;

        hideProgress();
        onFinish();

        if (response.isSuccessful()) {
            // body is null for 204 / Call<Void>; implementations must allow that
            onSuccess(response.body());
            return;
        }
        onError(ApiErrorParser.fromResponse(response), response.code());
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        if (call.isCanceled() || !isContextSafe()) return;

        hideProgress();
        onFinish();
        onError(ApiErrorParser.fromThrowable(t), -1);
    }

    /** Called on a 2xx response. The body can be null (204 / Call&lt;Void&gt;). */
    public abstract void onSuccess(@Nullable T responseBody);

    /** Called on a failed response or network error. Default: show a toast. */
    public void onError(@NonNull String message, int code) {
        if (code == 401) return; // the app is already sending the user to the login screen
        showToast(message);
    }

    /** Called before onSuccess/onError, e.g. to re-enable a button. */
    public void onFinish() {
    }

    protected boolean isContextSafe() {
        if (fragment != null) {
            return fragment.isAdded() && fragment.getContext() != null;
        }
        if (activity != null) {
            return !activity.isFinishing() && !activity.isDestroyed();
        }
        return false;
    }

    @Nullable
    protected Context getContext() {
        if (fragment != null) return fragment.getContext();
        return activity;
    }

    private void hideProgress() {
        if (progressView != null) {
            progressView.setVisibility(View.GONE);
        }
    }

    protected void showToast(String message) {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
