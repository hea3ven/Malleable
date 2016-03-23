package com.hea3ven.metals

import com.hea3ven.tools.bootstrap.Bootstrap
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(modid = ModMetals.MODID, version = ModMetals.VERSION, dependencies = ModMetals.DEPENDENCIES)
class ModMetals {
	@Mod.EventHandler
	fun OnPreInit(event: FMLPreInitializationEvent?) {
		proxy.onPreInitEvent(event)
	}

	@Mod.EventHandler
	fun OnInit(event: FMLInitializationEvent?) {
		proxy.onInitEvent(event)
	}

	@Mod.EventHandler
	fun OnPostInit(event: FMLPostInitializationEvent?) {
		proxy.onPostInitEvent(event)
	}

	companion object {
		const val MODID = "metals"
		const val VERSION = "1.8.9-1.0.0"
		const val DEPENDENCIES = "required-after:Forge@[11.15.1.1723,)"

		const val guiIdMetalFurnace = 1;

		init {
			Bootstrap.init()
		}

		val proxy = ProxyModMetals()
	}
}

