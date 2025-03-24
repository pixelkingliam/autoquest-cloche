package pixel.autoquest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import dev.architectury.event.events.common.CommandRegistrationEvent
import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import pixel.autoquest.barter.Barter
import pixel.autoquest.item.Item
import pixel.autoquest.item.Rarity
import pixel.autoquest.item.RarityTypeAdapter
import pixel.autoquest.trading.ShopItem
import pixel.autoquest.trading.Trader
import pixel.autoquest.trading.TraderStats
import java.io.File
import java.net.URI
import java.net.URL
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.typeOf

object Net {
    var items = arrayOf<Item>();
    var ip = ""
    var namespaces = arrayOf<String>();
    fun initialize() {
    }
    fun getBarters(player: UUID): Array<Barter>
    {
        val url = URL("http://%s/profile/barters".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .addHeader("Player", player.toString())
            .method("GET", null)
            .build()
        val response = client.newCall(request).execute()

        val result = response.code()
        val bodyResult = response.body().string()
        val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
            .registerTypeAdapter(Rarity::class.java, RarityTypeAdapter())
            .registerTypeAdapter(BarterMode::class.java, BarterModeTypeAdapter())
            .create()
        val type = object : TypeToken<Array<Barter>>() {}.type
        return gson.fromJson(bodyResult, type)
        //TODO
    }
    fun setPlayerSettingBool(player: UUID, setting: String, value: Boolean) {
        val url = URL("http://%s/profile/settings/bool".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .addHeader("Player", player.toString())
            .addHeader("Setting", setting)
            .addHeader("Value", "%b".format(value))
            .method("PUT", RequestBody.create(MediaType.parse("text/plain"), ""))
            .build()
        val response = client.newCall(request).execute()
    }
    fun getBarter(bId: String): Barter?
    {
        val url = URL("http://%s/barter".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .addHeader("Barter", bId)
            .method("GET", null)
            .build()
        val response = client.newCall(request).execute()

        val result = response.code()
        val bodyResult = response.body().string()
        Autoquest.logger.info(bodyResult)

        val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
            .registerTypeAdapter(Rarity::class.java, RarityTypeAdapter())
            .registerTypeAdapter(BarterMode::class.java, BarterModeTypeAdapter())
            .create()
        val type = object : TypeToken<Barter>() {}.type
        return gson.fromJson(bodyResult, type)
    }
    fun getEvaluate(bId: String, stack: ItemStack, playerId: UUID): Int {
        var durability = 1.0
        if (stack.isDamageableItem) {
            durability = ((stack.maxDamage - stack.damageValue).toDouble() / stack.maxDamage.toDouble())
        }
        val url = URL("http://%s/barter/evaluate".format(ip))
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .addHeader("Barter", bId)
            .addHeader("Count", stack.count.toString())
            .addHeader("Item", Autoquest.getId(stack))
            .addHeader("Player", playerId.toString())
            .addHeader("Durability", durability.toString())
            .method("GET", null)
            .build()
        val response = client.newCall(request).execute()

        val result = response.code()
        val bodyResult = response.body().string()
        return bodyResult.toInt()
    }
    fun getItemsFromServer() {
        val url = URL("http://%s/items".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .method("GET", null)
            .build()
        val response = client.newCall(request).execute()

        val result = response.code()
        val bodyResult = response.body().string()

        if (result != 200) {
            Autoquest.logger.info("Got error code! %d".format(result))
        }else {
            val gson = GsonBuilder().registerTypeAdapter(Rarity::class.java, RarityTypeAdapter()).create()
            val type = object : TypeToken<Array<Item>>() {}.type
            //logger.info(bodyResult)
            items = gson.fromJson(bodyResult, type)
        }


    }
    public fun refreshMods(server: MinecraftServer) {
        val url = URL("http://%s/modlist".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .put(RequestBody.create(MediaType.parse("text/json"), getModIdsAsJson()))
            .build()
        val response = client.newCall(request).execute()

        val result = response.code()

        if (result != 204) {
            if (result == 200) {
                Autoquest.logger.info("AQ200")
            }else {
                Autoquest.logger.info("Got error code! %d".format(result))

            }
        }


    }
    public fun sendFeedback(feedback: String, playerId: UUID) {
        val url = URL("http://%s/feedback".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .addHeader("Player", playerId.toString())
            .post(RequestBody.create(MediaType.parse("text/plain"), feedback))
            .build()
        val response = client.newCall(request).execute()

        //val result = response.code()

//		if (result != 204) {
//			if (result == 200) {
//				logger.info("AQ200")
//				server.playerManager.playerList.forEach { player: ServerPlayerEntity ->
//					player.sendMessage(Text.literal("Barter service is reloading. this might take a while."), true) // 'false' means it won't be shown as a system message
//				}
//			}else {
//				logger.info("Got error code! %d".format(result))
//
//			}
//		}else {
//			logger.info("AQ204")
//		}


    }
    fun completeBarter(barter: Barter, player: ServerPlayer) {
        val url = URI.create("http://%s/barter/complete".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url.toURL())
            .addHeader("Session", Autoquest.key)
            .addHeader("Player", player.uuid.toString())
            .addHeader("Barter", barter.id)
            .method("GET", null)
            .build()
        val response = client.newCall(request).execute()

        val result = response.code()
        val bodyResult = response.body().string()

        if (result != 200) {
            Autoquest.logger.info("completeBarter failed: bodyResult: %s\nresult: %d".format(bodyResult, result))
        }
        val output = ItemStack(Autoquest.regItems.get(ResourceLocation.parse(barter.output))!!, barter.outputCount)
        Autoquest.give(player, output)
        player.giveExperiencePoints(barter.experience)
        for(item in convertToMoney(barter.outputMoneyCount)) {
            Autoquest.give(player, item)
        }


    }
    fun convertToMoney(totalCount: Int): List<ItemStack> {
        var moneyItems = items.filter {
            it.attributes.contains("Currency")
        }
        moneyItems = moneyItems.sortedBy { it.value }.reversed()
        val list = mutableListOf<ItemStack>()
        var count = totalCount
        for (item in moneyItems) {
            val quantity = count / item.value
            if (quantity > 0) {
                list.add(ItemStack(Autoquest.regItems.get(ResourceLocation.parse(item.id))!!, quantity))
                count %= item.value
            }
        }
        return list


    }
    // Dictionary<string, Dictionary<int, List<ShopItem>>>
    public fun getTraderStock(trader: String) : HashMap<Int, Array<ShopItem>> {
        val url = URL("http://%s/traders/stock".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .addHeader("Trader", trader)
            .addHeader("Connection", "close")
            .method("GET", null)
            .build()
        val response = client.newCall(request).execute()

        val result = response.code()
        val bodyResult = response.body().string()
        Autoquest.logger.info(bodyResult)
        if (result != 200 && result != 201) {
            Autoquest.logger.info("getProfile failed: bodyResult: %s\nresult: %d".format(bodyResult, result))
        }

        val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter()).create()
        val type = object : TypeToken<HashMap<Int, Array<ShopItem>>>() {}.type
        val res: HashMap<Int, Array<ShopItem>> = gson.fromJson(bodyResult, type)
        return  res
    }
    public fun getProfile(playerId: UUID) : Profile {
        val url = URL("http://%s/profile".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .addHeader("Connection", "close")
            .addHeader("Player", playerId.toString())
            .method("GET", null)
            .build()
        val response = client.newCall(request).execute()

        val result = response.code()
        val bodyResult = response.body().string()

        if (result != 200 && result != 201) {
            Autoquest.logger.info("getProfile failed: bodyResult: %s\nresult: %d".format(bodyResult, result))
        }

        val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter()).create()
        val type = object : TypeToken<Profile>() {}.type
        return gson.fromJson(bodyResult, type)
    }
    fun setProfileDisplayName(playerId: UUID, playerName: String) {

        val url = URL("http://%s/profile/name".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .addHeader("Connection", "close")
            .addHeader("Player", playerId.toString())
            .method("POST", RequestBody.create(MediaType.parse("text/plain"), playerName))
            .build()
        val response = client.newCall(request).execute()

        val result = response.code()
        val bodyResult = response.body().string()
    }
    fun getAllowedItems(barter: Barter, itemList: Array<Item>): List<Item> {
        val filteredItems = when (barter.mode) {
            BarterMode.TagOnly -> itemList.filter { it.rarity.ordinal >= barter.rarity.ordinal && it.tags.contains(barter.tag) }
            BarterMode.PoolOnly -> itemList.filter { it.rarity.ordinal >= barter.rarity.ordinal && it.pool == barter.pool }
            BarterMode.TagAndPool -> itemList.filter { it.rarity.ordinal >= barter.rarity.ordinal && it.tags.contains(barter.tag) && it.pool == barter.pool }
        }
        return filteredItems
    }
    fun getModIdsAsJson(): String {
        val modIds = mutableListOf<String>() // Use a set to avoid duplicates

        // Iterate through all registered items
        for (item in Autoquest.regItems) {
            val id = Autoquest.getId(item)!!
            if (modIds.contains(id.split(":")[0])) {
                continue;
            }
            modIds.add(id.split(":")[0]) // The mod ID is stored as the 'namespace' part of the Identifier
        }

        // Convert the set of mod IDs to a JSON string
        return Gson().toJson(modIds)
    }
    private fun getTraders(): Array<Trader> {
        val url = URL("http://%s/traders".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .method("GET", null)
            .build()
        val response = client.newCall(request).execute()

        val result = response.code()
        val bodyResult = response.body().string()
        if (result != 200) {
            Autoquest.logger.info("getTraders failed: bodyResult: %s\nresult: %d".format(bodyResult, result))
        }
        val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter()).create()
        val type = object : TypeToken<Array<Trader>>() {}.type
        return gson.fromJson(bodyResult, type)


    }
    private fun getTraderStats(playerId: UUID): HashMap<String, TraderStats> {
        val url = URL("http://%s/traders/player/progress".format(ip))
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Session", Autoquest.key)
            .addHeader("Player", playerId.toString())
            .method("GET", null)
            .build()
        val response = client.newCall(request).execute()

        val result = response.code()
        val bodyResult = response.body().string()
        Autoquest.logger.info(bodyResult)

        if (result != 200) {
            Autoquest.logger.info("getTraderStats failed: bodyResult: %s\nresult: %d".format(bodyResult, result))
        }
        val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter()).create()
        val type = object : TypeToken<HashMap<String, TraderStats>>() {}.type
        return gson.fromJson(bodyResult, type)


    }
}