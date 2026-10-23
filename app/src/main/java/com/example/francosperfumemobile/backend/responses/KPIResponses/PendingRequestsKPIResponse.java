package com.example.francosperfumemobile.backend.responses.KPIResponses;

import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingRequestsKPIResponse {
    @SerializedName("pendingApporvalsList")
    private List<DisplayRequestDTO> pendingApporvalsList;
    @SerializedName("pendingApprovalsCount")
    private int pendingApprovalsCount;

    public List<DisplayRequestDTO> getPendingApporvalsList() {
        return pendingApporvalsList;
    }

    public int getPendingApprovalsCount() {
        return pendingApprovalsCount;
    }
}
