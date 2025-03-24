package pixel.autoquest.client

import dev.architectury.networking.NetworkManager
import pixel.autoquest.Autoquest
import pixel.autoquest.BSerializer
import pixel.autoquest.Profile
import pixel.autoquest.item.Item
import pixel.autoquest.packets.ForceItems
import pixel.autoquest.packets.ForceProfile

class AutoquestClient {
    var cachedProfile: Profile? = null
    var items: Array<Item>? = null
    fun init() {
        NetworkManager.NetworkReceiver<ForceProfile>() { packet, context ->
            cachedProfile = packet.profile
            Autoquest.logger.info("Received new profile")
        }
        NetworkManager.NetworkReceiver<ForceItems>() { packet, context ->
            items = packet.items
            Autoquest.logger.info("Received new items")
        }

    }
}