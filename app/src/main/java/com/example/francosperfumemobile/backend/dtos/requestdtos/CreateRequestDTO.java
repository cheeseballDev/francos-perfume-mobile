package com.example.francosperfumemobile.backend.dtos.requestdtos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Body of POST api/request. Same shape the website sends:
 * {fromBranch: "Sta. Lucia", toBranch: "Riverbanks", requestMessage, items: [{productId, requestedQty}]}
 * (branches are sent by name, not id).
 */
public class CreateRequestDTO {
    @SerializedName("requestMessage")
    private String RequestMessage;
    @SerializedName("fromBranch")
    private String FromBranch;
    @SerializedName("toBranch")
    private String ToBranch;
    @SerializedName("items")
    private List<CreateRequestItemDTO> Items;

    public String getRequestMessage() {
        return RequestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        RequestMessage = requestMessage;
    }

    public String getFromBranch() {
        return FromBranch;
    }

    public void setFromBranch(String fromBranch) {
        FromBranch = fromBranch;
    }

    public String getToBranch() {
        return ToBranch;
    }

    public void setToBranch(String toBranch) {
        ToBranch = toBranch;
    }

    public List<CreateRequestItemDTO> getItems() {
        return Items;
    }

    public void setItems(List<CreateRequestItemDTO> items) {
        Items = items;
    }
}
