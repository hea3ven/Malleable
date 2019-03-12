package com.hea3ven.malleable.item

import com.hea3ven.malleable.ModMalleable
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient

enum class MalleableToolMaterials(private val repairIngredientName:String, private val blockBreakingSpeed: Float, private val durability: Int, private val attackDamage: Float, private val enchantability: Int, private val miningLevel: Int) : ToolMaterial {
    BRONZE("bronze_ingot", 2.0F, 1024, 6.0F, 22, 1),
    STEEL("steel_ingot", 7.0F, 3072, 2.5F, 14,3 ),
    COBALT("cobalt_ingot", 9.0F, 2048, 4.0F, 16, 3),
    FERCOSTEEL("fercosteel_ingot", 10.0F, 4096, 4.5F, 12, 3),
    TUNGSTEN("tungsten_ingot", 7.0F, 5120, 2.5F, 20, 3),
    MUSHETSTEEL("mushetsteel_ingot", 8.0F, 8192, 3.0F, 12, 3),
    ;


    override fun getRepairIngredient() = Ingredient.ofItems(ModMalleable.items[repairIngredientName]!!.item)!!

    override fun getBlockBreakingSpeed() = blockBreakingSpeed

    override fun getDurability() = durability

    override fun getAttackDamage() = attackDamage

    override fun getEnchantability() = enchantability

    override fun getMiningLevel() = miningLevel
}
