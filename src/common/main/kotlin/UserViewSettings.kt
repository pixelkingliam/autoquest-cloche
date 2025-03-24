package pixel.autoquest

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserViewSettings(
    @SerializedName("ShowToolTipPrice") val showToolTipPrice: Boolean,
    @SerializedName("ShowToolTipRarity") val showToolTipRarity: Boolean,
    @SerializedName("ShowToolTipTags") val showToolTipTags: Boolean,
    @SerializedName("ShowToolTipFamily") val showToolTipFamily: Boolean,
) : Serializable