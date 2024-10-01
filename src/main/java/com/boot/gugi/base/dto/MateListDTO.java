package com.boot.gugi.base.dto;

import lombok.Data;

import java.util.List;

@Data
public class MateListDTO {

    private List<MateDTO> posts;
    private Integer totalCount;

    public MateListDTO(List<MateDTO> posts, Integer totalCount) {
        this.posts = posts;
        this.totalCount = totalCount;
    }
}
