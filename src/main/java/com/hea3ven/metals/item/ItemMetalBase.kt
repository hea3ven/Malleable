package com.hea3ven.metals.item

import com.hea3ven.metals.metal.Metal
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemMetalBase(override val metals: Array<Metal>) : Item(), ItemMetal {
	init {
		setHasSubtypes(true);
	}

	fun getMetaForMetal(metal: Metal): Int {
		var i = 0
		while (metals[i] != metal)
			i++;
		return i;
	}

	fun createStack(metal: Metal): ItemStack = createStack(metal, 1)

	fun createStack(metal: Metal, size: Int): ItemStack = ItemStack(this, size, getMetaForMetal(metal))

	override fun getSubItems(item: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
		metals.forEach { subItems.add(createStack(it)) }
	}

	override fun getUnlocalizedName(stack: ItemStack): String {
		return super.getUnlocalizedName(stack) + "." + getMetal(stack).getName();
	}

	companion object {
		val NUGGETS = arrayOf(Metal.COPPER, Metal.TIN, Metal.BRONZE, Metal.IRON, Metal.STEEL, Metal.COBALT,
				Metal.FERCO_STEEL, Metal.TUNGSTEN, Metal.MUSHET_STEEL)
		val INGOTS = arrayOf(Metal.COPPER, Metal.TIN, Metal.BRONZE, Metal.STEEL, Metal.COBALT,
				Metal.FERCO_STEEL, Metal.TUNGSTEN, Metal.MUSHET_STEEL);

		@SideOnly(Side.CLIENT)
		fun getColorHandler() = object : IItemColor {
			override fun getColorFromItemstack(stack: ItemStack, tintIndex: Int)
					= (stack.item as ItemMetal).getMetal(stack).color
		}

		@SideOnly(Side.CLIENT)
		fun getToolsColorHandler() = object : IItemColor {
			override fun getColorFromItemstack(stack: ItemStack, tintIndex: Int)
					= if (tintIndex == 1) (stack.item as ItemMetal).getMetal(stack).color else -1
		}
	}
}

