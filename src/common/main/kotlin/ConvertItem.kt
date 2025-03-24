package pixel.autoquest

import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import pixel.autoquest.Autoquest.regItems
import pixel.autoquest.Autoquest.give


class ConvertItem(properties: Properties, inputCount: Int, outputCount: Int, output: String) : net.minecraft.world.item.Item(properties) {
    val inputCount = inputCount
    val outputCount = outputCount
    val output = output
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(interactionHand)
        if (level.isClientSide()) {
            return InteractionResultHolder.pass(stack)
        }
        if (stack.count >= inputCount) {
            //Logger.info("READDME ${user.server!!.isPvpEnabled}")
            stack.shrink(inputCount)
            //user.dropItem(ItemStack(Registries.ITEM.get(Identifier(output)), outputCount), true)
            val item = regItems.get(ResourceLocation.parse(output))
            give(player, ItemStack(item!!))

        }
        return InteractionResultHolder.success(stack)
    }
    override fun appendHoverText(
        itemStack: ItemStack,
        tooltipContext: TooltipContext,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        val item = regItems.get(
            ResourceLocation.parse(output)
        )
        list.add(Component.literal("Right-click while holding %dx of this item to create %dx %s".format(inputCount, outputCount, item!!.asItem().getName(ItemStack(item)).string)))

    }
}