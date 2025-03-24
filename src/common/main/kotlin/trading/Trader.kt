package pixel.autoquest.trading;

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Trader(
    @SerializedName("Id")
    val id: String,

    @SerializedName("ShortName")
    val shortName: String,

    @SerializedName("LongName")
    val longName: String,

    @SerializedName("Description")
    val description: String,

    @SerializedName("PreferredTags")
    val preferredTags: Array<String>,

    @SerializedName("PreferredFamilies")
    val preferredFamilies: Array<String>,

    @SerializedName("Levels")
    val levels: Array<TraderLevel>

): Serializable {
    fun getTraderLevel(stats: TraderStats, barterLevel: Int): Int {
        var maxLevelForRep = 0
        var maxLevelForMoney = 0
        var maxLevelForBarter = 0
        for (level in levels) {
            if (level.traderReputationRequirement <= stats.reputation) {
                maxLevelForRep++
            }
            if (level.financialRequirement <= stats.moneyTraded) {
                maxLevelForMoney++
            }
            if (level.barterLevelRequirement <= barterLevel) {
                maxLevelForBarter++
            }
        }
        var value = 0
        if (maxLevelForRep <= maxLevelForMoney && maxLevelForRep <= maxLevelForBarter) {
            value = maxLevelForRep
        }
        if (maxLevelForBarter <= maxLevelForMoney) {
            value = maxLevelForBarter
        }
        value = maxLevelForMoney
        return value - 1
    }
}