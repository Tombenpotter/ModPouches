package tombenpotter.modpouches.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import tombenpotter.modpouches.items.ItemModPouch;

public class PacketToggleRefill implements IMessage, IMessageHandler<PacketToggleRefill, IMessage> {

    public PacketToggleRefill() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public IMessage onMessage(PacketToggleRefill message, MessageContext ctx) {
        ItemStack pouchStack = ctx.getServerHandler().playerEntity.getHeldItem();
        if (pouchStack != null) {
            ItemModPouch.setRefillActivated(pouchStack, !ItemModPouch.getRefillActivated(pouchStack));
            ctx.getServerHandler().playerEntity.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("text.ModPouches.refill")
                    + ": " + EnumChatFormatting.YELLOW + ItemModPouch.getRefillActivated(pouchStack)));
        }
        return null;
    }
}
