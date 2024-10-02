package com.boot.gugi.model;

import com.boot.gugi.base.Enum.AgeGroup;
import com.boot.gugi.base.Enum.GenderPreference;
import com.boot.gugi.base.Enum.Stadium;
import com.boot.gugi.base.Enum.Team;
import com.boot.gugi.base.dto.MateDTO;
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

    @Enumerated(EnumType.STRING)
    private GenderPreference gender;

    @Enumerated(EnumType.STRING)
    private AgeGroup age;

    @Enumerated(EnumType.STRING)
    private Team team;

    @Enumerated(EnumType.STRING)
    private Stadium stadium;

    public MateDTO toDTO() {
        return new MateDTO(
                this.gender,
                this.age,
                this.gameDate,
                this.team,
                this.participants,
                this.totalMembers,
                this.stadium,
                this.title,
                this.content,
                this.contact
        );
    }
}