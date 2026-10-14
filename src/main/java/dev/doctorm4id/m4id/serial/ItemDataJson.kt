package dev.doctorm4id.m4id.serial

//abstract class ItemDataJson<T> : ItemData<T>() {
//
//    override val serializersModule: SerializersModule
//        get() = M4id.Serial.DefaultSerializers
//
//    private val format = Json {
//        this.serializersModule = this@ItemDataJson.serializersModule
//    }
//
//    override val defaultTag: Tag
//        get() = format.encodeToStringTag(ser, default())
//
//    override fun encode(value: T): Tag {
//        return format.encodeToStringTag(ser, value)
//    }
//
//    override fun decode(nbt: Tag): T {
//        return format.decodeFromStringTag(ser, nbt as NbtString)
//    }
//
//}