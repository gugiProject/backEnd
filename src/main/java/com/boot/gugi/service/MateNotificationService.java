package com.boot.gugi.service;

import com.boot.gugi.base.Enum.MateStatus;
import com.boot.gugi.base.dto.ApplicantNotificationDTO;
import com.boot.gugi.base.dto.NotificationResponseDTO;
import com.boot.gugi.base.dto.StatusNotificationDTO;
import com.boot.gugi.model.MatePostApplicant;
import com.boot.gugi.repository.MatePostApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MateNotificationService {

    @Autowired
    private MatePostApplicantRepository applicantRepository;

    @Autowired
    private UserService userService;

    public NotificationResponseDTO getNotifications(UUID userId) {
        NotificationResponseDTO response = new NotificationResponseDTO();
        List<MatePostApplicant> applicants = applicantRepository.findByOwnerId(userId);

        // 신청자 알림
        List<ApplicantNotificationDTO> applicantNotifications = applicants.stream()
                .filter(applicant -> applicant.getStatus() == MateStatus.PENDING)
                .map(applicant -> {
                    ApplicantNotificationDTO dto = new ApplicantNotificationDTO();
                    dto.setPostTitle(applicant.getMatePost().getTitle());
                    dto.setNickName(applicant.getApplicant().getNickName());
                    return dto;
                })
                .collect(Collectors.toList());
        response.setApplicantNotifications(applicantNotifications);

        List<MatePostApplicant> myApplicants = applicantRepository.findByApplicantId(userId);
        // 수락&거절 알림
        List<StatusNotificationDTO> statusNotifications = myApplicants.stream()
                .filter(applicant -> applicant.getStatus() == MateStatus.ACCEPTED || applicant.getStatus() == MateStatus.REJECTED)
                .map(applicant -> {
                    StatusNotificationDTO dto = new StatusNotificationDTO();
                    dto.setPostTitle(applicant.getMatePost().getTitle());
                    dto.setStatus(applicant.getStatus() == MateStatus.ACCEPTED ? "수락" : "거절");
                    return dto;
                })
                .collect(Collectors.toList());
        response.setStatusNotifications(statusNotifications);

        return response;
    }
}
