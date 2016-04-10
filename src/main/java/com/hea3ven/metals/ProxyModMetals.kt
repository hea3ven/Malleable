package com.hea3ven.metals

import com.hea3ven.metals.block.BlockMetalBase
import com.hea3ven.metals.block.BlockMetalBlock
import com.hea3ven.metals.block.BlockMetalFurnace
import com.hea3ven.metals.block.BlockMetalOre
import com.hea3ven.metals.block.tileentity.TileMetalFurnace
import com.hea3ven.metals.client.gui.GuiMetalFurnace
import com.hea3ven.metals.item.*
import com.hea3ven.metals.item.crafting.MetalFurnaceRecipes
import com.hea3ven.metals.metal.Metal
import com.hea3ven.metals.util.getMetalRecipes
import com.hea3ven.metals.world.WorldGeneratorOre
import com.hea3ven.tools.commonutils.inventory.ISimpleGuiHandler
import com.hea3ven.tools.commonutils.item.crafting.SimpleRecipeBuilder
import com.hea3ven.tools.commonutils.mod.ProxyModBase
import com.hea3ven.tools.commonutils.util.WorldHelper
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.inventory.Container
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import java.util.*

class ProxyModMetals : ProxyModBase(ModMetals.MODID) {

	override fun onPreInitEvent(event: FMLPreInitializationEvent?) {
		super.onPreInitEvent(event)

		Metal.initVanillaMetals()
	}

	override fun onInitEvent(event: FMLInitializationEvent?) {
		for (it in BlockMetalOre.ORES) {
			OreDictionary.registerOre(it.oreName, ore.createStack(it))
		}
		for (it in BlockMetalBlock.BLOCKS) {
			OreDictionary.registerOre(it.blockName, block.createStack(it))
		}
		for (metal in ItemMetalBase.INGOTS) {
			OreDictionary.registerOre(metal.ingotName, ingot.createStack(metal))
		}
		for (metal in ItemMetalBase.NUGGETS) {
			OreDictionary.registerOre(metal.nuggetName, nugget.createStack(metal))
		}

		Metal.initToolRepairMaterials()

		GameRegistry.registerWorldGenerator(WorldGeneratorOre(ore), 1)

		super.onInitEvent(event)

	}

	val ore: BlockMetalOre = BlockMetalOre().apply {
		setUnlocalizedName("ore")
		setHardness(3.0F)
		setResistance(5.0F)
		setCreativeTab(CreativeTabs.tabBlock)
	}

	val block: BlockMetalBlock = BlockMetalBlock().apply {
		setUnlocalizedName("blockMetal")
		setHardness(5.0F)
		setResistance(10.0F)
		setCreativeTab(CreativeTabs.tabBlock)
	}
	val metalFurnace: BlockMetalFurnace = BlockMetalFurnace().apply {
		unlocalizedName = "metalFurnace"
		setHardness(3.5F)
		setCreativeTab(CreativeTabs.tabBlock)
	}

	val nugget = ItemMetalBase(ItemMetalBase.NUGGETS).apply {
		setUnlocalizedName("nugget")
		creativeTab = CreativeTabs.tabMaterials
	}

	val ingot = ItemMetalBase(ItemMetalBase.INGOTS).apply {
		setUnlocalizedName("ingot")
		creativeTab = CreativeTabs.tabMaterials
	}

	private val tools: MutableSet<Item> = mutableSetOf()

	private val armors: MutableSet<Item> = mutableSetOf()

	override fun registerBlocks() {
		addBlock(ore, "ore", ItemBlockMetal(ore))
		addBlock(block, "block_metal", ItemBlockMetal(block))
		addBlock(metalFurnace, "metal_furnace")
	}

	override fun registerTileEntities() {
		addTileEntity(TileMetalFurnace::class.java, "metal_furnace")
	}

