package com.hea3ven.malleable.item

import com.hea3ven.malleable.metal.Metal
import net.minecraft.item.*

class ItemMetalSword(override val metal: Metal) : ItemSword(metal.toolMaterial), ItemMetalSingle {
}
