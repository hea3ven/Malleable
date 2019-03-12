package com.hea3ven.malleable.metal

import com.hea3ven.malleable.item.MalleableArmorMaterials
import com.hea3ven.malleable.item.MalleableToolMaterials
import com.hea3ven.tools.commonutils.util.ReflectionUtil
import net.minecraft.item.*
import net.minecraft.util.registry.Registry
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class Metal(val metalName: String, val toolMaterial: ToolMaterial?, val armorMaterial: ArmorMaterial?, val color: Int,
        val genMinY: Int, val genMaxY: Int, val genCount: Int, val genSize: Int, val genDimension: Int?) :
        Comparable<Metal> {

    companion object {
        val IRON = Metal("iron", ToolMaterials.IRON, ArmorMaterials.IRON, (216 shl 16) + (216 shl 8) + 216, 0, 64, 20,
                         9, null)
        val COPPER = Metal("copper", null, null, (210 shl 16) + (80 shl 8) + 50, 32, 64, 15, 12, 0)
        val TIN = Metal("tin", null, null, (185 shl 16) + (210 shl 8) + 230, 45, 90, 24, 7, 0)
        val GOLD = Metal("gold", ToolMaterials.GOLD, ArmorMaterials.GOLD, 0, 0, 32, 2, 9, null)
        val BRONZE = Metal("bronze", MalleableToolMaterials.BRONZE, MalleableArmorMaterials.BRONZE,
                           (225 shl 16) + (170 shl 8) + 80, 0, 0, 0, 0, null)
        val STEEL = Metal("steel", MalleableToolMaterials.STEEL, MalleableArmorMaterials.STEEL,
                          (100 shl 16) + (100 shl 8) + 110, 0, 0, 0, 0, null)
        val COBALT = Metal("cobalt", MalleableToolMaterials.COBALT, MalleableArmorMaterials.COBALT,
                           (65 shl 16) + (90 shl 8) + 205, 16, 70, 10, 15, -1)
        val FERCOSTEEL = Metal("fercosteel", MalleableToolMaterials.FERCOSTEEL, MalleableArmorMaterials.FERCOSTEEL,
                               (50 shl 16) + (60 shl 8) + 105, 0, 0, 0, 0, null)
        val TUNGSTEN = Metal("tungsten", MalleableToolMaterials.TUNGSTEN, MalleableArmorMaterials.TUNGSTEN,
                             (60 shl 16) + (50 shl 8) + 50, 8, 64, 30, 5, 1)
        val MUSHETSTEEL = Metal("mushetsteel", MalleableToolMaterials.MUSHETSTEEL, MalleableArmorMaterials.MUSHETSTEEL,
                                (25 shl 16) + (35 shl 8) + 25, 0, 0, 0, 0, null)

        val values = arrayOf(IRON, COPPER, TIN, GOLD, BRONZE, STEEL, COBALT, FERCOSTEEL, TUNGSTEN, MUSHETSTEEL)

        fun initVanillaMetals() {
            ReflectionUtil.reflectField(ToolMaterials::class.java, "durability", "field_8924", { field ->
                field.set(ToolMaterials.IRON, 2048)
                field.set(ToolMaterials.GOLD, 512)
                field.set(ToolMaterials.DIAMOND, 1)
            })
            ReflectionUtil.reflectField(ArmorMaterials::class.java, "durabilityMultiplier", "field_7883", { field ->
                val modifiersField = Field::class.java.getDeclaredField("modifiers")
                modifiersField.isAccessible = true
                modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
                field.setInt(ArmorMaterials.IRON, 20)
                field.setInt(ArmorMaterials.GOLD, 10)
                field.setInt(ArmorMaterials.DIAMOND, 1)
                modifiersField.setInt(field, field.modifiers and Modifier.FINAL)
                modifiersField.isAccessible = false
            })
            Registry.ITEM.forEach { item ->
                if (item is ToolItem) {
                    ReflectionUtil.reflectField(Item::class.java, "durability", "field_8012", { field ->
                        field.set(item, item.type.durability)
                    })
                } else if (item is ArmorItem) {
                    ReflectionUtil.reflectField(Item::class.java, "durability", "field_8012", { field ->
                        field.set(item, item.material.getDurability(item.slotType))
                    })
                }
            }
            //            ReflectionHelper.setPrivateValue(ToolMaterials::class.java, ToolMaterials.IRON, 2048, 6);
            //            initVanillaTools(2048, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE,
            //                             Items.IRON_SWORD)
            // ArmorMaterial.IRON.maxDamageFactorIn = 20
            //            ReflectionHelper.setPrivateValue(ArmorMaterials::class.java, ArmorMaterials.IRON, 20, 6);
            //            initVanillaArmor(ArmorMaterials.IRON, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS,
            //                             Items.IRON_BOOTS)

            // ToolMaterial.GOLD.maxUses = 512
            //            ReflectionHelper.setPrivateValue(ToolMaterials::class.java, ToolMaterials.IRON, 512, 6);
            //            initVanillaTools(512, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE,
            //                             Items.IRON_SWORD)
            // ArmorMaterial.IRON.maxDamageFactorIn = 20
            //            ReflectionHelper.setPrivateValue(ArmorMaterials::class.java, ArmorMaterials.IRON, 10, 6);
            //            initVanillaArmor(ArmorMaterials.GOLD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
            //                             Items.GOLDEN_BOOTS)

            // ToolMaterial.DIAMOND.maxUses = 1
            //            ReflectionHelper.setPrivateValue(ToolMaterials::class.java, ToolMaterials.IRON, 1, 6);
            //            initVanillaTools(1, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SWORD)
            // ArmorMaterial.DIAMOND.maxDamageFactorIn = 1
            //            ReflectionHelper.setPrivateValue(ArmorMaterials::class.java, ArmorMaterials.DIAMOND, 1, 6);
            //            initVanillaArmor(ArmorMaterials.DIAMOND, Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE,
            //                             Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS)
        }

        //        private fun initVanillaTools(damage: Int, vararg items: Item) {
        //            items.forEach { it.maxDamage = damage }
        //        }

        //        private fun initVanillaArmor(mat: ArmorMaterials, headItem: ItemArmor, chestItem: ItemArmor,
        //                leggingItem: ItemArmor, bootsItem: ItemArmor) {
        //            headItem.setMaxDamage(mat.getDurability(EntityEquipmentSlot.HEAD));
        //            chestItem.setMaxDamage(mat.getDurability(EntityEquipmentSlot.CHEST));
        //            leggingItem.setMaxDamage(mat.getDurability(EntityEquipmentSlot.LEGS));
        //            bootsItem.setMaxDamage(mat.getDurability(EntityEquipmentSlot.FEET));
        //        }

        //        fun initToolRepairMaterials() {
        //            for (metal in values) {
        //                if (metal.toolMaterial == null || metal.toolMaterial.ordinal < 5) continue
        //                metal.toolMaterial.setRepairItem(ModMetals.proxy.ingot.createStack(metal))
        //            }
        //        }
    }

    /*
    fun getOreStack() = when (this) {
        IRON -> ItemStack(Blocks.IRON_ORE)
        GOLD -> ItemStack(Blocks.GOLD_ORE)
        else -> {
            if (oreName != null) ModMetals.proxy.ore.createStack(this)
            else throw IllegalArgumentException(name)
        }
    }

    fun getBlockStack() = when (this) {
        IRON -> ItemStack(Blocks.IRON_BLOCK)
        GOLD -> ItemStack(Blocks.GOLD_BLOCK)
        else -> {
            if (ModMetals.proxy.block.metals.contains(this)) ModMetals.proxy.block.createStack(this)
            else throw IllegalArgumentException(name)
        }
    }

    fun getIngotStack() = when (this) {
        IRON -> ItemStack(Items.IRON_INGOT)
        GOLD -> ItemStack(Items.GOLD_INGOT)
        else -> {
            if (ModMetals.proxy.ingot.metals.contains(this)) ModMetals.proxy.ingot.createStack(this)
            else throw IllegalArgumentException(name)
        }
    }

    fun getNuggetStack() = when (this) {
        GOLD -> ItemStack(Items.GOLD_NUGGET)
        else -> {
            if (ModMetals.proxy.nugget.metals.contains(this)) ModMetals.proxy.nugget.createStack(this)
            else throw IllegalArgumentException(name)
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
    */

    override fun compareTo(other: Metal): Int {
        return if (this == other) 0 else this.metalName.compareTo(other.metalName)
    }

    enum class ItemType {
        ORE, BLOCK, INGOT, NUGGET
    }
}
