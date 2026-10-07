package com.example.francosperfumemobile.backend.dtos.deliverydtos;

public class DeliveryFilterDTO {
    private int PageCount = 1;
    private int PageSize = 20;
    private String Search;
    private String FromBranch;
    private String ToBranch;
    private String Status;
    private String FromDate;
    private String ToDate;
    private String Direction;

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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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

    public String getFromBranch() {
        return FromBranch;
    }

    public void setFromBranch(String fromBranch) {
        FromBranch = fromBranch;
    }

    public String getToBranch() {
        return ToBranch;
    }

    public void setToBranch(String toBranch) {
        ToBranch = toBranch;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }


}