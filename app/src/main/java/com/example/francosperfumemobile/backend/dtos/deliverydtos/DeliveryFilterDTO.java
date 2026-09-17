package com.example.francosperfumemobile.backend.dtos.deliverydtos;

public class DeliveryFilterDTO {
    private int PageCount = 1;
    private int PageSize = 20;
    private String Search;
    private Integer FromBranchId;
    private Integer ToBranchId;
    private String DeliveryStatus;
    private String FromDate;
    private String ToDate;

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

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

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
}