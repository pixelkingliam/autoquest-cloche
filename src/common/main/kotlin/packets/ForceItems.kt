package pixel.autoquest.packets

import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type
import pixel.autoquest.BSerializer
import pixel.autoquest.item.Item


class ForceItems(public val items: Array<Item>): CustomPacketPayload {
    var arr: ByteArray = BSerializer.serialize(items)
    public val CODEC = StreamCodec.composite(
        ByteBufCodecs.byteArray(arr.size),
        ForceItems::arr,
    ) {
        ForceItems(items)
    }
    override fun type(): CustomPacketPayload.Type<out ForceItems> {

        return Type(AQPackets.ForceItems)
    }

}