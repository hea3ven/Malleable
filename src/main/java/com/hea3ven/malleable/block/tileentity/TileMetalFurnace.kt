package com.hea3ven.malleable.block.tileentity

import com.hea3ven.malleable.block.BlockMetalFurnace
import com.hea3ven.malleable.item.crafting.MetalFurnaceRecipes
import com.hea3ven.tools.commonutils.inventory.GenericContainer
import com.hea3ven.tools.commonutils.inventory.IUpdateHandler
import com.hea3ven.tools.commonutils.tileentity.TileMachine
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.inventory.Container
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.Constants
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper

class TileMetalFurnace : TileMachine(), ITickable, IItemHandler, IUpdateHandler {
	private val slots = arrayOfNulls<ItemStack>(4)

	var progress = 0
	var fuel = 0
	var fuelCapacity = 1

	override fun shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newSate: IBlockState)
			= oldState.block != newSate.block

	override fun update() {
		if (!world.isRemote) {
			val litOrig = isBurning()
			if (litOrig && canSmelt()) {
				progress++
				if (progress >= MAX_PROGRESS) {
					progress = 0
					smeltItems()
				}
			}

			if (isBurning())
				fuel--

			if (!isBurning() && canSmelt()) {
				burnFuel()
			}

			val lit = isBurning()
			if (litOrig != lit) {
				world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockMetalFurnace.LIT, lit), 3)
			}
		}
	}

	private fun canSmelt(): Boolean {
		if (slots[0] == null && slots[1] == null)
			return false

		val recipe = MetalFurnaceRecipes.getRecipe(slots[0], slots[1], true)
		if (recipe == null)
			return false
		if (slots[3] == null)
			return true
		if (!slots[3]!!.isItemEqual(recipe.output))
			return false
		val resultSize = slots[3]!!.stackSize + recipe.output.stackSize
		return resultSize <= 64 && resultSize <= slots[3]!!.getMaxStackSize()
		//		return resultSize <= getInventoryStackLimit() && resultSize <= slots[3]!!.getMaxStackSize()
	}

	private fun smeltItems() {
		val result = MetalFurnaceRecipes.smelt(slots[0], slots[1])
		if (slots[0] != null && slots[0]!!.stackSize <= 0)
			slots[0] = null
		if (slots[1] != null && slots[1]!!.stackSize <= 0)
			slots[1] = null

		if (slots[3] == null)
			slots[3] = result
		else {
			slots[3]!!.stackSize += result!!.stackSize
		}
	}

	private fun isBurning() = fuel > 0

	private fun burnFuel() {
		val stack = slots[2]
		if (stack != null) {
			if ( stack.item == Items.COAL && stack.metadata == 0) {
				stack.stackSize--
				if (stack.stackSize <= 0)
					slots[2] = null
				fuel = 800
				fuelCapacity = 800
			} else if ((stack.item as? ItemBlock)?.block == Blocks.COAL_BLOCK) {
				stack.stackSize--
				if (stack.stackSize <= 0)
					slots[2] = null
				fuel = 8000
				fuelCapacity = 8000
			}
		}
	}

	override fun writeToNBT(compound: NBTTagCompound) {
		super.writeToNBT(compound)

		compound.setInteger("Progress", progress)
		compound.setInteger("Fuel", fuel)
		compound.setInteger("FuelCapacity", fuelCapacity)

		compound.setTag("Slots", NBTTagList().apply {
			for (i in 0..slots.size - 1) {
				val slot = slots[i]
				if (slot != null) {
					this.appendTag(NBTTagCompound().apply {
						setByte("Slot", i.toByte())
						slot.writeToNBT(this)
					})
				}
			}
		})
	}

	override fun readFromNBT(compound: NBTTagCompound) {
		super.readFromNBT(compound)

		progress = compound.getInteger("Progress")
		fuel = compound.getInteger("Fuel")
		fuelCapacity = compound.getInteger("FuelCapacity")

		val slotsNbt = compound.getTagList("Slots", Constants.NBT.TAG_COMPOUND)
		for (i in 0..slotsNbt.tagCount() - 1) {
			val slotNbt = slotsNbt.getCompoundTagAt(i)
			val slot = slotNbt!!.getByte("Slot")
			if (0 <= slot && slot < slots.size) {
				slots[slot.toInt()] = ItemStack.loadItemStackFromNBT(slotNbt)
			}
		}
	}


	fun getContainer(playerInv: InventoryPlayer): Container {
		return GenericContainer()
				.addInputSlots(this, 0, 38, 17, 2, 1)
				.addInputSlots(this, 2, 47, 53, 1, 1)
				.addOutputSlots(this, 3, 116, 35, 1, 1)
				.setUpdateHandler(this)
				.addPlayerSlots(playerInv)
	}

	override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
				super.hasCapability(capability, facing)
	}

	@Suppress("UNCHECKED_CAST")
	override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return when (facing) {
				null -> this as T
				EnumFacing.DOWN -> object : IItemHandler {
					override fun getStackInSlot(slot: Int) = this@TileMetalFurnace.getStackInSlot(3)
					override fun insertItem(slot: Int, stack: ItemStack?, simulate: Boolean) = stack
					override fun getSlots() = 1
					override fun extractItem(slot: Int, amount: Int, simulate: Boolean) =
							this@TileMetalFurnace.extractItem(3, amount, simulate)
				} as T
				else -> this as T
			}
		}
		return super.getCapability(capability, facing)
	}

	override fun getStackInSlot(slot: Int): ItemStack? {
		return slots[slot]
	}

	override fun insertItem(slot: Int, stack: ItemStack?, simulate: Boolean): ItemStack? {
		if (stack == null || stack.stackSize == 0)
			return null

		if (!isItemValidForSlot(slot, stack))
			return stack

		val slotStack = slots[slot]
		if (slotStack == null) {
			if (!simulate)
				slots[slot] = stack
			return null
		} else {
			if (!ItemStack.areItemsEqual(slotStack, stack))
				return stack

			if (slotStack.stackSize + stack.stackSize < slotStack.maxStackSize) {
				if (!simulate)
					slotStack.stackSize += stack.stackSize
				return null
			} else {
				val result = stack.copy()
				result.stackSize -= slotStack.maxStackSize - slotStack.stackSize
				if (!simulate)
					slotStack.stackSize = slotStack.maxStackSize
				return result
			}
		}
	}

	override fun getSlots() = 4

	override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack? {
		val stack = slots[slot] ?: return null

		if (amount < stack.stackSize) {
			val result = ItemHandlerHelper.copyStackWithSize(stack, amount)
			if (!simulate)
				stack.stackSize -= amount
			return result
		} else {
			if (!simulate)
				slots[slot] = null
			return stack
		}
	}


	private fun isItemValidForSlot(slot: Int, stack: ItemStack): Boolean {
		if (slot < 2) {
			if (ItemStack.areItemsEqual(slots[slot], stack))
				return true
			if (slots[0] == null)
				return MetalFurnaceRecipes.getRecipe(stack, slots[1], false) != null
			else if (slots[1] == null)
				return MetalFurnaceRecipes.getRecipe(slots[0], stack, false) != null
			else
				return false
		} else if (slot < 3)
			return (stack.item == Items.COAL && stack.metadata == 0) ||
					((stack.item as? ItemBlock)?.block == Blocks.COAL_BLOCK)
		else
			return true
	}

	override fun getField(id: Int) = when (id) {
		FIELD_PROGRESS -> progress
		FIELD_FUEL -> fuel
		FIELD_FUELCAPACITY -> fuelCapacity
		else -> 0
	}

	override fun setField(id: Int, value: Int) {
		when (id) {
			FIELD_PROGRESS -> progress = value
			FIELD_FUEL -> fuel = value
			FIELD_FUELCAPACITY -> fuelCapacity = value
		}
	}

	override fun getFieldCount() = 3


	companion object {
		const val MAX_PROGRESS = 400

		const val FIELD_PROGRESS = 0
		const val FIELD_FUEL = 1
		const val FIELD_FUELCAPACITY = 2
	}
}

