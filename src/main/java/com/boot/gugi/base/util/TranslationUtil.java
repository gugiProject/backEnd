package com.boot.gugi.base.util;

import com.boot.gugi.model.Translate;

public class TranslationUtil {
    public static <E extends Enum<E> & Translate> String toEnglish(E enumValue) {
        return enumValue.toEnglish();
    }
    public static <E extends Enum<E> & Translate> String toKorean(E enumValue) {
        return enumValue.toKorean();
    }

    public static <E extends Enum<E> & Translate> E fromKorean(String koreanName, Class<E> enumClass) {
        for (E enumValue : enumClass.getEnumConstants()) {
            if (enumValue.toKorean().equals(koreanName)) {
                return enumValue;
            }
        }
        return null;
    }

    public static <E extends Enum<E> & Translate> E fromEnglish(String englishName, Class<E> enumClass) {
        for (E enumValue : enumClass.getEnumConstants()) {
            if (enumValue.toEnglish().equals(englishName)) {
                return enumValue;
            }
        }
        return null;
    }
}
