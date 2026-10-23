package com.example.francosperfumemobile.backend.responses.KPIResponses;

import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDTO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpcomingDeliveriesKPIResponse {
    @SerializedName("upcomingDeliveriesCount")
    private int deliveryCount;
    @SerializedName("upcomingDeliveriesList")
    private List<DisplayDeliveryDTO> deliveryList;

    public int getDeliveryCount() { return deliveryCount; }

    public List<DisplayDeliveryDTO> getDeliveryList() {
        return deliveryList;
    }
}
