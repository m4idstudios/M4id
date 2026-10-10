package dev.doctorm4id.m4id.criterion

//? > 1.20.1 {

import com.mojang.serialization.JsonOps
import dev.doctorm4id.m4id.M4id
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.advancements.critereon.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.GsonHelper
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Predicate
import kotlin.jvm.optionals.getOrNull
//import kotlin.reflect.full.isSubclassOf
typealias SimpleTriggerInstance = SimpleCriterionTrigger.SimpleInstance
typealias SimpleTrigger = SimpleCriterionTrigger<SimpleTriggerInstance>
typealias SimpleTriggerPredicate = Predicate<SimpleTriggerInstance>
typealias GsonObject = com.google.gson.JsonObject

/**
 * Accessed via [M4id.Criterion][dev.doctorm4id.M4id.Criterion]
 */
class M4idCriterionApi internal constructor() {

    fun interface KambrikCriterionSubscriber {
        fun handle(player: ServerPlayer, criterion: SimpleTrigger, predicate: SimpleTriggerPredicate)
    }

    /**
     * A registered handler. [trigger] is the trigger the conditions were declared for, or null if only
     * a bare conditions instance was supplied (in which case the trigger can't be checked).
     */
    private class Handler(
        val trigger: CriterionTrigger<*>?,
        val conditions: SimpleTriggerInstance,
        val func: ServerPlayer.() -> Unit
    )

    private val handlers = CopyOnWriteArrayList<Handler>()

    private val subscribers = CopyOnWriteArrayList<KambrikCriterionSubscriber>()

    fun handleGameTrigger(player: ServerPlayer, criterion: SimpleTrigger, predicate: SimpleTriggerPredicate) {
        for (subscriber in subscribers) {
            subscriber.handle(player, criterion, predicate)
        }
        for (handler in handlers) {
            // If we know which trigger these conditions belong to, only test against that trigger
            if (handler.trigger != null && handler.trigger !== criterion) continue
            if (testAgainst(criterion, handler.conditions, predicate)) {
                handler.func(player)
            }
        }
    }

    fun subscribe(subscriber: KambrikCriterionSubscriber) {
        subscribers.add(subscriber)
    }

    fun addCriterionHandler(jsonString: String, func: ServerPlayer.() -> Unit) {
        addCriterionHandler(Json.decodeFromString(JsonObject.serializer(), jsonString), func)
    }

    fun addCriterionHandler(jsonObject: JsonObject, func: ServerPlayer.() -> Unit) {
        val gsonData = GsonHelper.parse(jsonObject.toString()) // KSX Json to GSON Json
        val criterion = createCriterionFromGson(gsonData) ?: return
        val conditions = criterion.triggerInstance as? SimpleTriggerInstance ?: return
        // Keep the trigger the JSON named, so we only fire for that trigger
        handlers.add(Handler(criterion.trigger, conditions, func))
    }

    /**
     * Adds a handler for a bare conditions instance. Since the trigger isn't known, the handler is
     * tested against every simple trigger that fires; a trigger whose predicate doesn't accept
     * these conditions is skipped.
     */
    fun addCriterionHandler(absCond: SimpleTriggerInstance, func: ServerPlayer.() -> Unit) {
        handlers.add(Handler(null, absCond, func))
    }

    fun createCriterionConditionsFromJson(jsonCriterion: JsonObject): SimpleTriggerInstance? {
        val gsonData = GsonHelper.parse(jsonCriterion.toString()) // KSX Json to GSON Json
        return createCriterionConditionsFromGson(gsonData)
    }

    fun createCriterionConditionsFromGson(gsonCriterion: GsonObject): SimpleTriggerInstance? {
        return createCriterionFromGson(gsonCriterion)?.triggerInstance as? SimpleTriggerInstance
    }

    private fun createCriterionFromGson(gsonCriterion: GsonObject): Criterion<*>? {
        return try {
            Criterion.CODEC.decode(JsonOps.INSTANCE, gsonCriterion).result().getOrNull()?.first
        } catch (e: Exception) {
            M4id.Logger.error("Unable to decode criterion from JSON", e)
            null
        }
    }

    fun <T : SimpleTriggerInstance> testAgainst(criterion: SimpleCriterionTrigger<T>, conditions: SimpleTriggerInstance, predicate: Predicate<T>): Boolean {
        return try {
            @Suppress("UNCHECKED_CAST")
            predicate.test(conditions as T)
        } catch (e: ClassCastException) {
            // These conditions belong to a different kind of trigger than the one that fired
            false
        }
    }

}

//? }