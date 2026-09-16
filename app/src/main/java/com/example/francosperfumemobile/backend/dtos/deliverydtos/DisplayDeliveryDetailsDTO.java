package com.example.francosperfumemobile.backend.dtos.deliverydtos;

import java.util.Date;
import java.util.List;

public class DisplayDeliveryDetailsDTO {
    private int DeliveryId;
    private String DeliveryDisplayId;
    private String DeliveryStatus;
    private String Direction;
    private String FromBranchName;
    private String ToBranchName;
    private Date CreatedAt;
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

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public List<DeliveryItemDTO> getItems() {
        return Items;
    }

    public void setItems(List<DeliveryItemDTO> items) {
        Items = items;
    }
}