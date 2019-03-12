package com.hea3ven.malleable.world.gen.feature

import com.google.common.collect.ImmutableMap
import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.world.gen.feature.FeatureConfig

class EndOreFeatureConfig(val state: BlockState, val size: Int) : FeatureConfig {

    override fun <T> serialize(var1: DynamicOps<T>): Dynamic<T> {
        return Dynamic(var1, var1.createMap(
                ImmutableMap.of(var1.createString("size"), var1.createInt(size), var1.createString("state"),
                        BlockState.serialize(var1, state).value)));
    }


}

fun <T> deserialize(dyn: Dynamic<T>): EndOreFeatureConfig {
    val size = dyn.get("size").asInt(0);
    val state = dyn.get("state").map(BlockState::deserialize).orElse(Blocks.AIR.defaultState);
    return EndOreFeatureConfig(state, size);
}
