package tombenpotter.modpouches.util;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tombenpotter.modpouches.ModPouches;

public class CopyNBTRecipe extends ShapedOreRecipe {

    static {
        RecipeSorter.register(ModPouches.modid + ":CopyNBTRecipe", CopyNBTRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
    }

    public CopyNBTRecipe(ItemStack result, Object... recipe) {
        super(result, recipe);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        ItemStack pouch = inventoryCrafting.getStackInSlot(4).copy();

        ItemStack result = super.getCraftingResult(inventoryCrafting);
        if (pouch.hasTagCompound())
            result.setTagCompound(pouch.stackTagCompound);

        return result;
    }
}
