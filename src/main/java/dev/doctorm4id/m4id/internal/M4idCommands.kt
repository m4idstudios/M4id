package dev.doctorm4id.m4id.internal

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import dev.doctorm4id.m4id.M4id
import dev.doctorm4id.m4id.command.addCommand
import dev.doctorm4id.m4id.command.m4idServerCommand
import dev.doctorm4id.m4id.command.suggestionList
import dev.doctorm4id.m4id.ext.ResourceLocation
import dev.doctorm4id.m4id.text.sendSuccess
import dev.doctorm4id.m4id.text.textLiteral
import dev.doctorm4id.percale.Percale
import dev.doctorm4id.percale.contextualCodec
import dev.doctorm4id.percale.reverse.GsonElementSerializer
import dev.doctorm4id.percale.reverse.GsonObjectSerializer
import dev.doctorm4id.percale.reverse.PercaleJson
import dev.doctorm4id.percale.reverse.toSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

object M4idCommands {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>, buildContext: CommandBuildContext, selection: Commands.CommandSelection) {

        dispatcher.addCommand(M4id.MOD_ID) {

            requires { it.hasPermission(2) }

            "dump" {
                "registry" {
                    val dumpables = suggestionList { BuiltInRegistries.REGISTRY.entrySet().toList().map { it.key.location() } }
                    argResource("dump_what", items = dumpables) runs { what ->
                        dumpRegistry(what()).run(this)
                    }
                }
            }

            if (!M4id.xplat().isDevelopmentEnvironment()) return@addCommand

            "test" {
                "text" runs text()
                argString("doot") { doot ->
                    this runs {
                        println(doot())
                    }
                }

                // TODO: Fix ([23:02:38] [Render thread/WARN] (Minecraft) Unknown custom packet payload: m4id:test)
                // I swear it worked before I am not crazy....
                "net" runs {
                    try {
                        M4id.Logger.debug("Sending Net Test Message..")
                        TestMsg("net send here").sendToClient(source.playerOrException)
                    } catch (e: Exception) {
                        M4id.Logger.debug("M4id Net Test Message Failed.")
                        e.printStackTrace()
                    }
                }

                "hand" runs {
                    handTests(this)
                }

                "item" runs {
                    itemTests(this, buildContext)
                }

                "percale" {
                    "toggle" {
                        "logs" runs {
                            Percale.shouldSyslog = !Percale.shouldSyslog
                            println("Percale logging is now: ${Percale.shouldSyslog}")
                        }
                    }
                }
            }
        }


    }

    fun itemTests(commandContext: CommandContext<CommandSourceStack>, registryAccess: CommandBuildContext) {
        commandContext.run {

            try {

                val player = source.playerOrException
                val held = player.mainHandItem

                val json = Json {
                    serializersModule = SerializersModule {
                        contextualCodec(ResourceLocation.CODEC)
                        contextual(GsonElementSerializer)
                        contextual(GsonObjectSerializer)
                    }
                    prettyPrint = true
                }

                val itemCodec = ItemStack.CODEC

                val percaleFormat = PercaleJson(RegistryOps.create(JsonOps.INSTANCE, source.server.registryAccess()), json)

                val itemString = percaleFormat.dynamicEncodeToString(held, itemCodec.toSerializer())

                println(itemString)

//                val encoded = itemCodec.encodeStart(RegistryOps.of(JsonOps.INSTANCE, source.server.registryManager), held)
//                println(encoded)

                for (comp in held.componentsPatch.entrySet()) {
                    println("COMP:")
                    println(comp.key)
                    comp.key.codec()
                    val cv = comp.value.getOrNull()
                    println(cv)
                    val codec = comp.key.codec() as Codec<Any>
                    println("CODEC: $codec")
                    val codSer = codec.toSerializer()
                    println("CODESER: $codSer")

                    try {
                        if (cv != null) {
                            println("GEN SERD..")
                            val serd = percaleFormat.dynamicEncodeToString(cv, codSer)
                            println("SERD: $serd")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun handTests(commandContext: CommandContext<CommandSourceStack>) {
        commandContext.run {

            try {
                println("Doing hand test")

                val player = source.playerOrException
                val held = player.mainHandItem

                val json = Json {
                    serializersModule = SerializersModule {
                        contextualCodec(ResourceLocation.CODEC)
                        contextualCodec(ItemStack.CODEC)
                        contextual(GsonElementSerializer)
                        contextual(GsonObjectSerializer)
                    }
                    prettyPrint = true
                }

                val regOps = RegistryOps.create(JsonOps.INSTANCE, source.server.registryAccess())

                val percaleFormat = PercaleJson(regOps, json)

                val itemCodec = ItemStack.CODEC

                val itemSer = itemCodec.toSerializer(JsonObject.serializer())

                println("Item Ser is: $itemSer - ${itemSer.descriptor} - ${itemSer.descriptor.kind}")


                val jsony = """
                {
                "id": "minecraft:stick",
                "count": 1,
                "components": {
                    "minecraft:damage": 2,
                    "minecraft:custom_name": "\"Sword of Love\"",
                    "minecraft:repair_cost": 1,
                    "minecraft:enchantments": {
                        "levels": {
                            "minecraft:sharpness": 5
                        }
                    }
                }
                }
                """.trimIndent()

                val comps = percaleFormat.dynamicDecodeFromString(jsony, itemSer)

                println("COMPS:")
                println(comps)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun dumpRegistry(what: ResourceLocation) = m4idServerCommand {
        if (BuiltInRegistries.REGISTRY.containsKey(what)) {
            val reg = BuiltInRegistries.REGISTRY[what]!!
            M4id.Logger.info("Contents of registry '$what':")
            reg.keySet().forEach { id ->
                M4id.Logger.info("  * [ID] $id")
            }
            source.sendSuccess("Dumped contents of '$what' to log.")
        } else {
            source.sendFailure(textLiteral("There is no registry with that name."))
        }
    }

    private fun text() = m4idServerCommand {
        val test = Component.literal("Hello World!")
        source.sendSystemMessage(test)
    }
}