package com.hea3ven.metals.item

import com.hea3ven.metals.metal.Metal
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemTool

//class ItemMetalAxe(override val metal: Metal) : ItemAxe(metal.toolMaterial), ItemMetalSingle {
class ItemMetalAxe(override val metal: Metal) : ItemTool(metal.toolMaterial, EFFECTIVE_ON), ItemMetalSingle {
	override fun getStrVsBlock(stack: ItemStack, state: IBlockState): Float {
		val material = state.getMaterial();
		if ( material != Material.wood && material != Material.plants && material != Material.vine )
			return super.getStrVsBlock(stack, state)
		else
			return this.efficiencyOnProperMaterial;
	}

	companion object {
		private val EFFECTIVE_ON = setOf(Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2,
				Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder,
				Blocks.wooden_button, Blocks.wooden_pressure_plate)
	}

}
