package com.hea3ven.malleable.item

import com.hea3ven.malleable.ModMalleable
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

enum class MalleableArmorMaterials(private val repairIngredientName: String, private val durability: Int,
        private val protectionAmounts: IntArray, private val enchantability: Int, private val equipSound: SoundEvent,
        private val toughness: Float) : ArmorMaterial {
    BRONZE("bronze_ingot", 15, intArrayOf(1, 5, 3, 2), 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0f), STEEL(
            "steel_ingot", 28, intArrayOf(2, 6, 5, 2), 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0f),
    COBALT("cobalt_ingot", 20, intArrayOf(2, 7, 5, 3), 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0f), FERCOSTEEL(
            "fercosteel_ingot", 38, intArrayOf(2, 7, 5, 3), 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0f),
    TUNGSTEN("tungsten_ingot", 40, intArrayOf(3, 8, 6, 3), 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0f), MUSHETSTEEL(
            "mushetsteel_ingot", 56, intArrayOf(3, 8, 6, 3), 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0f);


    override fun getDurability(slot: EquipmentSlot) = durability
    override fun getProtectionAmount(slot: EquipmentSlot) = protectionAmounts[slot.entitySlotId]
    override fun getEnchantability() = enchantability
    override fun getEquipSound() = equipSound
    override fun getRepairIngredient() = Ingredient.ofItems(ModMalleable.items[repairIngredientName]!!.item)!!
    @Environment(EnvType.CLIENT)
    override fun getName() = name.toLowerCase()

    override fun getToughness() = toughness
}
