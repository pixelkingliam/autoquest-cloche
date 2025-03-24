package pixel.autoquest.trading

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TraderLevel(
    @SerializedName("FinancialRequirement")
    val financialRequirement: Int,
    @SerializedName("BarterLevelRequirement")
    val barterLevelRequirement: Int,
    @SerializedName("TraderReputationRequirement")
    val traderReputationRequirement: Float,
    @SerializedName("Service")
    val service: String,
    @SerializedName("WaresBudget")
    val waresBudget: Int
): Serializable