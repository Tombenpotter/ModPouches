package tombenpotter.modpouches.util;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tombenpotter.modpouches.ModPouches;
import tombenpotter.modpouches.gui.PouchInventory;
import tombenpotter.modpouches.items.ItemModPouch;

public class RandomUtils {

    public static String MOD_TAG = "MP_Mod";
    public static String COLOR_TAG = "MP_Color";
    public static String PICKUP_TAG = "MP_Pickup";
    public static String REFILL_TAG = "MP_Refill";

    public static int POUCH_GUI = 0;
    public static int POUCH_SLOTS = 54;

    public static int CRAFTING_POUCH_GUI = 1;
    public static int CRAFTING_POUCH_SLOTS = 27;

    public static EntityItem dropItemStackInWorld(World world, double x, double y, double z, ItemStack stack) {
        if (!world.isRemote) {
            float f = 0.7F;
            float d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            float d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            float d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            EntityItem entityitem = new EntityItem(world, x + d0, y + d1, z + d2, stack);
            entityitem.delayBeforeCanPickup = 1;
            if (stack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
            }
            world.spawnEntityInWorld(entityitem);
            return entityitem;
        }
        return null;
    }

    public static String getModForItem(Item item) {
        try {
            ModContainer mod = GameData.findModOwner(GameData.getItemRegistry().getNameForObject(item));
            return mod == null ? "Minecraft" : mod.getName();
        } catch (NullPointerException ignored) {
        }
        return "Minecraft";
    }

    public static String getModForItem(ItemStack stack) {
        return getModForItem(stack.getItem());
    }

    public static String getModForItem(Block block) {
        return getModForItem(new ItemStack(block));
    }

    public static String getModIDForItem(Item item) {
        try {
            ModContainer mod = GameData.findModOwner(GameData.getItemRegistry().getNameForObject(item));
            return mod == null ? "Minecraft" : mod.getModId();
        } catch (NullPointerException ignored) {
        }
        return "Minecraft";
    }

    public static String getModIDForItem(ItemStack stack) {
        return getModIDForItem(stack.getItem());
    }

    public static String getModIDForItem(Block block) {
        return getModIDForItem(new ItemStack(block));
    }

    public static boolean isItemValidForPouch(ItemStack stack, ItemStack pouchStack) {
        String mod = getModForItem(stack);
        String pouchMod = ItemModPouch.getMod(pouchStack);

        return mod.equals(pouchMod) ||
                (!ModPouches.modDependencies.get(mod).isEmpty() &&
                        ModPouches.modDependencies.get(mod).contains(ModPouches.modIDsForNames.get(pouchMod)) &&
                        ConfigHandler.allowChildrenItems);
    }

    public static boolean placeItemStackInPouch(ItemStack stack, ItemStack pouchStack, EntityPlayer player) {
        int slotNumber = 0;
        if (pouchStack.getItemDamage() == 0)
            slotNumber = POUCH_SLOTS;
        else if (pouchStack.getItemDamage() == 1)
            slotNumber = CRAFTING_POUCH_SLOTS;

        PouchInventory pouchInventory = new PouchInventory(slotNumber, player, pouchStack);

        for (int i = 0; i < pouchInventory.getSizeInventory(); i++) {
            if (pouchInventory.getStackInSlot(i) == null || (pouchInventory.getStackInSlot(i).getItem() == stack.getItem()) && pouchInventory.getStackInSlot(i).getItemDamage() == stack.getItemDamage()) {
                if (isItemValidForPouch(stack, pouchStack)) {
                    insertStackIntoInventory(stack, pouchInventory, ForgeDirection.UNKNOWN);
                    pouchInventory.saveContents();
                }
            }
        }
        return stack.stackSize == 0;
    }

    public static void placePouchContentsInInventory(ItemStack pouchStack, IInventory inventory, EntityPlayer player, int side) {
        int slotNumber = 0;
        if (pouchStack.getItemDamage() == 0)
            slotNumber = POUCH_SLOTS;
        else if (pouchStack.getItemDamage() == 1)
            slotNumber = CRAFTING_POUCH_SLOTS;

        PouchInventory pouchInventory = new PouchInventory(slotNumber, player, pouchStack);
        for (int i = 0; i < pouchInventory.getSizeInventory(); i++) {
            if (pouchInventory.getStackInSlot(i) != null) {
                ItemStack stack = pouchInventory.getStackInSlot(i);
                insertStackIntoInventory(stack, inventory, ForgeDirection.getOrientation(side));
            }
        }
        pouchInventory.saveContents();
    }

    public static ItemStack insertStackIntoInventory(ItemStack stack, IInventory inventory, ForgeDirection dir) {
        if (stack == null)
            return null;

        boolean[] canBeInserted = new boolean[inventory.getSizeInventory()];

        if (inventory instanceof ISidedInventory) {
            int[] array = ((ISidedInventory) inventory).getAccessibleSlotsFromSide(dir.ordinal());
            for (int in : array)
                canBeInserted[in] = inventory.isItemValidForSlot(in, stack) && ((ISidedInventory) inventory).canInsertItem(in, stack, dir.ordinal());

        } else {
            for (int i = 0; i < canBeInserted.length; i++)
                canBeInserted[i] = inventory.isItemValidForSlot(i, stack);
        }

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (!canBeInserted[i])
                continue;

            ItemStack[] combinedStacks = combineStacks(stack, inventory.getStackInSlot(i));
            stack = combinedStacks[0];
            inventory.setInventorySlotContents(i, combinedStacks[1]);

            if (stack.stackSize <= 0)
                return stack;
        }

        return stack;
    }

    /**
     * Adapted from the Blood Magic repository. https://github.com/WayofTime/BloodMagic/
     * Licensed under the Creative Commons Attribution 4.0 International License
     * This is a bit more copypasta than I had hoped. Will do some cleanup/reformatting/rewriting
     * later.
     *
     * @param initial - First stack to combine
     * @param merge   - Second stack to combine
     * @return - Combined stacks
     */
    public static ItemStack[] combineStacks(ItemStack initial, ItemStack merge) {
        ItemStack[] returned = new ItemStack[2];

        if (canCombine(initial, merge)) {
            int transferredAmount = merge == null ? initial.stackSize : Math.min(merge.getMaxStackSize() - merge.stackSize, initial.stackSize);

            if (transferredAmount > 0) {
                ItemStack copyStack = initial.splitStack(transferredAmount);

                if (merge == null)
                    merge = copyStack;
                else
                    merge.stackSize += transferredAmount;
            }
        }

        returned[0] = initial;
        returned[1] = merge;

        return returned;
    }

    /**
     * Adapted from the Blood Magic repository. https://github.com/WayofTime/BloodMagic/
     * Licensed under the Creative Commons Attribution 4.0 International License
     * This is a bit more copypasta than I had hoped. Will do some cleanup/reformatting/rewriting
     * later.
     *
     * @param initial - First stack to check
     * @param merge   - Second stack to check
     * @return - If the stacks can be combined
     */
    public static boolean canCombine(ItemStack initial, ItemStack merge) {
        if (initial == null)
            return false;

        if (merge == null)
            return true;

        if (initial.isItemStackDamageable() ^ merge.isItemStackDamageable())
            return false;

        return initial.getItem() == merge.getItem() && initial.getItemDamage() == merge.getItemDamage() && ItemStack.areItemStackTagsEqual(initial, merge);
    }
}
