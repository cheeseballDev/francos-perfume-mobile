package com.example.francosperfumemobile.backend.endpoints;

import com.example.francosperfumemobile.backend.dtos.deliverydtos.*;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.*;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface DeliveryAPIEndpoints {

    @GET("")
    Call<DeliveryListResponse> getDeliveries(@QueryMap Map<String, String> filter);

    @POST("{requestId}")
    Call<CreateDeliveryResponse> createDeliveryFromRequest(@Path("requestId") int requestId);

    @POST("{deliveryId}/dispatch")
    Call<DispatchDeliveryResponse> dispatchDelivery(@Path("deliveryId") int deliveryId);

    @POST("{deliveryId}/receive")
    Call<ReceiveDeliveryResponse> receiveDelivery(
            @Path("deliveryId") int deliveryId,
            @Body ReceiveDeliveryDTO dto);

    @PATCH("{requestId}/close")
    Call<CloseRequestResponse> closeRequest(@Path("requestId") int requestId);

    @GET("{deliveryId}")
    Call<DeliveryDetailResponse> getDeliveryDetails(@Path("deliveryId") int deliveryId);

    @POST("{deliveryId}/cancel")
    Call<CancelDeliveryResponse> cancelDelivery(@Path("deliveryId") int deliveryId);

    @GET("filters")
    Call<DeliveryFiltersResponse> getDeliveryFilters();
}