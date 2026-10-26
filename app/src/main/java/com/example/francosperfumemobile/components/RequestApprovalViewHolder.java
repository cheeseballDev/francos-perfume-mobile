package com.example.francosperfumemobile.components;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestApprovalDetailsDTO;
import com.example.francosperfumemobile.helpers.DateUtils;
import com.example.francosperfumemobile.helpers.StatusStyle;

// One step of the request timeline (approvals, dispatch, transit)
public class RequestApprovalViewHolder extends RecyclerView.ViewHolder {

    ImageView timelineIcon;
    View iconTile, line;
    TextView title, subtitle, status, remarks, date;

    public RequestApprovalViewHolder(View view) {
        super(view);
        timelineIcon = view.findViewById(R.id.image_view_timeline_icon);
        iconTile = view.findViewById(R.id.layout_timeline_icon);
        line = view.findViewById(R.id.line_top);
        title = view.findViewById(R.id.text_view_timeline_title);
        subtitle = view.findViewById(R.id.text_view_timeline_subtitle);
        status = view.findViewById(R.id.text_view_timeline_status);
        remarks = view.findViewById(R.id.text_view_timeline_remarks);
        date = view.findViewById(R.id.text_view_timeline_date);
    }

    public void bind(DisplayRequestApprovalDetailsDTO approvalRequest) {
        bind(approvalRequest, false);
    }

    public void bind(DisplayRequestApprovalDetailsDTO approvalRequest, boolean isLast) {
        String approvalStatus = StatusStyle.normalize(approvalRequest.getStatus());
        String approvalStage = StatusStyle.normalize(approvalRequest.getStage());

        title.setText(approvalStage.isEmpty() ? "—" : StatusStyle.label(approvalStage));
        subtitle.setText(getSubtitleText(approvalStage, approvalStatus, approvalRequest.getApproverName()));
        StatusStyle.apply(status, approvalStatus.isEmpty() ? "PENDING" : approvalStatus);

        String note = approvalRequest.getRemarks();
        if (note != null && !note.trim().isEmpty() && !"N/A".equalsIgnoreCase(note.trim())) {
            remarks.setText(itemView.getContext().getString(R.string.quoted_format, note.trim()));
            remarks.setVisibility(View.VISIBLE);
        } else {
            remarks.setVisibility(View.GONE);
        }

        if (approvalRequest.getApprovedAt() != null && !approvalRequest.getApprovedAt().isEmpty() && !"PENDING".equals(approvalStatus)) {
            date.setText(DateUtils.formatDateTime(approvalRequest.getApprovedAt()));
            date.setVisibility(View.VISIBLE);
        } else {
            date.setVisibility(View.GONE);
        }

        line.setVisibility(isLast ? View.INVISIBLE : View.VISIBLE);
        setTimelineIcon(approvalStatus);
    }

    private String getSubtitleText(String stage, String status, String approvedBy) {
        String who = approvedBy == null || approvedBy.trim().isEmpty() ? "—" : approvedBy;
        if ("DISPATCH".equals(stage)) {
            return "DISPATCHED".equals(status) ? "Dispatched by: " + who : "Waiting to be dispatched";
        }
        if ("TRANSIT".equals(stage)) {
            if ("IN TRANSIT".equals(status)) return "Sent by: " + who + "\nBe ready to receive the perfumes.";
            return "PENDING".equals(status) ? "Waiting to be sent" : "Items delivered";
        }
        switch (status) {
            case "PENDING":
            case "":
                return "Waiting for approval";
            case "REJECTED":
                return "Rejected by: " + who;
            case "CANCELLED":
                return "Cancelled";
            case "BYPASSED":
                return "Skipped";
            default:
                return "Approved by: " + who;
        }
    }

    public void setTimelineIcon(String status) {
        StatusStyle.Tone tone = StatusStyle.toneOf(status);
        int icon;
        switch (tone) {
            case SUCCESS:
                icon = R.drawable.ic_check;
                break;
            case DANGER:
                icon = R.drawable.ic_x;
                break;
            case PROGRESS:
                icon = R.drawable.ic_truck;
                break;
            case PENDING:
                icon = R.drawable.ic_clock;
                break;
            default:
                icon = R.drawable.ic_minus;
        }
        int color = ContextCompat.getColor(itemView.getContext(), StatusStyle.textColor(tone));
        timelineIcon.setImageResource(icon);
        timelineIcon.setImageTintList(ColorStateList.valueOf(color));
        iconTile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), StatusStyle.bgColor(tone))));
    }
}
