package com.hea3ven.malleable.client.gui

import com.hea3ven.malleable.block.tileentity.TileMetalFurnace
import com.sun.javafx.image.BytePixelSetter
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.ResourceLocation

class GuiMetalFurnace(playerInv: InventoryPlayer, val te: TileMetalFurnace) : GuiContainer(
		te.getContainer(playerInv)) {

	override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(BG_RESOURCE);

		// Background
		var k = (this.width - this.xSize) / 2;
		var l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		// Progress Bar
		var i1 = te.progress * 24 / 400;
		this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);

		// Burn Progress
		if (te.fuelCapacity != 0)
			i1 = te.fuel * 13 / te.fuelCapacity;
		else
			i1 = 0
		this.drawTexturedModalRect(k + 47, l + 36 + 13 - i1, 176, 13 - i1, 14, i1 + 1);
	}

	companion object {
		val BG_RESOURCE = ResourceLocation("malleable:textures/gui/container/metal_furnace.png");
	}

}
