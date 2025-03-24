package pixel.autoquest

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import pixel.autoquest.Autoquest.MOD_ID


object ModBlocks {
    val blocks: DeferredRegister<Block> = DeferredRegister.create(MOD_ID, Registries.BLOCK)
    val MoneyPile: RegistrySupplier<Block> = blocks.register(
        "money_pile"
    ) { ->
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL))
    }

    fun initialize() {
        blocks.register()
    }


}