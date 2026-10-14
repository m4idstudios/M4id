package dev.doctorm4id.m4id.platform.fabric.command

//? fabric {

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.doctorm4id.m4id.command.ArgDsl
import dev.doctorm4id.m4id.command.M4idCommandApi
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.loader.api.FabricLoader

/**
 * Can be used to add a clientside only command to the game
 *
 * @see [ClientCommandManager]
 *
 * @param baseCommandName The name of the command. The first word you type. e.g. `m4id` becomes `/m4id`.
 * @param toDispatcher The dispatcher of this command. This is provided in a [CommandRegistrationCallback][net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback]
 * @param func The Command DSL describing your function.
 */
fun M4idCommandApi.addClientCommand(
    baseCommandName: String,
    func: ArgDsl<FabricClientCommandSource, LiteralArgumentBuilder<FabricClientCommandSource>>
) {
    if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
        addSourcedCommand(baseCommandName, ClientCommandManager.getActiveDispatcher() as CommandDispatcher<FabricClientCommandSource>, func)
    }
}

//? }