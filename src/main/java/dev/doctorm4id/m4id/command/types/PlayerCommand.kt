package dev.doctorm4id.m4id.command.types

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import dev.doctorm4id.m4id.text.sendFailure
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.level.ServerPlayer

/**
 * @param func The command context.
 */
class PlayerCommand(val func: CommandContext<CommandSourceStack>.(player: ServerPlayer) -> Int) : Command<CommandSourceStack> {

    override fun run(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.player
        if (player == null) {
            ctx.source.sendFailure("Individual is not a human... BANISH THY BOT.")
            return 0
        }
        return func(ctx, player)
    }
}