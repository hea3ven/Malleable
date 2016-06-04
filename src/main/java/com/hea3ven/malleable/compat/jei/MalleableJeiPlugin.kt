package com.hea3ven.malleable.compat.jei

import com.hea3ven.malleable.client.gui.GuiMetalFurnace
import com.hea3ven.malleable.item.crafting.MetalFurnaceRecipe
import com.hea3ven.malleable.item.crafting.MetalFurnaceRecipes
import mezz.jei.api.*
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

@JEIPlugin
class MalleableJeiPlugin : IModPlugin {
	override fun register(registry: IModRegistry) {
		registry.addRecipeCategories(MetalFurnaceRecipeCategory(registry.jeiHelpers.guiHelper));
		registry.addRecipeHandlers(MetalFurnaceRecipeHandler());
		registry.addRecipes(MetalFurnaceRecipes.getAll().toMutableList());
		registry.addRecipeClickArea(GuiMetalFurnace::class.java, 78, 32, 28, 21,
				MetalFurnaceRecipeHandler.uid)
	}

	override fun onRuntimeAvailable(jeiRuntime: IJeiRuntime) {
	}
}

class MetalFurnaceRecipeCategory(guiHelper: IGuiHelper) : IRecipeCategory {

	private val background = guiHelper.createDrawable(GuiMetalFurnace.BG_RESOURCE, 37, 16, 100, 54);

	override fun getUid() = MetalFurnaceRecipeHandler.uid

	override fun getTitle() = I18n.format("gui.malleable.jei.metalFurnace");

	override fun drawAnimations(minecraft: Minecraft) {
	}

	override fun setRecipe(recipeLayout: IRecipeLayout, recipe: IRecipeWrapper) {
		if (recipe !is MetalFurnaceRecipeWrapper)
			return

		var slot = 0
		for (input in recipe.inputs) {
			recipeLayout.itemStacks.init(slot, true, 0 + 18 * slot, 0);
			recipeLayout.itemStacks.setFromRecipe(slot, input);
			slot++
		}
		recipeLayout.itemStacks.init(2, false, 9, 36);
		recipeLayout.itemStacks.setFromRecipe(2, listOf(ItemStack(Items.COAL), ItemStack(Blocks.COAL_BLOCK)));
		recipeLayout.itemStacks.init(3, false, 78, 18);
		recipeLayout.itemStacks.setFromRecipe(3, recipe.outputs[0]);
	}

	override fun getBackground() = background

	override fun drawExtras(minecraft: Minecraft) {
	}

}

class MetalFurnaceRecipeHandler : IRecipeHandler<MetalFurnaceRecipe> {
	override fun getRecipeWrapper(recipe: MetalFurnaceRecipe) = MetalFurnaceRecipeWrapper(recipe)

	override fun getRecipeClass() = MetalFurnaceRecipe::class.java

	override fun isRecipeValid(recipe: MetalFurnaceRecipe) = recipe is MetalFurnaceRecipe

	override fun getRecipeCategoryUid() = uid

	companion object {
		val uid = "malleable.metalfurnace"
	}
}

class MetalFurnaceRecipeWrapper(val recipe: MetalFurnaceRecipe) : IRecipeWrapper {
	override fun drawAnimations(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int) {
	}

	override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int,
			mouseY: Int) {
	}

	override fun getTooltipStrings(mouseX: Int, mouseY: Int) = null

	override fun getFluidInputs() = null

	override fun handleClick(minecraft: Minecraft, mouseX: Int, mouseY: Int, mouseButton: Int) = false

	override fun getOutputs() = listOf<ItemStack>(recipe.output)

	override fun getFluidOutputs() = null

	override fun getInputs() = listOf(recipe.input1, recipe.input2).filter { it != null }.map { it!! }
}

