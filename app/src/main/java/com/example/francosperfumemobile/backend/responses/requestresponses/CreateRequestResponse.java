package com.example.francosperfumemobile.backend.responses.requestresponses;

import com.example.francosperfumemobile.backend.dtos.requestdtos.CreateRequestDTO;

public class CreateRequestResponse {
    private CreateRequestDTO data;

    public CreateRequestDTO getData() { return data; }
    public void setData(CreateRequestDTO data) { this.data = data; }
}