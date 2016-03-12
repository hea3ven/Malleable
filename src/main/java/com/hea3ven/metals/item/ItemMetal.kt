package com.hea3ven.metals.item

import com.hea3ven.metals.metal.Metal
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class ItemMetal(val metals: Array<Metal>) : Item() {
	init {
		setHasSubtypes(true);
	}

	fun getMetal(meta: Int): Metal = metals[meta]

	fun getMetaForMetal(metal: Metal): Int {
		var i = 0
		while (metals[i] != metal)
			i++;
		return i;
	}

	fun createStack(metal: Metal): ItemStack = createStack(metal, 1)

	fun createStack(metal: Metal, size: Int): ItemStack = ItemStack(this, size, getMetaForMetal(metal))

	override fun getColorFromItemStack(stack: ItemStack, renderPass: Int): Int =
			getMetal(stack.metadata).color

	override fun getSubItems(item: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
		metals.forEach { subItems.add(createStack(it)) }
	}

	override fun getUnlocalizedName(stack: ItemStack): String {
		return super.getUnlocalizedName(stack) + "." + getMetal(stack.metadata).getName();
	}

	companion object {
		val NUGGETS = arrayOf(Metal.COPPER, Metal.TIN, Metal.BRONZE, Metal.IRON, Metal.STEEL, Metal.COBALT,
				Metal.FERCO_STEEL, Metal.TUNGSTEN, Metal.MUSHET_STEEL)
		val INGOTS = arrayOf(Metal.COPPER, Metal.TIN, Metal.BRONZE, Metal.STEEL, Metal.COBALT,
				Metal.FERCO_STEEL, Metal.TUNGSTEN, Metal.MUSHET_STEEL);
	}
}
