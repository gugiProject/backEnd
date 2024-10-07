package com.boot.gugi.base.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotificationResponseDTO {
    private List<ApplicantNotificationDTO> applicantNotifications;
    private List<StatusNotificationDTO> statusNotifications;
}
