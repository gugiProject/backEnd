package com.boot.gugi.base.Enum;

import com.boot.gugi.model.Translate;

public enum GenderPreference implements Translate {
    MALE_ONLY("남자만", "MALE_ONLY"),
    FEMALE_ONLY("여자만", "FEMALE_ONLY"),
    ANY("상관없음", "ANY");

    private final String displayNameKorean;
    private final String displayNameEnglish;

    GenderPreference(String displayNameKorean, String displayNameEnglish) {
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