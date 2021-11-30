package com.company;

public class Action {

    private final String description;
    private final Quality quality;

    public Action(String description, Quality quality) {
        this.description = description;
        this.quality = quality;
    }

    public String getDescription() {
        return description;
    }

    public Quality getQuality() {
        return quality;
    }
}
