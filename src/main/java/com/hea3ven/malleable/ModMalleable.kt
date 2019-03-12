package com.hea3ven.malleable

import com.hea3ven.malleable.block.MetalBlock
import com.hea3ven.malleable.block.MetalOreBlock
import com.hea3ven.malleable.item.*
import com.hea3ven.malleable.metal.Metal
import com.hea3ven.malleable.world.gen.feature.EndOreFeature
import com.hea3ven.malleable.world.gen.feature.EndOreFeatureConfig
import com.hea3ven.tools.commonutils.mod.Mod
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biome.configureFeature
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.decorator.Decorator
import net.minecraft.world.gen.decorator.RangeDecoratorConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.OreFeatureConfig

object ModMalleable : Mod("malleable") {

    init {
        for (metal in arrayOf(Metal.COPPER, Metal.TIN, Metal.COBALT, Metal.TUNGSTEN)) {
            addBlock("${metal.metalName}_ore",
                    MetalOreBlock(metal, Block.Settings.of(Material.STONE).strength(3.0f, 3.0f)),
                    ItemGroup.BUILDING_BLOCKS)
        }
        for (metal in arrayOf(Metal.COPPER, Metal.TIN, Metal.BRONZE, Metal.STEEL, Metal.COBALT, Metal.FERCOSTEEL,
                Metal.TUNGSTEN, Metal.MUSHETSTEEL)) {
            addBlock("${metal.metalName}_block",
                    MetalBlock(metal, Block.Settings.of(Material.METAL).strength(3.0f, 10.0f)),
                    ItemGroup.BUILDING_BLOCKS)
            addItem("${metal.metalName}_nugget", Item(Item.Settings().itemGroup(ItemGroup.MATERIALS)))
            addItem("${metal.metalName}_ingot", Item(Item.Settings().itemGroup(ItemGroup.MATERIALS)))
        }
        for (metal in arrayOf(Metal.BRONZE, Metal.STEEL, Metal.COBALT, Metal.FERCOSTEEL, Metal.TUNGSTEN,
                Metal.MUSHETSTEEL)) {
            addItem("${metal.metalName}_pickaxe",
                    MetalPickaxeItem(metal, 1, -2.8f, Item.Settings().itemGroup(ItemGroup.TOOLS)))
            addItem("${metal.metalName}_shovel",
                    MetalShovelItem(metal, 1.5f, -3.5f, Item.Settings().itemGroup(ItemGroup.TOOLS)))
            addItem("${metal.metalName}_sword",
                    MetalSwordItem(metal, 3, -2.4f, Item.Settings().itemGroup(ItemGroup.TOOLS)))
            addItem("${metal.metalName}_axe",
                    MetalAxeItem(metal, 6.0f, -3.2f, Item.Settings().itemGroup(ItemGroup.TOOLS)))
            addItem("${metal.metalName}_hoe", MetalHoeItem(metal, 0.0f, Item.Settings().itemGroup(ItemGroup.TOOLS)))
            addItem("${metal.metalName}_helmet",
                    ArmorItem(metal.armorMaterial, EquipmentSlot.HEAD, Item.Settings().itemGroup(ItemGroup.COMBAT)))
            addItem("${metal.metalName}_chestplate",
                    ArmorItem(metal.armorMaterial, EquipmentSlot.CHEST, Item.Settings().itemGroup(ItemGroup.COMBAT)))
            addItem("${metal.metalName}_leggings",
                    ArmorItem(metal.armorMaterial, EquipmentSlot.LEGS, Item.Settings().itemGroup(ItemGroup.COMBAT)))
            addItem("${metal.metalName}_boots",
                    ArmorItem(metal.armorMaterial, EquipmentSlot.FEET, Item.Settings().itemGroup(ItemGroup.COMBAT)))
        }
        for (metal in arrayOf(Metal.BRONZE, Metal.STEEL, Metal.FERCOSTEEL, Metal.MUSHETSTEEL)) {
            addItem("${metal.metalName}_mix", Item(Item.Settings().itemGroup(ItemGroup.MATERIALS)))
        }

        //        addBlock("metal_furnace",
        //                 BlockMetalFurnace(Block.Settings.of(Material.STONE).strength(3.5f, 5.0f), id("metal_furnace")),
        //                 ItemGroup.DECORATIONS)

    }

    override fun onPreInit() {
        Metal.initVanillaMetals()
    }

    override fun onPostInit() {
        val endOreFeature = Registry.register(Registry.FEATURE, Identifier("malleable:end_ore"), EndOreFeature())
        for (biome in Registry.BIOME) {
            for (ore in arrayOf(Metal.COPPER, Metal.TIN, Metal.COBALT, Metal.TUNGSTEN)) {
                if (biome.category == Biome.Category.THE_END) {
                    if (ore.genDimension == 1) {
                        biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, configureFeature(endOreFeature,
                                EndOreFeatureConfig(getBlockInfo(ore.metalName + "_ore").block.defaultState,
                                        ore.genSize), Decorator.COUNT_RANGE,
                                RangeDecoratorConfig(ore.genCount, ore.genMinY, 0, ore.genMaxY)));
                    }
                } else {
                    val target = if (biome.category == Biome.Category.NETHER && ore.genDimension == -1) {
                        OreFeatureConfig.Target.NETHERRACK
                    } else if (biome.category != Biome.Category.THE_END && biome.category != Biome.Category.NETHER && ore.genDimension == 0) {
                        OreFeatureConfig.Target.NATURAL_STONE
                    } else {
                        null
                    }

                    if (target != null) {
                        biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, configureFeature(Feature.ORE,
                                OreFeatureConfig(target, getBlockInfo(ore.metalName + "_ore").block.defaultState,
                                        ore.genSize), Decorator.COUNT_RANGE,
                                RangeDecoratorConfig(ore.genCount, ore.genMinY, 0, ore.genMaxY)));
                    }
                }
            }
        }
    }

}

