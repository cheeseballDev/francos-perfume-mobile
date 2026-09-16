package com.example.francosperfumemobile.backend.endpoints;

import com.example.francosperfumemobile.backend.dtos.requestdtos.*;
import com.example.francosperfumemobile.backend.responses.requestresponses.*;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface RequestAPIEndpoints {

    @GET("")
    Call<RequestListResponse> getRequests(@QueryMap Map<String, String> filter);

    @GET("{id}")
    Call<RequestDetailResponse> getRequestDetails(@Path("id") int requestId);

    @POST("")
    Call<CreateRequestResponse> createRequest(@Body CreateRequestDTO dto);

    @PATCH("{requestId}/approve")
    Call<ApprovalResponse> approveRequest(
            @Path("requestId") int requestId,
            @Body java.util.List<ApproveRequestDTO> approvals);

    @PATCH("{requestId}/reject")
    Call<RejectResponse> rejectRequest(
            @Path("requestId") int requestId,
            @Body RejectRequestDTO dto);

    @PATCH("{requestId}/cancel")
    Call<CancelResponse> cancelRequest(@Path("requestId") int requestId);

    @GET("filters")
    Call<RequestFiltersResponse> getRequestFilters();

    @GET("{requestId}/summary")
    Call<RequestSummaryResponse> getRequestSummary(@Path("requestId") int requestId);
}