package com.example.francosperfumemobile.backend.endpoints;

import com.example.francosperfumemobile.backend.responses.KPIResponses.InventoryCountKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.LowStockKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.PendingRequestsKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.RequestsInUserStageKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.UpcomingDeliveriesKPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
public interface KPIAPIEndpoints {
    @GET("api/kpi/inventory/low-stock")
    Call<LowStockKPIResponse> getLowStockKPI();

    @GET("api/kpi/request/pending")
    Call<RequestsInUserStageKPIResponse> getRequestsInUserStageKPI();

    @GET("api/kpi/delivery/upcoming")
    Call<UpcomingDeliveriesKPIResponse> getUpcomingDeliveriesKPI();

    @GET("api/kpi/request/pending/approval")
    Call<PendingRequestsKPIResponse> getPendingApprovalsKPI();
    @GET("api/kpi/inventory/stock-count")
    Call<InventoryCountKPIResponse> getInventoryStockCountKPI();
}