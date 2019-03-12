package com.hea3ven.malleable.item

import com.hea3ven.malleable.metal.Metal
import net.minecraft.item.SwordItem

class MetalSwordItem(val metal: Metal, attackDamage: Int, attackSpeed: Float, settings: Settings) :
        SwordItem(metal.toolMaterial, attackDamage, attackSpeed, settings)
