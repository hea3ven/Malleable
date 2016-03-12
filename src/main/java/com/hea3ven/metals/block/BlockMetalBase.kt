package com.hea3ven.metals.block

import com.hea3ven.metals.metal.Metal
import com.hea3ven.tools.commonutils.block.metadata.MetadataManagerBuilder
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockState
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

abstract class BlockMetalBase(material: Material, override val metals: Array<Metal>) :
		Block(material), BlockMetal {

	val metaManager = MetadataManagerBuilder(this).map(4, getMetalProperty()).build()

	override fun createBlockState(): BlockState? = BlockState(
			this, getMetalProperty())

	protected abstract fun getMetalProperty(): IProperty<Metal>

	override fun getMetaFromState(state: IBlockState) = metaManager.getMeta(state)

	override fun getStateFromMeta(meta: Int) = metaManager.getState(meta)

	override fun getRenderColor(state: IBlockState) = state.getValue(getMetalProperty()).color

	override fun colorMultiplier(world: IBlockAccess, pos: BlockPos, renderPass: Int) =
			getRenderColor(world.getBlockState(pos))

	override fun damageDropped(state: IBlockState) = getMetaFromState(state)

	override fun getDamageValue(world: World, pos: BlockPos) = damageDropped(world.getBlockState(pos))

	override fun getSubBlocks(item: Item, tab: CreativeTabs?, list: MutableList<ItemStack>) {
		metals.forEach { list.add(createStack(it)) }
	}

	fun createStack(metal: Metal) = createStack(metal, 1)

	fun createStack(metal: Metal, size: Int) = ItemStack(this, size,
			damageDropped(getDefaultState().withProperty(getMetalProperty(), metal)))
}