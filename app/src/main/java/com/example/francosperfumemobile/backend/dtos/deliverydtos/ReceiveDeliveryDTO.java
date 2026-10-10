package com.example.francosperfumemobile.backend.dtos.deliverydtos;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class ReceiveDeliveryDTO {
    @SerializedName("expiryDate")
    private Date ExpiryDate;
    private List<ReceiveDeliveryItemDTO> Items;

    public Date getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        ExpiryDate = expiryDate;
    }

    public List<ReceiveDeliveryItemDTO> getItems() {
        return Items;
    }

    public void setItems(List<ReceiveDeliveryItemDTO> items) {
        Items = items;
    }
}