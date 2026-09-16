package com.example.francosperfumemobile.backend.repository;

import android.content.Context;
import com.example.francosperfumemobile.backend.endpoints.RequestAPIEndpoints;
import com.example.francosperfumemobile.backend.dtos.requestdtos.*;
import com.example.francosperfumemobile.backend.responses.requestresponses.*;
import com.example.francosperfumemobile.backend.retrofit.RetrofitClient;
import retrofit2.Call;
import java.util.Map;

public class RequestRepository {

    private final RequestAPIEndpoints api;

    public RequestRepository(Context context) {
        this.api = RetrofitClient.getInstance(context).create(RequestAPIEndpoints.class);
    }

    public Call<RequestListResponse> getRequests(Map<String, String> filter) {
        return api.getRequests(filter);
    }

    public Call<RequestDetailResponse> getRequestDetails(int requestId) {
        return api.getRequestDetails(requestId);
    }

    public Call<CreateRequestResponse> createRequest(CreateRequestDTO dto) {
        return api.createRequest(dto);
    }

    public Call<ApprovalResponse> approveRequest(int requestId,
                                                 java.util.List<ApproveRequestDTO> approvals) {
        return api.approveRequest(requestId, approvals);
    }

    public Call<RejectResponse> rejectRequest(int requestId, RejectRequestDTO dto) {
        return api.rejectRequest(requestId, dto);
    }

    public Call<CancelResponse> cancelRequest(int requestId) {
        return api.cancelRequest(requestId);
    }

    public Call<RequestFiltersResponse> getRequestFilters() {
        return api.getRequestFilters();
    }

    public Call<RequestSummaryResponse> getRequestSummary(int requestId) {
        return api.getRequestSummary(requestId);
    }
}