package com.example.francosperfumemobile.helpers;

import java.util.ArrayList;
import java.util.List;

public class TimelineUtils {
    /*
    public static List<TimelineItem> buildContextualTimeline(
            String currentUserRole,
            String requestStatus,
            List<ApprovalDto> approvals) {

        List<TimelineItem> items = new ArrayList<>();
        String role = currentUserRole != null ? currentUserRole.toUpperCase().trim() : "";

        // 1. OWNER REPLENISHMENT VIEW (Push model: Destination Manager must accept)
        if (role.contains("OWNER")) {
            items.add(getApprovalStep(approvals, "DESTINATION MANAGER", "Destination Branch Manager Review"));
        }
        // 2. REQUESTING BRANCH MANAGER VIEW (Implicitly approved by creator, looking downstream)
        else if (role.contains("REQUESTING_MANAGER") || role.contains("DESTINATION_MANAGER")) {
            items.add(getApprovalStep(approvals, "SOURCE MANAGER", "Source Branch Manager Review"));
            items.add(getApprovalStep(approvals, "OWNER", "Owner Review"));
        }
        // 3. SOURCE BRANCH MANAGER / STAFF VIEW
        else if (role.contains("SOURCE")) {
            items.add(getApprovalStep(approvals, "SOURCE MANAGER", "Source Branch Review"));
            items.add(getApprovalStep(approvals, "OWNER", "Owner Review"));
        }
        // 4. REQUESTING INVENTORY STAFF VIEW (Default full branch chain)
        else {
            items.add(getApprovalStep(approvals, "REQUESTING MANAGER", "Request Branch Manager Review"));
            items.add(getApprovalStep(approvals, "SOURCE MANAGER", "Source Branch Manager Review"));
            items.add(getApprovalStep(approvals, "OWNER", "Owner Review"));
        }

        // Common fulfillment end-steps for all timelines
        boolean isDispatched = "IN TRANSIT".equalsIgnoreCase(requestStatus)
                || "OUT FOR DISPATCH".equalsIgnoreCase(requestStatus)
                || "COMPLETED".equalsIgnoreCase(requestStatus);

        boolean isCompleted = "COMPLETED".equalsIgnoreCase(requestStatus);

        items.add(new TimelineItem(
                "For Dispatch",
                isDispatched ? "Dispatched and on the way" : "Waiting for dispatch",
                isDispatched ? "DISPATCHED" : "PENDING"
        ));

        items.add(new TimelineItem(
                "Stock Arrival",
                isCompleted ? "Delivery acknowledged" : "Waiting to be received",
                isCompleted ? "COMPLETED" : "PENDING"
        ));

        // Mark steps after any rejection as skipped
        boolean rejectionFound = false;
        for (TimelineItem item : items) {
            if (rejectionFound) {
                item.setSkipped(true);
            }
            if ("REJECTED".equalsIgnoreCase(item.getStatus())) {
                rejectionFound = true;
            }
        }

        return items;
    }

    private static TimelineItem getApprovalStep(List<ApprovalDto> approvals, String stageKey, String defaultTitle) {
        if (approvals != null) {
            for (ApprovalDto approval : approvals) {
                if (approval.getStage() != null && approval.getStage().toUpperCase().contains(stageKey)) {
                    String subtitle = approval.getApprover() != null ? approval.getApprover() : "Waiting for approval";
                    return new TimelineItem(defaultTitle, subtitle, approval.getStatus());
                }
            }
        }
        return new TimelineItem(defaultTitle, "Waiting for approval", "PENDING");
    }

     */
}