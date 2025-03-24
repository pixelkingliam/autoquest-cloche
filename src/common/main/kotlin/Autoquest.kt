package pixel.autoquest

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dev.architectury.registry.registries.Registrar
import dev.architectury.registry.registries.RegistrarManager
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.block.Block
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object Autoquest {
    const val MOD_ID: String = "autoquest"
    var key = ""
    public val Manager: Supplier<RegistrarManager> = Suppliers.memoize {
        RegistrarManager.get(MOD_ID)
    };
    var autoQuestAutoLogin:  GameRules.Key<GameRules. BooleanValue>? = null;

    val regItems: Registrar<Item> = Manager.get().get(Registries.ITEM)
    //val Items: DeferredRegister<Item> = DeferredRegister.create(MOD_ID, Registries.ITEM)
    val Blocks: Registrar<Block> = Manager.get().get(Registries.BLOCK)
    //val Blocks: DeferredRegister<Block> = DeferredRegister.create(MOD_ID, Registries.BLOCK)
    public var connected = false
    public val logger = LoggerFactory.getLogger("autoquest")
    fun init(pi: PlatformImplementation) {
        //Todo
        //autoQuestAutoLogin = pi.registerGameRule("AutoQuestReconnect", GameRules.Category.MISC, GameRules.BooleanValue.(false));

        ModBlocks.initialize()
        ModItems.initialize()
        Net.initialize()
        Commands.initialize()
        logger.info("Hello kotlin world!")
        // Write common init code here.
    }
    fun give(player: Player, itemStack: ItemStack) {
        player.level().addFreshEntity(ItemEntity(player.level(), player.position().x,player.position().y+0.2f,player.position().z, itemStack))
    }
    fun getId(item: ItemStack): String {
        val id2 = BuiltInRegistries.ITEM.getKey(item.item)
        return "%s:%s".format(id2.namespace, id2.path)
    }
    fun getId(item: Item): String {
        val id2 = BuiltInRegistries.ITEM.getKey(item)
        return "%s:%s".format(id2.namespace, id2.path)
    }
//    fun openBarterInput(barter: Barter, playerId: UUID) {
//        val player = ServerUtils.server.playerManager.playerList.first {
//            it.uuid == playerId
//        }
//        player.openHandledScreen(BarterInputScreenHandlerFactory(barter.id))
//    }
}

class LocalDateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    override fun write(out: JsonWriter, value: LocalDateTime?) {
        out.value(value?.format(formatter))
    }

    override fun read(reader: JsonReader): LocalDateTime? {
        return LocalDateTime.parse(reader.nextString(), formatter)
    }
}