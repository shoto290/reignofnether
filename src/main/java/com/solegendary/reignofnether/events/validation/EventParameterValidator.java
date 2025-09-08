package com.solegendary.reignofnether.events.validation;

import com.solegendary.reignofnether.config.EventEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventParameterValidator {

    public static ValidationResult validate(EventEntry entry) {
        if (entry == null) {
            return ValidationResult.failure("Event entry is null");
        }

        if (entry.type == null || entry.type.trim().isEmpty()) {
            return ValidationResult.failure("Event type is required");
        }

        switch (entry.type.toUpperCase()) {
            case "COMMAND":
                return validateCommandEvent(entry);
            case "PATROL":
                return validatePatrolEvent(entry);
            case "AUTO":
                return validateAutoEvent(entry);
            default:
                return ValidationResult.failure("Unknown event type: " + entry.type);
        }
    }

    private static ValidationResult validateCommandEvent(EventEntry entry) {
        List<String> errors = new ArrayList<>();
        
        Object commandObj = entry.parameters.get("command");
        if (commandObj == null) {
            errors.add("Command parameter is required for COMMAND events");
        } else if (!(commandObj instanceof String)) {
            errors.add("Command parameter must be a string");
        } else {
            String command = (String) commandObj;
            if (command.trim().isEmpty()) {
                errors.add("Command parameter cannot be empty");
            }
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
    }

    private static ValidationResult validatePatrolEvent(EventEntry entry) {
        List<String> errors = new ArrayList<>();

        Object entityTypeObj = entry.parameters.get("entityType");
        if (entityTypeObj == null) {
            errors.add("entityType parameter is required for PATROL events");
        } else if (!(entityTypeObj instanceof String)) {
            errors.add("entityType parameter must be a string");
        } else {
            String entityTypeStr = (String) entityTypeObj;
            if (!isValidEntityType(entityTypeStr)) {
                errors.add("Invalid entity type: " + entityTypeStr);
            }
        }

        Object minCountObj = entry.parameters.get("minCount");
        Object maxCountObj = entry.parameters.get("maxCount");
        
        int minCount = validateIntParameter(minCountObj, "minCount", 1, errors);
        int maxCount = validateIntParameter(maxCountObj, "maxCount", 1, errors);
        
        if (minCount > maxCount && errors.isEmpty()) {
            errors.add("minCount cannot be greater than maxCount");
        }

        Object isAggressiveObj = entry.parameters.get("isAggressive");
        if (isAggressiveObj != null && !(isAggressiveObj instanceof Boolean)) {
            errors.add("isAggressive parameter must be a boolean");
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
    }

    private static ValidationResult validateAutoEvent(EventEntry entry) {
        List<String> errors = new ArrayList<>();

        Object subTypeObj = entry.parameters.get("subType");
        if (subTypeObj == null) {
            errors.add("subType parameter is required for AUTO events");
        } else if (!(subTypeObj instanceof String)) {
            errors.add("subType parameter must be a string");
        } else {
            String subType = (String) subTypeObj;
            if (!subType.equals("COMMAND") && !subType.equals("PATROL")) {
                errors.add("Invalid subType: " + subType + ". Must be COMMAND or PATROL");
            }
        }

        if (entry.autoConfig != null) {
            ValidationResult autoConfigResult = validateAutoConfig(entry.autoConfig);
            if (!autoConfigResult.isValid()) {
                errors.addAll(autoConfigResult.getErrors());
            }
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
    }

    private static ValidationResult validateAutoConfig(com.solegendary.reignofnether.config.AutoEventConfig autoConfig) {
        List<String> errors = new ArrayList<>();

        if (autoConfig.getCooldown() < 0) {
            errors.add("Cooldown must be non-negative");
        }

        if (autoConfig.getRarity() < 0 || autoConfig.getRarity() > 20) {
            errors.add("Rarity must be between 0 and 20");
        }

        if (autoConfig.getMinDistance() < 0) {
            errors.add("minDistance must be non-negative");
        }

        if (autoConfig.getMaxDistance() < 0) {
            errors.add("maxDistance must be non-negative");
        }

        if (autoConfig.getMinDistance() > autoConfig.getMaxDistance()) {
            errors.add("minDistance cannot be greater than maxDistance");
        }

        if (autoConfig.getRequiresPlayers() < 0) {
            errors.add("requiresPlayers must be non-negative");
        }

        if (autoConfig.getWorldTimeMin() < 0 || autoConfig.getWorldTimeMin() >= 24000) {
            errors.add("worldTimeMin must be between 0 and 23999");
        }

        if (autoConfig.getWorldTimeMax() < 0 || autoConfig.getWorldTimeMax() >= 24000) {
            errors.add("worldTimeMax must be between 0 and 23999");
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
    }

    private static boolean isValidEntityType(String entityTypeStr) {
        try {
            ResourceLocation entityLocation = new ResourceLocation(entityTypeStr);
            return ForgeRegistries.ENTITY_TYPES.containsKey(entityLocation);
        } catch (Exception e) {
            return false;
        }
    }

    private static int validateIntParameter(Object obj, String paramName, int defaultValue, List<String> errors) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Number) {
            int value = ((Number) obj).intValue();
            if (value < 0) {
                errors.add(paramName + " must be non-negative");
                return defaultValue;
            }
            return value;
        } else {
            errors.add(paramName + " parameter must be a number");
            return defaultValue;
        }
    }
}