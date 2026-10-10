package dev.doctorm4id.m4id.command.types

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import dev.doctorm4id.m4id.text.sendFailure
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.level.ServerPlayer

/**
 * This represents a command that is run by a server player.
 * A useful shortcut since most commands take a player input.
 *
 * @param func The command context, giving also a player parameter
 */
class PlayerCommand(val func: CommandContext<CommandSourceStack>.(player: ServerPlayer) -> Int) : Command<CommandSourceStack> {

    override fun run(ctx: CommandContext<CommandSourceStack>): Int {
        // The source is null for the console, command blocks, functions and so on
        val player = ctx.source.player
        if (player == null) {
            ctx.source.sendFailure("Only Players can send Player commands.")
            return 0
        }
        return func(ctx, player)
    }

}