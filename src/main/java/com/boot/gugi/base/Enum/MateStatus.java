package com.boot.gugi.base.Enum;

public enum MateStatus {
    CREATOR,
    PENDING,
    APPROVED,
    REJECTED,
    FULL;

    @Override
    public String toString() {
        switch (this) {
            case CREATOR: return "방장";
            case PENDING: return "대기";
            case APPROVED: return "승인";
            case REJECTED: return "거부";
            case FULL: return "모집완료";
            default: throw new IllegalArgumentException();
        }
    }
}
