package com.boot.gugi.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String name;
    private String nickName;
    private String email;
    private String gender;
    private Integer age;
    private String team;
    private String introduction;
}