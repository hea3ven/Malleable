package com.hea3ven.malleable.block

import com.hea3ven.malleable.metal.Metal
import com.hea3ven.tools.commonutils.block.properties.PropertyValues
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.util.BlockRenderLayer

class BlockMetalBlock : BlockMetalBase(Material.IRON, BLOCKS) {

	init {
		soundType = SoundType.METAL
	}

	override fun getMetalProperty() = PropertyValues.create("metal", Metal::class.java, *BLOCKS)

	override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED

	companion object {
		val BLOCKS: Array<Metal> = arrayOf(Metal.COPPER, Metal.TIN, Metal.BRONZE, Metal.STEEL, Metal.COBALT,
				Metal.FERCO_STEEL, Metal.TUNGSTEN, Metal.MUSHET_STEEL)

	}
}