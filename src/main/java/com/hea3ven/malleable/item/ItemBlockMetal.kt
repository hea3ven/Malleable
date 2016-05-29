package com.hea3ven.malleable.item

import com.hea3ven.malleable.block.BlockMetal
import net.minecraft.block.Block
import net.minecraft.item.ItemColored

class ItemBlockMetal(block: Block) : ItemColored(block, true) {
	init {
		setSubtypeNames((block as BlockMetal).metals.map { it.name }.toTypedArray())
	}

}

