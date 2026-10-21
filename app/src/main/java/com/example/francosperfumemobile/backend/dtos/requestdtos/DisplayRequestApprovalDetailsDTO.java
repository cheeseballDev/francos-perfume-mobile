package com.example.francosperfumemobile.backend.dtos.requestdtos;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DisplayRequestApprovalDetailsDTO {
    @SerializedName("stage")
    private String Stage;
    @SerializedName("status")
    private String Status;
    @SerializedName("remarks")
    private String Remarks;
    @SerializedName("approvedAt")
    private String ApprovedAt;
    @SerializedName("approverId")
    private String ApproverId;
    @SerializedName("approverName")
    private String ApproverName;
    @SerializedName("requesterEmployeeId")
    public String RequesterEmployeeId;
    @SerializedName("requesterEmployeeRole")
    public String RequesterEmployeeRole;
    @SerializedName("fromBranchName")
    public String FromBranchName;
    @SerializedName("toBranchName")
    public String ToBranchName;

    public String getStage() {
        return Stage;
    }

    public void setStage(String stage) {
        Stage = stage;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getApprovedAt() {
        return ApprovedAt;
    }

    public void setApprovedAt(String approvedAt) {
        ApprovedAt = approvedAt;
    }

    public String getApproverId() {
        return ApproverId;
    }

    public void setApproverId(String approverId) {
        ApproverId = approverId;
    }
    public String getApproverName() {
        return ApproverName;
    }

    public void setApproverName(String approverName) {
        ApproverName = approverName;
    }


    public String getRequesterEmployeeId() {
        return RequesterEmployeeId;
    }

    public String getRequesterEmployeeRole() {
        return RequesterEmployeeRole;
    }

    public String getFromBranchName() {
        return FromBranchName;
    }

    public String getToBranchName() {
        return ToBranchName;
    }
}