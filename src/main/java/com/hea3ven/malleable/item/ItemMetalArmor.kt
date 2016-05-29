package com.hea3ven.malleable.item

import com.hea3ven.malleable.ModMetals
import com.hea3ven.malleable.metal.Metal
import net.minecraft.entity.Entity
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack

class ItemMetalArmor(override val metal: Metal, slot: EntityEquipmentSlot)
: ItemArmor(metal.armorMaterial, -1, slot), ItemMetalSingle {
	override fun getArmorTexture(stack: ItemStack, entity: Entity, slot: EntityEquipmentSlot, type: String?)
			= when {
		type == "overlay" -> "malleable:textures/models/armor/base_armor_1_overlay.png"
		slot != EntityEquipmentSlot.LEGS -> "malleable:textures/models/armor/base_armor_1.png"
		else -> "malleable:textures/models/armor/base_armor_2.png"
	}

	override fun hasColor(stack: ItemStack?): Boolean {
		return true
	}

	override fun getColor(stack: ItemStack?): Int {
		return metal.color
	}

	override fun getIsRepairable(toRepair: ItemStack, repair: ItemStack): Boolean {
		if (repair.item != ModMetals.proxy.ingot)
			return false
		val armor = toRepair.item as ItemMetalArmor
		return armor.metal == ModMetals.proxy.ingot.getMetal(repair)
	}
}
