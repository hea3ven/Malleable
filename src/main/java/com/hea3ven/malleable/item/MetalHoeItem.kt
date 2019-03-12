package com.hea3ven.malleable.item

import com.hea3ven.malleable.metal.Metal
import net.minecraft.item.HoeItem
import net.minecraft.item.Item

class MetalHoeItem(val metal: Metal, swingSpeed: Float, settings: Item.Settings) :
        HoeItem(metal.toolMaterial, swingSpeed, settings)
