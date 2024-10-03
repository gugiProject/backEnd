package com.boot.gugi.base.Enum;

import com.boot.gugi.model.Translate;

public enum Team implements Translate {
    LG("LG", "LG"),
    KT("KT", "KT"),
    SSG("SSG", "SSG"),
    NC("NC", "NC"),
    DOOSAN("두산", "DOOSAN"),
    KIA("KIA", "KIA"),
    LOTTE("롯데", "LOTTE"),
    SAMSUNG("삼성", "SAMSUNG"),
    HANWHA("한화", "HANWHA"),
    KIWOOM("키움", "KIWOOM"),
    ANY("상관없음", "ANY");

    private final String displayNameKorean;
    private final String displayNameEnglish;

    Team(String displayNameKorean, String displayNameEnglish) {
        this.displayNameKorean = displayNameKorean;
        this.displayNameEnglish = displayNameEnglish;
    }

    @Override
    public String toEnglish() {
        return displayNameEnglish.equals(displayNameKorean) ? displayNameKorean : displayNameEnglish;
    }

    @Override
    public String toKorean() {
        return displayNameKorean.equals(displayNameEnglish) ? displayNameEnglish : displayNameKorean;
    }
}