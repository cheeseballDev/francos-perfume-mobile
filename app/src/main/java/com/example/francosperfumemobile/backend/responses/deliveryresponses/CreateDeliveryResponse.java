package com.example.francosperfumemobile.backend.responses.deliveryresponses;

public class CreateDeliveryResponse {
    private int deliveryId;
    private String deliveryDisplayId;
    private String deliveryStatus;

    public int getDeliveryId() { return deliveryId; }
    public void setDeliveryId(int deliveryId) { this.deliveryId = deliveryId; }
    public String getDeliveryDisplayId() { return deliveryDisplayId; }
    public void setDeliveryDisplayId(String deliveryDisplayId) { this.deliveryDisplayId = deliveryDisplayId; }
    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }
}