package com.boot.gugi.base.Enum;

import com.boot.gugi.model.Translate;

public enum MateStatus implements Translate {
    CREATOR("방장", "CREATOR"),
    PENDING("대기", "PENDING"),
    APPROVED("수락", "APPROVED"),
    REJECTED("거절", "REJECTED");

    private final String displayNameKorean;
    private final String displayNameEnglish;

    MateStatus(String displayNameKorean, String displayNameEnglish) {
        this.displayNameKorean = displayNameKorean;
        this.displayNameEnglish = displayNameEnglish;
    }

    @Override
    public String toEnglish() {
        return displayNameEnglish;
    }

    @Override
    public String toKorean() {
        return displayNameKorean;
    }
}
