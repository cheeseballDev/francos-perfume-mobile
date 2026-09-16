package com.example.francosperfumemobile.backend.responses.deliveryresponses;

import com.example.francosperfumemobile.backend.dtos.deliverydtos.*;

import java.util.List;

public class DeliveryListResponse {
    private List<DisplayDeliveryDTO> data;

    public List<DisplayDeliveryDTO> getData() { return data; }
    public void setData(List<DisplayDeliveryDTO> data) { this.data = data; }
}