package com.hea3ven.malleable.world.gen.feature

import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IWorld
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig
import net.minecraft.world.gen.feature.Feature
import java.util.*

class EndOreFeature : Feature<EndOreFeatureConfig>({ deserialize(it) }) {
    override fun generate(iWorld_1: IWorld, var2: ChunkGenerator<out ChunkGeneratorConfig>, random_1: Random,
            blockPos_1: BlockPos, oreFeatureConfig_1: EndOreFeatureConfig): Boolean {
        val float_1 = random_1.nextFloat() * 3.1415927F;
        val float_2 = oreFeatureConfig_1.size / 8.0F;
        val int_1 = MathHelper.ceil((oreFeatureConfig_1.size / 16.0F * 2.0F + 1.0F) / 2.0F);
        val double_1 = (blockPos_1.getX() + MathHelper.sin(float_1) * float_2);
        val double_2 = (blockPos_1.getX() - MathHelper.sin(float_1) * float_2);
        val double_3 = (blockPos_1.getZ() + MathHelper.cos(float_1) * float_2);
        val double_4 = (blockPos_1.getZ() - MathHelper.cos(float_1) * float_2);
        val int_2 = true;
        val double_5 = (blockPos_1.getY() + random_1.nextInt(3) - 2);
        val double_6 = (blockPos_1.getY() + random_1.nextInt(3) - 2);
        val int_3 = blockPos_1.getX() - MathHelper.ceil(float_2) - int_1;
        val int_4 = blockPos_1.getY() - 2 - int_1;
        val int_5 = blockPos_1.getZ() - MathHelper.ceil(float_2) - int_1;
        val int_6 = 2 * (MathHelper.ceil(float_2) + int_1);
        val int_7 = 2 * (2 + int_1);

        for (int_8 in int_3..int_3 + int_6) {
            for (int_9 in int_5..int_5 + int_6) {
                if (int_4 <= iWorld_1.getTop(Heightmap.Type.OCEAN_FLOOR_WG, int_8, int_9)) {
                    return generateVeinPart(iWorld_1, random_1, oreFeatureConfig_1, double_1.toDouble(),
                            double_2.toDouble(), double_3.toDouble(), double_4.toDouble(), double_5.toDouble(),
                            double_6.toDouble(), int_3, int_4, int_5, int_6, int_7);
                }
            }
        }

        return false;
    }

    protected fun generateVeinPart(iWorld_1: IWorld, random_1: Random, oreFeatureConfig_1: EndOreFeatureConfig,
            double_1: Double, double_2: Double, double_3: Double, double_4: Double, double_5: Double, double_6: Double,
            int_1: Int, int_2: Int, int_3: Int, int_4: Int, int_5: Int): Boolean {
        var int_6 = 0
        val bitSet_1 = BitSet(int_4 * int_5 * int_4)
        val `blockPos$Mutable_1` = BlockPos.Mutable()
        val doubles_1 = DoubleArray(oreFeatureConfig_1.size * 4)

        var int_8: Int
        var double_12: Double
        var double_13: Double
        var double_14: Double
        var double_15: Double
        int_8 = 0
        while (int_8 < oreFeatureConfig_1.size) {
            val float_1 = int_8.toFloat() / oreFeatureConfig_1.size.toFloat()
            double_12 = MathHelper.lerp(float_1.toDouble(), double_1, double_2)
            double_13 = MathHelper.lerp(float_1.toDouble(), double_5, double_6)
            double_14 = MathHelper.lerp(float_1.toDouble(), double_3, double_4)
            double_15 = random_1.nextDouble() * oreFeatureConfig_1.size.toDouble() / 16.0
            val double_11 = ((MathHelper.sin(3.1415927f * float_1) + 1.0f).toDouble() * double_15 + 1.0) / 2.0
            doubles_1[int_8 * 4 + 0] = double_12
            doubles_1[int_8 * 4 + 1] = double_13
            doubles_1[int_8 * 4 + 2] = double_14
            doubles_1[int_8 * 4 + 3] = double_11
            ++int_8
        }

        int_8 = 0
        while (int_8 < oreFeatureConfig_1.size - 1) {
            if (doubles_1[int_8 * 4 + 3] > 0.0) {
                for (int_9 in int_8 + 1 until oreFeatureConfig_1.size) {
                    if (doubles_1[int_9 * 4 + 3] > 0.0) {
                        double_12 = doubles_1[int_8 * 4 + 0] - doubles_1[int_9 * 4 + 0]
                        double_13 = doubles_1[int_8 * 4 + 1] - doubles_1[int_9 * 4 + 1]
                        double_14 = doubles_1[int_8 * 4 + 2] - doubles_1[int_9 * 4 + 2]
                        double_15 = doubles_1[int_8 * 4 + 3] - doubles_1[int_9 * 4 + 3]
                        if (double_15 * double_15 > double_12 * double_12 + double_13 * double_13 + double_14 * double_14) {
                            if (double_15 > 0.0) {
                                doubles_1[int_9 * 4 + 3] = -1.0
                            } else {
                                doubles_1[int_8 * 4 + 3] = -1.0
                            }
                        }
                    }
                }
            }
            ++int_8
        }

        int_8 = 0
        while (int_8 < oreFeatureConfig_1.size) {
            val double_16 = doubles_1[int_8 * 4 + 3]
            if (double_16 >= 0.0) {
                val double_17 = doubles_1[int_8 * 4 + 0]
                val double_18 = doubles_1[int_8 * 4 + 1]
                val double_19 = doubles_1[int_8 * 4 + 2]
                val int_11 = Math.max(MathHelper.floor(double_17 - double_16), int_1)
                val int_12 = Math.max(MathHelper.floor(double_18 - double_16), int_2)
                val int_13 = Math.max(MathHelper.floor(double_19 - double_16), int_3)
                val int_14 = Math.max(MathHelper.floor(double_17 + double_16), int_11)
                val int_15 = Math.max(MathHelper.floor(double_18 + double_16), int_12)
                val int_16 = Math.max(MathHelper.floor(double_19 + double_16), int_13)

                for (int_17 in int_11..int_14) {
                    val double_20 = (int_17.toDouble() + 0.5 - double_17) / double_16
                    if (double_20 * double_20 < 1.0) {
                        for (int_18 in int_12..int_15) {
                            val double_21 = (int_18.toDouble() + 0.5 - double_18) / double_16
                            if (double_20 * double_20 + double_21 * double_21 < 1.0) {
                                for (int_19 in int_13..int_16) {
                                    val double_22 = (int_19.toDouble() + 0.5 - double_19) / double_16
                                    if (double_20 * double_20 + double_21 * double_21 + double_22 * double_22 < 1.0) {
                                        val int_20 = int_17 - int_1 + (int_18 - int_2) * int_4 + (int_19 - int_3) * int_4 * int_5
                                        if (!bitSet_1.get(int_20)) {
                                            bitSet_1.set(int_20)
                                            `blockPos$Mutable_1`.set(int_17, int_18, int_19)
                                            if (iWorld_1.getBlockState(
                                                            `blockPos$Mutable_1`).block == Blocks.END_STONE) {
                                                iWorld_1.setBlockState(`blockPos$Mutable_1`, oreFeatureConfig_1.state,
                                                        2)
                                                ++int_6
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ++int_8
        }

        return int_6 > 0
    }
}