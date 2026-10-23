package com.example.francosperfumemobile.backend.responses.KPIResponses;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LowStockKPIResponse {
    @SerializedName("lowStockProductCount")
    private int lowStockProductCount;

    @SerializedName("lowStockProductsList")
    private List<DisplayInventoryDTO> lowStockProductsList;
    public int getLowStockProductCount() { return lowStockProductCount; }
    public List<DisplayInventoryDTO> getLowStockProductsList() { return lowStockProductsList; }
}