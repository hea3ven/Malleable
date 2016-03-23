package com.hea3ven.metals.block

import com.hea3ven.metals.ModMetals
import com.hea3ven.metals.block.tileentity.TileMetalFurnace
import com.hea3ven.metals.client.gui.GuiMetalFurnace
import com.hea3ven.metals.metal.Metal
import com.hea3ven.tools.commonutils.block.base.BlockMachine
import com.hea3ven.tools.commonutils.block.metadata.MetadataManagerBuilder
import com.hea3ven.tools.commonutils.block.properties.PropertyValues
import net.minecraft.block.BlockFurnace
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockState
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

class BlockMetalFurnace : BlockMachine(Material.iron, ModMetals.MODID,
		ModMetals.guiIdMetalFurnace), BlockMetal {

	override val metals: Array<Metal> = arrayOf(Metal.COPPER, Metal.BRONZE, Metal.STEEL, Metal.COBALT)

	val metaManager = MetadataManagerBuilder(this).map(2, BlockFurnace.FACING).map(2, METAL).build()

	override fun createNewTileEntity(worldIn: World, meta: Int) = TileMetalFurnace()

	override fun createBlockState() = BlockState(this, BlockFurnace.FACING, METAL)

	override fun getMetaFromState(state: IBlockState) = metaManager.getMeta(state)

	override fun getStateFromMeta(meta: Int) = metaManager.getState(meta)

	override fun getRenderColor(state: IBlockState) = state.getValue(METAL).color

	override fun colorMultiplier(world: IBlockAccess, pos: BlockPos, renderPass: Int) =
			getRenderColor(world.getBlockState(pos))

	override fun onBlockPlaced(worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float,
			hitZ: Float, meta: Int, placer: EntityLivingBase): IBlockState? {
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
				.withProperty(BlockFurnace.FACING, placer.getHorizontalFacing().getOpposite());
	}

	override fun getSubBlocks(item: Item, tab: CreativeTabs?, list: MutableList<ItemStack>) {
		METALS.forEach { list.add(createStack(it)) }
	}

	override fun damageDropped(state: IBlockState) = getMetaFromState(
			defaultState.withProperty(METAL, state.getValue(METAL)))

	override fun getDamageValue(world: World, pos: BlockPos) = damageDropped(world.getBlockState(pos))

	fun createStack(metal: Metal) = ItemStack(this, 1,
			damageDropped(getDefaultState().withProperty(METAL, metal)))

	fun getTier(state: IBlockState) = when (state.getValue(METAL)) {
		Metal.BRONZE -> 1
		Metal.STEEL -> 2
		Metal.COBALT -> 3
		Metal.COPPER -> 0
		else -> -1
	}

	companion object {

		val METALS = arrayOf(Metal.BRONZE, Metal.STEEL, Metal.COBALT, Metal.COPPER);

		val METAL = PropertyValues.create("metal", Metal::class.java, *METALS)
	}
}