package com.hea3ven.malleable.item

import com.hea3ven.malleable.metal.Metal
import net.minecraft.item.*

class ItemMetalShovel(override val metal: Metal) : ItemSpade(metal.toolMaterial), ItemMetalSingle {
}
