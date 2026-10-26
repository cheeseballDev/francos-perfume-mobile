package com.example.francosperfumemobile.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.DeliveryItemsAdapter;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.ReceiveDeliveryDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.ReceiveDeliveryItemDTO;
import com.example.francosperfumemobile.backend.repository.DeliveryRepository;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.CancelDeliveryResponse;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.DeliveryDetailResponse;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.DispatchDeliveryResponse;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.ReceiveDeliveryResponse;
import com.example.francosperfumemobile.helpers.AccessRules;
import com.example.francosperfumemobile.helpers.ButtonLoading;
import com.example.francosperfumemobile.helpers.DateUtils;
import com.example.francosperfumemobile.helpers.DialogHelper;
import com.example.francosperfumemobile.helpers.InsetsHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.example.francosperfumemobile.helpers.StatusStyle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Delivery details (website: DeliveryDetailsPage + DeliveryConfirmationPage).
 *
 * Only the actions that make sense are shown (AccessRules.deliveryActions):
 *  - sending branch, before dispatch:      Mark in transit, Cancel
 *  - receiving branch, while in transit:   the receive form + Confirm delivery
 *  - anything else:                        "No action required"
 * Every action asks for confirmation and disables the buttons while it runs.
 * Receiving sends the expiry date the user picked (it used to be hard-coded to 2026-12-31).
 */
public class DeliveryDetailsActivity extends BaseActivity {

    private static final String EXTRA_DELIVERY_ID = "DELIVERY_ID";
    private static final String STATE_EXPIRY = "expiry";

    private int deliveryId;
    private DisplayDeliveryDetailsDTO details;
    private AccessRules.DeliveryActions actions = null;
    private String expiryIso; // yyyy-MM-ddT12:00:00Z
    private Calendar expiryCalendar;

    private View topBar, scrollContent, bottomBar, layoutInboundActions, layoutOutboundActions, receiveCard, emptyState, progress;
    private SwipeRefreshLayout swipeRefresh;
    private TextView textViewId, textViewDirection, textViewStatus, textViewItemsHint, textViewNoAction, textViewActionsHint;
    private TextView infoFrom, infoTo, infoCreatedBy, infoDate, infoProducts, infoUnits;
    private MaterialButton buttonConfirmDelivery, buttonCancel, buttonMarkInTransit;
    private TextInputLayout layoutExpiry;
    private TextInputEditText editTextExpiry;
    private RecyclerView recyclerViewItems;
    private DeliveryItemsAdapter deliveryItemsAdapter;

    public static Intent newIntent(Context context, int deliveryId) {
        Intent intent = new Intent(context, DeliveryDetailsActivity.class);
        intent.putExtra(EXTRA_DELIVERY_ID, deliveryId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinishing()) return;
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_details);

