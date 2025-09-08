package com.solegendary.reignofnether.config;

import com.solegendary.reignofnether.events.EventConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AutoEventConfig {
    private final Integer cooldown;
    private final Double rarity;
    private final Boolean announcement;
    private final Integer minDistance;
    private final Integer maxDistance;
    private final Integer requiresPlayers;
    private final Integer worldTimeMin;
    private final Integer worldTimeMax;
    private final List<String> biomeRestrictions;
    private final Boolean enabledByDefault;

    public AutoEventConfig() {
        this(null, null, null, null, null, null, null, null, null, null);
    }

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
        this.biomeRestrictions = biomeRestrictions != null ? 
            Collections.unmodifiableList(new ArrayList<>(biomeRestrictions)) : null;
        this.enabledByDefault = enabledByDefault;
    }

    public int getCooldown() {
        return cooldown != null ? cooldown : EventConstants.Defaults.COOLDOWN_SECONDS;
    }

    public double getRarity() {
        return rarity != null ? rarity : EventConstants.Defaults.RARITY;
    }

    public boolean isAnnouncement() {
        return announcement != null ? announcement : EventConstants.Defaults.ANNOUNCEMENT;
    }

    public int getMinDistance() {
        return minDistance != null ? minDistance : EventConstants.Defaults.MIN_DISTANCE;
    }

    public int getMaxDistance() {
        return maxDistance != null ? maxDistance : EventConstants.Defaults.MAX_DISTANCE;
    }

    public int getRequiresPlayers() {
        return requiresPlayers != null ? requiresPlayers : EventConstants.Defaults.REQUIRES_PLAYERS;
    }

    public int getWorldTimeMin() {
        return worldTimeMin != null ? worldTimeMin : EventConstants.Defaults.WORLD_TIME_MIN;
    }

    public int getWorldTimeMax() {
        return worldTimeMax != null ? worldTimeMax : EventConstants.Defaults.WORLD_TIME_MAX;
    }

    public List<String> getBiomeRestrictions() {
        return biomeRestrictions;
    }

    public boolean isEnabledByDefault() {
        return enabledByDefault != null ? enabledByDefault : EventConstants.Defaults.ENABLED_BY_DEFAULT;
    }
    
    public static class Builder {
        private Integer cooldown;
        private Double rarity;
        private Boolean announcement;
        private Integer minDistance;
        private Integer maxDistance;
        private Integer requiresPlayers;
        private Integer worldTimeMin;
        private Integer worldTimeMax;
        private List<String> biomeRestrictions;
        private Boolean enabledByDefault;
        
        public Builder cooldown(int cooldown) {
            this.cooldown = cooldown;
            return this;
        }
        
        public Builder rarity(double rarity) {
            this.rarity = rarity;
            return this;
        }
        
        public Builder announcement(boolean announcement) {
            this.announcement = announcement;
            return this;
        }
        
        public Builder minDistance(int minDistance) {
            this.minDistance = minDistance;
            return this;
        }
        
        public Builder maxDistance(int maxDistance) {
            this.maxDistance = maxDistance;
            return this;
        }
        
        public Builder requiresPlayers(int requiresPlayers) {
            this.requiresPlayers = requiresPlayers;
            return this;
        }
        
        public Builder worldTimeMin(int worldTimeMin) {
            this.worldTimeMin = worldTimeMin;
            return this;
        }
        
        public Builder worldTimeMax(int worldTimeMax) {
            this.worldTimeMax = worldTimeMax;
            return this;
        }
        
        public Builder biomeRestrictions(List<String> biomeRestrictions) {
            this.biomeRestrictions = biomeRestrictions != null ? new ArrayList<>(biomeRestrictions) : null;
            return this;
        }
        
        public Builder enabledByDefault(boolean enabledByDefault) {
            this.enabledByDefault = enabledByDefault;
            return this;
        }
        
        public AutoEventConfig build() {
            return new AutoEventConfig(cooldown, rarity, announcement, minDistance, maxDistance,
                requiresPlayers, worldTimeMin, worldTimeMax, biomeRestrictions, enabledByDefault);
        }
    }
}