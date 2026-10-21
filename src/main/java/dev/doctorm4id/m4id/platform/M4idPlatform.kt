package dev.doctorm4id.m4id.platform

interface M4idPlatform {

	fun isModLoaded(modId: String?): Boolean

	fun loader(): ModLoader?

	fun mcVersion(): String?

	fun isDevelopmentEnvironment(): Boolean

	fun isDebug(): Boolean {
		return isDevelopmentEnvironment()
	}

	enum class ModLoader {
		FABRIC, NEOFORGE, FORGE, QUILT
	}
}
