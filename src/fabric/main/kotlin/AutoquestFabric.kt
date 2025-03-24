package pixel.autoquestmodern.fabric

import net.fabricmc.api.ModInitializer
import pixel.autoquest.Autoquest


class AutoquestFabric : ModInitializer {
    override fun onInitialize() {
        Autoquest.init(FabricPlatform())
    }
}