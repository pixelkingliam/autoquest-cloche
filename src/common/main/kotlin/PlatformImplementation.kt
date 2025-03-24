package pixel.autoquest

import net.minecraft.world.item.Item
import net.minecraft.world.level.GameRules

abstract class PlatformImplementation {
    abstract fun <T: GameRules.Value<T>> registerGameRule(string: String, category: GameRules.Category, type: GameRules.Type<T>): GameRules. Key<T>
    //abstract fun registerItem(name: String, settings: Item.Properties, itemFactory: (Item.Properties) -> Item ): Item
}