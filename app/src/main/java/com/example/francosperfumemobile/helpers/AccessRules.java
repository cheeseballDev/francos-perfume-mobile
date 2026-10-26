package com.example.francosperfumemobile.helpers;

import androidx.annotation.Nullable;

import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestApprovalDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDetailsDTO;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Who may do what. Mirrors the website:
 *  - roles:     App.jsx ProtectedRoute (inventory/requests/deliveries = MANAGER, OWNER, STAFF)
 *  - approvals: src/utils/requestAuthorizationUtils.js canUserApproveRequest
 *  - deliveries: DeliveryDetailsPage.jsx renderActionPanel
 * The backend still checks everything again; this only decides which buttons to show.
 */
public final class AccessRules {

    /** Roles that have screens in the mobile app. ADMIN and CASHIER use the website / POS. */
    private static final Set<String> MOBILE_ROLES = new HashSet<>(Arrays.asList("OWNER", "MANAGER", "STAFF"));

    /** Request statuses where nobody can approve or reject any more. */
    private static final Set<String> FINISHED_REQUEST_STATUSES = new HashSet<>(Arrays.asList(
            "APPROVED", "REJECTED", "CANCELLED", "CANCELED", "COMPLETED", "RECEIVED",
            "PARTIAL", "PARTIALLY COMPLETED", "CLOSED", "CLOSED PARTIAL",
            "IN TRANSIT", "DISPATCHED", "PENDING DISPATCH", "DELIVERY CANCELLED"));

    private AccessRules() {
    }

    public static String normalize(@Nullable String value) {
        if (value == null) return "";
        return value.replace('_', ' ').trim().replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);
    }

    public static boolean canUseMobileApp(@Nullable String role) {
        return MOBILE_ROLES.contains(normalize(role));
    }

    // ------------------------------------------------------------------ requests

    public static boolean isRequestFinished(@Nullable String requestStatus) {
        return FINISHED_REQUEST_STATUSES.contains(normalize(requestStatus));
    }

    /** The approval step that is waiting right now, or null. */
    @Nullable
    public static DisplayRequestApprovalDetailsDTO currentPendingApproval(@Nullable List<DisplayRequestApprovalDetailsDTO> approvals) {
        if (approvals == null) return null;
        for (DisplayRequestApprovalDetailsDTO approval : approvals) {
            if (approval != null && "PENDING".equals(normalize(approval.getStatus()))) {
                return approval;
            }
        }
        return null;
    }

    /** Same as the website: the user must be the one the current stage is waiting for. */
    public static boolean canApproveRequest(SessionManager session, @Nullable DisplayRequestDetailsDTO request) {
        if (session == null || request == null) return false;
        if (isRequestFinished(request.getRequestStatus())) return false;

        DisplayRequestApprovalDetailsDTO pending = currentPendingApproval(request.getApprovals());
        if (pending == null) return false;

        String stage = normalize(pending.getStage());
        String role = normalize(session.getRole());
        int branchId = session.getBranchId();

        switch (stage) {
            case "SOURCE BRANCH MANAGER":
                return role.contains("MANAGER") && branchId == request.getFromBranchId();
            case "DESTINATION BRANCH MANAGER":
                return role.contains("MANAGER") && branchId == request.getToBranchId();
            case "OWNER":
                return "OWNER".equals(role);
            default:
                return false;
        }
    }

    /** Website shows "Cancel Request" while the request is still pending. */
    public static boolean canCancelRequest(@Nullable DisplayRequestDetailsDTO request) {
        return request != null && "PENDING".equals(normalize(request.getRequestStatus()));
    }

    // ---------------------------------------------------------------- deliveries

    /** Which delivery buttons to show. */
    public static final class DeliveryActions {
        public final boolean canMarkInTransit;
        public final boolean canCancel;
        public final boolean canReceive;

        DeliveryActions(boolean canMarkInTransit, boolean canCancel, boolean canReceive) {
            this.canMarkInTransit = canMarkInTransit;
            this.canCancel = canCancel;
            this.canReceive = canReceive;
        }

        public boolean any() {
            return canMarkInTransit || canCancel || canReceive;
        }
    }

    public static DeliveryActions deliveryActions(SessionManager session, @Nullable DisplayDeliveryDetailsDTO delivery) {
        if (session == null || delivery == null) return new DeliveryActions(false, false, false);

        String direction = normalize(delivery.getDirection());
        String status = normalize(delivery.getDeliveryStatus());

        if ("OUTBOUND".equals(direction)) {
            // Sending branch: dispatch it, or cancel before it leaves
            boolean pendingDispatch = "PENDING".equals(status) || "PENDING DISPATCH".equals(status);
            boolean finishedOrMoving = status.equals("IN TRANSIT") || status.equals("DISPATCHED")
                    || status.equals("COMPLETED") || status.equals("PARTIAL") || status.equals("RECEIVED")
                    || status.equals("DELIVERED") || status.equals("CANCELLED") || status.equals("DELIVERY CANCELLED")
                    || status.equals("CLOSED");
            return new DeliveryActions(pendingDispatch, !finishedOrMoving, false);
        }

        if ("INBOUND".equals(direction)) {
            // Receiving branch: confirm what arrived once it is on the way
            boolean onTheWay = "IN TRANSIT".equals(status) || "ACCEPTED".equals(status) || "DISPATCHED".equals(status);
            String myBranch = normalize(session.getBranchLocation());
            boolean isDestination = !myBranch.isEmpty() && myBranch.equals(normalize(delivery.getToBranchName()));
            return new DeliveryActions(false, false, onTheWay && isDestination);
        }

        return new DeliveryActions(false, false, false);
    }
}
