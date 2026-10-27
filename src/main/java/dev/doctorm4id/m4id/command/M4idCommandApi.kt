package dev.doctorm4id.m4id.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.doctorm4id.m4id.M4id
import net.minecraft.commands.CommandSourceStack

/**
 * Accessed via [M4id.Command][dev.doctorm4id.m4id.M4id.Command]
 */
class M4idCommandApi internal constructor() {

    init {
        M4id.Logger.debug("M4id Command API Initialized.")
    }

    /**
     * Can be used to add a normal (serverside) command to the game.
     *
     * @param baseCommandName The name of the command. The first word you type. e.g. `m4id` becomes `/m4id`.
     * @param toDispatcher The dispatcher of this command. This is provided in a [CommandRegistrationCallback][net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback]
     * @param func The Command DSL describing your function.
     */
    fun addCommand(
        baseCommandName: String,
        toDispatcher: CommandDispatcher<CommandSourceStack>,
        func: ArgDsl<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>>
    ) {
        addSourcedCommand(baseCommandName, toDispatcher, func)
    }

    fun <SRC> addSourcedCommand(
        baseCommandName: String,
        toDispatcher: CommandDispatcher<SRC>,
        func: ArgDsl<SRC, LiteralArgumentBuilder<SRC>>
    ) {
        toDispatcher.register(
            M4idArgBuilder(
                LiteralArgumentBuilder.literal<SRC>(baseCommandName)
            ).apply(func).doFinalization()
        )
    }
}