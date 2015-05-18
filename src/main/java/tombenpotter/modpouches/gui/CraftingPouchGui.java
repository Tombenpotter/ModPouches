package tombenpotter.modpouches.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tombenpotter.modpouches.ModPouches;

public class CraftingPouchGui extends GuiContainer {

    public static ResourceLocation texture = new ResourceLocation(ModPouches.texturePath + "textures/gui/crafting_pouch.png");
    public PouchInventory pouchInventory;
    public CraftingPouchContainer craftingPouchContainer;
    public int inventoryRows;

    public CraftingPouchGui(CraftingPouchContainer craftingPouchContainer) {
        super(craftingPouchContainer);
        this.craftingPouchContainer = craftingPouchContainer;
        this.pouchInventory = craftingPouchContainer.pouchInventory;

        short short1 = 222;
        int i = short1 - 108;
        this.inventoryRows = 6;
        this.ySize = i + this.inventoryRows * 18;
    }

    public void drawGuiContainerForegroundLayer(int par1, int par2) {
        String s = pouchInventory.getInventoryName();
        if (s.length() > 30)
            this.fontRendererObj.setUnicodeFlag(true);
        this.fontRendererObj.drawString(s, 5, 6, 4210752);
        this.fontRendererObj.setUnicodeFlag(false);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 93, 4210752);
    }

    public void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(k, l + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
