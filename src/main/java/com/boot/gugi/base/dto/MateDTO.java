package com.boot.gugi.base.dto;

import com.boot.gugi.base.Enum.AgeGroup;
import com.boot.gugi.base.Enum.GenderPreference;
import com.boot.gugi.base.Enum.Stadium;
import com.boot.gugi.base.Enum.Team;
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

    private GenderPreference gender;

    private AgeGroup ageGroup;

    private LocalDate gameDate;

    private Team team;

    //private Integer participants;

    private Integer totalMembers;

    private Stadium stadium;

    private String title;

    private String content;

    private String contact;

}