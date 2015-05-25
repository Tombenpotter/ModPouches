package tombenpotter.modpouches.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.DefaultOverlayHandler;
import tombenpotter.modpouches.ModPouches;
import tombenpotter.modpouches.gui.CraftingPouchGui;

public class NEIConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        API.registerGuiOverlayHandler(CraftingPouchGui.class, new DefaultOverlayHandler(), "crafting");
    }

    @Override
    public String getName() {
        return ModPouches.name;
    }

    @Override
    public String getVersion() {
        return ModPouches.version;
    }
}
