package com.hea3ven.metals.block

import com.hea3ven.metals.metal.Metal
import com.hea3ven.tools.commonutils.block.properties.PropertyValues
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.util.EnumWorldBlockLayer

class BlockMetalOre : BlockMetalBase(Material.rock, ORES) {

	override fun getMetalProperty(): IProperty<Metal> = METAL_ORE

	override fun getBlockLayer(): EnumWorldBlockLayer = EnumWorldBlockLayer.CUTOUT_MIPPED

	companion object {
		val ORES: Array<Metal> = arrayOf(Metal.COPPER, Metal.TIN, Metal.COBALT, Metal.TUNGSTEN);

		val METAL_ORE = PropertyValues.create("metal", Metal::class.java, *ORES)
	}
}