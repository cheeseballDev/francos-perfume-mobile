package com.example.francosperfumemobile.backend.endpoints;

import com.example.francosperfumemobile.backend.dtos.deliverydtos.*;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.*;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface DeliveryAPIEndpoints {

    @GET("api/deliveries")
    Call<DeliveryListResponse> getDeliveries(
            @Query("PageNumber") int pageNumber,
            @Query("PageSize") int pageSize,
            @Query("Search") String search,
            @Query("FromBranch") String fromBranch,
            @Query("ToBranch") String toBranch,
            @Query("Status") String deliveryStatus,
            @Query("FromDate") String fromDate,
            @Query("ToDate") String toDate
    );


    @POST("api/deliveries/{deliveryId}/dispatch")
    Call<DispatchDeliveryResponse> dispatchDelivery(@Path("deliveryId") int deliveryId);

    @POST("api/deliveries/{deliveryId}/receive")
    Call<ReceiveDeliveryResponse> receiveDelivery(
            @Path("deliveryId") int deliveryId,
            @Body ReceiveDeliveryDTO dto);

    @GET("api/deliveries/{deliveryId}")
    Call<DeliveryDetailResponse> getDeliveryDetails(@Path("deliveryId") int deliveryId);

    @POST("api/deliveries/{deliveryId}/cancel")
    Call<CancelDeliveryResponse> cancelDelivery(@Path("deliveryId") int deliveryId);

    @GET("api/deliveries/filters")
    Call<DeliveryFiltersResponse> getDeliveryFilters();
}