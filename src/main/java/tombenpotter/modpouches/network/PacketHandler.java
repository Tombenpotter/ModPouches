package tombenpotter.modpouches.network;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import tombenpotter.modpouches.ModPouches;

public class PacketHandler {

    public static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(ModPouches.channel);

    public static void registerPackets() {
        INSTANCE.registerMessage(PacketTogglePickup.class, PacketTogglePickup.class, 0, Side.SERVER);
        INSTANCE.registerMessage(PacketToggleRefill.class, PacketToggleRefill.class, 1, Side.SERVER);
    }
}
