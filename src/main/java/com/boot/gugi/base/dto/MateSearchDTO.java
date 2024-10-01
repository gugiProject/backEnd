package com.boot.gugi.base.dto;

import com.boot.gugi.base.Enum.AgeGroup;
import com.boot.gugi.base.Enum.GenderPreference;
import com.boot.gugi.base.Enum.Stadium;
import com.boot.gugi.base.Enum.Team;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MateSearchDTO {
    private GenderPreference gender;
    private AgeGroup ageGroup;
    private LocalDate gameDate;
    private Team team;
    private Integer totalMembers;
    private Stadium stadium;
}
