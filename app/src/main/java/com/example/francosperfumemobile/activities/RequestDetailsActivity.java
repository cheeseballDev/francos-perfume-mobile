package com.example.francosperfumemobile.activities;

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
import com.example.francosperfumemobile.adapters.RequestApprovalAdapter;
import com.example.francosperfumemobile.adapters.RequestedItemsAdapter;
import com.example.francosperfumemobile.backend.dtos.requestdtos.ApproveRequestDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestApprovalDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.RejectRequestDTO;
import com.example.francosperfumemobile.backend.repository.RequestRepository;
import com.example.francosperfumemobile.backend.responses.requestresponses.ApprovalResponse;
import com.example.francosperfumemobile.backend.responses.requestresponses.CancelResponse;
import com.example.francosperfumemobile.backend.responses.requestresponses.RejectResponse;
import com.example.francosperfumemobile.backend.responses.requestresponses.RequestDetailResponse;
import com.example.francosperfumemobile.dialogs.MessageDialog;
import com.example.francosperfumemobile.helpers.AccessRules;
import com.example.francosperfumemobile.helpers.ButtonLoading;
import com.example.francosperfumemobile.helpers.DateUtils;
import com.example.francosperfumemobile.helpers.DialogHelper;
import com.example.francosperfumemobile.helpers.InsetsHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.example.francosperfumemobile.helpers.StatusStyle;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Request details (website: RequestDetailsPage).
 * Approve / Reject only show for the person the current approval stage is waiting for
 * (AccessRules.canApproveRequest, same rule as the website). Reject needs remarks and both
 * ask for confirmation. Buttons are disabled while a call runs, so nothing is sent twice.
 * After an action the screen reloads, and the list behind it refreshes (RESULT_OK).
 */
public class RequestDetailsActivity extends BaseActivity {

    private static final String EXTRA_REQUEST_ID = "REQUEST_ID";

    private final List<DisplayRequestApprovalDetailsDTO> listRequestTimeline = new ArrayList<>();
    private int requestId;
    private DisplayRequestDetailsDTO details;

    private View topBar, scrollContent, layoutRequestActions, emptyState;
    private View progressBarMain;
    private SwipeRefreshLayout swipeRefresh;
    private TextView textViewRequestId, textViewDirection, textViewStatus, textViewStage, textViewProductsHint, textViewActionsHint;
    private TextView infoFrom, infoTo, infoCreatedBy, infoDate;
    private MaterialButton buttonApprove, buttonReject, buttonOpenMessage, buttonCancelRequest;
    private RequestApprovalAdapter requestApprovalAdapter;
    private RequestedItemsAdapter requestedItemsAdapter;

    public static Intent newIntent(Context context, int requestId) {
        Intent intent = new Intent(context, RequestDetailsActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinishing()) return;
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_details);

        requestId = getIntent().getIntExtra(EXTRA_REQUEST_ID, -1);
        if (requestId <= 0) {
            Toast.makeText(this, R.string.error_invalid_request, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeUI();
        initializeRecyclerViews();
        initializeListeners();
        fetchRequestDetails(true);
    }

    private void initializeUI() {
        View root = findViewById(R.id.drawerlayout_request_details);
        topBar = findViewById(R.id.top_navigation_bar_request_details);
        layoutRequestActions = findViewById(R.id.layout_request_actions);
        InsetsHelper.applyEdgeToEdge(root, topBar, null, true);
        setupDetailTopBar(topBar, getString(R.string.request_details_title), null);

        scrollContent = findViewById(R.id.scroll_request_details);
        swipeRefresh = findViewById(R.id.swipe_refresh_request_details);
        swipeRefresh.setColorSchemeResources(R.color.fp_foreground);
        swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.fp_card);
        progressBarMain = findViewById(R.id.progress_bar_main);
        emptyState = findViewById(R.id.layout_empty_request_details);

        textViewRequestId = findViewById(R.id.text_view_request_details_id);
        textViewDirection = findViewById(R.id.text_view_request_details_direction);
        textViewStatus = findViewById(R.id.text_view_request_details_status);
        textViewStage = findViewById(R.id.text_view_request_details_stage);
        textViewProductsHint = findViewById(R.id.text_view_requested_products_hint);
        textViewActionsHint = findViewById(R.id.text_view_request_actions_hint);

        infoFrom = info(R.id.info_from_branch, R.string.from_branch_text);
        infoTo = info(R.id.info_to_branch, R.string.to_branch_text);
        infoCreatedBy = info(R.id.info_created_by, R.string.created_by_text);
        infoDate = info(R.id.info_date_submitted, R.string.date_submitted_text);

        buttonApprove = findViewById(R.id.button_accept);
        buttonReject = findViewById(R.id.button_reject_cancel);
        buttonOpenMessage = findViewById(R.id.button_open_message);
        buttonCancelRequest = findViewById(R.id.button_cancel_request);
    }

