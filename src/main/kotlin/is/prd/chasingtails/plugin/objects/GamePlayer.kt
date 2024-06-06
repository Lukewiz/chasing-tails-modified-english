/*
 * Copyright (C) 2024 Paradise Dev Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package `is`.prd.chasingtails.plugin.objects

import `is`.prd.chasingtails.plugin.managers.ChasingTailsGameManager.gamePlayers
import `is`.prd.chasingtails.plugin.managers.ChasingTailsGameManager.mainMasters
import `is`.prd.chasingtails.plugin.objects.ChasingTailsUtils.formatUsername
import `is`.prd.chasingtails.plugin.objects.ChasingTailsUtils.gamePlayerData
import `is`.prd.chasingtails.plugin.objects.ChasingTailsUtils.mappedColors
import `is`.prd.chasingtails.plugin.objects.ChasingTailsUtils.scoreboard
import `is`.prd.chasingtails.plugin.objects.ChasingTailsUtils.server
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.EntityEffect
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Display
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.NumberConversions

/**
 * @author aroxu, DytroC, ContentManager
 */

class GamePlayer(private val parameterPlayer: Player) : ConfigurationSerializable {
    companion object {
        @JvmStatic
        fun deseralize(args: Map<String, Any?>): GamePlayer {
            val player = args["player"] as Player
            val originalMaster = args["master"] as GamePlayer
            val originalTemporaryDeathDuration = args["temporaryDeathDuration"] as Int

            return GamePlayer(player).apply {
                master = originalMaster
                temporaryDeathDuration = originalTemporaryDeathDuration
            }
        }
    }

    val player
        get() = parameterPlayer

    val offlinePlayer: OfflinePlayer
        get() = server.getOfflinePlayer(player.uniqueId)

    val color: TextColor get() = scoreboard.getPlayerTeam(offlinePlayer)?.color() ?: NamedTextColor.WHITE

    val koreanColor get() = mappedColors[color] ?: "하얀색"

    val name get() = offlinePlayer.name ?: ""

    val coloredName get() = text(player.formatUsername(), color)

    val target
        get() = mainMasters[(mainMasters.indexOf((master ?: this)) + 1) % mainMasters.size]

    var master: GamePlayer? = null

    var tooFar: Boolean = false

    val isDeadTemporarily
        get() = temporaryDeathDuration > 0

    var lastlyReceivedDamage: Pair<GamePlayer, Int>? = null
    var temporaryDeathDuration = 0
        set(value) {
            if (value <= 0) {
                deathTimer?.remove()
                deathTimer = null

                player.playEffect(EntityEffect.TOTEM_RESURRECT)
                player.addPotionEffects(
                    listOf(
                        PotionEffect(PotionEffectType.RESISTANCE, 20 * 5, 1, false, false, false),
                        PotionEffect(PotionEffectType.REGENERATION, 20 * 45, 1, false, false, false),
                        PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 40, 0, false, false, false)
                    )
                )

                sendMessage(text("부활하셨습니다!"))

                val masterLocation = master?.player?.location
                if (masterLocation != null) {
                    initializeAsSlave()
                    player.teleport(masterLocation)
                }
            } else {
                (deathTimer ?: player.world.spawn(
                    player.location.add(0.0, 2.3, 0.0),
                    TextDisplay::class.java,
                ) {
                    it.isPersistent = true
                    it.alignment = TextDisplay.TextAlignment.CENTER
                    it.billboard = Display.Billboard.CENTER
                    player.addPassenger(it)
                }.also {
                    deathTimer = it
                }).text(
                    text("부활까지 ${NumberConversions.ceil(value / 20.0)}초", NamedTextColor.YELLOW)
                )
            }

            field = value
        }

    private var deathTimer: TextDisplay? = null

    fun enslave(slave: GamePlayer, inherited: Boolean = false) {
        slave.master = this
        mainMasters.remove(slave)

        equipSlaveArmor(slave.player)

        if (!inherited) {
            gamePlayers.forEach {
                if (it.master == slave) enslave(it, true)
            }

            slave.initializeAsSlave()
            scoreboard.getPlayerTeam(slave.player)?.unregister()
            slave.player.playEffect(EntityEffect.TOTEM_RESURRECT)
            slave.player.sendMessage(
                text("처치당하셨습니다! 앞으로 ").append(player.gamePlayerData!!.coloredName)
                    .append(text("의 꼬리가 됩니다.", NamedTextColor.WHITE))
            )

            slave.player.inventory.addItem(ItemStack(Material.COMPASS))

            sendMessage(
                text("목표를 처치하는데 성공하셨습니다! ").append(slave.player.gamePlayerData!!.coloredName).append(text("님이 꼬리가 됩니다."))
            )

        }

        scoreboard.getPlayerTeam(offlinePlayer)?.addPlayer(slave.offlinePlayer)

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = (20 - (
                (gamePlayers.count { it.master == this }) * 2
                )).toDouble()
    }

    fun sendMessage(message: ComponentLike) = player.sendMessage(message)
    fun alert(message: String) = sendMessage(text(message, NamedTextColor.RED))

    override fun equals(other: Any?) = other is GamePlayer && other.player.uniqueId == player.uniqueId
    override fun hashCode() = player.uniqueId.hashCode()

    override fun serialize(): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        map["player"] = player
        map["target"] = target
        map["master"] = master
        map["temporaryDeathDuration"] = temporaryDeathDuration

        return map
    }

    fun temporarilyKillPlayer(duration: Int) {
        player.apply {
            health = getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
            foodLevel = 20
            saturation = 3F
            exhaustion = 0F
            world.getNearbyLivingEntities(location, 30.0).forEach {
                if (it is Mob) {
                    if (it.target?.uniqueId == uniqueId) it.target = null
                }
            }
        }

        temporaryDeathDuration = duration
        tooFar = false
    }

    private fun initializeAsSlave() = player.apply {
        health = 10.0
        getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 10.0

        foodLevel = 20
        saturation = 3F
        exhaustion = 0F
    }

    private fun equipSlaveArmor(slave: Player) = slave.inventory.apply {
        helmet = ItemStack(Material.LEATHER_HELMET).applyColorToArmor()
        chestplate = ItemStack(Material.LEATHER_CHESTPLATE).applyColorToArmor()
        leggings = ItemStack(Material.LEATHER_LEGGINGS).applyColorToArmor()
        boots = ItemStack(Material.LEATHER_BOOTS).applyColorToArmor()
    }

    private fun ItemStack.applyColorToArmor() = apply {
        editMeta {
            if (it is LeatherArmorMeta) it.setColor(Color.fromRGB(color.value()))
            it.isUnbreakable = true
            it.addItemFlags(*ItemFlag.entries.toTypedArray())
            it.addEnchant(Enchantment.BINDING_CURSE, 1, true)
        }
    }
}