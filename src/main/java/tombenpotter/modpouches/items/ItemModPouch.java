package tombenpotter.modpouches.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
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

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + String.valueOf(stack.getItemDamage());
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

        for (String mod : ModPouches.loadedModNames) {
            ItemStack pouch = new ItemStack(this, 1, 0);
            pouch.setTagCompound(new NBTTagCompound());
            setMod(pouch, mod);
            random.setSeed(mod.hashCode() | 0xFF000000);
            setColor(pouch, random.nextInt());
            list.add(pouch);

            ItemStack craftingPouch = new ItemStack(this, 1, 1);
            craftingPouch.setTagCompound(new NBTTagCompound());
            setMod(craftingPouch, mod);
            random.setSeed(mod.hashCode() | 0xFF000000);
            setColor(craftingPouch, random.nextInt());
            list.add(craftingPouch);
        }
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
        }
        return stack;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if (pass == 0) return getColor(stack);
        else return super.getColorFromItemStack(stack, pass);
    }
}
