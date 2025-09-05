# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Reign of Nether is a Minecraft Forge mod that transforms Minecraft into a Real-Time Strategy (RTS) game inspired by classics like Starcraft, Warcraft, and Age of Empires. The mod uses vanilla Minecraft assets and mobs (Illagers, Creepers, Piglins) as RTS units, with buildings constructed from Minecraft blocks.

Version: 1.2.1c
Minecraft Version: 1.20.1
Forge Version: 47.4.0

## Development Commands

Since `gradlew` is not executable by default, you'll need to make it executable first:
```bash
chmod +x ./gradlew
```

### Build and Run Commands
- `./gradlew build` - Build the mod
- `./gradlew runClient` - Run Minecraft client with the mod loaded
- `./gradlew runServer` - Run dedicated server with the mod loaded
- `./gradlew runData` - Run data generation

### Testing
- `./gradlew test` - Run JUnit tests (uses JUnit Platform)

### Development
- `./gradlew reobfJar` - Create obfuscated jar for distribution
- `./gradlew genParchmentOBF` - Generate Parchment mappings

The mod uses Parchment mappings (2023.09.03-1.20.1) for deobfuscation.

## Architecture Overview

### Core Package Structure
- `com.solegendary.reignofnether` - Root package containing the main mod class
- `ability/` - Unit and building abilities system (cooldowns, channeling, effects)
- `alliance/` - Team/alliance management for multiplayer
- `building/` - Building construction, placement, and management system
- `unit/` - RTS unit system with goals, AI, and custom renderers
- `research/` - Technology tree and upgrade system
- `resources/` - Resource management (population, materials)
- `hud/` - RTS-style user interface components
- `camera/` - Isometric camera system for top-down RTS view
- `registrars/` - Forge registration handlers for items, entities, blocks, etc.

### Key Architectural Patterns

**Client-Server Architecture**: The mod uses Forge's client-server model with:
- `*ClientEvents.java` - Client-side event handlers
- `*ServerEvents.java` - Server-side event handlers  
- `*ClientboundPacket.java` - Server-to-client network packets
- `*ServerboundPacket.java` - Client-to-server network packets
- `*SaveData.java` - World save data persistence

**Entity System**: Uses Minecraft's entity system extended with:
- Custom AI goals in `unit/goals/`
- Custom renderers and models in `unit/modelling/`
- Unit-specific behavior classes in `unit/units/`

**Building System**: Buildings are constructed from actual Minecraft blocks with:
- `Building.java` - Core building logic and health based on block count
- `BuildingBlock.java` - Individual block management within structures
- `GarrisonableBuilding.java` - Buildings that can house units

**Race System**: Three playable races:
- Villagers - Traditional medieval units
- Monsters - Undead and hostile mobs
- Piglins - Nether-based units

### Data Persistence
The mod uses custom save data classes that extend Minecraft's `SavedData` system to persist RTS state across world saves/loads.

### Network Protocol
Custom networking layer handles RTS-specific communications like unit commands, building construction, and ability usage between client and server.

### Configuration
Uses Forge's config system with `ReignOfNetherCommonConfigs` for server/client settings stored in `reignofnether-common-*.toml`.