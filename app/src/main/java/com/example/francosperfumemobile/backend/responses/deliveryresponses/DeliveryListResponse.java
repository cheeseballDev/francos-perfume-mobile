package com.example.francosperfumemobile.backend.responses.deliveryresponses;

import com.example.francosperfumemobile.backend.dtos.deliverydtos.*;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeliveryListResponse {

    @SerializedName("totalDeliveries")
    private int totalDeliveries;
    @SerializedName("totalDeliveryPages")
    private int totalDeliveryPages;
    @SerializedName("pageCount")
    private int pageCount;
    @SerializedName("pageSize")

    private int pageSize;
    public int getTotalDeliveries() {
        return totalDeliveries;
    }
    public void setTotalDeliveries(int totalDeliveries) {
        this.totalDeliveries = totalDeliveries;
    }
    public int getTotalDeliveryPages() {
        return totalDeliveryPages;
    }
    public void setTotalDeliveryPages(int totalDeliveryPages) {
        this.totalDeliveryPages = totalDeliveryPages;
    }
    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    @SerializedName("data")

    private List<DisplayDeliveryDTO> data;

    public List<DisplayDeliveryDTO> getData() { return data; }
    public void setData(List<DisplayDeliveryDTO> data) { this.data = data; }
}