package dev.doctorm4id.m4id.platform.fabric

//? fabric {

import dev.doctorm4id.m4id.platform.M4idPlatform
import net.fabricmc.loader.api.FabricLoader

class FabricM4idPlatform : M4idPlatform {

	override fun isModLoaded(modId: String?): Boolean {
		return FabricLoader.getInstance().isModLoaded(modId)
	}

	override fun loader(): M4idPlatform.ModLoader {
		return M4idPlatform.ModLoader.FABRIC
	}

	override fun mcVersion(): String? {
		return FabricLoader.getInstance().rawGameVersion
	}

	override fun isDevelopmentEnvironment(): Boolean {
		return FabricLoader.getInstance().isDevelopmentEnvironment
	}
}

//? }
