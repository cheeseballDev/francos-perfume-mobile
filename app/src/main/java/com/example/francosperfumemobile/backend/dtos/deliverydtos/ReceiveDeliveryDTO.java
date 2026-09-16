package com.example.francosperfumemobile.backend.dtos.deliverydtos;

import java.util.List;

public class ReceiveDeliveryDTO {
    private String ExpiryDate;
    private List<ReceiveDeliveryItemDTO> Items;

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public List<ReceiveDeliveryItemDTO> getItems() {
        return Items;
    }

    public void setItems(List<ReceiveDeliveryItemDTO> items) {
        Items = items;
    }
}