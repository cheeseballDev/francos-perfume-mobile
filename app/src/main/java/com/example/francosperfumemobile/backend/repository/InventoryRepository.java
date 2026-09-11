package com.example.francosperfumemobile.backend.repository;

import com.example.francosperfumemobile.backend.dtos.inventorydtos.AddInventoryDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryBatchDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.EditBatchDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.ProductFilterDTO;
import com.example.francosperfumemobile.backend.endpoints.InventoryAPIEndpoints;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.BatchResponse;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.FilterResponse;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryResponse;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.backend.retrofit.RetrofitClient;

import retrofit2.Call;

public class InventoryRepository {

    private final InventoryAPIEndpoints inventoryApi;

    public InventoryRepository() {
        this.inventoryApi = RetrofitClient.getInstance().create(InventoryAPIEndpoints.class);
    }

    public Call<InventoryResponse> displayInventory(ProductFilterDTO filter) {
        return inventoryApi.getInventory(
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
        return inventoryApi.getInventoryItemDetails(productId);
    }

    public Call<BatchResponse> getInventoryBatches(DisplayInventoryBatchDTO dto) {
        return inventoryApi.getInventoryBatches(
                dto.getProductId(),
                dto.getBranchId()
        );
    }

    public Call<Void> updateInventoryBatch(int batchId, EditBatchDTO dto) {
        return inventoryApi.updateInventoryBatch(batchId, dto);
    }

    public Call<Void> addNewInventory(AddInventoryDTO dto) {
        return inventoryApi.addNewInventory(dto);
    }

    public Call<FilterResponse> getInventoryFilters() {
        return inventoryApi.getInventoryFilters();
    }
}