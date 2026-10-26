package com.example.francosperfumemobile.helpers;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;

/**
 * Page-by-page navigation for the list screens (Inventory, Requests, Deliveries).
 * Each page replaces the list, like the website's table pagination, so Previous never duplicates rows.
 *
 * Usage:
 *   PaginationHelper pager = new PaginationHelper(prev, next, label, page -> load(page));
 *   pager.onPageLoaded(page, totalPages, totalItems);   // after each response
 *   pager.setLoading(true/false);                        // ignores taps while loading
 */
public class PaginationHelper {

    public interface OnPageRequested {
        void onPageRequested(int page);
    }

    private final View buttonPrevious;
    private final View buttonNext;
    private final TextView label;
    private final OnPageRequested listener;

    private int currentPage = 1;
    private int totalPages = 1;
    private boolean loading = false;
    private boolean loadedOnce = false;

    public PaginationHelper(@Nullable View buttonPrevious, @Nullable View buttonNext,
                            @Nullable TextView label, OnPageRequested listener) {
        this.buttonPrevious = buttonPrevious;
        this.buttonNext = buttonNext;
        this.label = label;
        this.listener = listener;

        if (buttonPrevious != null) {
            buttonPrevious.setOnClickListener(v -> {
                if (!loading && currentPage > 1) listener.onPageRequested(currentPage - 1);
            });
        }
        if (buttonNext != null) {
            buttonNext.setOnClickListener(v -> {
                if (!loading && currentPage < totalPages) listener.onPageRequested(currentPage + 1);
            });
        }
        refresh(0);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        updateButtons();
    }

    /**
     * @param page       the page that was just loaded (1-based)
     * @param pages      total pages from the backend (0 or less = work it out from totalItems/pageSize)
     * @param totalItems total rows from the backend
     * @param pageSize   rows per page
     */
    public void onPageLoaded(int page, int pages, int totalItems, int pageSize) {
        this.currentPage = Math.max(1, page);
        if (pages <= 0) {
            pages = pageSize > 0 ? (int) Math.ceil(totalItems / (double) pageSize) : 1;
        }
        this.totalPages = Math.max(1, pages);
        this.loading = false;
        this.loadedOnce = true;
        refresh(totalItems);
    }

    private void refresh(int totalItems) {
        if (label != null) {
            if (!loadedOnce) {
                label.setText("");
            } else if (totalItems <= 0) {
                label.setText("No results");
            } else {
                label.setText(String.format(Locale.getDefault(), "Page %d of %d · %d %s",
                        currentPage, totalPages, totalItems, totalItems == 1 ? "result" : "results"));
            }
        }
        updateButtons();
    }

    private void updateButtons() {
        if (buttonPrevious != null) {
            buttonPrevious.setEnabled(!loading && currentPage > 1);
            buttonPrevious.setAlpha(buttonPrevious.isEnabled() ? 1f : 0.35f);
        }
        if (buttonNext != null) {
            buttonNext.setEnabled(!loading && currentPage < totalPages);
            buttonNext.setAlpha(buttonNext.isEnabled() ? 1f : 0.35f);
        }
    }
}