        deliveryId = getIntent().getIntExtra(EXTRA_DELIVERY_ID, 0);
        if (deliveryId <= 0) {
            Toast.makeText(this, R.string.error_invalid_delivery, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (savedInstanceState != null) expiryIso = savedInstanceState.getString(STATE_EXPIRY);

        initializeUI();
        fetchDeliveryDetails(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_EXPIRY, expiryIso);
    }

    private void initializeUI() {
        View root = findViewById(R.id.drawerlayout_delivery_details);
        topBar = findViewById(R.id.top_navigation_bar_delivery_confirmation);
        InsetsHelper.applyEdgeToEdge(root, topBar, null, true);
        setupDetailTopBar(topBar, getString(R.string.delivery_details_title), null);

        scrollContent = findViewById(R.id.scroll_delivery_details);
        swipeRefresh = findViewById(R.id.swipe_refresh_delivery_details);
        swipeRefresh.setColorSchemeResources(R.color.fp_foreground);
        swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.fp_card);
        swipeRefresh.setOnRefreshListener(() -> fetchDeliveryDetails(false));
        progress = findViewById(R.id.progress_bar_delivery_details);
        emptyState = findViewById(R.id.layout_empty_delivery_details);

        textViewId = findViewById(R.id.text_view_delivery_details_id);
        textViewDirection = findViewById(R.id.text_view_delivery_details_direction);
        textViewStatus = findViewById(R.id.text_view_delivery_details_status);
        textViewItemsHint = findViewById(R.id.text_view_delivery_items_hint);
        textViewNoAction = findViewById(R.id.text_view_delivery_no_action);
        textViewActionsHint = findViewById(R.id.text_view_delivery_actions_hint);

        infoFrom = info(R.id.info_delivery_from, R.string.from_branch_text);
        infoTo = info(R.id.info_delivery_to, R.string.to_branch_text);
        infoCreatedBy = info(R.id.info_delivery_created_by, R.string.created_by_text);
        infoDate = info(R.id.info_delivery_date, R.string.date_created_text);
        infoProducts = info(R.id.info_delivery_products, R.string.products_label);
        infoUnits = info(R.id.info_delivery_units, R.string.total_units_label);

        bottomBar = findViewById(R.id.layout_bottom_bar);
        layoutInboundActions = findViewById(R.id.layout_inbound_actions);
        layoutOutboundActions = findViewById(R.id.layout_outbound_actions);
        buttonConfirmDelivery = findViewById(R.id.button_confirm_delivery);
        buttonCancel = findViewById(R.id.button_cancel_request);
        buttonMarkInTransit = findViewById(R.id.button_mark_in_transit);

        receiveCard = findViewById(R.id.card_receive_details);
        layoutExpiry = findViewById(R.id.text_input_layout_expiry_date);
        editTextExpiry = findViewById(R.id.edit_text_expiry_date);
        editTextExpiry.setOnClickListener(v -> pickExpiryDate());
        layoutExpiry.setEndIconOnClickListener(v -> pickExpiryDate());

        recyclerViewItems = findViewById(R.id.recycler_view_delivery_items);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));

        buttonConfirmDelivery.setOnClickListener(v -> confirmReceive());
        buttonCancel.setOnClickListener(v -> confirmCancel());
        buttonMarkInTransit.setOnClickListener(v -> confirmMarkInTransit());
    }

    private TextView info(int includeId, int labelRes) {
        View row = findViewById(includeId);
        ((TextView) row.findViewById(R.id.text_view_info_label)).setText(labelRes);
        return row.findViewById(R.id.text_view_info_value);
    }

    // ------------------------------------------------------------------ loading

    private void fetchDeliveryDetails(boolean firstLoad) {
        if (firstLoad) progress.setVisibility(View.VISIBLE);
        new DeliveryRepository(this).getDeliveryDetails(deliveryId).enqueue(new SafeCallback<DeliveryDetailResponse>(this) {
            @Override
            public void onFinish() {
                progress.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onSuccess(@Nullable DeliveryDetailResponse response) {
                DisplayDeliveryDetailsDTO data = response != null ? response.getData() : null;
                if (data == null) {
                    showLoadError(getString(R.string.error_delivery_not_found));
                    return;
                }
                bind(data);
            }

            @Override
            public void onError(@NonNull String message, int code) {
                if (code == 401) return;
                if (details == null) showLoadError(message);
                else super.onError(message, code);
            }
        });
    }

    private void showLoadError(String message) {
        scrollContent.setVisibility(View.INVISIBLE);
        bottomBar.setVisibility(View.GONE);
        ((TextView) emptyState.findViewById(R.id.text_view_empty_title)).setText(R.string.error_load_title);
        ((TextView) emptyState.findViewById(R.id.text_view_empty_message)).setText(message);
        View retry = emptyState.findViewById(R.id.button_empty_retry);
        retry.setVisibility(View.VISIBLE);
        retry.setOnClickListener(v -> {
            emptyState.setVisibility(View.GONE);
            fetchDeliveryDetails(true);
        });
        emptyState.setVisibility(View.VISIBLE);
    }

    private void bind(DisplayDeliveryDetailsDTO data) {
        details = data;
        actions = AccessRules.deliveryActions(sessionManager, data);
        emptyState.setVisibility(View.GONE);
        scrollContent.setVisibility(View.VISIBLE);

        setDetailTitle(topBar, data.getDeliveryDisplayId(), getString(R.string.delivery_details_title));
        textViewId.setText(data.getDeliveryDisplayId());
        StatusStyle.apply(textViewStatus, data.getDeliveryStatus());
        if (data.getDirection() != null && !data.getDirection().isEmpty()) {
            StatusStyle.apply(textViewDirection, data.getDirection());
            textViewDirection.setVisibility(View.VISIBLE);
        } else {
            textViewDirection.setVisibility(View.GONE);
        }

        List<DeliveryItemDTO> items = data.getItems() != null ? data.getItems() : new ArrayList<>();
        int units = 0;
        for (DeliveryItemDTO item : items) units += item.getQuantity();

        infoFrom.setText(orDash(data.getFromBranchName()));
        infoTo.setText(orDash(data.getToBranchName()));
        infoCreatedBy.setText(orDash(data.getCreatedBy()));
        infoDate.setText(DateUtils.formatDate(data.getCreatedAt()));
        infoProducts.setText(String.valueOf(items.size()));
        infoUnits.setText(String.valueOf(units));

        deliveryItemsAdapter = new DeliveryItemsAdapter(items, actions.canReceive, false);
        recyclerViewItems.setAdapter(deliveryItemsAdapter);

        // Receive form
        receiveCard.setVisibility(actions.canReceive ? View.VISIBLE : View.GONE);
        textViewItemsHint.setVisibility(actions.canReceive ? View.VISIBLE : View.GONE);
        textViewItemsHint.setText(R.string.receive_items_hint);
        if (actions.canReceive) showExpiry();

        // Buttons
        bottomBar.setVisibility(actions.any() ? View.VISIBLE : View.GONE);
        layoutOutboundActions.setVisibility(actions.canMarkInTransit || actions.canCancel ? View.VISIBLE : View.GONE);
        buttonMarkInTransit.setVisibility(actions.canMarkInTransit ? View.VISIBLE : View.GONE);
        buttonCancel.setVisibility(actions.canCancel ? View.VISIBLE : View.GONE);
        layoutInboundActions.setVisibility(actions.canReceive ? View.VISIBLE : View.GONE);
        textViewActionsHint.setText(actions.canReceive ? R.string.hint_receive_action
                : actions.canMarkInTransit ? R.string.hint_dispatch_action : R.string.hint_cancel_action);

        textViewNoAction.setVisibility(actions.any() ? View.GONE : View.VISIBLE);
        textViewNoAction.setText(getString(R.string.no_action_required, StatusStyle.label(data.getDeliveryStatus()).toLowerCase(Locale.getDefault())));
    }

    // ------------------------------------------------------------- expiry date

    private void showExpiry() {
        if (expiryIso == null) {
            editTextExpiry.setText("");
            return;
        }
        expiryCalendar = DateUtils.toCalendar(expiryIso);
        editTextExpiry.setText(DateUtils.formatDate(expiryIso));
        layoutExpiry.setError(null);
    }

    private void pickExpiryDate() {
        Calendar initial = expiryCalendar != null ? expiryCalendar : Calendar.getInstance();
        if (expiryCalendar == null) initial.add(Calendar.YEAR, 1);
        DatePickerDialog dialog = new DatePickerDialog(this, (picker, year, month, day) -> {
            expiryIso = DateUtils.toIsoUtcDate(year, month, day);
            showExpiry();
        }, initial.get(Calendar.YEAR), initial.get(Calendar.MONTH), initial.get(Calendar.DAY_OF_MONTH));
        // An expiry date in the past makes no sense for stock that just arrived
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    // ------------------------------------------------------------------ actions

    private void confirmReceive() {
        if (details == null || deliveryItemsAdapter == null) return;
        List<DeliveryItemDTO> items = deliveryItemsAdapter.getItems();
        if (items.isEmpty()) {
            Toast.makeText(this, R.string.error_no_items_to_receive, Toast.LENGTH_SHORT).show();
            return;
        }
        if (expiryIso == null) {
            layoutExpiry.setError(getString(R.string.error_expiry_required));
            scrollContent.post(() -> ((androidx.core.widget.NestedScrollView) scrollContent).smoothScrollTo(0, receiveCard.getTop()));
            return;
        }

        boolean complete = true;
        for (DeliveryItemDTO item : items) {
            if (!item.isReceivedChecked() || item.getReceivedQty() != item.getQuantity()) {
                complete = false;
                break;
            }
        }

        // Same wording as the website's confirmation dialog
        DialogHelper.confirm(this,
                getString(complete ? R.string.receive_confirm_title_complete : R.string.receive_confirm_title_partial),
                getString(complete ? R.string.receive_confirm_message_complete : R.string.receive_confirm_message_partial),
                getString(complete ? R.string.receive_confirm_button_complete : R.string.receive_confirm_button_partial),
                !complete,
                () -> receive(items));
    }

    private void receive(List<DeliveryItemDTO> items) {
        List<ReceiveDeliveryItemDTO> receiveItems = new ArrayList<>();
        for (DeliveryItemDTO item : items) {
            ReceiveDeliveryItemDTO receiveItem = new ReceiveDeliveryItemDTO();
            // requestItemId when the backend sends it; the website sends the product id
            Integer requestItemId = item.getRequestItemIdAsInt();
            receiveItem.setRequestItemId(requestItemId != null ? requestItemId : item.getProductId());
            int qty = item.isReceivedChecked() ? Math.max(0, Math.min(item.getReceivedQty(), item.getQuantity())) : 0;
            receiveItem.setReceivedQty(qty);
            receiveItem.setRemarks(qty < item.getQuantity() ? item.getReceiveRemark() : null);
            receiveItems.add(receiveItem);
        }

        ReceiveDeliveryDTO dto = new ReceiveDeliveryDTO();
        dto.setExpiryDate(expiryIso);
        dto.setItems(receiveItems);

        ButtonLoading.start(buttonConfirmDelivery, getString(R.string.confirming));
        new DeliveryRepository(this).receiveDelivery(deliveryId, dto).enqueue(new SafeCallback<ReceiveDeliveryResponse>(this) {
            @Override
            public void onFinish() {
                ButtonLoading.stop(buttonConfirmDelivery);
            }

            @Override
            public void onSuccess(@Nullable ReceiveDeliveryResponse response) {
                onActionDone(getString(R.string.toast_delivery_received));
            }
        });
    }

    private void confirmMarkInTransit() {
        DialogHelper.confirm(this, getString(R.string.mark_in_transit_confirm_title),
                getString(R.string.mark_in_transit_confirm_message), getString(R.string.mark_in_transit), false, () -> {
                    setBusy(buttonMarkInTransit, getString(R.string.updating));
                    new DeliveryRepository(this).dispatchDelivery(deliveryId).enqueue(new SafeCallback<DispatchDeliveryResponse>(this) {
                        @Override
                        public void onFinish() {
                            setIdle();
                        }

                        @Override
                        public void onSuccess(@Nullable DispatchDeliveryResponse response) {
                            onActionDone(getString(R.string.toast_delivery_in_transit));
                        }
                    });
                });
    }

    private void confirmCancel() {
        DialogHelper.confirm(this, getString(R.string.cancel_delivery_confirm_title),
                getString(R.string.cancel_delivery_confirm_message), getString(R.string.cancel_delivery), true, () -> {
                    setBusy(buttonCancel, getString(R.string.cancelling));
                    new DeliveryRepository(this).cancelDelivery(deliveryId).enqueue(new SafeCallback<CancelDeliveryResponse>(this) {
                        @Override
                        public void onFinish() {
                            setIdle();
                        }

                        @Override
                        public void onSuccess(@Nullable CancelDeliveryResponse response) {
                            onActionDone(getString(R.string.toast_delivery_cancelled));
                        }
                    });
                });
    }

    private void setBusy(MaterialButton active, String text) {
        ButtonLoading.start(active, text);
        if (active != buttonCancel) buttonCancel.setEnabled(false);
        if (active != buttonMarkInTransit) buttonMarkInTransit.setEnabled(false);
    }

    private void setIdle() {
        ButtonLoading.stop(buttonCancel);
        ButtonLoading.stop(buttonMarkInTransit);
    }

    private void onActionDone(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK); // the deliveries list refreshes when we go back
        expiryIso = null;
        fetchDeliveryDetails(false);
    }

    private static String orDash(@Nullable String value) {
        return value == null || value.trim().isEmpty() ? "—" : value;
    }
}
