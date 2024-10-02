package com.boot.gugi.base.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MateResponseDTO {
    private UUID userId;
    private UUID postId;
}
