package tombenpotter.modpouches;

import com.google.common.collect.HashMultimap;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import tombenpotter.modpouches.gui.GuiHandler;
import tombenpotter.modpouches.items.ItemModPouch;
import tombenpotter.modpouches.proxies.CommonProxy;
import tombenpotter.modpouches.util.AnvilHandler;
import tombenpotter.modpouches.util.ConfigHandler;
import tombenpotter.modpouches.util.RandomUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Mod(modid = ModPouches.modid, name = ModPouches.name, version = ModPouches.version)
public class ModPouches {

    public static final String modid = "ModPouches";
    public static final String name = "Mod Pouches";
    public static final String texturePath = "modpouches:";
    public static final String clientProxy = "tombenpotter.modpouches.proxies.ClientProxy";
    public static final String commonProxy = "tombenpotter.modpouches.proxies.CommonProxy";
    public static final String channel = "ModPouches";
    public static final String version = "@VERSION@";

    public static Set<String> loadedModNames = new HashSet<String>();
    public static HashMap<String, String> modIDsForNames = new HashMap<String, String>();
    public static HashMultimap<String, String> modDependencies = HashMultimap.create();
    public static ItemModPouch itemModPouch;
    public static final CreativeTabs pouchTab = new CreativeTabs("tab" + modid) {
        @Override
        public ItemStack getIconItemStack() {
            return new ItemStack(itemModPouch, 1, 0);
        }

        @Override
        public Item getTabIconItem() {
            return itemModPouch;
        }
    };
    @SidedProxy(clientSide = clientProxy, serverSide = commonProxy)
    public static CommonProxy proxy;
    @Mod.Instance(ModPouches.modid)
    public static ModPouches instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        proxy.preLoad();

        itemModPouch = new ItemModPouch();
        GameRegistry.registerItem(itemModPouch, "ItemModPouch");

        GameRegistry.addShapedRecipe(new ItemStack(itemModPouch), "SSS", "LCL", "LCL", 'S', Items.string, 'C', Blocks.chest, 'L', Items.leather);
        GameRegistry.addShapelessRecipe(new ItemStack(itemModPouch), new ItemStack(itemModPouch));

        MinecraftForge.EVENT_BUS.register(new AnvilHandler());
        FMLCommonHandler.instance().bus().register(new AnvilHandler());
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.load();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postLoad();

        for (Item item : GameData.getItemRegistry().typeSafeIterable()) {
            String mod = RandomUtils.getModForItem(item);
            loadedModNames.add(mod);
        }

        for (String mod : ConfigHandler.modBlacklist) {
            loadedModNames.remove(mod);
        }

        for (ModContainer modContainer : Loader.instance().getActiveModList()) {
            String name = modContainer.getName();
            if (loadedModNames.contains(name)) {
                for (ArtifactVersion artifactVersion : modContainer.getRequirements()) {
                    modDependencies.put(name, artifactVersion.getLabel());
                }
                modIDsForNames.put(name, modContainer.getModId());
            }
        }
    }
}
