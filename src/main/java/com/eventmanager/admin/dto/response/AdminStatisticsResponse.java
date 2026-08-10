package com.eventmanager.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminStatisticsResponse {

    private long totalUsers;
    private long totalEvents;
    private long totalRegistrations;
}