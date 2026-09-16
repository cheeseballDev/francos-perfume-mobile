package com.example.francosperfumemobile.backend.responses.requestresponses;

import com.example.francosperfumemobile.backend.dtos.requestdtos.*;

import java.util.List;

public class RequestFiltersResponse {
    private List<RequestFilterDTO> data;

    public List<RequestFilterDTO> getData() { return data; }
    public void setData(List<RequestFilterDTO> data) { this.data = data; }
}