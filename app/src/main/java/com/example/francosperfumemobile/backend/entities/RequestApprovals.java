package com.example.francosperfumemobile.backend.entities;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class RequestApprovals {

    @SerializedName("approvalId")
    private int ApprovalId;

    @SerializedName("requestId")
    private int RequestId;

    @SerializedName("stage")
    private String Stage;

    @SerializedName("approverId")
    private Integer ApproverId;

    @SerializedName("approvedAt")
    private Date ApprovedAt;

    @SerializedName("remarks")
    private String Remarks;

    @SerializedName("status")
    private String Status;

    public RequestApprovals() {
    }

    public int getApprovalId() {
        return ApprovalId;
    }

    public void setApprovalId(int approvalId) {
        ApprovalId = approvalId;
    }

    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }

    public String getStage() {
        return Stage;
    }

    public void setStage(String stage) {
        Stage = stage;
    }

    public Integer getApproverId() {
        return ApproverId;
    }

    public void setApproverId(Integer approverId) {
        ApproverId = approverId;
    }

    public Date getApprovedAt() {
        return ApprovedAt;
    }

    public void setApprovedAt(Date approvedAt) {
        ApprovedAt = approvedAt;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}