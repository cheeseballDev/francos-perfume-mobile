package com.example.francosperfumemobile.backend.responses.requestresponses;

import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;

import java.util.List;

public class RequestListResponse {

    private int totalRequests;
    private int totalRequestPages;
    private int pageCount;
    private int pageSize;
    private List<DisplayRequestDTO> data;
    public int getTotalRequests() {
        return totalRequests;
    }
    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }
    public int getTotalRequestPages() {
        return totalRequestPages;
    }
    public void setTotalRequestPages(int totalRequestPages) {
        this.totalRequestPages = totalRequestPages;
    }
    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public List<DisplayRequestDTO> getData() {
        return data;
    }
    public void setData(List<DisplayRequestDTO> data) {
        this.data = data;
    }
}