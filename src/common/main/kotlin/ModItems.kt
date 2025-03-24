package pixel.autoquest

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
//import pixel.autoquest.Autoquest.Blocks
import pixel.autoquest.Autoquest.MOD_ID


object ModItems {
    val items: DeferredRegister<Item> = DeferredRegister.create(MOD_ID, Registries.ITEM)

    val CopperCoin: RegistrySupplier<Item> = items.register("copper_coin") {
        ConvertItem(Item.Properties().stacksTo(64), 10, 1, "autoquest:copper_coin_stack")
    }

    val CopperCoinStack: RegistrySupplier<Item> = items.register("copper_coin_stack") {
        Item(Item.Properties().stacksTo(64))
    }

    val SilverCoin: RegistrySupplier<Item> = items.register("silver_coin") {
        ConvertItem(Item.Properties().stacksTo(64), 10, 1, "autoquest:silver_coin_stack")
    }

    val SilverCoinStack: RegistrySupplier<Item> = items.register("silver_coin_stack") {
        Item(Item.Properties().stacksTo(64))
    }

    val MoneyBill: RegistrySupplier<Item> = items.register("money_bill") {
        ConvertItem(Item.Properties().stacksTo(64), 20, 1, "autoquest:money_roll")
    }

    val MoneyRoll: RegistrySupplier<Item> = items.register("money_roll") {
        Item(Item.Properties().stacksTo(64))
    }
    val MoneyPile: RegistrySupplier<Item> = items.register("money_pile") {
        BlockItem(ModBlocks.MoneyPile.get(), Item.Properties().stacksTo(64))
    }
    fun initialize() {
        items.register()
    }
}