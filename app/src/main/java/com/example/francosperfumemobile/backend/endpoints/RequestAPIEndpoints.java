package com.example.francosperfumemobile.backend.endpoints;

import com.example.francosperfumemobile.backend.dtos.requestdtos.*;
import com.example.francosperfumemobile.backend.responses.requestresponses.*;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface RequestAPIEndpoints {

    @GET("api/request")
    Call<RequestListResponse> getRequests(
            @Query("PageCount") int pageCount,
            @Query("PageSize") int pageSize,
            @Query("Search") String search,
            @Query("FromBranchId") Integer fromBranchId,
            @Query("ToBranchId") Integer toBranchId,
            @Query("RequestStatus") String requestStatus,
            @Query("Direction") String direction,
            @Query("FromDate") String fromDate,
            @Query("ToDate") String toDate
    );

    @GET("api/request/{id}")
    Call<RequestDetailResponse> getRequestDetails(
            @Path("id") int requestId
    );

    @POST("api/request")
    Call<CreateRequestResponse> createRequest(
            @Body CreateRequestDTO dto
    );

    @PATCH("api/request/{requestId}/approve")
    Call<ApprovalResponse> approveRequest(
            @Path("requestId") int requestId,
            @Body List<ApproveRequestDTO> approvals
    );

    @PATCH("api/request/{requestId}/reject")
    Call<RejectResponse> rejectRequest(
            @Path("requestId") int requestId,
            @Body RejectRequestDTO dto
    );

    @PATCH("api/request/{requestId}/cancel")
    Call<CancelResponse> cancelRequest(
            @Path("requestId") int requestId
    );

    @GET("api/request/filters")
    Call<RequestFiltersResponse> getRequestFilters();

    @GET("api/request/{requestId}/summary")
    Call<RequestSummaryResponse> getRequestSummary(
            @Path("requestId") int requestId
    );
}
