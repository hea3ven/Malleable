package com.hea3ven.metals.world

import com.hea3ven.metals.block.BlockMetalOre
import com.hea3ven.metals.metal.Metal
import net.minecraft.block.state.pattern.BlockHelper
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.feature.WorldGenMinable
import net.minecraftforge.fml.common.IWorldGenerator
import java.util.*

class WorldGeneratorOre(val ore: BlockMetalOre) : IWorldGenerator {

	private var minables: Map<Int, List<MinableDefinition>>? = null

	override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World,
			chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
		if (minables == null)
			initMinables()

		val dimMinables = minables!![world.provider!!.dimensionId]
		if (dimMinables == null)
			return
		for (minDef in dimMinables) {
			for (i in 0..minDef.metal.genCount) {
				val veinX = chunkX * 16 + rand.nextInt(16);
				val veinZ = chunkZ * 16 + rand.nextInt(16);
				val y = minDef.metal.genMinY + rand.nextInt(minDef.metal.genMaxY - minDef.metal.genMinY);
				minDef.mineable.generate(world, rand, BlockPos(veinX, y, veinZ));
			}
		}
	}

	private fun initMinables() {
		minables = BlockMetalOre.ORES.map { metal ->
			MinableDefinition(metal,
					WorldGenMinable(ore.defaultState.withProperty(BlockMetalOre.METAL_ORE, metal),
							metal.genSize, BlockHelper.forBlock(when (metal.genDimension) {
						0 -> Blocks.stone
						-1 -> Blocks.netherrack
						1 -> Blocks.end_stone
						else -> null
					})))
		}.filter { it.metal.genDimension != null }.groupBy { it.metal.genDimension!! }
	}

	private data class MinableDefinition(val metal: Metal, val mineable: WorldGenMinable)
}

