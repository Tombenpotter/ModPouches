package tombenpotter.modpouches.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tombenpotter.modpouches.ModPouches;
import tombenpotter.modpouches.util.RandomUtils;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == RandomUtils.POUCH_GUI && player.getHeldItem() != null && player.getHeldItem().getItem() == ModPouches.itemModPouch) {
            return new PouchContainer(player.inventory, new PouchInventory(RandomUtils.POUCH_SLOTS, player, player.getHeldItem()));
        } else {
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == RandomUtils.POUCH_GUI && player.getHeldItem() != null && player.getHeldItem().getItem() == ModPouches.itemModPouch) {
            return new PouchGui(new PouchContainer(player.inventory, new PouchInventory(RandomUtils.POUCH_SLOTS, player, player.getHeldItem())));
        } else {
            return null;
        }
    }
}
