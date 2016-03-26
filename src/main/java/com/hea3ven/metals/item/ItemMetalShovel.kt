package com.hea3ven.metals.item

import com.hea3ven.metals.metal.Metal
import net.minecraft.item.*

class ItemMetalShovel(override val metal: Metal) : ItemSpade(metal.toolMaterial), ItemMetalSingle {
}