    private TextView info(int includeId, int labelRes) {
        View row = findViewById(includeId);
        ((TextView) row.findViewById(R.id.text_view_info_label)).setText(labelRes);
        return row.findViewById(R.id.text_view_info_value);
    }

    private void initializeRecyclerViews() {
        RecyclerView products = findViewById(R.id.recycler_view_requested_products);
        RecyclerView timeline = findViewById(R.id.recycler_view_request_timeline);

        requestedItemsAdapter = new RequestedItemsAdapter(new ArrayList<>());
        requestedItemsAdapter.setOnApprovalsChanged(this::updateApproveButton);
        requestApprovalAdapter = new RequestApprovalAdapter(listRequestTimeline);

        products.setLayoutManager(new LinearLayoutManager(this));
        products.setAdapter(requestedItemsAdapter);
        timeline.setLayoutManager(new LinearLayoutManager(this));
        timeline.setAdapter(requestApprovalAdapter);
    }

    private void initializeListeners() {
        swipeRefresh.setOnRefreshListener(() -> fetchRequestDetails(false));
        buttonApprove.setOnClickListener(v -> confirmApprove());
        buttonReject.setOnClickListener(v -> confirmReject());
        buttonOpenMessage.setOnClickListener(v -> {
            String message = details != null ? details.getRequestMessage() : null;
            MessageDialog.newInstance(getString(R.string.request_message_title), message)
                    .show(getSupportFragmentManager(), "MessageDialogTag");
        });
        buttonCancelRequest.setOnClickListener(v -> confirmCancel());
    }

    // ------------------------------------------------------------------ loading

