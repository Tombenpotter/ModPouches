package tombenpotter.modpouches.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class CraftingPouchContainer extends Container {

    public PouchInventory pouchInventory;
    public InventoryPlayer inventoryPlayer;
    public InventoryCrafting craftMatrix;
    public IInventory craftingResult;
    public int numRows;

    public CraftingPouchContainer(InventoryPlayer inventoryPlayer, PouchInventory pouchInventory) {
        this.pouchInventory = pouchInventory;
        this.inventoryPlayer = inventoryPlayer;
        this.craftMatrix = new InventoryCrafting(this, 3, 3);
        this.craftingResult = new InventoryCraftResult();
        this.numRows = pouchInventory.getSizeInventory() / 9;

        int i = (6 - 4) * 18;
        int j;
        int k;

        for (j = 0; j < this.numRows; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(new PouchSlot(pouchInventory, k + j * 9, 8 + k * 18, 22 + (j + 3) * 18));
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 3; ++k) {
                this.addSlotToContainer(new Slot(this.craftMatrix, k + j * 3, 30 + k * 18, 17 + j * 18));
            }
        }
        this.addSlotToContainer(new SlotCrafting(inventoryPlayer.player, this.craftMatrix, this.craftingResult, 0, 124, 35));

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventoryPlayer, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryPlayer, j, 8 + j * 18, 161 + i));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    public void onCraftMatrixChanged(IInventory inventory) {
        this.craftingResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, inventoryPlayer.player.worldObj));
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
            //Crafting Output slot
            if (slotNumber == 36) {
                if (!pouchInventory.isItemValidForSlot(slotNumber, itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true)) {
                        return null;
                    }
                } else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false)) {
                    return null;
                }
            } else if (slotNumber < this.numRows * 9) {
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
            slot.onPickupFromSlot(player, itemstack1);
        }
        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        pouchInventory.saveContents();
        player.inventory.setInventorySlotContents(player.inventory.currentItem, pouchInventory.pouchStack);
        if (!player.worldObj.isRemote) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
                if (itemstack != null) {
                    player.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }

    @Override
    public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player) {
        if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem()) {
            return null;
        }
        return super.slotClick(slot, button, flag, player);
    }
}
