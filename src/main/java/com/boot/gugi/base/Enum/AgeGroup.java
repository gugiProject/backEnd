package com.boot.gugi.base.Enum;

import com.boot.gugi.model.Translate;

public enum AgeGroup implements Translate {
    AGE_10s("10대", "AGE_10s"),
    AGE_20s("20대", "AGE_20s"),
    AGE_30s("30대", "AGE_30s"),
    AGE_40s("40대", "AGE_40s"),
    AGE_50s("50대", "AGE_50s"),
    ANY("상관없음", "ANY");

    private final String displayNameKorean;
    private final String displayNameEnglish;

    AgeGroup(String displayNameKorean, String displayNameEnglish) {
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
