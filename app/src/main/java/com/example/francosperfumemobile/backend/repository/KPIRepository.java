package com.example.francosperfumemobile.backend.repository;

import android.content.Context;

import com.example.francosperfumemobile.backend.endpoints.KPIAPIEndpoints;
import com.example.francosperfumemobile.backend.responses.KPIResponses.InventoryCountKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.LowStockKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.PendingRequestsKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.RequestsInUserStageKPIResponse;
import com.example.francosperfumemobile.backend.responses.KPIResponses.UpcomingDeliveriesKPIResponse;
import com.example.francosperfumemobile.backend.retrofit.RetrofitClient;

import retrofit2.Call;

public class KPIRepository {
    private final KPIAPIEndpoints kpiApi;

    public KPIRepository(Context context) {
        this.kpiApi = RetrofitClient.getInstance(context).create(KPIAPIEndpoints.class);
    }

    public Call<LowStockKPIResponse> getLowStockKPI() {
        return kpiApi.getLowStockKPI();
    }

    public Call<RequestsInUserStageKPIResponse> getRequestsInUserStageKPI() {
        return kpiApi.getRequestsInUserStageKPI();
    }

    public Call<InventoryCountKPIResponse> getInventoryStockCountKPI() {
        return kpiApi.getInventoryStockCountKPI();
    }

    public Call<PendingRequestsKPIResponse> getPendingApprovalsKPI() {
        return kpiApi.getPendingApprovalsKPI();
    }

    public Call<UpcomingDeliveriesKPIResponse> getUpcomingDeliveriesKPI(){
        return kpiApi.getUpcomingDeliveriesKPI();
    }
}
