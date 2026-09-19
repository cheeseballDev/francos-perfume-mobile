package com.example.francosperfumemobile.backend.repository;

import android.content.Context;

import com.example.francosperfumemobile.backend.dtos.inventorydtos.AddInventoryDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryBatchDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.EditBatchDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.InventorySearchFilterDTO;
import com.example.francosperfumemobile.backend.endpoints.InventoryAPIEndpoints;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.BatchResponse;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryFilterResponse;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryResponse;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.backend.retrofit.RetrofitClient;

import retrofit2.Call;

public class InventoryRepository {

    private final InventoryAPIEndpoints api;

    public InventoryRepository(Context context) {
        this.api = RetrofitClient.getInstance(context).create(InventoryAPIEndpoints.class);
    }

    public Call<InventoryResponse> displayInventory(InventorySearchFilterDTO filter) {
        return api.getInventory(
                filter.getSearch(),
                filter.getProductType(),
                filter.getProductGender(),
                filter.getBranch(),
                filter.getFromDate(),
                filter.getToDate(),
                filter.getPageCount(),
                filter.getPageSize()
        );
    }

    public Call<DisplayInventoryDTO> getInventoryItemDetails(int productId) {
        return api.getInventoryItemDetails(productId);
    }

    public Call<BatchResponse> getInventoryBatches(DisplayInventoryBatchDTO dto) {
        return api.getInventoryBatches(
                dto.getProductId(),
                dto.getBranchId()
        );
    }

    public Call<Void> updateInventoryBatch(int batchId, EditBatchDTO dto) {
        return api.updateInventoryBatch(batchId, dto);
    }

    public Call<Void> addNewInventory(AddInventoryDTO dto) {
        return api.addNewInventory(dto);
    }

    public Call<InventoryFilterResponse> getInventoryFilters() {
        return api.getInventoryFilters();
    }
}