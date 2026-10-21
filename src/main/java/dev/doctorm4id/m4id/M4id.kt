package dev.doctorm4id.m4id

import dev.doctorm4id.m4id.command.M4idCommandApi
import dev.doctorm4id.m4id.criterion.M4idCriterionApi
import dev.doctorm4id.m4id.ext.ResourceLocation
import dev.doctorm4id.m4id.input.M4idInputApi
import dev.doctorm4id.m4id.logging.M4idLoggingApi
import dev.doctorm4id.m4id.message.M4idMessageApi
import dev.doctorm4id.m4id.platform.M4idPlatform
import org.apache.logging.log4j.LogManager
import dev.doctorm4id.m4id.serial.M4idSerialApi

//? fabric {

import dev.doctorm4id.m4id.platform.fabric.FabricM4idPlatform

//?} neoforge {

/*import dev.doctorm4id.m4id.platform.neoforge.NeoforgePlatform

*///? }

@SuppressWarnings("LoggingSimilarMessage")
object M4id {

	const val MOD_ID: String = /*$ mod_id*/"m4id";
	const val MOD_VERSION: String =  /*$ mod_version*/"0.1.0";
	const val MOD_FRIENDLY_NAME: String =  /*$ mod_name*/"M4id";

	private val PLATFORM: M4idPlatform = createPlatformInstance()

	val Logger = LogManager.getLogger("M4id")

	fun idOf(unique: String) = ResourceLocation(MOD_ID, unique)

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

	fun onInitialize() {
		Logger.info( "Initializing {} on {}", MOD_ID, xplat().loader() )
		Logger.debug( "{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME )
	}

	fun onInitializeClient() {
		Logger.info( "Initializing {} Client on {}", MOD_ID, xplat().loader() )
		Logger.debug( "{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME )
	}

	fun xplat(): M4idPlatform { return PLATFORM }

	private fun createPlatformInstance(): M4idPlatform {
		//? fabric {

		return FabricM4idPlatform()

		//? } neoforge {

		/*return NeoforgePlatform()

		*///?}
	}
}
