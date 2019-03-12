package com.hea3ven.malleable.item

import com.hea3ven.malleable.metal.Metal
import net.minecraft.item.*

class MetalShovelItem(val metal: Metal, attackDamage: Float, attackSpeed: Float, settings: Settings) :
        ShovelItem(metal.toolMaterial, attackDamage, attackSpeed, settings)
