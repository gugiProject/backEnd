package com.boot.gugi.base.dto;

import com.boot.gugi.base.Enum.Sex;
import com.boot.gugi.base.Enum.Team;
import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String nickName;
    private String email;
    private Sex gender;
    private Integer age;
    private Team team;
    private String profileImg;
    private String introduction;
}