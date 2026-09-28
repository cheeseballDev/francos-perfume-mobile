package com.example.francosperfumemobile.components;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestApprovalDetailsDTO;

// THIS CLASS IS BASICALLY FOR THE TIMELINE
public class RequestApprovalViewHolder extends RecyclerView.ViewHolder {

    ImageView timelineIcon;
    TextView title, subtitle, status;
    public RequestApprovalViewHolder(View view) {
        super(view);
        timelineIcon = view.findViewById(R.id.image_view_timeline_icon);
        title = view.findViewById(R.id.text_view_timeline_title);
        subtitle = view.findViewById(R.id.text_view_timeline_subtitle);
        status = view.findViewById(R.id.text_view_timeline_status);
    }

    public void bind(DisplayRequestApprovalDetailsDTO approvalRequest) {


        String approvalStatus = approvalRequest.getStatus();
        String approvalStage = approvalRequest.getStage();
        String approvedBy = approvalRequest.getApprover();

        setTimelineIcon(approvalStatus);

        title.setText(approvalStage);

        subtitle.setText(getSubtitleText(approvalStage, approvalStatus, approvedBy));
        setStatusStyle(approvalStatus);

    }
    private String getSubtitleText(String stage, String status, String approvedBy) {
        if (!isDeliveryStage(stage)) {
            if (!"PENDING".equals(status)) {
                return String.format("Approved by: %s", approvedBy);
            }
            return "Waiting for approval";
        }

        if ("DISPATCH".equals(stage)) {
            if ("DISPATCHED".equals(status)) {
                return String.format("Dispatched by: %s", approvedBy);
            }
            return "Waiting to be dispatched";
        }

        if ("TRANSIT".equals(stage)) {
            if ("IN TRANSIT".equals(status)) {
                return String.format("Sent by: %s\nBe ready to receive the perfumes.", approvedBy);
            }
            return "Items delivered";
        }
        return "Unknown";
    }

    private boolean isDeliveryStage(String stage) {
        return "DISPATCH".equals(stage) || "TRANSIT".equals(stage);
    }

    private void setStatusStyle(String approvalStatus) {
        int textColorRes;
        int bgColorRes;

        switch (approvalStatus) {
            case "PENDING":
                textColorRes = R.color.status_pending_text;
                bgColorRes = R.color.status_pending_bg;
                break;

            case "REJECTED":
                textColorRes = R.color.status_rejected_text;
                bgColorRes = R.color.status_rejected_bg;
                break;

            case "IN TRANSIT":
                textColorRes = R.color.status_dispatched_text;
                bgColorRes = R.color.status_dispatched_bg;
                break;

            case "APPROVED":
            case "DISPATCHED":
            case "RECEIVED":
                textColorRes = R.color.status_approved_text;
                bgColorRes = R.color.status_approved_bg;

                break;

            default:
                textColorRes = R.color.black;
                bgColorRes = R.color.muted;
                status.setText(approvalStatus.isEmpty() ? "Unknown" : approvalStatus);
                break;
        }

        status.setBackgroundResource(R.drawable.badge_design);

        int bgColor = ContextCompat.getColor(status.getContext(), bgColorRes);
        int textColor = ContextCompat.getColor(status.getContext(), textColorRes);

        status.setBackgroundTintList(ColorStateList.valueOf(bgColor));
        status.setTextColor(textColor);
        status.setText(approvalStatus);
    }

    public void setTimelineIcon(String status) {
        timelineIcon.setImageTintList(null);
        switch (status) {
            case "PENDING":
                timelineIcon.setImageResource(R.drawable.icon_pending);
                break;
            case "APPROVED":
            case "DISPATCHED":
            case "RECEIVED":
                timelineIcon.setImageResource(R.drawable.icon_approved);
                break;
            case "REJECTED":
                timelineIcon.setImageResource(R.drawable.icon_reject);
                break;
            case "IN TRANSIT":
                timelineIcon.setImageResource(R.drawable.icon_delivery);
                int blueColor = ContextCompat.getColor(timelineIcon.getContext(), R.color.blue);
                timelineIcon.setImageTintList(ColorStateList.valueOf(blueColor));
                break;
            default:
                timelineIcon.setImageResource(R.drawable.icon_question_mark);
                break;
        }
    }

}
