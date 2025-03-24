package pixel.autoquest

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserSettings(
    @SerializedName("View") val view: UserViewSettings,

) : Serializable
