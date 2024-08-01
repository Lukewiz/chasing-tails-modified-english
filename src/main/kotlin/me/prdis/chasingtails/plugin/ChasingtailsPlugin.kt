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

package me.prdis.chasingtails.plugin

import me.prdis.chasingtails.plugin.commands.ChasingtailsCommand.registerCommand
import me.prdis.chasingtails.plugin.config.ChasingtailsConfig.resetConfigGameProgress
import me.prdis.chasingtails.plugin.config.ChasingtailsConfig.saveConfigGameProgress
import me.prdis.chasingtails.plugin.config.GamePlayerData
import me.prdis.chasingtails.plugin.managers.ChasingTailsGameManager.gameHalted
import me.prdis.chasingtails.plugin.managers.ChasingTailsGameManager.isRunning
import me.prdis.chasingtails.plugin.managers.ChasingTailsGameManager.startGame
import net.kyori.adventure.text.Component.text
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin


/**
 * @author aroxu, DytroC, ContentManager as Vector3
 */

class ChasingtailsPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: ChasingtailsPlugin
            private set
    }

    override fun onEnable() {
        instance = this

        ConfigurationSerialization.registerClass(GamePlayerData::class.java)

        isRunning = config.getBoolean("isRunning")
        if (isRunning) {
            startGame()
            logger.warning("이전 서버 종료 때 게임이 진행중이었습니다. 자동으로 게임이 재개되어 시작 명령어를 입력하실 필요가 없습니다.")
            logger.info("성공적으로 플러그인이 활성화되었습니다.")
        }
        logger.info("Chasing Tails")
        logger.info("")
        logger.info("A spigot plugin Developed by Paradise Dev Team")
        logger.info("최신 업데이트는 https://github.com/ParadiseDevTeam/chasing-tails/releases/tag/cbd50c 에서 다운로드하세요!")
        logger.info("QBBX에 의해 변형됨.")
        logger.warning("이슈는 QBBX에게 물어보지 마시고 ")
        logger.warning("https://github.com/ParadiseDevTeam/chasing-tails/issues에서 질문해주세요.")
        logger.info("성공적으로 플러그인이 활성화되었습니다.")
        registerCommand()
    }

    override fun onDisable() {
        if (isRunning) {
//            if (!gameHalted) {
//                saveConfigGameProgress()
//            }

            server.onlinePlayers.forEach {
                it.kick(text("Server closed"))
            }
        } else {
            resetConfigGameProgress()
        }
    }
}
