package com.example.francosperfumemobile.backend.dtos.deliverydtos;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class DisplayDeliveryDetailsDTO {
    @SerializedName("deliveryId")
    private int DeliveryId;
    @SerializedName("deliveryDisplayId")
    private String DeliveryDisplayId;
    @SerializedName("deliveryStatus")
    private String DeliveryStatus;
    @SerializedName("direction")
    private String Direction;
    @SerializedName("fromBranchName")
    private String FromBranchName;

    @SerializedName("toBranchName")
    private String ToBranchName;
    @SerializedName("createdAt")
    private String CreatedAt;
    @SerializedName("createdBy")
    private String CreatedBy;
    private List<DeliveryItemDTO> Items;

    public int getDeliveryId() {
        return DeliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        DeliveryId = deliveryId;
    }

    public String getDeliveryDisplayId() {
        return DeliveryDisplayId;
    }

    public void setDeliveryDisplayId(String deliveryDisplayId) {
        DeliveryDisplayId = deliveryDisplayId;
    }

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
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

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public List<DeliveryItemDTO> getItems() {
        return Items;
    }

    public void setItems(List<DeliveryItemDTO> items) {
        Items = items;
    }
}