package tombenpotter.modpouches.util;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import tombenpotter.modpouches.ModPouches;
import tombenpotter.modpouches.items.ItemModPouch;

public class RandomUtils {

    public static String MOD_TAG = "MP_Mod";
    public static String COLOR_TAG = "MP_Color";

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
}
