package com.hea3ven.malleable.world

import com.hea3ven.malleable.block.BlockMetalOre
import com.hea3ven.malleable.metal.Metal
import net.minecraft.block.state.pattern.BlockMatcher
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkGenerator
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.feature.WorldGenMinable
import net.minecraftforge.fml.common.IWorldGenerator
import java.util.*

class WorldGeneratorOre(val ore: BlockMetalOre) : IWorldGenerator {

	private var minables: Map<Int, List<MinableDefinition>>? = null

	override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World,
			chunkGenerator: IChunkGenerator, chunkProvider: IChunkProvider) {
		if (minables == null)
			initMinables()

		val dimMinables = minables!![world.provider!!.dimension] ?: return
		for (minDef in dimMinables) {
			for (i in 0..minDef.metal.genCount) {
				val veinX = chunkX * 16 + rand.nextInt(16)
				val veinZ = chunkZ * 16 + rand.nextInt(16)
				val y = minDef.metal.genMinY + rand.nextInt(minDef.metal.genMaxY - minDef.metal.genMinY)
				minDef.mineable.generate(world, rand, BlockPos(veinX, y, veinZ))
			}
		}
	}

	private fun initMinables() {
		minables = BlockMetalOre.ORES
				.map { metal -> createMinable(metal) }
				.filter { it.metal.genDimension != null }
				.groupBy { it.metal.genDimension!! }
	}

	private fun createMinable(metal: Metal)
			= MinableDefinition(metal,
			WorldGenMinable(ore.defaultState.withProperty(ore.getMetalProperty(), metal), metal.genSize,
					BlockMatcher.forBlock(when (metal.genDimension) {
						0 -> Blocks.STONE
						-1 -> Blocks.NETHERRACK
						1 -> Blocks.END_STONE
						else -> null
					})))

	private data class MinableDefinition(val metal: Metal, val mineable: WorldGenMinable)
}

