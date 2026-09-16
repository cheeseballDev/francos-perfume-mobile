package com.example.francosperfumemobile.backend.dtos.deliverydtos;

public class DeliveryFilterDTO {
    private int PageNumber;
    private int PageSize;
    private String Search;
    private Integer FromBranchId;
    private Integer ToBranchId;
    private String DeliveryStatus;
    private java.util.Date FromDate;
    private java.util.Date ToDate;

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

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

    public java.util.Date getFromDate() {
        return FromDate;
    }

    public void setFromDate(java.util.Date fromDate) {
        FromDate = fromDate;
    }

    public java.util.Date getToDate() {
        return ToDate;
    }

    public void setToDate(java.util.Date toDate) {
        ToDate = toDate;
    }
}