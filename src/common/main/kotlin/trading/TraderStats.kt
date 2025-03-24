package pixel.autoquest.trading

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TraderStats(

    @SerializedName("MoneyTraded")
    val moneyTraded: Int,
    @SerializedName("ValueSold")
    val valueSold: Int,
    @SerializedName("Reputation")
    val reputation: Float,
): Serializable