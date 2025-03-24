package pixel.autoquest.barter

import com.google.gson.annotations.SerializedName
import net.minecraft.world.item.Rarity
import pixel.autoquest.BarterMode
import java.io.Serializable
import java.time.LocalDateTime

data class Barter(
    @SerializedName("InputValue") val inputValue: Int,
    @SerializedName("Progress") val progress: Map<String, Int>,
    @SerializedName("Output") val output: String,
    @SerializedName("OutputMoneyCount") val outputMoneyCount: Int,
    @SerializedName("OutputCount") val outputCount: Int,
    @SerializedName("Experience") val experience: Int,
    @SerializedName("BarterXP") val barterXP: Int,
    @SerializedName("IsDynamic") val isDynamic: Boolean,
    @SerializedName("IsWeekly") val isWeekly: Boolean,
    @SerializedName("Level") val level: Int,
    @SerializedName("ExpirationDate") val expirationDate: LocalDateTime,
    @SerializedName("Id") val id: String,
    @SerializedName("Mode") val mode: BarterMode,
    @SerializedName("Tag") val tag: String?,
    @SerializedName("Pool") val pool: String?,
    @SerializedName("Attribute") val attribute: String?,
    @SerializedName("Name") val name: String,
    @SerializedName("Description") val description: String,
    @SerializedName("MinimumRarity") val rarity: Rarity

): Serializable