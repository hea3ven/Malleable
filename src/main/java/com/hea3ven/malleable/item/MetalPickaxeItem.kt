package com.hea3ven.malleable.item

import com.hea3ven.malleable.metal.Metal
import net.minecraft.item.*

class MetalPickaxeItem(val metal: Metal, attackDamage: Int, attackSpeed: Float, settings: Settings) :
        PickaxeItem(metal.toolMaterial, attackDamage, attackSpeed, settings)
