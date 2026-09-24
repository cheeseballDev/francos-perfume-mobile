package com.example.francosperfumemobile.backend.dtos.requestdtos;

import java.util.Date;

public class DisplayRequestApprovalDetailsDTO {
    private String Stage;
    private String Status;
    private String Remarks;
    private Date ApprovedAt;
    private String Approver;
    public String RequesterEmployeeId;
    public String RequesterEmployeeRole;
    public String FromBranchName;
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

    public Date getApprovedAt() {
        return ApprovedAt;
    }

    public void setApprovedAt(Date approvedAt) {
        ApprovedAt = approvedAt;
    }

    public String getApprover() {
        return Approver;
    }

    public void setApprover(String approver) {
        Approver = approver;
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