package com.hea3ven.metals.item.crafting

import com.hea3ven.metals.ModMetals
import com.hea3ven.metals.metal.Metal
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

object MetalFurnaceRecipes {

	private val recipes: MutableMap<Pair<ItemStack, ItemStack?>, MetalFurnaceRecipe> = hashMapOf()

	fun addRecipe(output: ItemStack, input1: ItemStack, input2: ItemStack? = null) {
		recipes.put(input1 to input2, MetalFurnaceRecipe(input1, input2, output))
	}

	fun addMetalRecipe(metal: Metal) {
		if (metal.oreName != null) {
			for (input in getStacks(metal.oreName, 1))
				MetalFurnaceRecipes.addRecipe(metal.getNuggetStack().apply { stackSize = 3 }, input)
		}
		for (input in getStacks(metal.ingotName, 9))
			MetalFurnaceRecipes.addRecipe(metal.getBlockStack(), input)
		for (input in getStacks(metal.nuggetName, 9))
			MetalFurnaceRecipes.addRecipe(metal.getIngotStack(), input)
	}

	fun addAlloyRecipe(inputMetal1: Metal, sizeInput1: Int, inputMetal2: Metal, sizeInput2: Int,
			outputMetal: Metal, outputSize: Int) {
		if (inputMetal2.oreName != null) {
			for (input2 in getStacks(inputMetal2.oreName, sizeInput2)) {
				addAlloyRecipe(outputMetal, outputSize * 3, Metal.ItemType.NUGGET, inputMetal1, sizeInput1,
						Metal.ItemType.ORE, input2)
			}
		}
		for (input2 in getStacks(inputMetal2.blockName, sizeInput2)) {
			addAlloyRecipe(outputMetal, outputSize, Metal.ItemType.BLOCK, inputMetal1, sizeInput1,
					Metal.ItemType.BLOCK, input2)
		}
		for (input2 in getStacks(inputMetal2.ingotName, sizeInput2)) {
			addAlloyRecipe(outputMetal, outputSize, Metal.ItemType.INGOT, inputMetal1, sizeInput1,
					Metal.ItemType.INGOT, input2)
		}
		for (input2 in getStacks(inputMetal2.nuggetName, sizeInput2)) {
			addAlloyRecipe(outputMetal, outputSize, Metal.ItemType.NUGGET, inputMetal1, sizeInput1,
					Metal.ItemType.NUGGET, input2)
		}
		addMetalRecipe(outputMetal)
	}

	fun addAlloyRecipe(outputMetal: Metal, outputSize: Int, outputType: Metal.ItemType, inputMetal1: Metal,
			sizeInput1: Int, inputType1: Metal.ItemType, input2: ItemStack) {
		val name = inputMetal1.getOreDictName(inputType1)
		if (name == null)
			return
		for (input1 in getStacks(name, sizeInput1)) {
			addRecipe(outputMetal.getStack(outputType).apply { stackSize = outputSize }, input1, input2);
		}
	}

	private fun getStacks(name: String, size: Int)
			= OreDictionary.getOres(name).map { it.copy().apply { stackSize = size } }

	fun getRecipe(inputStack1: ItemStack?, inputStack2: ItemStack?, matchSize: Boolean): MetalFurnaceRecipe? {
		var stack1 = inputStack1
		var stack2 = inputStack2
		if (stack1 == null && stack2 == null)
			return null;
		if (stack1 == null) {
			stack1 = stack2;
			stack2 = null;
		}

		for (it in recipes) {
			var inputs = it.key
			var input2 = inputs.second
			var input1 = inputs.first
			if ((!ItemStack.areItemsEqual(input1, stack1) ||
					!ItemStack.areItemsEqual(input2, stack2)) &&
					(!ItemStack.areItemsEqual(input1, stack2) ||
							!ItemStack.areItemsEqual(input2, stack1)))
				continue

			if (matchSize &&
					(ItemStack.areItemsEqual(input1, stack1) && input1.stackSize > stack1!!.stackSize) ||
					(ItemStack.areItemsEqual(input1,
							stack2) && input1.stackSize > stack2!!.stackSize))
				continue

			if (matchSize && input2 != null &&
					((ItemStack.areItemsEqual(input2, stack1) && input2.stackSize > stack1!!.stackSize) ||
							(ItemStack.areItemsEqual(input2, stack2) &&
									input2.stackSize > stack2!!.stackSize)))
				continue
			return it.value
		}
		return null
	}

	fun smelt(itemStack: ItemStack?, itemStack1: ItemStack?): ItemStack? {
		var recipe = getRecipe(itemStack, itemStack1, true)
		return recipe!!.smelt(itemStack, itemStack1)
	}
}

class MetalFurnaceRecipe(val input1: ItemStack, val input2: ItemStack?, val output: ItemStack) {

	fun smelt(stack1: ItemStack?, stack2: ItemStack?): ItemStack {
		val swap = ItemStack.areItemsEqual(input1, stack2)
		val s1 = if (!swap) stack1 else stack2
		val s2 = if (!swap) stack2 else stack1
		s1!!.stackSize -= input1.stackSize;
		if (input2 != null)
			s2!!.stackSize -= input2.stackSize;
		return output.copy();
	}
}
