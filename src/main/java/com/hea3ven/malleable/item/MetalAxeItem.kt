package com.hea3ven.malleable.item

import com.hea3ven.malleable.metal.Metal
import net.minecraft.item.AxeItem

class MetalAxeItem(val metal: Metal, attackDamage: Float, attackSpeed: Float, settings: Settings) :
        AxeItem(metal.toolMaterial, attackDamage, attackSpeed, settings)
