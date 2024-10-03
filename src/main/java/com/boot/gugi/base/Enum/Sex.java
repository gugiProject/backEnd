package com.boot.gugi.base.Enum;

import com.boot.gugi.model.Translate;

public enum Sex implements Translate {
    MALE("남자", "MALE"),
    FEMALE("여자", "FEMALE");

    private final String displayNameKorean;
    private final String displayNameEnglish;

    Sex(String displayNameKorean, String displayNameEnglish) {
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
