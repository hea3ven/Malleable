package com.hea3ven.malleable.item

import com.hea3ven.malleable.metal.Metal
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemTool

//class ItemMetalAxe(override val metal: Metal) : ItemAxe(metal.toolMaterial), ItemMetalSingle {
class ItemMetalAxe(override val metal: Metal) : ItemTool(metal.toolMaterial, EFFECTIVE_ON), ItemMetalSingle {
	override fun getStrVsBlock(stack: ItemStack, state: IBlockState): Float {
		val material = state.getMaterial();
		if ( material != Material.WOOD && material != Material.PLANTS && material != Material.VINE )
			return super.getStrVsBlock(stack, state)
		else
			return this.efficiencyOnProperMaterial;
	}

	companion object {
		private val EFFECTIVE_ON = setOf(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2,
				Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER,
				Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE)
	}

}
