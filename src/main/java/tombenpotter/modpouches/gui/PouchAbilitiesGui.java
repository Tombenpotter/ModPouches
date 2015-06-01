package tombenpotter.modpouches.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import tombenpotter.modpouches.network.PacketHandler;
import tombenpotter.modpouches.network.PacketTogglePickup;
import tombenpotter.modpouches.network.PacketToggleRefill;

public class PouchAbilitiesGui extends GuiScreen {

    public int guiLeft, guiTop;
    public int xSize = 192;
    public int ySize = 192;
    public GuiButton pickup;
    public GuiButton refill;

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        guiLeft = (this.width - this.xSize) / 2;
        guiTop = (this.height - this.ySize) / 2;

        buttonList.add(pickup = new GuiButton(0, guiLeft, guiTop, StatCollector.translateToLocal("text.ModPouches.pickup")));
        buttonList.add(refill = new GuiButton(1, guiLeft, 3 * guiTop / 4, StatCollector.translateToLocal("text.ModPouches.refill")));
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            PacketHandler.INSTANCE.sendToServer(new PacketTogglePickup());
        } else if (button.id == 1) {
            PacketHandler.INSTANCE.sendToServer(new PacketToggleRefill());
        }
    }
}
