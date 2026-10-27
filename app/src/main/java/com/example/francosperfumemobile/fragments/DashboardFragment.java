package com.example.francosperfumemobile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.activities.DeliveryDetailsActivity;
import com.example.francosperfumemobile.activities.MainActivity;
import com.example.francosperfumemobile.activities.RequestDetailsActivity;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.PendingApprovalDTO;
import com.example.francosperfumemobile.backend.repository.KPIRepository;
import com.example.francosperfumemobile.backend.responses.KPIResponses.InventoryCountKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.LowStockKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.PendingRequestsKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.RequestsInUserStageKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.UpcomingDeliveriesKPIResponse;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;
import com.example.francosperfumemobile.components.DashboardCardView;
import com.example.francosperfumemobile.helpers.DateUtils;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.example.francosperfumemobile.helpers.StatusStyle;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Dashboard with live numbers from the KPI endpoints (website: HomePage):
 * total inventory (+ low stock), pending approvals, pending deliveries, requests at your stage,
 * and short lists of what needs attention. Pull down to refresh.
 */
public class DashboardFragment extends Fragment {

    private static final int MAX_ROWS = 4;

    private DashboardCardView cardInventory, cardRequest, cardDispatch, cardDelivery;
    private SwipeRefreshLayout swipeRefresh;
    private View panelRequests, panelDeliveries, panelLowStock;
    private int pendingCalls = 0;
    private int lowStockCount = -1;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeUI(view);
        loadAll();
    }

    private void initializeUI(View view) {
        SessionManager session = new SessionManager(requireContext());
        TextView greeting = view.findViewById(R.id.text_view_dashboard_greeting);
        TextView subtitle = view.findViewById(R.id.text_view_dashboard_subtitle);
        String first = session.getFirstName();
        greeting.setText(first != null && !first.isEmpty()
                ? getString(R.string.dashboard_greeting_name, DateUtils.greeting(), first)
                : DateUtils.greeting());
        String branch = session.getBranchLocation();
        subtitle.setText(branch != null ? getString(R.string.dashboard_subtitle_branch, branch) : getString(R.string.dashboard_subtitle));

        swipeRefresh = view.findViewById(R.id.swipe_refresh_dashboard);
        swipeRefresh.setColorSchemeResources(R.color.fp_foreground);
        swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.fp_card);
        swipeRefresh.setOnRefreshListener(this::loadAll);

        cardInventory = view.findViewById(R.id.dashboard_inventory_card);
        cardRequest = view.findViewById(R.id.dashboard_requests_card);
        cardDispatch = view.findViewById(R.id.dashboard_dispatch_card);
        cardDelivery = view.findViewById(R.id.dashboard_delivery_card);

        cardInventory.setTitle(getString(R.string.kpi_total_inventory));
        cardInventory.setIcon(R.drawable.ic_boxes, R.color.icon_green);
        cardInventory.setValue(getString(R.string.placeholder_dash));
        cardInventory.setOnClickListener(v -> goToTab(MainActivity.TAB_INVENTORY));

        cardRequest.setTitle(getString(R.string.kpi_pending_approvals));
        cardRequest.setIcon(R.drawable.ic_clock, R.color.icon_blue);
        cardRequest.setValue(getString(R.string.placeholder_dash));
        cardRequest.setSubText(getString(R.string.kpi_requests));
        cardRequest.setOnClickListener(v -> goToTab(MainActivity.TAB_REQUEST));

        cardDispatch.setTitle(getString(R.string.kpi_requests_your_stage));
        cardDispatch.setIcon(R.drawable.ic_file_text, R.color.icon_amber);
        cardDispatch.setValue(getString(R.string.placeholder_dash));
        cardDispatch.setSubText(getString(R.string.kpi_waiting_on_you));
        cardDispatch.setOnClickListener(v -> goToTab(MainActivity.TAB_REQUEST));

        cardDelivery.setTitle(getString(R.string.kpi_pending_deliveries));
        cardDelivery.setIcon(R.drawable.ic_truck, R.color.icon_blue);
        cardDelivery.setValue(getString(R.string.placeholder_dash));
        cardDelivery.setSubText(getString(R.string.kpi_deliveries));
        cardDelivery.setOnClickListener(v -> goToTab(MainActivity.TAB_DELIVERY));

        panelRequests = view.findViewById(R.id.panel_pending_requests);
        panelDeliveries = view.findViewById(R.id.panel_pending_deliveries);
        panelLowStock = view.findViewById(R.id.panel_low_stock);
        setupPanel(panelRequests, R.string.panel_pending_requests, R.string.view_all_requests, MainActivity.TAB_REQUEST);
        setupPanel(panelDeliveries, R.string.panel_pending_deliveries, R.string.view_all_deliveries, MainActivity.TAB_DELIVERY);
        setupPanel(panelLowStock, R.string.panel_low_stock, R.string.view_inventory, MainActivity.TAB_INVENTORY);
    }

    private void setupPanel(View panel, int titleRes, int actionRes, String tab) {
        ((TextView) panel.findViewById(R.id.text_view_panel_title)).setText(titleRes);
        TextView action = panel.findViewById(R.id.button_panel_action);
        action.setText(actionRes);
        action.setOnClickListener(v -> goToTab(tab));
    }

    private void goToTab(String tab) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).selectTab(tab);
        }
    }

    // ------------------------------------------------------------------ loading

    private void loadAll() {
        KPIRepository repository = new KPIRepository(requireContext());
        pendingCalls = 5;
        setPanelLoading(panelRequests, true);
        setPanelLoading(panelDeliveries, true);
        setPanelLoading(panelLowStock, true);

        repository.getInventoryStockCountKPI().enqueue(new SafeCallback<InventoryCountKPIResponse>(this) {
            @Override
            public void onFinish() {
                callDone();
            }

            @Override
            public void onSuccess(@Nullable InventoryCountKPIResponse body) {
                cardInventory.setValue(formatNumber(body != null ? body.getInventoryCount() : 0));
            }

            @Override
            public void onError(@NonNull String message, int code) {
                cardInventory.setValue(getString(R.string.placeholder_dash));
                if (code != 401) cardInventory.setSubText(getString(R.string.kpi_unavailable));
            }
        });

        repository.getLowStockKPI().enqueue(new SafeCallback<LowStockKPIResponse>(this) {
            @Override
            public void onFinish() {
                callDone();
            }

            @Override
            public void onSuccess(@Nullable LowStockKPIResponse body) {
                lowStockCount = body != null ? body.getLowStockProductCount() : 0;
                if (lowStockCount > 0) {
                    cardInventory.setSubBadge(getResources().getQuantityString(R.plurals.kpi_low_stock_items, lowStockCount, lowStockCount),
                            StatusStyle.Tone.PENDING, R.drawable.ic_alert_triangle);
                } else {
                    cardInventory.setSubBadge(getString(R.string.kpi_all_in_stock), StatusStyle.Tone.SUCCESS, R.drawable.ic_check);
                }
                fillLowStock(body != null ? body.getLowStockProductsList() : null);
            }

            @Override
            public void onError(@NonNull String message, int code) {
                showPanelEmpty(panelLowStock, getString(R.string.panel_error));
            }
        });

        repository.getPendingApprovalsKPI().enqueue(new SafeCallback<PendingRequestsKPIResponse>(this) {
            @Override
            public void onFinish() {
                callDone();
            }

            @Override
            public void onSuccess(@Nullable PendingRequestsKPIResponse body) {
                int count = body != null ? body.getPendingApprovalsCount() : 0;
                cardRequest.setValue(formatNumber(count));
                fillPendingRequests(body != null ? body.getPendingApprovalsList() : null);
            }

            @Override
            public void onError(@NonNull String message, int code) {
                showPanelEmpty(panelRequests, getString(R.string.panel_error));
            }
        });

        repository.getRequestsInUserStageKPI().enqueue(new SafeCallback<RequestsInUserStageKPIResponse>(this) {
            @Override
            public void onFinish() {
                callDone();
            }

            @Override
            public void onSuccess(@Nullable RequestsInUserStageKPIResponse body) {
                cardDispatch.setValue(formatNumber(body != null ? body.getRequestsInStage() : 0));
            }

            @Override
            public void onError(@NonNull String message, int code) {
                cardDispatch.setValue(getString(R.string.placeholder_dash));
            }
        });

        repository.getUpcomingDeliveriesKPI().enqueue(new SafeCallback<UpcomingDeliveriesKPIResponse>(this) {
            @Override
            public void onFinish() {
                callDone();
            }

            @Override
            public void onSuccess(@Nullable UpcomingDeliveriesKPIResponse body) {
                cardDelivery.setValue(formatNumber(body != null ? body.getDeliveryCount() : 0));
                fillDeliveries(body != null ? body.getDeliveryList() : null);
            }

            @Override
            public void onError(@NonNull String message, int code) {
                showPanelEmpty(panelDeliveries, getString(R.string.panel_error));
            }
        });
    }

    private void callDone() {
        pendingCalls--;
        if (pendingCalls <= 0 && swipeRefresh != null) swipeRefresh.setRefreshing(false);
    }

    // ------------------------------------------------------------------ panels

    private void fillPendingRequests(@Nullable List<PendingApprovalDTO> rows) {
        LinearLayout container = panelRequests.findViewById(R.id.layout_panel_rows);
        container.removeAllViews();
        setPanelLoading(panelRequests, false);
        if (rows == null || rows.isEmpty()) {
            showPanelEmpty(panelRequests, getString(R.string.panel_no_pending_requests));
            return;
        }
        for (int i = 0; i < Math.min(MAX_ROWS, rows.size()); i++) {
            PendingApprovalDTO row = rows.get(i);
            String title = row.getRequestDisplayId() != null ? row.getRequestDisplayId()
                    : String.format(Locale.US, "REQ-%03d", row.getRequestId());
            String status = row.getStage() != null ? row.getStage() : (row.getStatus() != null ? row.getStatus() : "PENDING");
            addRow(container, title, null, status, v -> {
                if (row.getRequestId() > 0) startActivity(RequestDetailsActivity.newIntent(requireContext(), row.getRequestId()));
            });
        }
    }

    private void fillDeliveries(@Nullable List<DisplayDeliveryDTO> rows) {
        LinearLayout container = panelDeliveries.findViewById(R.id.layout_panel_rows);
        container.removeAllViews();
        setPanelLoading(panelDeliveries, false);
        if (rows == null || rows.isEmpty()) {
            showPanelEmpty(panelDeliveries, getString(R.string.panel_no_pending_deliveries));
            return;
        }
        for (int i = 0; i < Math.min(MAX_ROWS, rows.size()); i++) {
            DisplayDeliveryDTO row = rows.get(i);
            String route = getString(R.string.route_format, safe(row.getFromBranchName()), safe(row.getToBranchName()));
            addRow(container, safe(row.getDeliveryDisplayId()), route, row.getDeliveryStatus(), v -> {
                if (row.getDeliveryId() > 0) startActivity(DeliveryDetailsActivity.newIntent(requireContext(), row.getDeliveryId()));
            });
        }
    }

    private void fillLowStock(@Nullable List<DisplayInventoryDTO> rows) {
        LinearLayout container = panelLowStock.findViewById(R.id.layout_panel_rows);
        container.removeAllViews();
        setPanelLoading(panelLowStock, false);
        if (rows == null || rows.isEmpty()) {
            showPanelEmpty(panelLowStock, getString(R.string.panel_no_low_stock));
            return;
        }
        for (int i = 0; i < Math.min(MAX_ROWS + 1, rows.size()); i++) {
            DisplayInventoryDTO row = rows.get(i);
            View rowView = addRow(container, safe(row.getProductName()), safe(row.getBranchName()), null,
                    v -> goToTab(MainActivity.TAB_INVENTORY));
            TextView badge = rowView.findViewById(R.id.text_view_row_badge);
            StatusStyle.applyStockBadge(badge, row.getProductQuantity(), row.getStockWarning());
            if (badge.getVisibility() == View.VISIBLE) {
                badge.setText(getResources().getQuantityString(R.plurals.units_left, row.getProductQuantity(), row.getProductQuantity()));
            }
        }
    }

    private View addRow(LinearLayout container, String title, @Nullable String subtitle, @Nullable String status,
                        View.OnClickListener onClick) {
        View row = LayoutInflater.from(container.getContext()).inflate(R.layout.item_dashboard_row, container, false);
        ((TextView) row.findViewById(R.id.text_view_row_title)).setText(title);
        TextView subtitleView = row.findViewById(R.id.text_view_row_subtitle);
        if (subtitle != null && !subtitle.isEmpty()) {
            subtitleView.setText(subtitle);
            subtitleView.setVisibility(View.VISIBLE);
        }
        TextView badge = row.findViewById(R.id.text_view_row_badge);
        if (status != null) {
            StatusStyle.apply(badge, status);
            badge.setVisibility(View.VISIBLE);
        }
        row.setOnClickListener(onClick);
        container.addView(row);
        return row;
    }

    private void setPanelLoading(View panel, boolean loading) {
        panel.findViewById(R.id.progress_panel).setVisibility(loading ? View.VISIBLE : View.GONE);
        if (loading) panel.findViewById(R.id.text_view_panel_empty).setVisibility(View.GONE);
    }

    private void showPanelEmpty(View panel, String text) {
        setPanelLoading(panel, false);
        TextView empty = panel.findViewById(R.id.text_view_panel_empty);
        empty.setText(text);
        empty.setVisibility(View.VISIBLE);
    }

    private static String formatNumber(int value) {
        return NumberFormat.getIntegerInstance(Locale.getDefault()).format(value);
    }

    private static String safe(@Nullable String value) {
        return value == null || value.isEmpty() ? "—" : value;
    }
}
