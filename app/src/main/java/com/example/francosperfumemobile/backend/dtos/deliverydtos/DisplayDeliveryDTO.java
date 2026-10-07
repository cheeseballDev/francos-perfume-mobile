package com.example.francosperfumemobile.backend.dtos.deliverydtos;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class DisplayDeliveryDTO {
    @SerializedName("deliveryId")
    private int DeliveryId;
    @SerializedName("requestId")
    private int RequestId;
    @SerializedName("deliveryDisplayId")
    private String DeliveryDisplayId;
    @SerializedName("requestDisplayId")
    private String RequestDisplayId;
    @SerializedName("direction")
    private String Direction;
    @SerializedName("deliveryStatus")
    private String DeliveryStatus;
    @SerializedName("createdAt")
    private String CreatedAt;
    @SerializedName("fromBranchName")
    private String FromBranchName;
    @SerializedName("toBranchName")
    private String ToBranchName;
    @SerializedName("itemCount")
    private int ItemCount;
    @SerializedName("totalUnits")
    private int TotalUnits;
    private List<DeliveryItemDTO> Items;

    public int getDeliveryId() {
        return DeliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        DeliveryId = deliveryId;
    }

    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }

    public String getDeliveryDisplayId() {
        return DeliveryDisplayId;
    }

    public void setDeliveryDisplayId(String deliveryDisplayId) {
        DeliveryDisplayId = deliveryDisplayId;
    }

    public String getRequestDisplayId() {
        return RequestDisplayId;
    }

    public void setRequestDisplayId(String requestDisplayId) {
        RequestDisplayId = requestDisplayId;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getFromBranchName() {
        return FromBranchName;
    }

    public void setFromBranchName(String fromBranchName) {
        FromBranchName = fromBranchName;
    }

    public String getToBranchName() {
        return ToBranchName;
    }

    public void setToBranchName(String toBranchName) {
        ToBranchName = toBranchName;
    }

    public int getItemCount() {
        return ItemCount;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }

    public int getTotalUnits() {
        return TotalUnits;
    }

    public void setTotalUnits(int totalUnits) {
        TotalUnits = totalUnits;
    }

    public List<DeliveryItemDTO> getItems() {
        return Items;
    }

    public void setItems(List<DeliveryItemDTO> items) {
        Items = items;
    }
}