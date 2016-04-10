package com.hea3ven.metals.item

import com.hea3ven.metals.block.BlockMetal
import net.minecraft.block.Block
import net.minecraft.item.ItemColored

class ItemBlockMetal(block: Block) : ItemColored(block, true) {
	init {
		setSubtypeNames((block as BlockMetal).metals.map { it.name }.toTypedArray())
	}

}

