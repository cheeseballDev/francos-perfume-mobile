package com.example.francosperfumemobile.backend.dtos.requestdtos;

public class RequestSummaryDTO {

    private int RequestId;
    private int ProductCount;
    private int TotalUnits;

    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }

    public int getProductCount() {
        return ProductCount;
    }

    public void setProductCount(int productCount) {
        ProductCount = productCount;
    }

    public int getTotalUnits() {
        return TotalUnits;
    }

    public void setTotalUnits(int totalUnits) {
        TotalUnits = totalUnits;
    }
}