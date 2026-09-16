package com.example.francosperfumemobile.backend.responses.requestresponses;

import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;

import java.util.List;

public class RequestListResponse {
    private List<DisplayRequestDTO> data;

    public List<DisplayRequestDTO> getData() { return data; }
    public void setData(List<DisplayRequestDTO> data) { this.data = data; }
}