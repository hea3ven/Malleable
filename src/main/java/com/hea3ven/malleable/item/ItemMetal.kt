package com.hea3ven.malleable.item

import com.hea3ven.malleable.metal.Metal
import net.minecraft.item.ItemStack

interface ItemMetal {

	val metals: Array<Metal>

	fun getMetal(stack: ItemStack) = metals[stack.metadata]

}

interface ItemMetalSingle : ItemMetal {
	val metal: Metal

	override val metals: Array<Metal>
		get() = arrayOf(metal)

	override fun getMetal(stack: ItemStack) = metal
}