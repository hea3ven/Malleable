package com.hea3ven.malleable.util

import com.hea3ven.malleable.metal.Metal
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.*
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.fml.relauncher.ReflectionHelper
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.ShapedOreRecipe
import java.util.*

fun getMetalRecipes(): List<IRecipe> {
	var recipes = mutableListOf<IRecipe>()
	val recipeList = CraftingManager.getInstance().recipeList!!
	for (recipe in ArrayList(recipeList)) {
		val output = recipe.recipeOutput
		if (output == null)
			continue
		if (recipe !is ShapedOreRecipe)
			continue

		if (output.item is ItemTool || output.item is ItemArmor || output.item is ItemHoe
				|| output.item is ItemSword)
			continue

		val found = recipe.input.any {
			(it is ItemStack && it.item == Items.IRON_INGOT)
					|| (it is List<*> && it.any { (it as ItemStack).item == Items.IRON_INGOT })
		}
		if (!found)
			continue
		if (output.item == Items.IRON_INGOT || output.item == Item.getItemFromBlock(Blocks.IRON_BLOCK))
			continue

		recipes.add(convertRecipe(recipe, Items.IRON_INGOT, Blocks.IRON_BLOCK, Metal.COPPER))
		recipes.add(convertRecipe(recipe, Items.IRON_INGOT, Blocks.IRON_BLOCK, Metal.TIN))
		recipes.add(convertRecipe(recipe, Items.IRON_INGOT, Blocks.IRON_BLOCK, Metal.COBALT))
		recipes.add(convertRecipe(recipe, Items.IRON_INGOT, Blocks.IRON_BLOCK, Metal.TUNGSTEN))
	}
	return recipes
}

private fun convertRecipe(recipe: ShapedOreRecipe, ingot: Item, block: Block, metal: Metal): IRecipe {
	return convertRecipe(recipe,
			mapOf(Pair("ingotIron", metal.ingotName), Pair("blockIron", metal.blockName)),
			mapOf(Pair(ingot, metal.ingotName), Pair(Item.getItemFromBlock(block)!!, metal.blockName)))
}

private fun convertRecipe(recipe: ShapedOreRecipe, oreTranslations: Map<String, String>,
		itemTranslations: Map<Item, String>): IRecipe {
	val width: Int = ReflectionHelper.getPrivateValue(ShapedOreRecipe::class.java, recipe, "width")
	val height: Int = ReflectionHelper.getPrivateValue(ShapedOreRecipe::class.java, recipe, "height")
	val recipeShape = Array(height, { "" })
	val ingredientsMapping: MutableMap<Any, Char> = mutableMapOf()
	var c = 'a'
	var row = 0
	var col = 0
	for (input in recipe.input) {
		var resultInput = input
		if (input is List<*>)
			resultInput = getOreDictName(input)

		if (resultInput is ItemStack && itemTranslations.containsKey(resultInput.item))
			resultInput = itemTranslations[resultInput.item]
		else if (resultInput is String && oreTranslations.containsKey(resultInput))
			resultInput = oreTranslations[resultInput]

		var key = ingredientsMapping[resultInput]
		if (key == null) {
			if (resultInput != null) {
				key = c
				ingredientsMapping.put(resultInput, c++)
			} else {
				key = ' '
			}
		}
		recipeShape[row] += key.toString()
		if (++col >= width) {
			row++
			col = 0
		}
	}
	val input = recipeShape.map { it }.plus(ingredientsMapping.flatMap { listOf(it.value, it.key) })
	return ShapedOreRecipe(recipe.recipeOutput, *Array(input.size, { input[it] }))
}

private fun getOreDictName(input: List<*>): String {
	for (name in OreDictionary.getOreNames()) {
		if (OreDictionary.getOres(name) == input)
			return name
	}
	throw RuntimeException("Could not find ore name")
}