	override fun registerItems() {
		addItem(nugget, "nugget")
		addItem(ingot, "ingot")
		for (metal in arrayOf(Metal.BRONZE, Metal.STEEL, Metal.COBALT, Metal.FERCO_STEEL, Metal.TUNGSTEN,
				Metal.MUSHET_STEEL)) {
			tools.add(addItem(ItemMetalPickaxe(metal).apply {
				setUnlocalizedName("pickaxe" + metal.name)
				setCreativeTab(CreativeTabs.tabTools)
			}, metal.name + "_pickaxe"))
			tools.add(addItem(ItemMetalShovel(metal).apply {
				setUnlocalizedName("shovel" + metal.name)
				setCreativeTab(CreativeTabs.tabTools)
			}, metal.name + "_shovel"))
			tools.add(addItem(ItemMetalSword(metal).apply {
				setUnlocalizedName("sword" + metal.name)
				setCreativeTab(CreativeTabs.tabCombat)
			}, metal.name + "_sword"))
			tools.add(addItem(ItemMetalAxe(metal).apply {
				setUnlocalizedName("axe" + metal.name)
				setCreativeTab(CreativeTabs.tabTools)
			}, metal.name + "_axe"))
			tools.add(addItem(ItemMetalHoe(metal).apply {
				setUnlocalizedName("hoe" + metal.name)
				setCreativeTab(CreativeTabs.tabTools)
			}, metal.name + "_hoe"))
			armors.add(addItem(ItemMetalArmor(metal, EntityEquipmentSlot.HEAD).apply {
				setUnlocalizedName("helmet" + metal.name)
				setCreativeTab(CreativeTabs.tabCombat)
			}, metal.name + "_helmet"))
			armors.add(addItem(ItemMetalArmor(metal, EntityEquipmentSlot.CHEST).apply {
				setUnlocalizedName("chestplate" + metal.name)
				setCreativeTab(CreativeTabs.tabCombat)
			}, metal.name + "_chestplate"))
			armors.add(addItem(ItemMetalArmor(metal, EntityEquipmentSlot.LEGS).apply {
				setUnlocalizedName("leggings" + metal.name)
				setCreativeTab(CreativeTabs.tabCombat)
			}, metal.name + "_leggings"))
			armors.add(addItem(ItemMetalArmor(metal, EntityEquipmentSlot.FEET).apply {
				setUnlocalizedName("boots" + metal.name)
				setCreativeTab(CreativeTabs.tabCombat)
			}, metal.name + "_boots"))
		}
	}

	@SideOnly(Side.CLIENT)
	override fun registerColors() {
		addColors(BlockMetalBase.colorHandler, ore, block)
		addItemColors(ItemMetalBase.colorHandler, nugget, ingot)
		addItemColors(object : IItemColor {
			override fun getColorFromItemstack(stack: ItemStack, tintIndex: Int)
					= if (tintIndex == 1) (stack.item as ItemMetal).getMetal(stack).color else -1
		}, tools)
		addItemColors(object : IItemColor {
			override fun getColorFromItemstack(stack: ItemStack, tintIndex: Int)
					= (stack.item as ItemMetal).getMetal(stack).color
		}, armors)
	}

	override fun registerGuis() {
		addGui(ModMetals.guiIdMetalFurnace, object : ISimpleGuiHandler {
			override fun createContainer(player: EntityPlayer, world: World, pos: BlockPos): Container {
				return WorldHelper.getTile<TileMetalFurnace>(world, pos).getContainer(player.inventory)
			}

			@SideOnly(Side.CLIENT)
			override fun createGui(player: EntityPlayer, world: World, pos: BlockPos): Gui {
				return GuiMetalFurnace(player.inventory, WorldHelper.getTile(world, pos))
			}
		})
	}

	override fun registerRecipes() {
		removeIngotSmeltingRecipes()
		removeDiamondItemsRecipes()
		FurnaceRecipes.instance().addSmeltingRecipe(nugget.createStack(Metal.COPPER, 3),
				ingot.createStack(Metal.COPPER), 0.7F)
		addMetalSmeltingRecipes()
		addMetalRecipes()
		addToolsAndArmorRecipes()
		addRecipe(metalFurnace, "xxx", "x x", "xxx", 'x', Blocks.brick_block)
	}

	private fun addToolsAndArmorRecipes() {
		for (metal in arrayOf(Metal.BRONZE, Metal.STEEL, Metal.COBALT, Metal.FERCO_STEEL, Metal.TUNGSTEN,
				Metal.MUSHET_STEEL)) {
			val pickaxe = Item.itemRegistry.getObject(ResourceLocation("metals:" + metal.name + "_pickaxe"))
			addRecipe(pickaxe, "xxx", " y ", " y ", 'x', metal.ingotName, 'y', "stickWood")
			val shovel = Item.itemRegistry.getObject(ResourceLocation("metals:" + metal.name + "_shovel"))
			addRecipe(shovel, "x", "y", "y", 'x', metal.ingotName, 'y', "stickWood")
			val axe = Item.itemRegistry.getObject(ResourceLocation("metals:" + metal.name + "_axe"))
			addRecipe(axe, "xx", "xy", " y", 'x', metal.ingotName, 'y', "stickWood")
			val hoe = Item.itemRegistry.getObject(ResourceLocation("metals:" + metal.name + "_hoe"))
			addRecipe(hoe, "xx", " y", " y", 'x', metal.ingotName, 'y', "stickWood")
			val sword = Item.itemRegistry.getObject(ResourceLocation("metals:" + metal.name + "_sword"))
			addRecipe(sword, "x", "x", "y", 'x', metal.ingotName, 'y', "stickWood")
			val helmet = Item.itemRegistry.getObject(ResourceLocation("metals:" + metal.name + "_helmet"))
			addRecipe(helmet, "xxx", "x x", 'x', metal.ingotName)
			val chestplate = Item.itemRegistry.getObject(
					ResourceLocation("metals:" + metal.name + "_chestplate"))
			addRecipe(chestplate, "x x", "xxx", "xxx", 'x', metal.ingotName)
			val leggings = Item.itemRegistry.getObject(ResourceLocation("metals:" + metal.name + "_leggings"))
			addRecipe(leggings, "xxx", "x x", "x x", 'x', metal.ingotName)
			val boots = Item.itemRegistry.getObject(ResourceLocation("metals:" + metal.name + "_boots"))
			addRecipe(boots, "x x", "x x", 'x', metal.ingotName)
		}
	}

