package com.hea3ven.malleable

import com.hea3ven.tools.bootstrap.Bootstrap
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.GameRegistry

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

	@Mod.EventHandler
	fun OnMissingMappings(event: FMLMissingMappingsEvent) {
		event.all.filter { it.resourceLocation.resourceDomain == "metals" }.forEach {
			if (it.type == GameRegistry.Type.BLOCK)
				it.remap(Block.REGISTRY.getObject(ResourceLocation(MODID, it.resourceLocation.resourcePath)))
			else
				it.remap(Item.REGISTRY.getObject(ResourceLocation(MODID, it.resourceLocation.resourcePath)))
		}
	}

	companion object {
		const val MODID = "malleable"
		const val VERSION = "1.10-1.0.0"
		const val DEPENDENCIES = "required-after:Forge@[12.18.0.1986,)"

		const val guiIdMetalFurnace = 1;

		init {
			Bootstrap.init()
		}

		val proxy = ProxyModMetals()
	}
}

