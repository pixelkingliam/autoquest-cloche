package pixel.autoquest.packets

import net.minecraft.resources.ResourceLocation


object AQPackets {
    public val RequestProfile = ResourceLocation.tryBuild("autoquest", "profile_request")!!
    public val ResponseProfile = ResourceLocation.tryBuild("autoquest", "profile_response")!!
    public val RequestBarters = ResourceLocation.tryBuild("autoquest", "barters_request")!!
    public val ResponseBarters = ResourceLocation.tryBuild("autoquest", "barters_response")!!
    public val RequestConnectionStatus = ResourceLocation.tryBuild("autoquest", "connection_request")!!
    public val ResponseConnectionStatus = ResourceLocation.tryBuild("autoquest", "connection_response")!!
    public val ForceProfile = ResourceLocation.tryBuild("autoquest", "force_response")!!
    public val ForceItems = ResourceLocation.tryBuild("autoquest", "force_items")!!
    public val SetSetting = ResourceLocation.tryBuild("autoquest", "set_setting")!!
    public val SetName = ResourceLocation.tryBuild("autoquest", "set_name")!!
    public val OpenBarter = ResourceLocation.tryBuild("autoquest", "open_barter")!!
    public val CompleteBarter = ResourceLocation.tryBuild("autoquest", "complete_barter")!!
    public val ConvertItem = ResourceLocation.tryBuild("autoquest", "convert_item")!!
    public val RequestTraders = ResourceLocation.tryBuild("autoquest", "traders_request")!!
    public val ResponseTraders = ResourceLocation.tryBuild("autoquest", "traders_response")!!
    public val RequestTraderStock = ResourceLocation.tryBuild("autoquest", "trader_stock_request")!!
    public val ResponseTraderStock = ResourceLocation.tryBuild("autoquest", "trader_stock_response")!!
}