package pixel.autoquest.packets

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type
import pixel.autoquest.Profile

class ForceProfile(public val profile: Profile): CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out ForceProfile> {

        return Type(AQPackets.ForceProfile)
    }

}