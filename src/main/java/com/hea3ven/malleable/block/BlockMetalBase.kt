package com.hea3ven.malleable.block

import com.hea3ven.malleable.item.ItemBlockMetal
import com.hea3ven.malleable.metal.Metal
import com.hea3ven.tools.commonutils.block.metadata.MetadataManagerBuilder
import com.hea3ven.tools.commonutils.client.renderer.color.IColorHandler
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

abstract class BlockMetalBase(material: Material, override val metals: Array<Metal>) :
		Block(material), BlockMetal {

	val metaManager = MetadataManagerBuilder(this).map(4, getMetalProperty()).build()

	override fun createBlockState() = BlockStateContainer(this, getMetalProperty())

	override fun getMetaFromState(state: IBlockState) = metaManager.getMeta(state)

	override fun getStateFromMeta(meta: Int) = metaManager.getState(meta)

	override fun damageDropped(state: IBlockState) = getMetaFromState(state)

	override fun getSubBlocks(item: Item, tab: CreativeTabs?, list: MutableList<ItemStack>) {
		metals.forEach { list.add(createStack(it)) }
	}

	fun createStack(metal: Metal) = createStack(metal, 1)

	fun createStack(metal: Metal, size: Int) = ItemStack(this, size,
			damageDropped(getDefaultState().withProperty(getMetalProperty(), metal)))

	companion object {

		@SideOnly(Side.CLIENT)
		fun getColorHandler() = object : IColorHandler {
			override fun getColorFromItemstack(stack: ItemStack, tintIndex: Int): Int {
				return ((stack.item as ItemBlockMetal).block as BlockMetal).getMetalFromStack(stack).color
			}

			override fun colorMultiplier(state: IBlockState, world: IBlockAccess, pos: BlockPos,
					tintIndex: Int): Int {
				return (state.block as BlockMetal).getMetalFromState(state).color
			}

		}
	}
}