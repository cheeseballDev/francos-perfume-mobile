package com.example.francosperfumemobile.backend.responses.KPIResponses;

import com.google.gson.annotations.SerializedName;

public class InventoryCountKPIResponse {
    @SerializedName("inventoryCount")
    private int inventoryCount;

    public int getInventoryCount() { return inventoryCount; }
}
