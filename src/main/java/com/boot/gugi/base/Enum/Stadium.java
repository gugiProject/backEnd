package com.boot.gugi.base.Enum;

import com.boot.gugi.model.Translate;

public enum Stadium implements Translate {
    JAMSIL("잠실 야구장", "JAMSIL"),
    GOCHEOK("고척 스카이돔", "GOCHEOK"),
    INCHEON("인천 SSG 랜더스필드", "INCHEON"),
    DAEGU("대구 삼성 라이온즈 파크", "DAEGU"),
    DAEJEON("대전 한화생명 이글스파크", "DAEJEON"),
    GWANGJU("광주 기아 챔피언스 필드", "GWANGJU"),
    CHANGWON("창원 NC 파크", "CHANGWON"),
    SUWON("수원 KT 위즈 파크", "SUWON"),
    BUSAN("부산 사직 야구장", "BUSAN"),
    ANY("상관없음", "ANY");

    private final String displayNameKorean;
    private final String displayNameEnglish;

    Stadium(String displayNameKorean, String displayNameEnglish) {
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
