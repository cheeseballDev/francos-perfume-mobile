package com.example.francosperfumemobile.backend.dtos.inventorydtos;

public class InventorySearchFilterDTO {
    private int PageCount = 1;
    private int PageSize = 20;
    private String Search = null;
    private String FromDate = null;
    private String ToDate = null;
    private String ProductType = null;
    private String ProductGender = null;
    private Integer Branch = null;
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

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public String getProductGender() {
        return ProductGender;
    }

    public void setProductGender(String productGender) {
        ProductGender = productGender;
    }

    public Integer getBranch() {
        return Branch;
    }

    public void setBranch(Integer branch) {
        Branch = branch;
    }
}