package pixel.autoquest

import com.google.gson.annotations.SerializedName
import pixel.autoquest.barter.Barter
import java.io.Serializable

data class Profile (
    @SerializedName("DisplayName") var displayName: String,
    @SerializedName("Name")  var name: String,
    @SerializedName("BarterExperience")  var barterExperience: Int,
    @SerializedName("BarterLevel") var barterLevel: Int,
    @SerializedName("Barters") @Transient var barters: Array<Barter>,
    @SerializedName("Settings") val settings: UserSettings
): Serializable