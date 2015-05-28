package tombenpotter.modpouches.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class PouchContainer extends Container {

    public PouchInventory pouchInventory;
    public InventoryPlayer inventoryPlayer;
    public int numRows;
    int pouchSlot;

    public PouchContainer(InventoryPlayer inventoryPlayer, PouchInventory pouchInventory) {
        this.inventoryPlayer = inventoryPlayer;
        this.pouchInventory = pouchInventory;
        this.pouchSlot = inventoryPlayer.currentItem;

        this.numRows = pouchInventory.getSizeInventory() / 9;
        int i = (this.numRows - 4) * 18;
        int j;
        int k;

        for (j = 0; j < this.numRows; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(new PouchSlot(pouchInventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventoryPlayer, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryPlayer, j, 8 + j * 18, 161 + i));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return pouchInventory.isUseableByPlayer(player);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotNumber);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (slotNumber < this.numRows * 9) {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true)) {
                    return null;
                }
            } else {
                if (!pouchInventory.isItemValidForSlot(slotNumber, itemstack1)) {
                    return null;
                } else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false)) {
                    return null;
                }
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        pouchInventory.saveContents();
        player.inventory.setInventorySlotContents(inventoryPlayer.currentItem, pouchInventory.pouchStack);

        super.onContainerClosed(player);
    }

    @Override
    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
        int clickedSlot = par1 - pouchInventory.getSizeInventory() - 27;

        if (clickedSlot == pouchSlot || (par3 == 2 && par2 == pouchSlot))
            return null;

        return super.slotClick(par1, par2, par3, par4EntityPlayer);
    }
}
