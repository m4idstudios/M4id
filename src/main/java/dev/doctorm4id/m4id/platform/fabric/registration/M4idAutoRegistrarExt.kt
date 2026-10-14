package dev.doctorm4id.m4id.platform.fabric.registration

//? fabric {

import dev.doctorm4id.m4id.registration.M4idAutoRegistrar
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

fun <T : AbstractContainerMenu> M4idAutoRegistrar.forScreen(key: String, factory: MenuType.MenuSupplier<T>, requiredFeatures: FeatureFlagSet): Lazy<MenuType<T>> {
    return key.forRegistration(BuiltInRegistries.MENU) { MenuType(factory, requiredFeatures) } as Lazy<MenuType<T>>
}
//
fun <T : AbstractContainerMenu, D> M4idAutoRegistrar.forExtendedScreen(
    key: String,
    factory: ExtendedScreenHandlerType.ExtendedFactory<T, D>,
    packetCodec: StreamCodec<FriendlyByteBuf, D>
): Lazy<MenuType<T>> {
    return key.forRegistration(BuiltInRegistries.MENU) { ExtendedScreenHandlerType(factory, packetCodec) } as Lazy<MenuType<T>>
}

//? }