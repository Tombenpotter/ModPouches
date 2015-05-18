package tombenpotter.modpouches.gui;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import tombenpotter.modpouches.util.RandomUtils;

public class PouchSlot extends Slot {

    public PouchSlot(PouchInventory pouchInventory, int index, int x, int y) {
        super(pouchInventory, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return RandomUtils.isItemValidForPouch(stack, ((PouchInventory) inventory).pouchStack);
    }
}
