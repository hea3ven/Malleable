package com.hea3ven.metals.metal

import com.hea3ven.metals.ModMetals
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.util.IStringSerializable
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fml.relauncher.ReflectionHelper

class Metal(
		val metalName: String,
		val toolMaterial: Item.ToolMaterial?,
		val armorMaterial: ItemArmor.ArmorMaterial?,
		val color: Int,
		val oreName: String?,
		val blockName: String,
		val ingotName: String,
		val nuggetName: String,
		val genMinY: Int,
		val genMaxY: Int,
		val genCount: Int,
		val genSize: Int,
		val genDimension: Int?) : Comparable<Metal>, IStringSerializable {

	companion object {
		val IRON = Metal("iron", Item.ToolMaterial.IRON, ItemArmor.ArmorMaterial.IRON,
				(216 shl 16) + (216 shl 8) + 216, "oreIron", "blockIron", "ingotIron", "nuggetIron", 0, 64,
				20, 9, null);
		val COPPER = Metal("copper", null, null, (210 shl 16) + (80 shl 8) + 50, "oreCopper", "blockCopper",
				"ingotCopper", "nuggetCopper", 32, 64, 15, 12, 0);
		val TIN = Metal("tin", null, null, (185 shl 16) + (210 shl 8) + 230, "oreTin", "blockTin", "ingotTin",
				"nuggetTin", 45, 90, 24, 7, 0);
		val GOLD = Metal("gold", Item.ToolMaterial.GOLD, ItemArmor.ArmorMaterial.GOLD, 0, "oreGold",
				"blockGold", "ingotGold", "nuggetGold", 0, 32, 2, 9, null);
		val BRONZE = Metal("bronze", EnumHelper.addToolMaterial("bronze", 1, 1024, 6.0F, 2.0F, 22),
				EnumHelper.addArmorMaterial("bronze", "armor", 15, intArrayOf(1, 5, 3, 2), 10,
						SoundEvents.ITEM_ARMOR_EQUIP_IRON), (225 shl 16) + (170 shl 8) + 80, null,
				"blockBronze", "ingotBronze", "nuggetBronze", 0, 0, 0, 0, null);
		val STEEL = Metal("steel", EnumHelper.addToolMaterial("steel", 3, 3072, 7.0F, 2.5F, 14),
				EnumHelper.addArmorMaterial("steel", "armor", 28, intArrayOf(2, 6, 5, 2), 10,
						SoundEvents.ITEM_ARMOR_EQUIP_IRON), (100 shl 16) + (100 shl 8) + 110, null,
				"blockSteel", "ingotSteel", "nuggetSteel", 0, 0, 0, 0, null);
		val COBALT = Metal("cobalt", EnumHelper.addToolMaterial("cobalt", 3, 2048, 9.0F, 4.0F, 16),
				EnumHelper.addArmorMaterial("cobalt", "armor", 20, intArrayOf(2, 7, 5, 3), 10,
						SoundEvents.ITEM_ARMOR_EQUIP_IRON), (65 shl 16) + (90 shl 8) + 205, "oreCobalt",
				"blockCobalt", "ingotCobalt", "nuggetCobalt", 16, 70, 10, 15, -1);
		val FERCO_STEEL = Metal("fercosteel",
				EnumHelper.addToolMaterial("fercoSteel", 3, 4096, 10.0F, 4.5F, 12),
				EnumHelper.addArmorMaterial("fercoSteel", "armor", 38, intArrayOf(2, 7, 5, 3), 10,
						SoundEvents.ITEM_ARMOR_EQUIP_IRON), (50 shl 16) + (60 shl 8) + 105, null,
				"blockFercoSteel", "ingotFercoSteel", "nuggetFercoSteel", 0, 0, 0, 0, null);
		val TUNGSTEN = Metal("tungsten", EnumHelper.addToolMaterial("tungsten", 3, 5120, 7.0F, 2.5F, 20),
				EnumHelper.addArmorMaterial("tungsten", "armor", 40, intArrayOf(3, 8, 6, 3), 10,
						SoundEvents.ITEM_ARMOR_EQUIP_IRON), (60 shl 16) + (50 shl 8) + 50, "oreTungsten",
				"blockTungsten", "ingotTungsten", "nuggetTungsten", 8, 64, 30, 5, 1);
		val MUSHET_STEEL = Metal("mushetsteel",
				EnumHelper.addToolMaterial("mushetSteel", 3, 8192, 8.0F, 3.0F, 12),
				EnumHelper.addArmorMaterial("mushetSteel", "armor", 56, intArrayOf(3, 8, 6, 3), 10,
						SoundEvents.ITEM_ARMOR_EQUIP_IRON), (25 shl 16) + (35 shl 8) + 25, null,
				"blockMushetSteel", "ingotMushetSteel", "nuggetMushetSteel", 0, 0, 0, 0, null);

		val values = arrayOf(IRON, COPPER, TIN, GOLD, BRONZE, STEEL, COBALT, FERCO_STEEL, TUNGSTEN,
				MUSHET_STEEL)

		fun initVanillaMetals() {
			// ToolMaterial.IRON.maxUses = 2048
			ReflectionHelper.setPrivateValue(Item.ToolMaterial::class.java, Item.ToolMaterial.IRON, 2048, 6);
			initVanillaTools(2048, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE,
					Items.IRON_SWORD)
			// ArmorMaterial.IRON.maxDamageFactorIn = 20
			ReflectionHelper.setPrivateValue(ItemArmor.ArmorMaterial::class.java,
					ItemArmor.ArmorMaterial.IRON, 20, 6);
			initVanillaArmor(ItemArmor.ArmorMaterial.IRON, Items.IRON_HELMET, Items.IRON_CHESTPLATE,
					Items.IRON_LEGGINGS, Items.IRON_BOOTS)

			// ToolMaterial.GOLD.maxUses = 512
			ReflectionHelper.setPrivateValue(Item.ToolMaterial::class.java, Item.ToolMaterial.IRON, 512, 6);
			initVanillaTools(512, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE,
					Items.IRON_SWORD)
			// ArmorMaterial.IRON.maxDamageFactorIn = 20
			ReflectionHelper.setPrivateValue(ItemArmor.ArmorMaterial::class.java,
					ItemArmor.ArmorMaterial.IRON, 10, 6);
			initVanillaArmor(ItemArmor.ArmorMaterial.GOLD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE,
					Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS)

			// ToolMaterial.DIAMOND.maxUses = 1
			ReflectionHelper.setPrivateValue(Item.ToolMaterial::class.java, Item.ToolMaterial.IRON, 1, 6);
			initVanillaTools(1, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE,
					Items.IRON_SWORD)
			// ArmorMaterial.DIAMOND.maxDamageFactorIn = 1
			ReflectionHelper.setPrivateValue(ItemArmor.ArmorMaterial::class.java,
					ItemArmor.ArmorMaterial.DIAMOND, 1, 6);
			initVanillaArmor(ItemArmor.ArmorMaterial.DIAMOND, Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE,
					Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS)
		}

		private fun initVanillaTools(damage: Int, vararg items: Item) {
			items.forEach { it.maxDamage = damage }
		}

		private fun initVanillaArmor(mat: ItemArmor.ArmorMaterial, headItem: ItemArmor, chestItem: ItemArmor,
				leggingItem: ItemArmor, bootsItem: ItemArmor) {
			headItem.setMaxDamage(mat.getDurability(EntityEquipmentSlot.HEAD));
			chestItem.setMaxDamage(mat.getDurability(EntityEquipmentSlot.CHEST));
			leggingItem.setMaxDamage(mat.getDurability(EntityEquipmentSlot.LEGS));
			bootsItem.setMaxDamage(mat.getDurability(EntityEquipmentSlot.FEET));
		}

		fun initToolRepairMaterials() {
			for (metal in values) {
				if (metal.toolMaterial == null || metal.toolMaterial.ordinal < 5)
					continue
				metal.toolMaterial.setRepairItem(ModMetals.proxy.ingot.createStack(metal))
			}
		}
	}

	override fun getName(): String? {
		return metalName;
	}

	fun getOreStack() = when (this) {
		IRON -> ItemStack(Blocks.IRON_ORE)
		GOLD -> ItemStack(Blocks.GOLD_ORE)
		else -> {
			if (oreName != null)
				ModMetals.proxy.ore.createStack(this)
			else
				throw IllegalArgumentException(name)
		}
	}

	fun getBlockStack() = when (this) {
		IRON -> ItemStack(Blocks.IRON_BLOCK)
		GOLD -> ItemStack(Blocks.GOLD_BLOCK)
		else -> {
			if (ModMetals.proxy.block.metals.contains(this))
				ModMetals.proxy.block.createStack(this)
			else
				throw IllegalArgumentException(name)
		}
	}

	fun getIngotStack() = when (this) {
		IRON -> ItemStack(Items.IRON_INGOT)
		GOLD -> ItemStack(Items.GOLD_INGOT)
		else -> {
			if (ModMetals.proxy.ingot.metals.contains(this))
				ModMetals.proxy.ingot.createStack(this)
			else
				throw IllegalArgumentException(name)
		}
	}

	fun getNuggetStack() = when (this) {
		GOLD -> ItemStack(Items.GOLD_NUGGET)
		else -> {
			if (ModMetals.proxy.nugget.metals.contains(this))
				ModMetals.proxy.nugget.createStack(this)
			else
				throw IllegalArgumentException(name)
		}
	}

	fun getStack(type: ItemType) = when (type) {
		ItemType.ORE -> getOreStack()
		ItemType.BLOCK -> getBlockStack()
		ItemType.INGOT -> getIngotStack()
		ItemType.NUGGET -> getNuggetStack()
		else -> throw IllegalArgumentException("type")
	}

	fun getOreDictName(type: ItemType) = when (type) {
		ItemType.ORE -> oreName
		ItemType.BLOCK -> blockName
		ItemType.INGOT -> ingotName
		ItemType.NUGGET -> nuggetName
		else -> throw IllegalArgumentException("type")
	}

	override fun compareTo(other: Metal): Int {
		return if (this == other) 0 else this.name!!.compareTo(other.name!!);
	}

	enum class ItemType {
		ORE,
		BLOCK,
		INGOT,
		NUGGET
	}
}
