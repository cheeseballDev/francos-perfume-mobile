package com.example.francosperfumemobile.backend.repository;

import android.content.Context;

import com.example.francosperfumemobile.backend.endpoints.ProductAPIEndpoints;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.ProductResponse;
import com.example.francosperfumemobile.backend.retrofit.RetrofitClient;

import retrofit2.Call;

public class ProductRepository {

    private final ProductAPIEndpoints productApi;

    public ProductRepository(Context context) {
        this.productApi = RetrofitClient.getInstance(context).create(ProductAPIEndpoints.class);
    }
    public Call<ProductResponse> getProducts() {
        return productApi.getProducts();
    }
}