	private fun removeIngotSmeltingRecipes() {
		val recipes = FurnaceRecipes.instance().getSmeltingList()!!
		for (entry in HashMap(recipes)) {
			if (entry.value.item == Items.iron_ingot) {
				recipes.remove(entry.key)
			} else if (entry.value.item == Items.gold_ingot) {
				recipes.remove(entry.key)
			}
		}
	}

	private fun removeDiamondItemsRecipes() {
		val diamondItems = arrayOf(Items.diamond_axe, Items.diamond_boots, Items.diamond_chestplate,
				Items.diamond_helmet, Items.diamond_hoe, Items.diamond_leggings, Items.diamond_pickaxe,
				Items.diamond_shovel, Items.diamond_sword)
		val recipeList = CraftingManager.getInstance().recipeList!!
		for (recipe in ArrayList(recipeList)) {
			if (recipe.recipeOutput != null && recipe.recipeOutput.item in diamondItems)
				recipeList.remove(recipe)
		}
		for (item in diamondItems) {
			item.creativeTab = null
		}
	}

	private fun addMetalSmeltingRecipes() {
		MetalFurnaceRecipes.addMetalRecipe(Metal.GOLD)
		MetalFurnaceRecipes.addMetalRecipe(Metal.TIN)
		MetalFurnaceRecipes.addMetalRecipe(Metal.COPPER)
		MetalFurnaceRecipes.addAlloyRecipe(Metal.COPPER, 3, Metal.TIN, 1, Metal.BRONZE, 2)
		MetalFurnaceRecipes.addMetalRecipe(Metal.IRON)
		MetalFurnaceRecipes.addAlloyRecipe(Metal.STEEL, 1, Metal.ItemType.INGOT, Metal.IRON, 1,
				Metal.ItemType.INGOT, ItemStack(Items.coal))
		MetalFurnaceRecipes.addAlloyRecipe(Metal.STEEL, 1, Metal.ItemType.INGOT, Metal.IRON, 9,
				Metal.ItemType.INGOT, ItemStack(Blocks.coal_block))
		MetalFurnaceRecipes.addAlloyRecipe(Metal.STEEL, 1, Metal.ItemType.BLOCK, Metal.IRON, 9,
				Metal.ItemType.BLOCK, ItemStack(Blocks.coal_block))
		MetalFurnaceRecipes.addMetalRecipe(Metal.COBALT)
		MetalFurnaceRecipes.addMetalRecipe(Metal.TUNGSTEN)
		MetalFurnaceRecipes.addAlloyRecipe(Metal.STEEL, 3, Metal.COBALT, 1, Metal.FERCO_STEEL, 2)
		MetalFurnaceRecipes.addAlloyRecipe(Metal.STEEL, 2, Metal.TUNGSTEN, 1, Metal.MUSHET_STEEL, 1)
	}

	private fun addMetalRecipes() {
		for (metal in BlockMetalBlock.BLOCKS) {
			addRecipe(block.createStack(metal), "xxx", "xxx", "xxx", 'x', metal.ingotName)
		}
		for (metal in ItemMetalBase.INGOTS) {
			addRecipe(ingot.createStack(metal), "xxx", "xxx", "xxx", 'x', metal.nuggetName)
			addRecipe(true, ingot.createStack(metal, 9), metal.blockName)
		}
		addRecipe(Items.iron_ingot, "xxx", "xxx", "xxx", 'x', Metal.IRON.nuggetName)
		for (metal in ItemMetalBase.NUGGETS) {
			addRecipe(true, nugget.createStack(metal, 9), metal.ingotName)
		}

		for (recipe in getMetalRecipes())
			addRecipe(recipe)
	}

	private fun addMetalRecipe(itemStack: ItemStack, vararg recipe: Any?) {
		for (recipeMetal in arrayOf(Metal.BRONZE.ingotName, Metal.COBALT.nuggetName)) {
			val newRecipe = arrayOfNulls<Any>(recipe.size)
			for (i in 0..recipe.size - 1) {
				if (recipe[i] == null)
					newRecipe[i] = recipeMetal
				else
					newRecipe[i] = recipe[i]
			}
			addRecipe(itemStack, *newRecipe)
		}
	}

}
