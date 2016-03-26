package com.hea3ven.metals.block

import com.hea3ven.metals.metal.Metal
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack

interface BlockMetal {
	val metals: Array<Metal>

	fun getMetalProperty(): IProperty<Metal>

	fun getMetalFromStack(stack: ItemStack) = metals[stack.metadata]

	fun getMetalFromState(state: IBlockState) = state.getValue(getMetalProperty())!!
}