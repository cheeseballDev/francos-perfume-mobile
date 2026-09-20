package com.example.francosperfumemobile.backend.responses.inventoryresponses;

import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BatchResponse {

    @SerializedName("productId")
    private int ProductId;
    @SerializedName("inventoryTotal")
    private int InventoryTotal;
    @SerializedName("batchTotal")
    private int BatchTotal;
    @SerializedName("batches")
    private List<DisplayBatchDTO> Batches;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getInventoryTotal() {
        return InventoryTotal;
    }

    public void setInventoryTotal(int inventoryTotal) {
        InventoryTotal = inventoryTotal;
    }

    public int getBatchTotal() {
        return BatchTotal;
    }

    public void setBatchTotal(int batchTotal) {
        BatchTotal = batchTotal;
    }

    public List<DisplayBatchDTO> getBatches() {
        return Batches;
    }

    public void setBatches(List<DisplayBatchDTO> batches) {
        Batches = batches;
    }
}