    private void fetchRequestDetails(boolean firstLoad) {
        if (firstLoad) progressBarMain.setVisibility(View.VISIBLE);
        new RequestRepository(this).getRequestDetails(requestId).enqueue(new SafeCallback<RequestDetailResponse>(this) {
            @Override
            public void onFinish() {
                progressBarMain.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onSuccess(@Nullable RequestDetailResponse response) {
                DisplayRequestDetailsDTO data = response != null ? response.getData() : null;
                if (data == null) {
                    showLoadError(getString(R.string.error_request_not_found));
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
        layoutRequestActions.setVisibility(View.GONE);
        ((TextView) emptyState.findViewById(R.id.text_view_empty_title)).setText(R.string.error_load_title);
        ((TextView) emptyState.findViewById(R.id.text_view_empty_message)).setText(message);
        View retry = emptyState.findViewById(R.id.button_empty_retry);
        retry.setVisibility(View.VISIBLE);
        retry.setOnClickListener(v -> {
            emptyState.setVisibility(View.GONE);
            fetchRequestDetails(true);
        });
        emptyState.setVisibility(View.VISIBLE);
    }

    private void bind(DisplayRequestDetailsDTO data) {
        details = data;
        emptyState.setVisibility(View.GONE);
        scrollContent.setVisibility(View.VISIBLE);

        setDetailTitle(topBar, data.getRequestDisplayId(), getString(R.string.request_details_title));
        textViewRequestId.setText(data.getRequestDisplayId());
        StatusStyle.apply(textViewStatus, data.getRequestStatus());
        if (data.getRequestDirection() != null && !data.getRequestDirection().isEmpty()) {
            StatusStyle.apply(textViewDirection, data.getRequestDirection());
            textViewDirection.setVisibility(View.VISIBLE);
        } else {
            textViewDirection.setVisibility(View.GONE);
        }

        DisplayRequestApprovalDetailsDTO pending = AccessRules.currentPendingApproval(data.getApprovals());
        if (pending != null && pending.getStage() != null) {
            textViewStage.setText(getString(R.string.waiting_for_stage, StatusStyle.label(pending.getStage())));
            textViewStage.setVisibility(View.VISIBLE);
        } else {
            textViewStage.setVisibility(View.GONE);
        }

        infoFrom.setText(orDash(data.getRequestedFrom()));
        infoTo.setText(orDash(data.getDeliveredTo()));
        infoCreatedBy.setText(orDash(data.getEmployeeName()));
        infoDate.setText(DateUtils.formatDate(data.getRequestDateSubmitted()));

        boolean hasMessage = data.getRequestMessage() != null && !data.getRequestMessage().trim().isEmpty();
        buttonOpenMessage.setText(hasMessage ? R.string.open_message_text : R.string.no_message_text);
        buttonOpenMessage.setEnabled(hasMessage);

        boolean canApprove = AccessRules.canApproveRequest(sessionManager, data);
        requestedItemsAdapter.setItems(data.getItems());
        requestedItemsAdapter.setReadOnly(!canApprove);
        textViewProductsHint.setText(canApprove ? R.string.requested_products_hint_edit : R.string.requested_products_hint_read);

        listRequestTimeline.clear();
        if (data.getApprovals() != null) listRequestTimeline.addAll(data.getApprovals());
        requestApprovalAdapter.notifyDataSetChanged();

        layoutRequestActions.setVisibility(canApprove ? View.VISIBLE : View.GONE);
        if (canApprove && pending != null) {
            textViewActionsHint.setText(getString(R.string.reviewing_as, StatusStyle.label(pending.getStage())));
        }
        buttonCancelRequest.setVisibility(AccessRules.canCancelRequest(data) ? View.VISIBLE : View.GONE);
        updateApproveButton();
    }

    private void updateApproveButton() {
        if (ButtonLoading.isLoading(buttonApprove)) return;
        boolean any = requestedItemsAdapter.getApprovedCount() > 0;
        buttonApprove.setEnabled(any);
        buttonApprove.setText(requestedItemsAdapter.allFullyApproved() ? R.string.approve_text : R.string.approve_partial_text);
    }

    // ------------------------------------------------------------------ actions

    private void confirmApprove() {
        if (requestedItemsAdapter.getApprovedCount() == 0) {
            Toast.makeText(this, R.string.error_nothing_approved, Toast.LENGTH_LONG).show();
            return;
        }
        String message = requestedItemsAdapter.allFullyApproved()
                ? getString(R.string.approve_confirm_message)
                : getString(R.string.approve_partial_confirm_message);
        DialogHelper.prompt(this, getString(R.string.approve_confirm_title), message,
                getString(R.string.hint_remarks_optional), null, getString(R.string.approve_text),
                false, false, this::approve);
    }

    private void approve(String remarks) {
        List<ApproveRequestDTO> approvals = requestedItemsAdapter.getApprovalList(remarks);
        setActionsBusy(buttonApprove, getString(R.string.approving));
        new RequestRepository(this).approveRequest(requestId, approvals).enqueue(new SafeCallback<ApprovalResponse>(this) {
            @Override
            public void onFinish() {
                setActionsIdle();
            }

            @Override
            public void onSuccess(@Nullable ApprovalResponse response) {
                onActionDone(getString(R.string.toast_request_approved));
            }
        });
    }

    private void confirmReject() {
        DialogHelper.prompt(this, getString(R.string.reject_confirm_title), getString(R.string.reject_confirm_message),
                getString(R.string.hint_reject_reason), null, getString(R.string.deny_text),
                true, true, this::reject);
    }

    private void reject(String remarks) {
        RejectRequestDTO dto = new RejectRequestDTO();
        dto.setRemarks(remarks); // what the user typed (was hard-coded "Request rejected")
        setActionsBusy(buttonReject, getString(R.string.rejecting));
        new RequestRepository(this).rejectRequest(requestId, dto).enqueue(new SafeCallback<RejectResponse>(this) {
            @Override
            public void onFinish() {
                setActionsIdle();
            }

            @Override
            public void onSuccess(@Nullable RejectResponse response) {
                onActionDone(getString(R.string.toast_request_rejected));
            }
        });
    }

    private void confirmCancel() {
        DialogHelper.confirm(this, getString(R.string.cancel_request_confirm_title),
                getString(R.string.cancel_request_confirm_message), getString(R.string.cancel_request), true, () -> {
                    ButtonLoading.start(buttonCancelRequest, getString(R.string.cancelling));
                    new RequestRepository(this).cancelRequest(requestId).enqueue(new SafeCallback<CancelResponse>(this) {
                        @Override
                        public void onFinish() {
                            ButtonLoading.stop(buttonCancelRequest);
                        }

                        @Override
                        public void onSuccess(@Nullable CancelResponse response) {
                            onActionDone(getString(R.string.toast_request_cancelled));
                        }
                    });
                });
    }

    private void onActionDone(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK); // the list refreshes when we go back
        fetchRequestDetails(false);
    }

    private void setActionsBusy(MaterialButton active, String text) {
        ButtonLoading.start(active, text);
        MaterialButton other = active == buttonApprove ? buttonReject : buttonApprove;
        other.setEnabled(false);
        requestedItemsAdapter.setReadOnly(true);
    }

    private void setActionsIdle() {
        ButtonLoading.stop(buttonApprove);
        ButtonLoading.stop(buttonReject);
        if (details != null) requestedItemsAdapter.setReadOnly(!AccessRules.canApproveRequest(sessionManager, details));
        updateApproveButton();
    }

    private static String orDash(@Nullable String value) {
        return value == null || value.trim().isEmpty() ? "—" : value;
    }
}
