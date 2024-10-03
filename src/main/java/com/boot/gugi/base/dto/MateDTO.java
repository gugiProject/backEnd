package com.boot.gugi.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MateDTO {
    private String title;
    private String content;
    private String contact;
    private Integer totalMembers;
    private LocalDate gameDate;
    private String gender;
    private String ageGroup;
    private String team;
    private String stadium;
}