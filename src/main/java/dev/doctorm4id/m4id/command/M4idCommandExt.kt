package dev.doctorm4id.m4id.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.Message
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.doctorm4id.m4id.M4id
import dev.doctorm4id.m4id.ext.commands.addAll
import net.minecraft.commands.CommandSourceStack
import net.minecraft.world.entity.player.Player

typealias ArgDsl<S, T> = M4idArgBuilder<S, T>.() -> Unit
typealias ArgDslTyped<S, A> = M4idArgBuilder<S, RequiredArgumentBuilder<S, A>>.(it: CommandContext<S>.() -> A) -> Unit
typealias KCommand<SRC> = CommandContext<SRC>.() -> Unit

/**
 * Command registration shortcut
 * A simple alternate for calling [dev.doctorm4id.m4id.M4id.Command]'s addSourcedCommand
 */
fun CommandDispatcher<CommandSourceStack>.addCommand(
    baseCommandName: String,
    func: ArgDsl<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>>
) {
    M4id.Command.addSourcedCommand(baseCommandName, this, func)
}

// Suggestion list providers

/**
 * Creates a suggestion provider from a list of objects
 * @param func A producer of said list of objects
 */
fun <SRC : CommandSourceStack> M4idArgBuilder<SRC, *>.suggestionList(func: () -> List<Any>): SuggestionProvider<SRC> {
    return SuggestionProvider<SRC> { context, builder ->
        builder.addAll(func().map { it.toString() })
        builder.buildFuture()
    }
}

/**
 * Creates a tooltipped suggestion provider from a list of pairs of string and message.
 * Each string, when hovered, will show the corresponding tooltip message.
 * @param func A producer of the list of pairs
 */
fun <SRC : CommandSourceStack> M4idArgBuilder<SRC, *>.suggestionListTooltipped(func: () -> List<Pair<String, Message>>): SuggestionProvider<SRC> {
    return SuggestionProvider<SRC> { _, builder ->
        for ((item, msg) in func()) {
            builder.suggest(item, msg)
        }
        builder.buildFuture()
    }
}

/**
 * Creates a M4id command. These commands do not return an integer, unlike
 * regular commands.
 */
fun <SRC : CommandSourceStack> m4idCommand(func: KCommand<SRC>): Command<SRC> {
    return Command<SRC> {
        it.func()
        1
    }
}

fun m4idServerCommand(func: KCommand<CommandSourceStack>): Command<CommandSourceStack> {
    return m4idCommand(func)
}




/**
 * Used to ensure that a command requires creative mode permissions to execute
 */
fun M4idArgBuilder<CommandSourceStack, *>.requiresCreative() {
    requires { it.entity is Player && it.player?.isCreative == true }
}

/**
 * Used to ensure that a command requires a specific op level or higher to execute
 */
fun M4idArgBuilder<CommandSourceStack, *>.requiresOp(opLevel: Int = 4) {
    requires { it.hasPermission(opLevel) }
}

/**
 * Used to ensure that a command requires creative mode or a specific op level or higher to execute
 */
fun M4idArgBuilder<CommandSourceStack, *>.requiresCreativeOrOp(opLevel: Int = 4) {
    requires { (it.entity is Player && it.player?.isCreative == true) || it.hasPermission(opLevel) }
}


