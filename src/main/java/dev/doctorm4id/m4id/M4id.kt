package dev.doctorm4id.m4id

import dev.doctorm4id.m4id.bridge.M4idSharedApi
import dev.doctorm4id.m4id.command.M4idCommandApi
import dev.doctorm4id.m4id.criterion.M4idCriterionApi
import dev.doctorm4id.m4id.ext.ResourceLocation
import dev.doctorm4id.m4id.input.M4idInputApi
import dev.doctorm4id.m4id.internal.TestMsg
import dev.doctorm4id.m4id.logging.M4idLoggingApi
import dev.doctorm4id.m4id.message.M4idMessageApi
import org.apache.logging.log4j.LogManager
import dev.doctorm4id.m4id.serial.M4idSerialApi
import dev.doctorm4id.m4id.pool.M4idPool
import dev.doctorm4id.m4id.tick.M4idTickApi
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.Logger
import java.util.ServiceLoader

@SuppressWarnings("LoggingSimilarMessage")
object M4id {

	// ─────────────────────────────────────────────────────────────
	// I wanna use this prety line.

	const val MOD_ID: String = /*$ mod_id*/"m4id";
	const val MOD_VERSION: String =  /*$ mod_version*/"0.1.0";
	const val MOD_FRIENDLY_NAME: String =  /*$ mod_name*/"M4id";

	val SharedApi: M4idSharedApi by lazy {
		ServiceLoader.load(M4idSharedApi::class.java).firstOrNull() ?:
		error("No M4idSharedApi provider found. " + "Check the platform's META-INF/services entry.")
	}

	val Logger: Logger = LogManager.getLogger(MOD_FRIENDLY_NAME)

	fun idOf(unique: String): ResourceLocation = ResourceLocation(MOD_ID, unique)

	val Criterion: M4idCriterionApi by lazy {
		M4idCriterionApi()
	}

	val Serial: M4idSerialApi by lazy {
		M4idSerialApi()
	}

	val Command: M4idCommandApi by lazy {
		M4idCommandApi()
	}

	val Message: M4idMessageApi by lazy {
		M4idMessageApi()
	}

	val Logging: M4idLoggingApi by lazy {
		M4idLoggingApi()
	}

	val Input: M4idInputApi by lazy {
		M4idInputApi()
	}

	val Tick: M4idTickApi by lazy {
		M4idTickApi()
	}

	fun <T> Pool(): M4idPool<T> = M4idPool()

	fun onInitialize() {
		Logger.info( "Initializing {} on {}", MOD_ID, SharedApi.platform )
		Logger.debug( "{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME )

		Message.registerClientMessage<TestMsg>(idOf("test"))
	}

	fun onInitializeClient() {
		Logger.info( "Initializing {} Client on {}", MOD_ID, SharedApi.platform )
		Logger.debug( "{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME )
	}
}
