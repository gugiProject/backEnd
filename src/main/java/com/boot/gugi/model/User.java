package com.boot.gugi.model;

import com.boot.gugi.base.Enum.Sex;
import com.boot.gugi.base.Enum.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user")
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
