package com.example.francosperfumemobile.backend.dtos.requestdtos;

public class RequestFilterDTO {
    private int PageNumber;
    private int PageSize;
    private String SearchTerm;
    private Integer FromBranchId;
    private Integer ToBranchId;
    private String RequestStatus;
    private java.util.Date StartDate;
    private java.util.Date EndDate;

    public int getPageNumber() {
        return PageNumber;
    }

    public void setPageNumber(int pageNumber) {
        PageNumber = pageNumber;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public String getSearchTerm() {
        return SearchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        SearchTerm = searchTerm;
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

    public java.util.Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(java.util.Date startDate) {
        StartDate = startDate;
    }

    public java.util.Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(java.util.Date endDate) {
        EndDate = endDate;
    }
}