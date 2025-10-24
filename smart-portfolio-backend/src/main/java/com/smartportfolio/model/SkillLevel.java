package com.smartportfolio.model;

public enum SkillLevel {
    BEGINNER("Başlangıç"),
    INTERMEDIATE("Orta"),
    ADVANCED("İleri");

    private final String displayName;

    SkillLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

