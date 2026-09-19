package com.example.francosperfumemobile.backend.responses.inventoryresponses;

import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InventoryResponse {
    @SerializedName("totalInventories")
    private int totalInventories;
    @SerializedName("totalInventoriesPages")
    private int totalInventoriesPages;
    @SerializedName("pageCount")
    private int pageCount;
    @SerializedName("pageSize")
    private int pageSize;
    @SerializedName("data")
    private List<DisplayInventoryDTO> data;

    public int getTotalInventories() { return totalInventories; }
    public void setTotalInventories(int totalInventories) { this.totalInventories = totalInventories; }
    public int getTotalInventoriesPages() {
        return totalInventoriesPages;
    }

    public void setTotalInventoriesPages(int totalInventoriesPages) { this.totalInventoriesPages = totalInventoriesPages; }

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

    public List<DisplayInventoryDTO> getData() {
        return data;
    }

    public void setData(List<DisplayInventoryDTO> data) {
        this.data = data;
    }
}