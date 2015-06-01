package tombenpotter.modpouches.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import tombenpotter.modpouches.items.ItemModPouch;

public class PacketTogglePickup implements IMessage, IMessageHandler<PacketTogglePickup, IMessage> {

    public PacketTogglePickup() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public IMessage onMessage(PacketTogglePickup message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        ItemStack pouchStack = player.getHeldItem();
        if (pouchStack != null) {
            ItemModPouch.setPickupActivated(pouchStack, !ItemModPouch.getPickupActivated(pouchStack));
            player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("text.ModPouches.pickup")
                    + ": " + EnumChatFormatting.YELLOW + ItemModPouch.getPickupActivated(pouchStack)));
            player.inventoryContainer.detectAndSendChanges();
        }
        return null;
    }
}
