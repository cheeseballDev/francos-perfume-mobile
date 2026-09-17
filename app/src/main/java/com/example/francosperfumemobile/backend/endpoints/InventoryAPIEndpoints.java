package com.example.francosperfumemobile.backend.endpoints;

import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryResponse;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.BatchResponse;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.EditBatchDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.AddInventoryDTO;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryFilterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InventoryAPIEndpoints {

    @GET("api/Inventory")
    Call<InventoryResponse> getInventory(
            @Query("Search") String search,
            @Query("ProductType") String productType,
            @Query("ProductGender") String productGender,
            @Query("Branch") Integer branch,
            @Query("FromDate") String fromDate,
            @Query("ToDate") String toDate,
            @Query("PageCount") int pageCount,
            @Query("PageSize") int pageSize
    );

    @GET("api/Inventory/{productId}")
    Call<DisplayInventoryDTO> getInventoryItemDetails(@Query("productId") int productId);

    @GET("api/Inventory/batch")
    Call<BatchResponse> getInventoryBatches(
            @Query("ProductId") int productId,
            @Query("BranchId") int branchId
    );

    @PATCH("api/Inventory/batch/{batchId}")
    Call<Void> updateInventoryBatch(
            @Query("batchId") int batchId,
            @Body EditBatchDTO dto
    );

    @POST("api/Inventory")
    Call<Void> addNewInventory(@Body AddInventoryDTO dto);

    @GET("api/Inventory/filters")
    Call<InventoryFilterResponse> getInventoryFilters();
}