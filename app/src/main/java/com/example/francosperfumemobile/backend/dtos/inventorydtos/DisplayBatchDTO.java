package com.example.francosperfumemobile.backend.dtos.inventorydtos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DisplayBatchDTO implements Parcelable {
    @SerializedName("batchId")
    private int BatchId;

    @SerializedName("batchDisplayId")
    private String BatchDisplayId;

    @SerializedName("batchItemId")
    private int BatchItemId;

    @SerializedName("productId")
    private int ProductId;

    @SerializedName("productName")
    private String ProductName;

    @SerializedName("branchId")
    private int BranchId;

    @SerializedName("branchLocation")
    private String BranchLocation;

    @SerializedName("quantity")
    private int Quantity;

    @SerializedName("createdAt")
    private String CreatedAt;

    @SerializedName("expiryDate")
    private String ExpiryDate;


    protected DisplayBatchDTO(Parcel in) {
        BatchId = in.readInt();
        BatchDisplayId = in.readString();
        BatchItemId = in.readInt();
        ProductId = in.readInt();
        ProductName = in.readString();
        BranchId = in.readInt();
        BranchLocation = in.readString();
        Quantity = in.readInt();
        CreatedAt = in.readString();
        ExpiryDate = in.readString();
    }

    public static final Creator<DisplayBatchDTO> CREATOR = new Creator<DisplayBatchDTO>() {
        @Override
        public DisplayBatchDTO createFromParcel(Parcel in) {
            return new DisplayBatchDTO(in);
        }

        @Override
        public DisplayBatchDTO[] newArray(int size) {
            return new DisplayBatchDTO[size];
        }
    };

    public int getBatchId() {
        return BatchId;
    }

    public void setBatchId(int batchId) {
        BatchId = batchId;
    }

    public String getBatchDisplayId() {
        return BatchDisplayId;
    }

    public void setBatchDisplayId(String batchDisplayId) {
        BatchDisplayId = batchDisplayId;
    }

    public int getBatchItemId() {
        return BatchItemId;
    }

    public void setBatchItemId(int batchItemId) {
        BatchItemId = batchItemId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getBranchId() {
        return BranchId;
    }

    public void setBranchId(int branchId) {
        BranchId = branchId;
    }

    public String getBranchLocation() {
        return BranchLocation;
    }

    public void setBranchLocation(String branchLocation) {
        BranchLocation = branchLocation;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }


    //idk wtf these do but apparently these needs to be generated for me to turn this to a normal class as said by the error thingy
    //using abstract results in the backend not returning shit
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(BatchId);
        dest.writeString(BatchDisplayId);
        dest.writeInt(BatchItemId);
        dest.writeInt(ProductId);
        dest.writeString(ProductName);
        dest.writeInt(BranchId);
        dest.writeString(BranchLocation);
        dest.writeInt(Quantity);
        dest.writeString(CreatedAt);
        dest.writeString(ExpiryDate);
    }
}