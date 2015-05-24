package tombenpotter.modpouches.util;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import tombenpotter.modpouches.ModPouches;
import tombenpotter.modpouches.gui.CraftingPouchContainer;
import tombenpotter.modpouches.gui.PouchContainer;
import tombenpotter.modpouches.gui.PouchInventory;
import tombenpotter.modpouches.items.ItemModPouch;

import java.awt.*;
import java.util.Random;

public class EventHandler {

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

    @SubscribeEvent
    public void onPouchCraft(PlayerEvent.ItemCraftedEvent event) {
        for (int i = 0; i < 9; i++) {
            if (i == 4 && (event.craftMatrix.getStackInSlot(i) == null || event.craftMatrix.getStackInSlot(i).getItem() != ModPouches.itemModPouch)) {
                return;
            } else if (i != 4 && (event.craftMatrix.getStackInSlot(i) == null || event.craftMatrix.getStackInSlot(i).getItem() != Item.getItemFromBlock(Blocks.crafting_table))) {
                return;
            }
        }

        if (!event.player.worldObj.isRemote) {
            PouchInventory pouchInventory = new PouchInventory(RandomUtils.POUCH_SLOTS, event.player, event.craftMatrix.getStackInSlot(4));
            for (int i = 0; i < pouchInventory.getSizeInventory(); i++) {
                event.player.dropPlayerItemWithRandomChoice(pouchInventory.getStackInSlot(i), false);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerPickup(EntityItemPickupEvent event) {
        ItemStack pickedUp = event.item.getEntityItem();
        if (pickedUp == null || pickedUp.stackSize <= 0)
            return;

        if (event.entityPlayer.openContainer instanceof PouchContainer || event.entityPlayer.openContainer instanceof CraftingPouchContainer)
            return;

        for (ItemStack stack : event.entityPlayer.inventory.mainInventory) {
            if (stack == null || stack.stackSize <= 0)
                continue;

            if (!(stack.getItem() instanceof ItemModPouch))
                continue;

            if (!stack.hasTagCompound() || !ItemModPouch.getPickupActivated(stack))
                continue;

            if (RandomUtils.placeItemStackInPouch(pickedUp, stack, event.entityPlayer)) {
                event.setResult(Event.Result.ALLOW);
                return;
            }
        }
    }
}
