package com.hea3ven.metals.item

import com.hea3ven.metals.metal.Metal
import net.minecraft.item.*

class ItemMetalSword(override val metal: Metal) : ItemSword(metal.toolMaterial), ItemMetalSingle {
}
