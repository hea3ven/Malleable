package com.hea3ven.metals.item

import com.hea3ven.metals.metal.Metal
import net.minecraft.entity.Entity
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack

class ItemMetalArmor(val metal: Metal, slot: EntityEquipmentSlot) : ItemArmor(metal.armorMaterial, -1, slot) {

//	override fun getColorFromItemStack(stack: ItemStack, renderPass: Int): Int {
//		if (renderPass == 0)
//			return metal.color
//		else
//			return super.getColorFromItemStack(stack, renderPass)
//	}
//
//	override fun getColor(stack: ItemStack): Int {
//		return getColorFromItemStack(stack, 0)
//	}

//	override fun getArmorTexture(stack: ItemStack?, entity: Entity?, slot: Int, type: String?): String? {
//		if (type == "overlay")
//			return "metals:textures/models/armor/base_armor_1_overlay.png"
//		if (slot != 2)
//			return "metals:textures/models/armor/base_armor_1.png"
//		else
//			return "metals:textures/models/armor/base_armor_2.png"
//	}
}
