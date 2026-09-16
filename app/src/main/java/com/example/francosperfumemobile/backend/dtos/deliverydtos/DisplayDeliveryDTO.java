package com.example.francosperfumemobile.backend.dtos.deliverydtos;

import java.util.Date;
import java.util.List;

public class DisplayDeliveryDTO {
    private int DeliveryId;
    private int RequestId;
    private String DeliveryDisplayId;
    private String RequestDisplayId;
    private String Direction;
    private String DeliveryStatus;
    private Date CreatedAt;
    private String FromBranchName;
    private String ToBranchName;
    private int ItemCount;
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

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
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