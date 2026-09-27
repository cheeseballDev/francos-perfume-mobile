package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestApprovalDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;

public class TimelineViewHolder {

    ImageView timelineIcon;
    // title will be stage here
    TextView title, subtitle, status;
    public TimelineViewHolder(View view) {
        super(view);
        timelineIcon = view.findViewById(R.id.image_view_timeline_icon);
        title = view.findViewById(R.id.text_view_timeline_title);
        subtitle = view.findViewById(R.id.text_view_timeline_subtitle);
        status = view.findViewById(R.id.text_view_timeline_status);
    }

    public void bind(DisplayRequestApprovalDetailsDTO approvalRequest) {
        title.setText();
    }
}
