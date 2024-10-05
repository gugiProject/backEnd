package com.boot.gugi.model;

import com.boot.gugi.base.Enum.*;
import com.boot.gugi.base.dto.UserDTO;
import com.boot.gugi.base.util.TranslationUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable=false)
    private String name;

    @Column
    private String nickName;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    private String gender;

    @Column(nullable=false)
    private Integer age;

    @Column
    private String team;

    @Column
    private String profileImg;

    @Column
    private String introduction;

    @OneToMany(mappedBy = "postStatus")
    private List<MatePostStatus> postStatuses;

    @OneToMany(mappedBy = "applicant")
    private List<MatePostApplicant> applicants;

    public UserDTO toDTO() {

        if (this.gender == null || this.gender.isEmpty()) {
            throw new IllegalArgumentException("성별 값이 유효하지 않습니다.");
        }
        Sex gender = TranslationUtil.fromEnglish(this.gender, Sex.class);
        if (gender == null) {
            throw new IllegalArgumentException("유효하지 않은 성별 값: " + this.gender);
        }
        String genderKorean = gender.toKorean();

        if (this.team == null || this.team.isEmpty()) {
            throw new IllegalArgumentException("팀 값이 유효하지 않습니다.");
        }
        Team team = TranslationUtil.fromEnglish(this.team, Team.class);
        if (team == null) {
            throw new IllegalArgumentException("유효하지 않은 팀 값: " + this.team);
        }
        String teamKorean = team.toKorean();

        return new UserDTO(
                this.name,
                this.nickName,
                this.email,
                genderKorean,
                this.age,
                teamKorean,
                this.introduction
        );
    }
}
