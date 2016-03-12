package com.hea3ven.metals.item

import com.hea3ven.metals.metal.Metal
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemAxe
import net.minecraft.item.ItemStack

class ItemMetalAxe(val metal: Metal) : ItemAxe(metal.toolMaterial) {

	override fun getColorFromItemStack(stack: ItemStack?, renderPass: Int): Int {
		if (renderPass == 1)
			return metal.color
		else
			return super.getColorFromItemStack(stack, renderPass)
	}
}
