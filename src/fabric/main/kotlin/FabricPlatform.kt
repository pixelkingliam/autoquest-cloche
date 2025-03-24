package pixel.autoquestmodern.fabric

import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.level.GameRules
import pixel.autoquest.PlatformImplementation


class FabricPlatform : PlatformImplementation() {
    override fun <T : GameRules.Value<T>> registerGameRule(
        string: String,
        category: GameRules.Category,
        type: GameRules.Type<T>
    ): GameRules.Key<T> {
        return GameRuleRegistry.register(string, category, type);
    }

}