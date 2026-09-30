package com.example.francosperfumemobile.backend.dtos.requestdtos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestFilterDTO {
    @SerializedName("pageCount")
    private int PageCount = 1;

    @SerializedName("pageSize")
    private int PageSize = 20;

    @SerializedName("search")
    private String Search;

    @SerializedName("fromBranchId")
    private Integer FromBranchId;

    @SerializedName("toBranchId")
    private Integer ToBranchId;

    @SerializedName("requestStatus")
    private String RequestStatus;

    @SerializedName("direction")
    private String Direction;

    @SerializedName("stage")
    private String Stage;

    @SerializedName("fromDate")
    private String FromDate;

    @SerializedName("toDate")
    private String ToDate;

    private List<DisplayRequestApprovalDetailsDTO> approvalDetailsDTOS;
    public int getPageCount() {
        return PageCount;
    }

    public void setPageCount(int pageCount) {
        PageCount = pageCount;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public String getSearch() {
        return Search;
    }

    public void setSearch(String search) {
        Search = search;
    }

    public Integer getFromBranchId() {
        return FromBranchId;
    }

    public void setFromBranchId(Integer fromBranchId) {
        FromBranchId = fromBranchId;
    }

    public Integer getToBranchId() {
        return ToBranchId;
    }

    public void setToBranchId(Integer toBranchId) {
        ToBranchId = toBranchId;
    }

    public String getRequestStatus() {
        return RequestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        RequestStatus = requestStatus;
    }
    public String getDirection(){return Direction;}
    public void setDirection(String direction){Direction = direction;}
    public String getStage(){return Stage;}
    public void setStage(String stage){Stage = stage;}

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }
    public List<DisplayRequestApprovalDetailsDTO> getApprovalDetailsDTOS() {
        return approvalDetailsDTOS;
    }

    public void setApprovalDetailsDTOS(List<DisplayRequestApprovalDetailsDTO> approvalDetailsDTOS) {
        this.approvalDetailsDTOS = approvalDetailsDTOS;
    }
}