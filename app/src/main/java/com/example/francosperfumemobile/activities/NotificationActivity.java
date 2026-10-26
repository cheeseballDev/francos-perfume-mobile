package com.example.francosperfumemobile.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.NotificationAdapter;
import com.example.francosperfumemobile.backend.dtos.NotificationDTO;
import com.example.francosperfumemobile.backend.repository.NotificationRepository;
import com.example.francosperfumemobile.backend.responses.NotificationListResponse;
import com.example.francosperfumemobile.helpers.ButtonLoading;
import com.example.francosperfumemobile.helpers.InsetsHelper;
import com.example.francosperfumemobile.helpers.ListScreenState;
import com.example.francosperfumemobile.helpers.NotificationRouter;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonElement;

import okhttp3.ResponseBody;

/**
 * Notifications list (website: NotificationDropdown): unread first look, mark one as read by
 * opening it, "Mark all as read", and tapping opens the request / delivery it is about.
 */
public class NotificationActivity extends BaseActivity {

    private NotificationAdapter adapter;
    private ListScreenState listState;
    private TextView unreadSummary;
    private MaterialButton buttonMarkAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinishing()) return;
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);

        View root = findViewById(R.id.notifications_root);
        View toolbar = findViewById(R.id.toolbar_notifications);
        InsetsHelper.applyEdgeToEdge(root, toolbar, null, false);

        findViewById(R.id.button_back).setOnClickListener(v -> finish());
        unreadSummary = findViewById(R.id.text_view_unread_summary);
        buttonMarkAll = findViewById(R.id.button_mark_all_read);
        buttonMarkAll.setOnClickListener(v -> markAllRead());

        RecyclerView recyclerView = findViewById(R.id.recycler_view_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this::open);
        recyclerView.setAdapter(adapter);

        SwipeRefreshLayout swipe = findViewById(R.id.swipe_refresh_notifications);
        listState = new ListScreenState(swipe, findViewById(R.id.progress_bar_notifications),
                findViewById(R.id.layout_empty_notifications), recyclerView);
        listState.setEmptyIcon(R.drawable.ic_bell);
        listState.setOnRefresh(this::load);

        load();
    }

    @Override
    protected void refreshUnreadBadge() {
        // This screen has no bell; the list itself shows what is unread
    }

    private void load() {
        listState.showLoading(adapter.getItemCount() > 0);
        new NotificationRepository(this).getNotifications().enqueue(new SafeCallback<JsonElement>(this) {
            @Override
            public void onSuccess(@Nullable JsonElement body) {
                NotificationListResponse response = NotificationListResponse.from(body);
                adapter.setItems(response.getNotifications());
                updateSummary();
                listState.showResult(adapter.getItemCount(), false);
                if (adapter.getItemCount() == 0) {
                    ((TextView) findViewById(R.id.layout_empty_notifications).findViewById(R.id.text_view_empty_title))
                            .setText(R.string.notifications_empty_title);
                    ((TextView) findViewById(R.id.layout_empty_notifications).findViewById(R.id.text_view_empty_message))
                            .setText(R.string.notifications_empty_message);
                }
            }

            @Override
            public void onError(@NonNull String message, int code) {
                if (code == 401) {
                    listState.stopLoading();
                    return;
                }
                if (!listState.showError(message, adapter.getItemCount() > 0, NotificationActivity.this::load)) {
                    super.onError(message, code);
                }
            }
        });
    }

    private void updateSummary() {
        int unread = adapter.unreadCount();
        unreadSummary.setText(unread == 0 ? getString(R.string.all_caught_up)
                : getResources().getQuantityString(R.plurals.unread_count, unread, unread));
        if (!ButtonLoading.isLoading(buttonMarkAll)) buttonMarkAll.setEnabled(unread > 0);
    }

    private void open(NotificationDTO notification, int position) {
        if (!notification.isRead()) {
            adapter.markRead(position);
            updateSummary();
            // Mark read on the server; the screen does not wait for it
            new NotificationRepository(this).markRead(notification.getNotificationId()).enqueue(new SafeCallback<ResponseBody>(this) {
                @Override
                public void onSuccess(@Nullable ResponseBody body) {
                }

                @Override
                public void onError(@NonNull String message, int code) {
                    // Not worth interrupting the user; it stays unread on the server
                }
            });
        }
        startActivity(NotificationRouter.intentFor(this, notification.getType(), notification.getReferenceId()));
    }

    private void markAllRead() {
        if (adapter.unreadCount() == 0) return;
        ButtonLoading.start(buttonMarkAll, null);
        new NotificationRepository(this).markAllRead().enqueue(new SafeCallback<ResponseBody>(this) {
            @Override
            public void onFinish() {
                ButtonLoading.stop(buttonMarkAll);
            }

            @Override
            public void onSuccess(@Nullable ResponseBody body) {
                adapter.markAllRead();
                updateSummary();
            }
        });
    }
}
