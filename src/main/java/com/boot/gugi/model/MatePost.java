package com.boot.gugi.model;

import com.boot.gugi.base.Enum.AgeGroup;
import com.boot.gugi.base.Enum.GenderPreference;
import com.boot.gugi.base.Enum.Stadium;
import com.boot.gugi.base.Enum.Team;
import com.boot.gugi.base.dto.MateDTO;
import com.boot.gugi.base.util.TranslationUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mate_post")
public class MatePost {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    private Integer participants;
    private Integer totalMembers;
    private LocalDate gameDate;
    private String title;
    private String content;
    private String contact;
    private LocalDateTime createdTimeAt;
    private LocalDateTime updatedTimeAt;
    private String gender;
    private String age;
    private String team;
    private String stadium;

    public MateDTO toDTO() {
        GenderPreference genderPreference = TranslationUtil.fromEnglish(this.gender, GenderPreference.class);
        String genderKorean = (genderPreference != null) ? genderPreference.toKorean() : "상관없음";

        AgeGroup age = TranslationUtil.fromEnglish(this.gender, AgeGroup.class);
        String ageKorean = (age != null) ? age.toKorean() : "상관없음";

        Team team = TranslationUtil.fromEnglish(this.gender, Team.class);
        String teamKorean = (team != null) ? team.toKorean() : "상관없음";

        Stadium stadium = TranslationUtil.fromEnglish(this.gender, Stadium.class);
        String stadiumKorean = (stadium != null) ? stadium.toKorean() : "상관없음";

        return new MateDTO(
                this.title,
                this.content,
                this.contact,
                this.totalMembers,
                this.gameDate,
                genderKorean,
                ageKorean,
                teamKorean,
                stadiumKorean
        );
    }
}