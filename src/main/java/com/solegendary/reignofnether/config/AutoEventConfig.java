package com.solegendary.reignofnether.config;

import java.util.List;

public class AutoEventConfig {
    public Integer cooldown;
    public Double rarity;
    public Boolean announcement;
    public Integer minDistance;
    public Integer maxDistance;
    public Integer requiresPlayers;
    public Integer worldTimeMin;
    public Integer worldTimeMax;
    public List<String> biomeRestrictions;
    public Boolean enabledByDefault;

    public AutoEventConfig() {}

    public AutoEventConfig(Integer cooldown, Double rarity, Boolean announcement, 
                          Integer minDistance, Integer maxDistance, Integer requiresPlayers,
                          Integer worldTimeMin, Integer worldTimeMax, List<String> biomeRestrictions,
                          Boolean enabledByDefault) {
        this.cooldown = cooldown;
        this.rarity = rarity;
        this.announcement = announcement;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.requiresPlayers = requiresPlayers;
        this.worldTimeMin = worldTimeMin;
        this.worldTimeMax = worldTimeMax;
        this.biomeRestrictions = biomeRestrictions;
        this.enabledByDefault = enabledByDefault;
    }

    public int getCooldown() {
        return cooldown != null ? cooldown : 300;
    }

    public double getRarity() {
        return rarity != null ? rarity : 0.001;
    }

    public boolean isAnnouncement() {
        return announcement != null ? announcement : true;
    }

    public int getMinDistance() {
        return minDistance != null ? minDistance : 50;
    }

    public int getMaxDistance() {
        return maxDistance != null ? maxDistance : 150;
    }

    public int getRequiresPlayers() {
        return requiresPlayers != null ? requiresPlayers : 1;
    }

    public int getWorldTimeMin() {
        return worldTimeMin != null ? worldTimeMin : 0;
    }

    public int getWorldTimeMax() {
        return worldTimeMax != null ? worldTimeMax : 24000;
    }

    public List<String> getBiomeRestrictions() {
        return biomeRestrictions;
    }

    public boolean isEnabledByDefault() {
        return enabledByDefault != null ? enabledByDefault : true;
    }
}