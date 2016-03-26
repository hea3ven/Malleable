package com.hea3ven.metals.item.crafting

import com.hea3ven.metals.ModMetals
import com.hea3ven.metals.metal.Metal
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

object MetalFurnaceRecipes {

	private val recipes: MutableMap<Pair<ItemStack, ItemStack?>, MetalFurnaceRecipe> = hashMapOf()

	fun addRecipe(output: ItemStack, oreInput1: String, sizeInput1: Int, oreInput2: String? = null, sizeInput2: Int = 0) {
		for (it in OreDictionary.getOres(oreInput1)) {
			var input1 = it.copy()
			input1.stackSize = sizeInput1
			if (oreInput2 != null ) {
				OreDictionary.getOres(oreInput2).forEach {
					var input2 = it.copy();
					input2.stackSize = sizeInput2;
					addRecipe(input1, input2, output);
				}
			} else {

			}
		}
	}

	fun addRecipe(output: ItemStack, input1: ItemStack, input2: ItemStack? = null) {
		recipes.put(input1 to input2, MetalFurnaceRecipe(input1, input2, output));
	}

	fun addMetalRecipe(metal: Metal) {
		for (it in OreDictionary.getOres(metal.oreName)) {
			var input1: ItemStack = it.copy()!!;
			if (metal == Metal.GOLD)
				MetalFurnaceRecipes.addRecipe(ItemStack(Items.gold_nugget), input1);
			else
				MetalFurnaceRecipes.addRecipe(ModMetals.proxy.nugget.createStack(metal, 3), input1);
		}
		for (it in OreDictionary.getOres(metal.ingotName)) {
			var input1 = it.copy();
			input1.stackSize = 9;
			if (metal == Metal.IRON)
				MetalFurnaceRecipes.addRecipe(ItemStack(Blocks.iron_block), input1);
			else if (metal == Metal.GOLD)
				MetalFurnaceRecipes.addRecipe(ItemStack(Blocks.gold_block), input1);
			else
				MetalFurnaceRecipes.addRecipe(ModMetals.proxy.block.createStack(metal), input1);
		}
		for (it in OreDictionary.getOres(metal.nuggetName)) {
			var input1 = it.copy();
			input1.stackSize = 9;
			if (metal == Metal.IRON)
				MetalFurnaceRecipes.addRecipe(ItemStack(Items.iron_ingot), input1);
			else if (metal == Metal.GOLD)
				MetalFurnaceRecipes.addRecipe(ItemStack(Items.gold_ingot), input1);
			else
				MetalFurnaceRecipes.addRecipe(ModMetals.proxy.ingot.createStack(metal), input1);
		}
	}

	fun addAlloyRecipe(inputMetal1: Metal, sizeInput1: Int, inputMetal2: Metal, sizeInput2: Int, outputMetal: Metal, sizeOutput: Int) {
		for (oreStack1 in OreDictionary.getOres(inputMetal1.oreName)) {
			var input1 = oreStack1.copy();
			input1.stackSize = sizeInput1;
			for (oreStack2 in OreDictionary.getOres(inputMetal2.oreName)) {
				var input2 = oreStack2.copy();
				input2.stackSize = sizeInput2;
				MetalFurnaceRecipes.addRecipe(ModMetals.proxy.nugget.createStack(outputMetal, sizeOutput * 3),
						input1, input2);
			}
		}
		for (oreStack1 in OreDictionary.getOres(inputMetal1.blockName)) {
			var input1 = oreStack1.copy();
			input1.stackSize = sizeInput1;
			for (oreStack2 in OreDictionary.getOres(inputMetal2.blockName)) {
				var input2 = oreStack2.copy();
				input2.stackSize = sizeInput2;
				MetalFurnaceRecipes.addRecipe(ModMetals.proxy.block.createStack(outputMetal, sizeOutput),
						input1, input2);
			}
		}
		for (oreStack1 in OreDictionary.getOres(inputMetal1.ingotName)) {
			var input1 = oreStack1.copy();
			input1.stackSize = sizeInput1;
			for (oreStack2 in OreDictionary.getOres(inputMetal2.ingotName)) {
				var input2 = oreStack2.copy();
				input2.stackSize = sizeInput2;
				MetalFurnaceRecipes.addRecipe(ModMetals.proxy.ingot.createStack(outputMetal, sizeOutput),
						input1, input2);
			}
		}
		for (oreStack1 in OreDictionary.getOres(inputMetal1.nuggetName)) {
			var input1 = oreStack1.copy();
			input1.stackSize = sizeInput1;
			for (oreStack2 in OreDictionary.getOres(inputMetal2.nuggetName)) {
				var input2 = oreStack2.copy();
				input2.stackSize = sizeInput2;
				MetalFurnaceRecipes.addRecipe(ModMetals.proxy.nugget.createStack(outputMetal, sizeOutput),
						input1, input2);
			}
		}
		for (oreStack in OreDictionary.getOres(outputMetal.ingotName)) {
			var input1 = oreStack.copy();
			input1.stackSize = 9;
			MetalFurnaceRecipes.addRecipe(ModMetals.proxy.block.createStack(outputMetal), input1);
		}
		for (oreStack in OreDictionary.getOres(outputMetal.nuggetName)) {
			var input1 = oreStack.copy();
			input1.stackSize = 9;
			MetalFurnaceRecipes.addRecipe(ModMetals.proxy.ingot.createStack(outputMetal), input1);
		}
	}

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
		var s1 = stack1
		var s2 = stack2
		if (ItemStack.areItemsEqual(input1, stack2) && ItemStack.areItemStackTagsEqual(input1, stack2)) {
			s2 = stack1;
			s1 = stack2;
		}
		s1!!.stackSize -= input1.stackSize;
		if (input2 != null)
			s2!!.stackSize -= input2.stackSize;
		return output.copy();
	}
}
