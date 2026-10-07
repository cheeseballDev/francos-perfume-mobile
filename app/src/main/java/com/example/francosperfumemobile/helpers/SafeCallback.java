package com.example.francosperfumemobile.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class SafeCallback<T> implements Callback<T> {

    private Fragment fragment;
    private Activity activity;
    private final ProgressBar progressBar;

    // Constructor for Fragments
    public SafeCallback(@NonNull Fragment fragment, @Nullable ProgressBar progressBar) {
        this.fragment = fragment;
        this.progressBar = progressBar;
    }

    // Constructor for Activities
    public SafeCallback(@NonNull Activity activity, @Nullable ProgressBar progressBar) {
        this.activity = activity;
        this.progressBar = progressBar;
    }

    // Overload without ProgressBar for Activities
    public SafeCallback(@NonNull Activity activity) {
        this(activity, null);
    }

    // Overload without ProgressBar for Fragments
    public SafeCallback(@NonNull Fragment fragment) {
        this(fragment, null);
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (!isContextSafe()) return;

        hideProgressBar();

        if (response.isSuccessful() && response.body() != null) {
            onSuccess(response.body());
        } else {
            String errorMsg = "HTTP " + response.code();
            try {
                if (response.errorBody() != null) {
                    errorMsg += ": " + response.errorBody().string();
                }
            } catch (Exception ignored) {}

            showToast("Operation failed (" + errorMsg + ")");
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        if (!isContextSafe()) return;

        hideProgressBar();
        showToast("Network error: " + t.getMessage());
    }

    public abstract void onSuccess(T responseBody);

    private boolean isContextSafe() {
        if (fragment != null) {
            return fragment.isAdded() && fragment.getContext() != null;
        }
        if (activity != null) {
            return !activity.isFinishing() && !activity.isDestroyed();
        }
        return false;
    }

    private Context getContext() {
        if (fragment != null) return fragment.getContext();
        return activity;
    }

    private void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showToast(String message) {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}