# Game Description

This document explains the mini-game plugin for the "Tail Tag" content in Paradise. (This text includes modifications made by me.)

---

## Basic System

- **Requires at least 3 players (spectators do not count towards this number). Up to 10 players maximum.**

- Nether and Overworld coordinates are in a 1:1 ratio (Overworld coordinates 1500, 1500 will open a portal to Nether coordinates 1500, 1500).

- Even if you capture the dragon, the End City portal remains inactive and cannot be respawned.

- You will be assigned a target player that you need to eliminate.

- **3-second invulnerability is not available.**

- Right-clicking with a diamond uses 1 diamond and provides the direction to your target player.

- **If a player leaves the game, they become the hunter’s slave. (V1.1 Release)**

## Target System

- You cannot hit players who are not your target.

    - Targets can attack hunters, but can only deal knockback and no damage. They can also be damaged by projectiles.

    - Hunter's slaves can be damaged and even killed.

- If you kill your target, they become your slave.

    - You will then receive a new target assignment.

    - If you hit your target once, they will become your slave even if they die naturally within 1 minute.

    - Each time a new slave is added, your maximum health decreases by 1 heart.

- If someone makes you their target and is within a 50-meter radius, a heartbeat sound is heard (indicating tension / the sound's volume varies with distance).

- The color sequence for Tail Tag is → Aqua → Gray → Blue → Light Blue → Cyan → Green → Yellow → Orange → Red → Pink →

## Death System

- Natural death: Respawn with the Immortality Totem effect after being unable to move for 2 minutes at the place of death.
  - Slave death: Acts like natural death for 30 seconds, then teleports to the owner’s location and respawns.
  - In both cases, inventory is saved.

- If you die naturally in the End, respawn at the End spawn point (Obsidian spawn) to prevent endless death in the End Void.

- If you are captured and killed: Retain items, respawn at the place of death, and become the slave of the player who killed you.

## Slave System

- Slaves must stay within 50 blocks of their owner; if they leave, they lose 1 heart every 0.5 seconds and eventually die.

- Slaves always have their owner's location displayed on their compass.

- If a slave dies, they respawn at their owner’s location and their hunger value resets (hunger, saturation, and fatigue all return to default values).

- Slaves have a permanent Weakness II buff and a maximum health of 5 hearts.

---

# How to Run

`/chasingtails start` or `/ct start`

This command starts or stops the game.

By default, the game progress is saved even after the server shuts down, so you don't need to re-enter the command unless you want to stop the game.

**Before starting the game, the server may temporarily slow down due to chunk loading caused by random teleportation. It is recommended to use the [Chunky](https://github.com/pop4959/Chunky) plugin to preload chunks.**

## Recommended Actions

When players are unable to move or are constantly floating in the air, they may be kicked from the server due to non-compliance with server rules. It is recommended to change `server.properties` to `enable-flying=true` before starting the game.
