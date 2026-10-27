package com.example.francosperfumemobile.backend.responses.KPIResponses;

import com.example.francosperfumemobile.backend.dtos.requestdtos.PendingApprovalDTO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingRequestsKPIResponse {
    // Was "pendingApporvalsList" (typo), so the list was always null
    @SerializedName("pendingApprovalsList")
    private List<PendingApprovalDTO> pendingApprovalsList;
    @SerializedName("pendingApprovalsCount")
    private int pendingApprovalsCount;

    public List<PendingApprovalDTO> getPendingApprovalsList() {
        return pendingApprovalsList;
    }

    public int getPendingApprovalsCount() {
        return pendingApprovalsCount;
    }
}
