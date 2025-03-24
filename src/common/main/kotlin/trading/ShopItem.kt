package pixel.autoquest.trading

import com.google.gson.annotations.SerializedName
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import pixel.autoquest.Autoquest
import java.io.Serializable

class ShopItem(
    @SerializedName("Item")
    val item: String,

    @SerializedName("Quantity")
    val quantity: Int,
): Serializable {
    public fun getItemStack(): ItemStack {
        return ItemStack(Autoquest.regItems.get(ResourceLocation.parse(item))!!, quantity)
    }
}