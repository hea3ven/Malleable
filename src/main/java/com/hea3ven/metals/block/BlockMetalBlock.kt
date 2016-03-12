package com.hea3ven.metals.block

import com.hea3ven.metals.metal.Metal
import com.hea3ven.tools.commonutils.block.properties.PropertyValues
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.util.EnumWorldBlockLayer

class BlockMetalBlock : BlockMetalBase(Material.iron, BLOCKS) {

	override fun getMetalProperty(): IProperty<Metal> = METAL_BLOCK

	override fun getBlockLayer(): EnumWorldBlockLayer = EnumWorldBlockLayer.CUTOUT_MIPPED

	companion object {
		val BLOCKS: Array<Metal> = arrayOf(Metal.COPPER, Metal.TIN, Metal.BRONZE, Metal.STEEL, Metal.COBALT,
				Metal.FERCO_STEEL, Metal.TUNGSTEN, Metal.MUSHET_STEEL);

		val METAL_BLOCK = PropertyValues.create("metal", Metal::class.java, *BLOCKS)
	}
}