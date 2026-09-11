package com.example.francosperfumemobile.backend.responses.inventoryresponses;

import com.example.francosperfumemobile.backend.entities.Inventory;
import java.util.List;

public class BatchResponse {
    private int ProductId;
    private int InventoryTotal;
    private int BatchTotal;
    private List<Inventory> Batches;
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

    public List<Inventory> getBatches() {
        return Batches;
    }

    public void setBatches(List<Inventory> batches) {
        Batches = batches;
    }
}