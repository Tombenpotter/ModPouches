package tombenpotter.modpouches.util;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    public static Configuration config;
    public static String[] modBlacklist;
    public static String[] modBlacklistDefault = {"Mod Pouches"};
    public static boolean allowChildrenItems;

    public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        String category;

        category = "General";
        config.addCustomCategoryComment(category, "Miscellaneous settings.");
        modBlacklist = config.getStringList("modBlacklist", category, modBlacklistDefault, "List of mods you don't want pouches to be created for. The default blacklist is simply removing Mod Pouches' pouch.\nSyntax is the mod name, not the modid.");
        allowChildrenItems = config.getBoolean("allowChildrenItems", category, true, "Whether or not to allow items from children mods to go into their parents' pouches");

        config.save();
    }
}
