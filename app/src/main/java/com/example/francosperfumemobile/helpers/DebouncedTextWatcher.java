package com.example.francosperfumemobile.helpers;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Waits until the user stops typing before searching, so one search runs
 * instead of one per key press (website: the search bar's debounce).
 */
public class DebouncedTextWatcher implements TextWatcher {

    public interface OnQueryChanged {
        void onQueryChanged(String query);
    }

    public static final long DEFAULT_DELAY_MS = 400;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final long delayMs;
    private final OnQueryChanged listener;
    private String lastQuery = null;
    private Runnable pending;

    public DebouncedTextWatcher(OnQueryChanged listener) {
        this(DEFAULT_DELAY_MS, listener);
    }

    public DebouncedTextWatcher(long delayMs, OnQueryChanged listener) {
        this.delayMs = delayMs;
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        final String query = s.toString().trim();
        if (pending != null) handler.removeCallbacks(pending);
        pending = () -> {
            if (query.equals(lastQuery)) return;
            lastQuery = query;
            listener.onQueryChanged(query);
        };
        handler.postDelayed(pending, delayMs);
    }

    /** Call from onDestroyView so a late search doesn't run on a closed screen. */
    public void cancel() {
        if (pending != null) handler.removeCallbacks(pending);
    }

    /** Lets the watcher know the current query so the first identical text doesn't search again. */
    public void setLastQuery(String query) {
        this.lastQuery = query;
    }

    /** TextWatcher with only afterTextChanged, to keep listeners short. */
    public abstract static class Simple implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}
