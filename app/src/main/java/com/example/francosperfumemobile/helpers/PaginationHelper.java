package com.example.francosperfumemobile.helpers;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class PaginationHelper {

    public static void updatePagination(
            int loadedCount,
            int totalCount,
            TextView textViewPagination,
            ImageButton buttonNextPage
    ) {
        if (textViewPagination != null) {
            if (totalCount > 0) {
                String countText = String.format(Locale.getDefault(), "Showing %d of %d items", loadedCount, totalCount);
                textViewPagination.setText(countText);
            } else {
                textViewPagination.setText("No items found");
            }
            textViewPagination.setVisibility(View.VISIBLE);
        }

        if (buttonNextPage != null) {
            buttonNextPage.setVisibility(loadedCount >= totalCount || loadedCount == 0 ? View.GONE : View.VISIBLE);
        }
    }

    public static void setLoadingState(
            boolean isInitialFetch,
            boolean isLoading,
            ProgressBar progressBarMain,
            ProgressBar progressBarPagination,
            ImageButton buttonNextPage
    ) {
        if (isInitialFetch) {
            if (progressBarMain != null) {
                progressBarMain.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        } else {
            if (progressBarPagination != null) {
                progressBarPagination.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
            if (buttonNextPage != null) {
                buttonNextPage.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        }
    }

    public static void setLoadingState(
            boolean isInitialFetch,
            boolean isLoading,
            ProgressBar progressBar
    ) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }
}