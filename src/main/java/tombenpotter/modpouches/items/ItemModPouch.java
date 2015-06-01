package tombenpotter.modpouches.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tombenpotter.modpouches.ModPouches;
import tombenpotter.modpouches.util.RandomUtils;

import java.util.List;
import java.util.Random;

public class ItemModPouch extends Item {

    public IIcon overlay;
    private Random random = new Random();

    public ItemModPouch() {
        setCreativeTab(ModPouches.pouchTab);
        setMaxStackSize(1);
        setUnlocalizedName(ModPouches.modid + ".mod.pouch");
    }

    public static String getMod(ItemStack stack) {
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey(RandomUtils.MOD_TAG))
            return stack.stackTagCompound.getString(RandomUtils.MOD_TAG);
        else
            return "";
    }

    public static void setMod(ItemStack stack, String mod) {
        stack.stackTagCompound.setString(RandomUtils.MOD_TAG, mod);
    }

    public static int getColor(ItemStack stack) {
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey(RandomUtils.COLOR_TAG))
            return stack.stackTagCompound.getInteger(RandomUtils.COLOR_TAG);
        else
            return 16777215;
    }

    public static void setColor(ItemStack stack, int color) {
        stack.stackTagCompound.setInteger(RandomUtils.COLOR_TAG, color);
    }

    public static void setPickupActivated(ItemStack stack, boolean isActivated) {
        if (stack.hasTagCompound())
            stack.stackTagCompound.setBoolean(RandomUtils.PICKUP_TAG, isActivated);
    }

    public static boolean getPickupActivated(ItemStack stack) {
        return stack.hasTagCompound() && stack.stackTagCompound.getBoolean(RandomUtils.PICKUP_TAG);
    }

    public static void setRefillActivated(ItemStack stack, boolean isActivated) {
        if (stack.hasTagCompound())
            stack.stackTagCompound.setBoolean(RandomUtils.REFILL_TAG, isActivated);
    }

    public static boolean getRefillActivated(ItemStack stack) {
        return stack.hasTagCompound() && stack.stackTagCompound.getBoolean(RandomUtils.REFILL_TAG);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + String.valueOf(stack.getItemDamage());
    }

    @Override
    public void registerIcons(IIconRegister ir) {
        this.itemIcon = ir.registerIcon(ModPouches.texturePath + "mod_pouch");
        this.overlay = ir.registerIcon(ModPouches.texturePath + "mod_pouch_thingy");
    }

    @Override
    public int getRenderPasses(int metadata) {
        return requiresMultipleRenderPasses() ? 2 : 1;
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 0) {
            return this.itemIcon;
        } else if (pass == 1) {
            return this.overlay;
        }
        return getIconFromDamageForRenderPass(stack.getItemDamage(), pass);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String mod = getMod(stack);
        if (!mod.equals(""))
            return getMod(stack) + " " + super.getItemStackDisplayName(stack);
        else
            return super.getItemStackDisplayName(stack);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        if (stack.stackTagCompound.hasKey(RandomUtils.MOD_TAG)) {
            if (stack.getItemDamage() == 0)
                player.openGui(ModPouches.instance, RandomUtils.POUCH_GUI, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            else if (stack.getItemDamage() == 1)
                player.openGui(ModPouches.instance, RandomUtils.CRAFTING_POUCH_GUI, world, (int) player.posX, (int) player.posY, (int) player.posZ);

            return stack;
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof IInventory && player.getHeldItem() != null && player.getHeldItem().getItem() == ModPouches.itemModPouch) {
            RandomUtils.placePouchContentsInInventory(player.getHeldItem(), (IInventory) world.getTileEntity(x, y, z), player, side);
            return true;
        }
        return false;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if (pass == 0)
            return getColor(stack);
        else
            return super.getColorFromItemStack(stack, pass);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
        list.add(StatCollector.translateToLocal("text.ModPouches.pickup") + ": " + EnumChatFormatting.YELLOW + getPickupActivated(stack));
        list.add(StatCollector.translateToLocal("text.ModPouches.refill") + ": " + EnumChatFormatting.YELLOW + getRefillActivated(stack));
    }
}
