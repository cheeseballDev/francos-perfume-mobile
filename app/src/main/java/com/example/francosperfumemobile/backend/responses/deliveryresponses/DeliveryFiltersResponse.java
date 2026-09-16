package com.example.francosperfumemobile.backend.responses.deliveryresponses;

import com.example.francosperfumemobile.backend.dtos.deliverydtos.*;
import java.util.List;

public class DeliveryFiltersResponse {
    private List<DeliveryFilterDTO> data;

    public List<DeliveryFilterDTO> getData() { return data; }
    public void setData(List<DeliveryFilterDTO> data) { this.data = data; }
}