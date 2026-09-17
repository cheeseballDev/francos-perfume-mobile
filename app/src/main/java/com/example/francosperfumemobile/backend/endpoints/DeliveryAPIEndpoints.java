package com.example.francosperfumemobile.backend.endpoints;

import com.example.francosperfumemobile.backend.dtos.deliverydtos.*;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.*;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface DeliveryAPIEndpoints {

    @GET("")
    Call<DeliveryListResponse> getDeliveries(
            @Query("PageNumber") int pageNumber,
            @Query("PageSize") int pageSize,
            @Query("Search") String search,
            @Query("FromBranchId") Integer fromBranchId,
            @Query("ToBranchId") Integer toBranchId,
            @Query("DeliveryStatus") String deliveryStatus,
            @Query("FromDate") String fromDate,
            @Query("ToDate") String toDate
    );


    @POST("{deliveryId}/dispatch")
    Call<DispatchDeliveryResponse> dispatchDelivery(@Path("deliveryId") int deliveryId);

    @POST("{deliveryId}/receive")
    Call<ReceiveDeliveryResponse> receiveDelivery(
            @Path("deliveryId") int deliveryId,
            @Body ReceiveDeliveryDTO dto);

    @GET("{deliveryId}")
    Call<DeliveryDetailResponse> getDeliveryDetails(@Path("deliveryId") int deliveryId);

    @POST("{deliveryId}/cancel")
    Call<CancelDeliveryResponse> cancelDelivery(@Path("deliveryId") int deliveryId);

    @GET("filters")
    Call<DeliveryFiltersResponse> getDeliveryFilters();
}