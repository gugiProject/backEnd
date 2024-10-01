package com.boot.gugi.base.Enum;

public enum Stadium {
    JAMSIL("잠실 야구장", Team.DOOSAN, Team.LG),
    GOCHEOK("고척 스카이돔", Team.KIWOOM),
    INCHEON("인천 SSG 랜더스필드", Team.SSG),
    DAEGU("대구 삼성 라이온즈 파크", Team.SAMSUNG),
    DAEJEON("대전 한화생명 이글스파크", Team.HANWHA),
    GWANGJU("광주 기아 챔피언스 필드", Team.KIA),
    CHANGWON("창원 NC 파크", Team.NC),
    SUWON("수원 KT 위즈 파크", Team.KT),
    BUSAN("부산 사직 야구장", Team.LOTTE);

    private final String stadiumName;
    private final Team[] homeTeams;

    Stadium(String stadiumName, Team... homeTeams) {
        this.stadiumName = stadiumName;
        this.homeTeams = homeTeams;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public Team[] getHomeTeams() {
        return homeTeams;
    }

    @Override
    public String toString() {
        return stadiumName;
    }
}
