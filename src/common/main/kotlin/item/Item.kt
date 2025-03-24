package pixel.autoquest.item

import com.google.gson.annotations.SerializedName
import pixel.autoquest.item.Rarity
import java.io.Serializable


class Item(
    @SerializedName("Tags")
    val tags: Array<String>,

    @SerializedName("Id")
    val id: String,

    @SerializedName("EnchantmentType")
    val enchantmentType: EnchantmentType,

    @SerializedName("Rarity")
    val rarity: Rarity,

    @SerializedName("Value")
    val value: Int,

    @SerializedName("Attributes")
    val attributes: Array<String>,

    @SerializedName("Pool")
    val pool: String
): Serializable