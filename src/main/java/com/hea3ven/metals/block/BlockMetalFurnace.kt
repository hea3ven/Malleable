package com.hea3ven.metals.block

import com.hea3ven.metals.ModMetals
import com.hea3ven.metals.block.tileentity.TileMetalFurnace
import com.hea3ven.tools.commonutils.block.base.BlockMachine
import com.hea3ven.tools.commonutils.block.metadata.MetadataManagerBuilder
import net.minecraft.block.BlockFurnace
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BlockMetalFurnace : BlockMachine(Material.ROCK, ModMetals.MODID, ModMetals.guiIdMetalFurnace) {

	init {
		soundType = SoundType.STONE
		defaultState = blockState.baseState
				.withProperty(BlockFurnace.FACING, EnumFacing.NORTH)
				.withProperty(LIT, false)
	}

	val metaManager = MetadataManagerBuilder(this).map(2, BlockFurnace.FACING).map(1, LIT).build()

	override fun createNewTileEntity(worldIn: World, meta: Int) = TileMetalFurnace()

	override fun createBlockState() = BlockStateContainer(this, BlockFurnace.FACING, LIT)

	override fun getMetaFromState(state: IBlockState) = metaManager.getMeta(state)

	override fun getStateFromMeta(meta: Int) = metaManager.getState(meta)

	override fun onBlockPlaced(worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float,
			hitZ: Float, meta: Int, placer: EntityLivingBase): IBlockState? {
		return defaultState
				.withProperty(BlockFurnace.FACING, placer.getHorizontalFacing().getOpposite())
	}

	override fun getLightValue(state: IBlockState) = if (state.getValue(LIT)) 13 else 0

	companion object {
		val LIT = PropertyBool.create("lit")
	}
}