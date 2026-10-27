package com.example.francosperfumemobile.backend.dtos.requestdtos;

import com.google.gson.annotations.SerializedName;

/** One row of the "pending approvals" KPI: a request waiting at some approval stage. */
public class PendingApprovalDTO {
    @SerializedName("approvalId")
    private int approvalId;
    @SerializedName("requestId")
    private int requestId;
    @SerializedName("requestDisplayId")
    private String requestDisplayId;
    @SerializedName("stage")
    private String stage;
    @SerializedName("status")
    private String status;

    public int getApprovalId() {
        return approvalId;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getRequestDisplayId() {
        return requestDisplayId;
    }

    public String getStage() {
        return stage;
    }

    public String getStatus() {
        return status;
    }
}
