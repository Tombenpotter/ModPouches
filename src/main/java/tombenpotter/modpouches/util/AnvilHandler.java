package tombenpotter.modpouches.util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import tombenpotter.modpouches.items.ItemModPouch;

import java.awt.*;
import java.util.Random;

public class AnvilHandler {

    private Random random = new Random();

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.left;
        ItemStack right = event.right;

        if (left == null || right == null)
            return;

        if (left.getItem() instanceof ItemModPouch) {
            if (right.getItem() instanceof ItemNameTag) {
                int color;
                try {
                    color = Color.decode(right.getDisplayName()).getRGB();
                } catch (NumberFormatException e) {
                    return;
                }
                ItemStack output = left.copy();

                if (!output.hasTagCompound())
                    output.setTagCompound(new NBTTagCompound());

                ItemModPouch.setColor(output, color);
                event.output = output;
                event.cost = 1;
                return;
            }

            if (left.hasTagCompound() && left.stackTagCompound.hasKey(RandomUtils.MOD_TAG))
                return;

            String mod = RandomUtils.getModForItem(right);
            ItemStack output = left.copy();

            if (!output.hasTagCompound())
                output.setTagCompound(new NBTTagCompound());

            ItemModPouch.setMod(output, mod);

            random.setSeed(mod.hashCode() | 0xFF000000);
            ItemModPouch.setColor(output, random.nextInt());

            event.output = output;
            event.cost = 1;
        }
    }

    @SubscribeEvent
    public void onAnvilCraft(AnvilRepairEvent event) {
        //This makes no sense because the event is wrongly called.
        ItemStack left = event.output;
        ItemStack right = event.left;

        if (left == null || right == null)
            return;

        if (right.getItem() instanceof ItemNameTag)
            return;

        if (left.getItem() instanceof ItemModPouch) {
            if (!event.entityPlayer.inventory.addItemStackToInventory(right.copy())) {
                RandomUtils.dropItemStackInWorld(event.entityPlayer.worldObj, event.entityPlayer.posX, event.entityPlayer.posY, event.entityPlayer.posZ, right.copy());
            }
        }
    }
}
