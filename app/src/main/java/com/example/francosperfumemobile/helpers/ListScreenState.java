package com.example.francosperfumemobile.helpers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.francosperfumemobile.R;

/**
 * Loading / empty / error states for a list screen, so all three lists behave the same:
 *  - first load: spinner in the middle
 *  - pull to refresh or page change: the pull-down spinner
 *  - nothing found: icon + message (+ "Try again" after an error)
 */
public class ListScreenState {

    private final SwipeRefreshLayout swipeRefresh;
    private final View progress;
    private final View emptyState;
    private final View list;
    private final TextView emptyTitle, emptyMessage;
    private final ImageView emptyIcon;
    private final View retryButton;

    public ListScreenState(SwipeRefreshLayout swipeRefresh, View progress, View emptyState, View list) {
        this.swipeRefresh = swipeRefresh;
        this.progress = progress;
        this.emptyState = emptyState;
        this.list = list;
        this.emptyTitle = emptyState.findViewById(R.id.text_view_empty_title);
        this.emptyMessage = emptyState.findViewById(R.id.text_view_empty_message);
        this.emptyIcon = emptyState.findViewById(R.id.image_view_empty_icon);
        this.retryButton = emptyState.findViewById(R.id.button_empty_retry);

        swipeRefresh.setColorSchemeResources(R.color.fp_foreground);
        swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.fp_card);
    }

    public void setOnRefresh(SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefresh.setOnRefreshListener(listener);
    }

    public void setEmptyIcon(@DrawableRes int icon) {
        emptyIcon.setImageResource(icon);
    }

    /** @param hasItems whether rows are already on screen (then use the small pull-down spinner) */
    public void showLoading(boolean hasItems) {
        emptyState.setVisibility(View.GONE);
        if (hasItems || swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
            progress.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.VISIBLE);
        }
    }

    public void showResult(int itemCount, boolean filtersActive) {
        stopLoading();
        list.setVisibility(View.VISIBLE);
        if (itemCount > 0) {
            emptyState.setVisibility(View.GONE);
            return;
        }
        emptyTitle.setText(R.string.empty_title);
        emptyMessage.setText(filtersActive ? R.string.empty_message_filters : R.string.empty_message_none);
        retryButton.setVisibility(View.GONE);
        emptyState.setVisibility(View.VISIBLE);
    }

    /** Shows the error in place of the list when it is empty; otherwise keeps the rows and returns false. */
    public boolean showError(String message, boolean hasItems, @Nullable Runnable retry) {
        stopLoading();
        if (hasItems) return false;
        emptyTitle.setText(R.string.error_load_title);
        emptyMessage.setText(message);
        retryButton.setVisibility(retry != null ? View.VISIBLE : View.GONE);
        if (retry != null) retryButton.setOnClickListener(v -> retry.run());
        emptyState.setVisibility(View.VISIBLE);
        return true;
    }

    public void stopLoading() {
        swipeRefresh.setRefreshing(false);
        progress.setVisibility(View.GONE);
    }
}
