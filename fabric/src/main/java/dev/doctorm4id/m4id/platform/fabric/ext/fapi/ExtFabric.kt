package dev.doctorm4id.m4id.platform.fabric.ext.fapi

//? fabric {

import net.fabricmc.loader.api.metadata.CustomValue

fun CustomValue.CvObject.toMap(): Map<String, CustomValue> {
    return associate { it.key to it.value }
}

//? }