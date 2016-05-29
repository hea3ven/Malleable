package com.hea3ven.malleable.item

import com.hea3ven.malleable.metal.Metal
import net.minecraft.item.*

class ItemMetalPickaxe(override val metal: Metal) : ItemPickaxe(metal.toolMaterial), ItemMetalSingle {
}
