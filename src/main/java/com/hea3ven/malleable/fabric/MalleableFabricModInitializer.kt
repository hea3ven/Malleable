package com.hea3ven.malleable.fabric

import com.hea3ven.malleable.ModMalleable
import com.hea3ven.tools.commonutils.mod.fabric.FabricServerModHandler
import net.fabricmc.api.ModInitializer

object MalleableFabricModInitializer : ModInitializer {
    override fun onInitialize() {
        FabricServerModHandler.onInitializeServer(ModMalleable)
    }
}