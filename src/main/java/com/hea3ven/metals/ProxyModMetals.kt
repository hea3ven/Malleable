package com.hea3ven.metals

import com.hea3ven.metals.block.BlockMetalBlock
import com.hea3ven.metals.block.BlockMetalFurnace
import com.hea3ven.metals.block.BlockMetalOre
import com.hea3ven.metals.block.tileentity.TileMetalFurnace
import com.hea3ven.metals.client.gui.GuiMetalFurnace
import com.hea3ven.metals.item.*
import com.hea3ven.metals.item.crafting.MetalFurnaceRecipes
import com.hea3ven.metals.metal.Metal
import com.hea3ven.metals.world.WorldGeneratorOre
import com.hea3ven.tools.commonutils.inventory.ISimpleGuiHandler
import com.hea3ven.tools.commonutils.item.crafting.SimpleRecipeBuilder
import com.hea3ven.tools.commonutils.mod.ProxyModBase
import com.hea3ven.tools.commonutils.util.WorldHelper
import net.minecraft.block.Block
import net.minecraft.client.gui.Gui
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.ItemHandlerHelper
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
		for (metal in ItemMetal.INGOTS) {
			OreDictionary.registerOre(metal.ingotName, ingot.createStack(metal));
		}
		for (metal in ItemMetal.NUGGETS) {
			OreDictionary.registerOre(metal.nuggetName, nugget.createStack(metal));
		}

		GameRegistry.registerWorldGenerator(WorldGeneratorOre(ore), 1);

		super.onInitEvent(event)

	}

	private val ore: BlockMetalOre = BlockMetalOre().apply {
		setUnlocalizedName("ore")
		setHardness(3.0F)
		setResistance(5.0F)
		setStepSound(Block.soundTypePiston)
		setCreativeTab(CreativeTabs.tabBlock)
	}

	val block: BlockMetalBlock = BlockMetalBlock().apply {
		setUnlocalizedName("blockMetal")
		setHardness(5.0F)
		setResistance(10.0F)
		setStepSound(Block.soundTypePiston)
		setCreativeTab(CreativeTabs.tabBlock)
	}
	val metalFurnace: BlockMetalFurnace = BlockMetalFurnace().apply {
		setUnlocalizedName("metalFurnace")
		setHardness(3.5F)
		setStepSound(Block.soundTypeMetal);
		setCreativeTab(CreativeTabs.tabBlock)
	}

	val nugget = ItemMetal(ItemMetal.NUGGETS).apply {
		setUnlocalizedName("nugget")
		setCreativeTab(CreativeTabs.tabMaterials)
	}

	val ingot = ItemMetal(ItemMetal.INGOTS).apply {
		setUnlocalizedName("ingot")
		setCreativeTab(CreativeTabs.tabMaterials)
	}

	private val toolsAndArmorRecipes: MutableList<SimpleRecipeBuilder> = arrayListOf()

	override fun registerBlocks() {
		addBlock(ore, "ore", ItemBlockMetal::class.java)
		addBlock(block, "block_metal", ItemBlockMetal::class.java)
		addBlock(metalFurnace, "metal_furnace", ItemBlockMetal::class.java);
	}

	override fun registerTileEntities() {
		addTileEntity(TileMetalFurnace::class.java, "metal_furnace");
	}

	override fun registerItems() {
		addItem(nugget, "nugget")
		addItem(ingot, "ingot")
		for (metal in arrayOf(Metal.BRONZE, Metal.STEEL, Metal.COBALT, Metal.FERCO_STEEL, Metal.TUNGSTEN,
				Metal.MUSHET_STEEL)) {
			addItem(ItemMetalPickaxe(metal).apply {
				setUnlocalizedName("pickaxe" + metal.name)
				setCreativeTab(CreativeTabs.tabTools)
			}, metal.name + "_pickaxe")
			toolsAndArmorRecipes.add(
					SimpleRecipeBuilder()
							.output("metals:" + metal.name + "_pickaxe")
							.ingredients(
									"xxx",
									" y ",
									" y ",
									"x", "metals:ingot@" + ingot.createStack(metal).metadata,
									"y", "stickWood"))
			addItem(ItemMetalShovel(metal).apply {
				setUnlocalizedName("shovel" + metal.name)
				setCreativeTab(CreativeTabs.tabTools)
			}, metal.name + "_shovel")
			toolsAndArmorRecipes.add(
					SimpleRecipeBuilder()
							.output("metals:" + metal.name + "_shovel")
							.ingredients(
									"x",
									"y",
									"y",
									"x", "metals:ingot@" + ingot.createStack(metal).metadata,
									"y", "stickWood"))
			addItem(ItemMetalAxe(metal).apply {
				setUnlocalizedName("axe" + metal.name)
				setCreativeTab(CreativeTabs.tabTools)
			}, metal.name + "_axe")
			toolsAndArmorRecipes.add(
					SimpleRecipeBuilder()
							.output("metals:" + metal.name + "_axe")
							.ingredients(
									"xx ",
									"xy ",
									" y ",
									"x", "metals:ingot@" + ingot.createStack(metal).metadata,
									"y", "stickWood"))
			addItem(ItemMetalHoe(metal).apply {
				setUnlocalizedName("hoe" + metal.name)
				setCreativeTab(CreativeTabs.tabTools)
			}, metal.name + "_hoe")
			toolsAndArmorRecipes.add(
					SimpleRecipeBuilder()
							.output("metals:" + metal.name + "_hoe")
							.ingredients(
									"xx ",
									" y ",
									" y ",
									"x", "metals:ingot@" + ingot.createStack(metal).metadata,
									"y", "stickWood"))
			addItem(ItemMetalSword(metal).apply {
				setUnlocalizedName("sword" + metal.name)
				setCreativeTab(CreativeTabs.tabCombat)
			}, metal.name + "_sword")
			toolsAndArmorRecipes.add(
					SimpleRecipeBuilder()
							.output("metals:" + metal.name + "_sword")
							.ingredients(
									"x",
									"x",
									"y",
									"x", "metals:ingot@" + ingot.createStack(metal).metadata,
									"y", "stickWood"))
			addItem(ItemMetalArmor(metal, 0).apply {
				setUnlocalizedName("helmet" + metal.name)
				setCreativeTab(CreativeTabs.tabCombat)
			}, metal.name + "_helmet")
			toolsAndArmorRecipes.add(
					SimpleRecipeBuilder()
							.output("metals:" + metal.name + "_helmet")
							.ingredients(
									"xxx",
									"x x",
									"x", "metals:ingot@" + ingot.createStack(metal).metadata))
			addItem(ItemMetalArmor(metal, 1).apply {
				setUnlocalizedName("chestplate" + metal.name)
				setCreativeTab(CreativeTabs.tabCombat)
			}, metal.name + "_chestplate")
			toolsAndArmorRecipes.add(
					SimpleRecipeBuilder()
							.output("metals:" + metal.name + "_chestplate")
							.ingredients(
									"x x",
									"xxx",
									"xxx",
									"x", "metals:ingot@" + ingot.createStack(metal).metadata))
			addItem(ItemMetalArmor(metal, 2).apply {
				setUnlocalizedName("leggings" + metal.name)
				setCreativeTab(CreativeTabs.tabCombat)
			}, metal.name + "_leggings")
			toolsAndArmorRecipes.add(
					SimpleRecipeBuilder()
							.output("metals:" + metal.name + "_leggings")
							.ingredients(
									"xxx",
									"x x",
									"x x",
									"x", "metals:ingot@" + ingot.createStack(metal).metadata))
			addItem(ItemMetalArmor(metal, 3).apply {
				setUnlocalizedName("boots" + metal.name)
				setCreativeTab(CreativeTabs.tabCombat)
			}, metal.name + "_boots")
			toolsAndArmorRecipes.add(
					SimpleRecipeBuilder()
							.output("metals:" + metal.name + "_boots")
							.ingredients(
									"x x",
									"x x",
									"x", "metals:ingot@" + ingot.createStack(metal).metadata))
		}
	}

	override fun registerGuis() {
		addGui(GuiMetalFurnace.id, object : ISimpleGuiHandler {
			override fun createContainer(player: EntityPlayer, world: World, pos: BlockPos): Container {
				return WorldHelper.getTile<TileMetalFurnace>(world, pos).getContainer(player.inventory);
			}

			@SideOnly(Side.CLIENT)
			override fun createGui(player: EntityPlayer, world: World, pos: BlockPos): Gui {
				return GuiMetalFurnace(player.inventory, WorldHelper.getTile(world, pos));
			}
		});
	}

	override fun registerRecipes() {
		removeIngotSmeltingRecipes()
		FurnaceRecipes.instance().addSmeltingRecipe(nugget.createStack(Metal.COPPER, 3),
				ingot.createStack(Metal.COPPER), 0.7F)
		for (recipe in toolsAndArmorRecipes)
			addRecipe(recipe.build())
		addMetalSmeltingRecipes()
		addMetalRecipes()
		for (metal in BlockMetalFurnace.METALS) {
			addRecipe(metalFurnace.createStack(Metal.BRONZE), "xxx", "x x", "xxx", 'x', "blockBronze");
		}
		addRecipe(metalFurnace.createStack(Metal.STEEL), "xxx", "x x", "xxx", 'x', "blockSteel");
		addRecipe(metalFurnace.createStack(Metal.COBALT), "xxx", "x x", "xxx", 'x', "blockCobalt");
	}

	private fun removeIngotSmeltingRecipes() {
		val recipes = FurnaceRecipes.instance().getSmeltingList()!!;
		for (entry in HashMap(recipes)) {
			if (entry.value.item == Items.iron_ingot) {
				recipes.remove(entry.key)
			} else if (entry.value.item == Items.gold_ingot) {
				recipes.remove(entry.key)
			}
		}
	}

	private fun addMetalSmeltingRecipes() {
		MetalFurnaceRecipes.addMetalRecipe(0, Metal.GOLD);
		MetalFurnaceRecipes.addMetalRecipe(0, Metal.TIN);
		MetalFurnaceRecipes.addMetalRecipe(0, Metal.COPPER);
		MetalFurnaceRecipes.addAlloyRecipe(0, Metal.COPPER, 3, Metal.TIN, 1, Metal.BRONZE, 2);
		MetalFurnaceRecipes.addMetalRecipe(1, Metal.IRON);
		for (stack in OreDictionary.getOres("oreIron")) {
			MetalFurnaceRecipes.addRecipe(1, stack, ItemStack(Items.coal), nugget.createStack(Metal.IRON));
		}
		for (stack in OreDictionary.getOres("blockIron")) {
			MetalFurnaceRecipes.addRecipe(1, stack, ItemStack(Blocks.coal_block),
					block.createStack(Metal.STEEL));
			MetalFurnaceRecipes.addRecipe(1, stack, ItemStack(Items.coal, 9), block.createStack(Metal.STEEL));
		}
		for (stack in OreDictionary.getOres("ingotIron")) {
			MetalFurnaceRecipes.addRecipe(1, stack, ItemStack(Items.coal), ingot.createStack(Metal.STEEL));
		}
		for (stack in OreDictionary.getOres("nuggetIron")) {
			MetalFurnaceRecipes.addRecipe(1, ItemHandlerHelper.copyStackWithSize(stack, 9),
					ItemStack(Items.coal), ingot.createStack(Metal.STEEL));
		}
		MetalFurnaceRecipes.addMetalRecipe(2, Metal.COBALT);
		MetalFurnaceRecipes.addMetalRecipe(2, Metal.TUNGSTEN);
		MetalFurnaceRecipes.addAlloyRecipe(3, Metal.STEEL, 3, Metal.COBALT, 1, Metal.FERCO_STEEL, 2);
		MetalFurnaceRecipes.addAlloyRecipe(3, Metal.STEEL, 2, Metal.TUNGSTEN, 1, Metal.MUSHET_STEEL, 1);
	}

	private fun addMetalRecipes() {
		for (metal in BlockMetalBlock.BLOCKS) {
			addRecipe(block.createStack(metal), "xxx", "xxx", "xxx", 'x', metal.ingotName);
		}
		for (metal in ItemMetal.INGOTS) {
			addRecipe(ingot.createStack(metal), "xxx", "xxx", "xxx", 'x', metal.nuggetName);
			addRecipe(true, ingot.createStack(metal, 9), metal.blockName);
		}
		addRecipe(Items.iron_ingot, "xxx", "xxx", "xxx", 'x', Metal.IRON.nuggetName);
		for (metal in ItemMetal.NUGGETS) {
			addRecipe(true, nugget.createStack(metal, 9), metal.ingotName);
		}

		addMetalRecipe(ItemStack(Blocks.rail, 16), "X X", "X#X", "X X", 'X', null, '#', "stickWood");
		addMetalRecipe(ItemStack(Blocks.activator_rail, 6), "XSX", "X#X", "XSX", 'X', null, '#',
				Blocks.redstone_torch, 'S', "stickWood");
		addMetalRecipe(ItemStack(Blocks.detector_rail, 6), "X X", "X#X", "XRX", 'X', null, 'R',
				"dustRedstone", '#', Blocks.stone_pressure_plate);
		addMetalRecipe(ItemStack(Items.minecart, 1), "# #", "###", '#', null);
		addMetalRecipe(ItemStack(Items.cauldron, 1), "# #", "# #", "###", '#', null);
		addMetalRecipe(ItemStack(Items.bucket, 1), "# #", " # ", '#', null);
		addMetalRecipe(ItemStack(Blocks.tripwire_hook, 2), "I", "S", "#", '#', "plankWood", 'S', "stickWood",
				'I', null);
		addMetalRecipe(ItemStack(Items.compass, 1), " # ", "#X#", " # ", '#', null, 'X', "dustRedstone");
		addMetalRecipe(ItemStack(Blocks.piston, 1), "TTT", "#X#", "#R#", '#', "cobblestone", 'X', null, 'R',
				"dustRedstone", 'T', "planksWood");
		addMetalRecipe(ItemStack(Blocks.hopper), "I I", "ICI", " I ", 'I', null, 'C', "chest");
	}

	private fun addMetalRecipe(itemStack: ItemStack, vararg recipe: Any?) {
		for (recipeMetal in arrayOf(Metal.BRONZE.ingotName, Metal.COBALT.nuggetName)) {
			val newRecipe = arrayOfNulls<Any>(recipe.size);
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
