package dev.doctorm4id.m4id.internal.mixins;

import dev.doctorm4id.m4id.M4id;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

/**
 * Hooks into SimpleCriterionTrigger to forward game trigger events to M4id's criterion handler.
 */
@Mixin(SimpleCriterionTrigger.class)
public class AbstractCriterionTriggerer<T extends SimpleCriterionTrigger.SimpleInstance> {
    @Inject(method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Predicate;)V",
            at = @At("HEAD")
    )
    private void injected_m4id(ServerPlayer player, Predicate<T> predicate, CallbackInfo ci) {
        M4id.INSTANCE.getCriterion().handleGameTrigger(
                player,
                (SimpleCriterionTrigger<SimpleCriterionTrigger.SimpleInstance>)(Object)this,
                (Predicate<SimpleCriterionTrigger.SimpleInstance>) predicate
        );
    }
}

