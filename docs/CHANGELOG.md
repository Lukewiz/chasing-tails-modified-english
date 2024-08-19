# Patch Notes (2023/8/8 - 2023/9/23)

## System Changes + Bug Fixes

1. [Players who join during the game are forced spectators] → [Players who join during the game are spectators]
2. [Game starts for all players connected to the server] → [Game starts for all players connected to the server except those in spectator mode]
3. [Admin connection not allowed] → [Admin connection allowed without logs]
4. [Potion brewing and creation not possible] → [Potion brewing and creation enabled]
5. [Hunter cannot be attacked] → [Hunters can be knocked back but cannot be damaged]
6. [Projectiles cannot be thrown at hunters] → [Projectiles can be thrown at hunters]
7. [Slave] title → [Tail] / Preventing excessive immersion for spectators
8. [Hunter's tail cannot be attacked] → [Hunter's tail can be attacked and killed]
9. **Fixed the issue where, after a 1-minute preliminary death timer expires, the player would become the slave of the last attacker.**
10. Fixed the issue where monsters would not recognize the player immediately after death.
11. Fixed the issue where attempting to commit suicide with Ender Pearls would result in the player becoming their own slave.
12. Fixed the issue where teleportation was possible in a natural death state.
13. Added: If there are no End Cities within the world border, the content progresses using a seed where an End City exists within the world border coordinates.
14. Added: Hunters' wolves can now attack targets.
15. Added: Hunters can now ignite and explode TNT to damage targets.
16. Herobrine removed.

## Incorrect Information Correction:
- **The natural death bug with flags has been definitely fixed.**
    - To clarify, if a player dies naturally within 1 minute after taking damage from a hunter, they become a slave. This is **normal behavior and not a bug.** The flag issue was due to a missed line of code.
- In the content video’s first episode, the cause of Wuyung’s death was **natural death from Ender Pearl fall damage**, and the game ends immediately at that death point. The slave integration code does not reach this point, and **this is not a bug.**

# Patch Notes (2024/6/5 - 2024/6/7)

## System Changes + Bug Fixes

### Complete Code Rewrite and Structure Cleanup

1. Removed the 1-heart reduction in maximum health per slave.
2. Slaves wear full sets of dyed leather armor (other armor types not allowed).
3. Save all player inventories.
4. When a slave dies, a message is displayed to the entire team: 'xx has died (respawning in 30 seconds).'
5. When a slave dies, they cannot act for 30 seconds at their death location before respawning at their owner’s location.
6. Display the remaining respawn time above the slave’s head.
7. No additional buffs for obtaining Ender Dragon eggs.
8. Removed world border.
9. Herobrine removed.

# V1.0 RELEASE (2024/7/31)

## System Changes

1. Reduced the number of players from 7-8 to 5.
2. Added a 3-second countdown at the start.
3. Changed the target-hunter relationship to <Red<Orange<Yellow<Light Blue<Cyan<Blue.
4. Herobrine removed.

# V1.1 RELEASE (2024/7/31)

## System Changes

1. **!!Important!!** From now on, if a player leaves, the game does not stop and the leaving player becomes the hunter's tail.
2. Increased the playable number of players by 2 (3-10 players).
3. Added 2 new teams (same as 2, Aqua and Gray).
4. Herobrine removed.
