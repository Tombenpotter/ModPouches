package tombenpotter.modpouches.gui.pouch;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import tombenpotter.modpouches.gui.PouchAbilitiesGui;
import tombenpotter.modpouches.gui.PouchInventory;

public class PouchGui extends GuiContainer {

    public static ResourceLocation texture = new ResourceLocation("textures/gui/container/generic_54.png");
    public PouchInventory pouchInventory;
    public PouchContainer pouchContainer;
    public int inventoryRows;
    public GuiButton abilities;

    public PouchGui(PouchContainer pouchContainer) {
        super(pouchContainer);
        this.pouchContainer = pouchContainer;
        this.pouchInventory = pouchContainer.pouchInventory;

        short short1 = 222;
        int i = short1 - 108;
        this.inventoryRows = pouchInventory.getSizeInventory() / 9;
        this.ySize = i + this.inventoryRows * 18;
    }

    public void drawGuiContainerForegroundLayer(int par1, int par2) {
        String s = pouchInventory.getInventoryName();
        if (s.length() > 30)
            this.fontRendererObj.setUnicodeFlag(true);
        this.fontRendererObj.drawString(s, 5, 6, 4210752);
        this.fontRendererObj.setUnicodeFlag(false);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 94, 4210752);
    }

    public void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(k, l + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        buttonList.add(abilities = new GuiButton(0, guiLeft + 4 * xSize / 6, guiTop, 42, 18, StatCollector.translateToLocal("text.ModPouches.abilities")));
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new PouchAbilitiesGui(pouchInventory.pouchStack));
        }
    }
}
