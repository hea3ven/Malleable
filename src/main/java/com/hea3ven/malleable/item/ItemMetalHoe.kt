package com.hea3ven.malleable.item

import com.hea3ven.malleable.metal.Metal
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemAxe
import net.minecraft.item.ItemHoe
import net.minecraft.item.ItemStack

class ItemMetalHoe(override val metal: Metal) : ItemHoe(metal.toolMaterial), ItemMetalSingle {
}
