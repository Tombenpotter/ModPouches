package tombenpotter.modpouches.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tombenpotter.modpouches.ModPouches;
import tombenpotter.modpouches.gui.craftingPouch.CraftingPouchContainer;
import tombenpotter.modpouches.gui.craftingPouch.CraftingPouchGui;
import tombenpotter.modpouches.gui.pouch.PouchContainer;
import tombenpotter.modpouches.gui.pouch.PouchGui;
import tombenpotter.modpouches.util.RandomUtils;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == RandomUtils.POUCH_GUI && player.getHeldItem() != null && player.getHeldItem().getItem() == ModPouches.itemModPouch) {
            return new PouchContainer(player.inventory, new PouchInventory(RandomUtils.POUCH_SLOTS, player, player.getHeldItem()));
        } else if (ID == RandomUtils.CRAFTING_POUCH_GUI && player.getHeldItem() != null && player.getHeldItem().getItem() == ModPouches.itemModPouch) {
            return new CraftingPouchContainer(player.inventory, new PouchInventory(RandomUtils.CRAFTING_POUCH_SLOTS, player, player.getHeldItem()));
        } else {
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == RandomUtils.POUCH_GUI && player.getHeldItem() != null && player.getHeldItem().getItem() == ModPouches.itemModPouch) {
            return new PouchGui(new PouchContainer(player.inventory, new PouchInventory(RandomUtils.POUCH_SLOTS, player, player.getHeldItem())));
        } else if (ID == RandomUtils.CRAFTING_POUCH_GUI && player.getHeldItem() != null && player.getHeldItem().getItem() == ModPouches.itemModPouch) {
            return new CraftingPouchGui(new CraftingPouchContainer(player.inventory, new PouchInventory(RandomUtils.CRAFTING_POUCH_SLOTS, player, player.getHeldItem())));
        } else {
            return null;
        }
    }
}
