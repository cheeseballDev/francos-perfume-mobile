package com.example.francosperfumemobile.backend.repository;

import android.content.Context;
import com.example.francosperfumemobile.backend.endpoints.DeliveryAPIEndpoints;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.*;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.*;
import com.example.francosperfumemobile.backend.retrofit.RetrofitClient;
import retrofit2.Call;
import java.util.Map;

public class DeliveryRepository {

    private final DeliveryAPIEndpoints api;

    public DeliveryRepository(Context context) {
        this.api = RetrofitClient.getInstance(context).create(DeliveryAPIEndpoints.class);
    }

    public Call<DeliveryListResponse> getDeliveries(Map<String, String> filter) {
        return api.getDeliveries(filter);
    }

    public Call<CreateDeliveryResponse> createDeliveryFromRequest(int requestId) {
        return api.createDeliveryFromRequest(requestId);
    }

    public Call<DispatchDeliveryResponse> dispatchDelivery(int deliveryId) {
        return api.dispatchDelivery(deliveryId);
    }

    public Call<ReceiveDeliveryResponse> receiveDelivery(int deliveryId, ReceiveDeliveryDTO dto) {
        return api.receiveDelivery(deliveryId, dto);
    }

    public Call<CloseRequestResponse> closeRequest(int requestId) {
        return api.closeRequest(requestId);
    }

    public Call<DeliveryDetailResponse> getDeliveryDetails(int deliveryId) {
        return api.getDeliveryDetails(deliveryId);
    }

    public Call<CancelDeliveryResponse> cancelDelivery(int deliveryId) {
        return api.cancelDelivery(deliveryId);
    }

    public Call<DeliveryFiltersResponse> getDeliveryFilters() {
        return api.getDeliveryFilters();
    }
}