package dev.doctorm4id.m4id.ext.packets

import net.minecraft.network.FriendlyByteBuf

fun FriendlyByteBuf.writeEnumM4id(enum: Enum<*>) {
    writeInt(enum.ordinal)
}

inline fun <reified T : Enum<T>> FriendlyByteBuf.readEnumM4id(): T {
    return enumValues<T>()[readInt()]
}