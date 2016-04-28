package com.hea3ven.metals.block

import com.hea3ven.metals.metal.Metal
import com.hea3ven.tools.commonutils.block.properties.PropertyValues
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.util.BlockRenderLayer

class BlockMetalOre : BlockMetalBase(Material.ROCK, ORES) {

	init {
		soundType = SoundType.STONE
	}

	override fun getMetalProperty() = METAL_ORE

	override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED

	companion object {
		val ORES: Array<Metal> = arrayOf(Metal.COPPER, Metal.TIN, Metal.COBALT, Metal.TUNGSTEN)

		val METAL_ORE = PropertyValues.create("metal", Metal::class.java, *ORES)
	}
}