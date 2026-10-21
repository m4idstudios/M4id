package dev.doctorm4id.m4id.platform.neoforge

//? neoforge {

/*import dev.doctorm4id.m4id.platform.Platform
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLLoader

class NeoforgeM4idPlatform : M4idPlatform {

	override fun isModLoaded(modId: String?): Boolean {
		return ModList.get().isLoaded(modId)
	}

	override fun loader(): Platform.ModLoader {
		return Platform.ModLoader.NEOFORGE
	}

	override fun mcVersion(): String {
		return ""
	}

	override fun isDevelopmentEnvironment(): Boolean {
		return !FMLLoader /*? if > 1.21.7 {*/ /*.getCurrent()*/ /*?}*/.isProduction()
	}
}

*///? }
