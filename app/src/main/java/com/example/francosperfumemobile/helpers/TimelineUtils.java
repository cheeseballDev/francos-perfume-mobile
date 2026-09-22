package com.example.francosperfumemobile.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineUtils {

    Map<String, List<String>> LIST_OF_USER_TIMELINES = Map.of(
            "OWNER", List.of(
                    "Destination Branch Review",
                    "For Dispatch",
                    "Stock Arrival"
            ),
            "REQUESTING BRANCH MANAGER", List.of(
                    "Source Branch Review",
                    "Owner Review",
                    "For Dispatch",
                    "Stock Arrival"
            ),
            "REQUESTING BRANCH STAFF", List.of(
                    "Requesting Branch Manager Review",
                    "Source Branch Review",
                    "Owner Review",
                    "For Dispatch",
                    "Stock Arrival"
            ),
            "SOURCE BRANCH MANAGER", List.of(
                    "Source Branch Review",
                    "Owner Review",
                    "For Dispatch",
                    "Stock Arrival"
            ),
            "SOURCE BRANCH STAFF", List.of(
                    "Source Branch Manager Review",
                    "Owner Review",
                    "For Dispatch",
                    "Stock Arrival"
            )
    );
    public TimelineUtils() {
        // constructor for idk
    }



    public void buildTimeline() {

        // pass the request here

        // TODO: get the highest number of approval_order


        // NOTES: IF CREATOR_ID == OWNER THEN PROCEED WITH CHANGING THE REQUESTING TO DESTINATION TAG

        // TODO: Replace with requestCreatorId
        if (1 == 1) {
            // TODO: ADD CHANGING OF REQUESTING -> DESTINATION TAG
        }

    }
}