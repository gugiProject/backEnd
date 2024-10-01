package com.boot.gugi.model;

import com.boot.gugi.base.Enum.Sex;
import com.boot.gugi.base.Enum.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column
    private String nickName;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Sex gender;

    @Column(nullable=false)
    private Integer age;

    @Column
    @Enumerated(EnumType.STRING)
    private Team team;

    @Column
    private String profileImg;

    @Column
    private String introduction;

    @OneToMany(mappedBy = "postStatus")
    private List<MatePostStatus> postStatuses;

    @OneToMany(mappedBy = "applicant")
    private List<MatePostApplicant> applicants;
}
