package pixel.autoquest

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import dev.architectury.event.events.common.CommandRegistrationEvent
import dev.architectury.networking.NetworkManager
import io.netty.buffer.Unpooled
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import pixel.autoquest.Autoquest.autoQuestAutoLogin
import pixel.autoquest.Autoquest.connected
import pixel.autoquest.Autoquest.key
import pixel.autoquest.Autoquest.logger
//import pixel.autoquest.Autoquest.regItems
import pixel.autoquest.Net.getItemsFromServer
import pixel.autoquest.Net.getModIdsAsJson
import pixel.autoquest.Net.getProfile
import pixel.autoquest.Net.ip
import pixel.autoquest.Net.items
import pixel.autoquest.Net.refreshMods
import pixel.autoquest.packets.AQPackets
import pixel.autoquest.packets.ForceItems
import pixel.autoquest.packets.ForceProfile
import java.io.FileWriter
import java.io.IOException

object Commands {
    fun initialize() {
        CommandRegistrationEvent.EVENT.register(CommandRegistrationEvent { commandDispatcher, commandBuildContext, commandSelection ->
            // DUMP ID
//            commandDispatcher.register(
//                literal("dumpid").then(
//                    argument("mod", StringArgumentType.string())
//                        /*.suggests { context: CommandContext<CommandSourceStack?>?, builder: SuggestionsBuilder? ->
//                            SharedSuggestionProvider.suggest(
//                                namespaces,
//                                builder
//                            )
//                        })*/.executes((Command { context: CommandContext<CommandSourceStack> ->
//                    val context =
//                        context as CommandContext<CommandSourceStack>
//                    if (ip.isEmpty()) {
//                        context.source.player!!
//                            .sendSystemMessage(Component.literal("AQ Server IP not set! use /aqip <IP>!"))
//                        return@Command 0
//                    }
//                    val mod = StringArgumentType.getString(context, "mod")
//                    var file: FileWriter? = null
//                    try {
//                        file = FileWriter(String.format("%s_ids.txt", mod))
//                    } catch (e: IOException) {
//                        throw RuntimeException(e)
//                    }
//                    context.source.sendSystemMessage(Component.literal(String.format("Dumping all ids from %s, this might take a while...", mod)))
//
//                    // Iterate through all registered items
//                    val reg =
//                        regItems
//                    for (item in reg) {
//                        // Get the item's ID (such as "minecraft:stone")
//                        val itemId = reg.getId(item).toString()
//                        if (!itemId.startsWith(String.format(mod))) {
//                            continue
//                        }
//                        // Write the ID to the file
//                        try {
//                            file.write(itemId)
//
//                            file.write(",\"\",\"\",\"\",\"\",\"\"")
//                            file.write("\n")
//                            file.close()
//                        } catch (e: IOException) {
//                            throw RuntimeException(e)
//                        }
//                    }
//                            context.source.player!!.sendSystemMessage(Component.literal(String.format("Dumping all ids from %s, this might take a while...", mod)))
//                    0
//                }))))
            // AUTOQUEST IP
            commandDispatcher.register(
                literal("aqip").requires{
                    it.hasPermission(1) || it.server.isSingleplayer
                }.then(argument("ip", StringArgumentType.string()).executes { context ->
                    ip = StringArgumentType.getString(context, "ip")
                    context.source.sendSystemMessage(Component.literal("IP Set! you can now use /connect and /register to make an AQ Profile"))
                    0
                }))
            // REGISTER
            commandDispatcher.register(
                literal("register").requires{
                    (it.hasPermission(1) || it.server.isSingleplayer) && ip != ""
                }.then(argument("servername", StringArgumentType.string())
                    .then(argument("password", StringArgumentType.string()).executes { context ->

                        if (ip == "") {
                            context.source.sendSystemMessage(Component.literal("AQ Server IP not set! use /aqip <IP>!"))
                            return@executes 0
                        }
                        val username = StringArgumentType.getString(context, "servername")
                        val password = StringArgumentType.getString(context, "password")
                        context.source.sendSystemMessage(Component.literal("Registering server..."))

                        try {
                            val client = OkHttpClient()
                            val request = Request.Builder()
                                .url("http://%s/register".format(ip))
                                .addHeader("Password", password)
                                .addHeader("Server", username)
                                //.method("POST", null)
                                .post(RequestBody.create(MediaType.parse("text/json"), getModIdsAsJson()))
                                .build()

                            val response = client.newCall(request).execute()

                            val result = response.code()
                            val bodyResult = response.body()?.toString()
                            context.source.sendSystemMessage(Component.literal(bodyResult!!))

                            if (result != 200) {
                                context.source.sendSystemMessage(Component.literal("Unknown Error! Is the server down?"))

                            }else {
                                context.source.sendSystemMessage(Component.literal("Success! Your minecraft server is now registered!"))
                            }

                        }catch(e: Exception) {
                            context.source.sendSystemMessage(Component.literal(e.message!!))
                        }
                        0
                    })))
            // CONNECT
            commandDispatcher.register(
                literal("connect").requires{
                    (it.hasPermission(1) || it.server.isSingleplayer) && ip != ""
                }.then(argument("servername", StringArgumentType.string())
                    .then(argument("password", StringArgumentType.string()).executes { context ->

                        val username = StringArgumentType.getString(context, "servername")
                        val password = StringArgumentType.getString(context, "password")
                        context.source.sendSystemMessage(Component.literal("Connecting server..."))

                        try {

                            val client = OkHttpClient()
                            val request = Request.Builder()
                                .url("http://%s/connect".format(ip))
                                .addHeader("Password", password)
                                .addHeader("Server", username)
                                .method("GET", null)
                                .build()
                            val response = client.newCall(request).execute()

                            val result = response.code()
                            val bodyResult = response.body().string()
                            //context.source.sendFeedback({ Text.literal(bodyResult))

                            if (result != 200) {
                                if (result == 401)
                                {
                                    context.source.sendSystemMessage(Component.literal("Failed to connect. Invalid Credentials"))

                                }else
                                {
                                    context.source.sendSystemMessage(Component.literal("Failed to connect. %d".format(result)))

                                }
                                0
                            }else {
                                if (!context.source.level.gameRules.getBoolean(autoQuestAutoLogin)) {
                                    context.source.sendSystemMessage(Component.literal("Connected! To avoid manually having to reconnect, set /gamerule AutoQuestReconnect to true"))
                                }else {
                                    logger.info("Auto logged in.")
                                }
                                connected = true
                                key = bodyResult
                            }
                            refreshMods(context.source.server)
                            logger.info("Getting items...")
                            getItemsFromServer()
                            for (player in context.source.server.playerList.players) {
                                val profile = getProfile(player.uuid)
                                val profilePacket = ForceProfile(profile)
                                //NetworkManager.sendToPlayer(player, profilePacket)
                                NetworkManager.sendToPlayer(player, ForceItems(items))
                            }
                        }catch(e: Exception) {
                            for(string in e.stackTraceToString().split("\n")) {
                                context.source.sendSystemMessage(Component.literal(string))

                            }
                            context.source.sendSystemMessage(Component.literal(e.message))
                        }
                        0

                    })))
        })
    }
}