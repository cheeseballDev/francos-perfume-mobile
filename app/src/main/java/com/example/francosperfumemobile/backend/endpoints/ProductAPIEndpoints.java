package com.example.francosperfumemobile.backend.endpoints;

import com.example.francosperfumemobile.backend.responses.inventoryresponses.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductAPIEndpoints {
    @GET("api/Products")
    Call<ProductResponse> getProducts();
}

