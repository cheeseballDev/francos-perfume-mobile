package com.example.francosperfumemobile.backend.dtos.requestdtos;

import java.util.List;

public class CreateRequestDTO {
    private String RequestMessage;
    private int FromBranch;
    private int ToBranch;
    private List<CreateRequestItemDTO> Items;

    public String getRequestMessage() {
        return RequestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        RequestMessage = requestMessage;
    }

    public int getFromBranch() {
        return FromBranch;
    }

    public void setFromBranch(int fromBranch) {
        FromBranch = fromBranch;
    }

    public int getToBranch() {
        return ToBranch;
    }

    public void setToBranch(int toBranch) {
        ToBranch = toBranch;
    }

    public List<CreateRequestItemDTO> getItems() {
        return Items;
    }

    public void setItems(List<CreateRequestItemDTO> items) {
        Items = items;
    }
}