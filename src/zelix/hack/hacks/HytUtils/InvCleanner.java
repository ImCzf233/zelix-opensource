package zelix.hack.hacks.HytUtils;

import java.util.*;
import zelix.hack.*;
import zelix.value.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.gui.inventory.*;
import zelix.utils.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.init.*;

public class InvCleanner extends Hack
{
    ModeValue one;
    ModeValue two;
    ModeValue three;
    ModeValue four;
    ModeValue five;
    NumberValue d;
    TimerUtils timer;
    ArrayList<Integer> whitelistedItems;
    public static int weaponSlot;
    public static int pickaxeSlot;
    public static int axeSlot;
    public static int shovelSlot;
    public static int blocksSlot;
    public static int appleSlot;
    public static int bowSlot;
    
    public InvCleanner() {
        super("InvCleaner", HackCategory.HYT_UTILS);
        this.timer = new TimerUtils();
        this.whitelistedItems = new ArrayList<Integer>();
        this.d = new NumberValue("Delay", 0.2, 0.1, 1.0);
        this.one = new ModeValue("one", new Mode[] { new Mode("none", false), new Mode("sword", true), new Mode("axe", false), new Mode("pickaxe", false), new Mode("shovel", false), new Mode("bow", false), new Mode("apple", false), new Mode("blocks", false) });
        this.two = new ModeValue("two", new Mode[] { new Mode("none", false), new Mode("sword", false), new Mode("axe", true), new Mode("pickaxe", false), new Mode("shovel", false), new Mode("bow", false), new Mode("apple", false), new Mode("blocks", false) });
        this.three = new ModeValue("three", new Mode[] { new Mode("none", false), new Mode("sword", false), new Mode("axe", false), new Mode("pickaxe", true), new Mode("shovel", false), new Mode("bow", false), new Mode("apple", false), new Mode("blocks", false) });
        this.four = new ModeValue("four", new Mode[] { new Mode("none", true), new Mode("sword", false), new Mode("axe", false), new Mode("pickaxe", false), new Mode("shovel", false), new Mode("bow", false), new Mode("apple", false), new Mode("blocks", false) });
        this.five = new ModeValue("five", new Mode[] { new Mode("none", false), new Mode("sword", false), new Mode("axe", false), new Mode("pickaxe", false), new Mode("shovel", false), new Mode("bow", false), new Mode("apple", false), new Mode("blocks", true) });
        this.addValue(this.d, this.one, this.two, this.three, this.four, this.five);
    }
    
    public static boolean isPlayerInLobby(final EntityPlayerSP player) {
        return player.inventory.getStackInSlot(0).isItemEqual(new ItemStack(Items.NETHER_STAR)) || player.inventory.getStackInSlot(8).isItemEqual(new ItemStack(Items.IRON_DOOR));
    }
    
    public void set() {
        InvCleanner.weaponSlot = 0;
        InvCleanner.pickaxeSlot = 0;
        InvCleanner.axeSlot = 0;
        InvCleanner.shovelSlot = 0;
        InvCleanner.blocksSlot = 0;
        InvCleanner.appleSlot = 0;
        InvCleanner.bowSlot = 0;
        if (this.one.getSelectMode().getName().equalsIgnoreCase("sword")) {
            InvCleanner.weaponSlot = 36;
        }
        if (this.two.getSelectMode().getName().equalsIgnoreCase("sword")) {
            InvCleanner.weaponSlot = 37;
        }
        if (this.three.getSelectMode().getName().equalsIgnoreCase("sword")) {
            InvCleanner.weaponSlot = 38;
        }
        if (this.four.getSelectMode().getName().equalsIgnoreCase("sword")) {
            InvCleanner.weaponSlot = 39;
        }
        if (this.five.getSelectMode().getName().equalsIgnoreCase("sword")) {
            InvCleanner.weaponSlot = 40;
        }
        if (this.one.getSelectMode().getName().equalsIgnoreCase("pickaxe")) {
            InvCleanner.pickaxeSlot = 36;
        }
        if (this.two.getSelectMode().getName().equalsIgnoreCase("pickaxe")) {
            InvCleanner.pickaxeSlot = 37;
        }
        if (this.three.getSelectMode().getName().equalsIgnoreCase("pickaxe")) {
            InvCleanner.pickaxeSlot = 38;
        }
        if (this.four.getSelectMode().getName().equalsIgnoreCase("pickaxe")) {
            InvCleanner.pickaxeSlot = 39;
        }
        if (this.five.getSelectMode().getName().equalsIgnoreCase("pickaxe")) {
            InvCleanner.pickaxeSlot = 40;
        }
        if (this.one.getSelectMode().getName().equalsIgnoreCase("axe")) {
            InvCleanner.axeSlot = 36;
        }
        if (this.two.getSelectMode().getName().equalsIgnoreCase("axe")) {
            InvCleanner.axeSlot = 37;
        }
        if (this.three.getSelectMode().getName().equalsIgnoreCase("axe")) {
            InvCleanner.axeSlot = 38;
        }
        if (this.four.getSelectMode().getName().equalsIgnoreCase("axe")) {
            InvCleanner.axeSlot = 39;
        }
        if (this.five.getSelectMode().getName().equalsIgnoreCase("axe")) {
            InvCleanner.axeSlot = 40;
        }
        if (this.one.getSelectMode().getName().equalsIgnoreCase("shovel")) {
            InvCleanner.shovelSlot = 36;
        }
        if (this.two.getSelectMode().getName().equalsIgnoreCase("shovel")) {
            InvCleanner.shovelSlot = 37;
        }
        if (this.three.getSelectMode().getName().equalsIgnoreCase("shovel")) {
            InvCleanner.shovelSlot = 38;
        }
        if (this.four.getSelectMode().getName().equalsIgnoreCase("shovel")) {
            InvCleanner.shovelSlot = 39;
        }
        if (this.five.getSelectMode().getName().equalsIgnoreCase("shovel")) {
            InvCleanner.shovelSlot = 40;
        }
        if (this.one.getSelectMode().getName().equalsIgnoreCase("blocks")) {
            InvCleanner.blocksSlot = 36;
        }
        if (this.two.getSelectMode().getName().equalsIgnoreCase("blocks")) {
            InvCleanner.blocksSlot = 37;
        }
        if (this.three.getSelectMode().getName().equalsIgnoreCase("blocks")) {
            InvCleanner.blocksSlot = 38;
        }
        if (this.four.getSelectMode().getName().equalsIgnoreCase("blocks")) {
            InvCleanner.blocksSlot = 39;
        }
        if (this.five.getSelectMode().getName().equalsIgnoreCase("blocks")) {
            InvCleanner.blocksSlot = 40;
        }
        if (this.one.getSelectMode().getName().equalsIgnoreCase("apple")) {
            InvCleanner.appleSlot = 36;
        }
        if (this.two.getSelectMode().getName().equalsIgnoreCase("apple")) {
            InvCleanner.appleSlot = 37;
        }
        if (this.three.getSelectMode().getName().equalsIgnoreCase("apple")) {
            InvCleanner.appleSlot = 38;
        }
        if (this.four.getSelectMode().getName().equalsIgnoreCase("apple")) {
            InvCleanner.appleSlot = 39;
        }
        if (this.five.getSelectMode().getName().equalsIgnoreCase("apple")) {
            InvCleanner.appleSlot = 40;
        }
        if (this.one.getSelectMode().getName().equalsIgnoreCase("bow")) {
            InvCleanner.bowSlot = 36;
        }
        if (this.two.getSelectMode().getName().equalsIgnoreCase("bow")) {
            InvCleanner.bowSlot = 37;
        }
        if (this.three.getSelectMode().getName().equalsIgnoreCase("bow")) {
            InvCleanner.bowSlot = 38;
        }
        if (this.four.getSelectMode().getName().equalsIgnoreCase("bow")) {
            InvCleanner.bowSlot = 39;
        }
        if (this.five.getSelectMode().getName().equalsIgnoreCase("bow")) {
            InvCleanner.bowSlot = 40;
        }
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        this.set();
        final long delay = (long)(this.d.getValue() * 1000.0);
        if (!(this.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (isPlayerInLobby(Wrapper.INSTANCE.player())) {
            return;
        }
        if (this.timer.hasReached(delay) && InvCleanner.weaponSlot >= 36) {
            if (!Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.weaponSlot).getHasStack()) {
                this.getBestWeapon(InvCleanner.weaponSlot);
            }
            else if (!this.isBestWeapon(Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.weaponSlot).getStack())) {
                this.getBestWeapon(InvCleanner.weaponSlot);
            }
        }
        if (this.timer.hasReached(delay) && InvCleanner.pickaxeSlot >= 36) {
            this.getBestPickaxe(InvCleanner.pickaxeSlot);
        }
        if (this.timer.hasReached(delay) && InvCleanner.shovelSlot >= 36) {
            this.getBestShovel(InvCleanner.shovelSlot);
        }
        if (this.timer.hasReached(delay) && InvCleanner.axeSlot >= 36) {
            this.getBestAxe(InvCleanner.axeSlot);
        }
        if (this.timer.hasReached(delay) && InvCleanner.bowSlot >= 36) {
            this.getBestBow(InvCleanner.bowSlot);
        }
        if (this.timer.hasReached(delay) && InvCleanner.blocksSlot >= 36) {
            this.setBlocks(InvCleanner.blocksSlot);
        }
        if (this.timer.hasReached(delay) && InvCleanner.appleSlot >= 36) {
            this.setApple(InvCleanner.appleSlot);
        }
        if (this.timer.hasReached(delay)) {
            for (int i = 9; i < 45; ++i) {
                if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                    final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                    if (this.shouldDrop(is, i)) {
                        this.drop(i);
                        this.timer.reset();
                        if (delay > 0L) {
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public boolean isBestBow(final ItemStack stack) {
        final float damage = this.getDamage(stack);
        for (int i = 9; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                if (this.getDamage(is) > damage && is.getItem() instanceof ItemBow) {
                    return false;
                }
            }
        }
        return stack.getItem() instanceof ItemBow;
    }
    
    public void getBestBow(final int slot) {
        for (int i = 9; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                if (this.isBestBow(is) && this.getDamage(is) > 0.0f && is.getItem() instanceof ItemBow) {
                    this.swap(i, slot - 36);
                    break;
                }
            }
        }
    }
    
    public void setApple(final int slot) {
        for (int i = 0; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemAppleGold) {
                    this.swap(i, slot - 36);
                    break;
                }
            }
        }
    }
    
    public void setBlocks(final int slot) {
        int blockCount = 0;
        final int s = 0;
        if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(slot).getStack().getItem() instanceof ItemBlock) {
            return;
        }
        for (int i = 0; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && blockCount < is.getCount()) {
                    blockCount = is.getCount();
                    this.swap(i, slot - 36);
                    break;
                }
            }
        }
    }
    
    public void shiftClick(final int slot) {
        this.mc.playerController.windowClick(Wrapper.INSTANCE.player().inventoryContainer.windowId, slot, 0, ClickType.PICKUP, (EntityPlayer)Wrapper.INSTANCE.player());
    }
    
    public void swap(final int slot1, final int hotbarSlot) {
        this.mc.playerController.windowClick(Wrapper.INSTANCE.player().inventoryContainer.windowId, slot1, hotbarSlot, ClickType.SWAP, (EntityPlayer)Wrapper.INSTANCE.player());
    }
    
    public void drop(final int slot) {
        this.mc.playerController.windowClick(Wrapper.INSTANCE.player().inventoryContainer.windowId, slot, 1, ClickType.THROW, (EntityPlayer)Wrapper.INSTANCE.player());
    }
    
    public boolean isBestWeapon(final ItemStack stack) {
        final float damage = this.getDamage(stack);
        for (int i = 9; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                if (this.getDamage(is) > damage && is.getItem() instanceof ItemSword) {
                    return false;
                }
            }
        }
        return stack.getItem() instanceof ItemSword;
    }
    
    public void getBestWeapon(final int slot) {
        for (int i = 9; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                if (this.isBestWeapon(is) && this.getDamage(is) > 0.0f && is.getItem() instanceof ItemSword) {
                    this.swap(i, slot - 36);
                    break;
                }
            }
        }
    }
    
    private float getDamage(final ItemStack stack) {
        float damage = 0.0f;
        final Item item = stack.getItem();
        if (item instanceof ItemTool) {
            final ItemTool tool = (ItemTool)item;
            damage += tool.getDamage(stack);
        }
        if (item instanceof ItemSword) {
            final ItemSword sword = (ItemSword)item;
            damage += sword.getAttackDamage();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(16), stack) * 1.25f + EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(20), stack) * 0.01f;
        return damage;
    }
    
    public boolean shouldDrop(final ItemStack stack, final int slot) {
        return !stack.getDisplayName().toLowerCase().contains("(right click)") && !stack.getDisplayName().toLowerCase().contains("¡ìk||") && (slot != InvCleanner.weaponSlot || !this.isBestWeapon(Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.weaponSlot).getStack())) && (slot != InvCleanner.pickaxeSlot || !this.isBestPickaxe(Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.pickaxeSlot).getStack()) || InvCleanner.pickaxeSlot < 0) && (slot != InvCleanner.axeSlot || !this.isBestAxe(Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.axeSlot).getStack()) || InvCleanner.axeSlot < 0) && (slot != InvCleanner.shovelSlot || !this.isBestShovel(Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.shovelSlot).getStack()) || InvCleanner.shovelSlot < 0) && (slot != InvCleanner.bowSlot || !this.isBestBow(Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.bowSlot).getStack()) || InvCleanner.bowSlot < 0) && ((stack.getItem() instanceof ItemBlock && this.getBlockCount() > 128) || (stack.getItem() instanceof ItemFood && !(stack.getItem() instanceof ItemAppleGold)) || (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor) || (!stack.getItem().getTranslationKey().contains("arrow") && (stack.getItem().getTranslationKey().contains("tnt") || stack.getItem().getTranslationKey().contains("stick") || stack.getItem().getTranslationKey().contains("egg") || stack.getItem().getTranslationKey().contains("string") || stack.getItem().getTranslationKey().contains("cake") || stack.getItem().getTranslationKey().contains("mushroom") || stack.getItem().getTranslationKey().contains("flint") || stack.getItem().getTranslationKey().contains("compass") || stack.getItem().getTranslationKey().contains("dyePowder") || stack.getItem().getTranslationKey().contains("feather") || stack.getItem().getTranslationKey().contains("bucket") || (stack.getItem().getTranslationKey().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) || stack.getItem().getTranslationKey().contains("snow") || stack.getItem().getTranslationKey().contains("fish") || stack.getItem().getTranslationKey().contains("enchant") || stack.getItem().getTranslationKey().contains("exp") || stack.getItem().getTranslationKey().contains("shears") || stack.getItem().getTranslationKey().contains("anvil") || stack.getItem().getTranslationKey().contains("torch") || stack.getItem().getTranslationKey().contains("seeds") || stack.getItem().getTranslationKey().contains("leather") || stack.getItem().getTranslationKey().contains("reeds") || stack.getItem().getTranslationKey().contains("skull") || stack.getItem().getTranslationKey().contains("record") || stack.getItem().getTranslationKey().contains("snowball") || stack.getItem() instanceof ItemGlassBottle || stack.getItem().getTranslationKey().contains("piston"))));
    }
    
    public ArrayList<Integer> getWhitelistedItem() {
        return this.whitelistedItems;
    }
    
    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock) {
                    blockCount += is.getCount();
                }
            }
        }
        return blockCount;
    }
    
    private void getBestPickaxe(final int slot) {
        for (int i = 9; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                if (this.isBestPickaxe(is) && InvCleanner.pickaxeSlot != i && !this.isBestWeapon(is)) {
                    if (!Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.pickaxeSlot).getHasStack()) {
                        this.swap(i, InvCleanner.pickaxeSlot - 36);
                        this.timer.reset();
                        return;
                    }
                    if (!this.isBestPickaxe(Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.pickaxeSlot).getStack())) {
                        this.swap(i, InvCleanner.pickaxeSlot - 36);
                        this.timer.reset();
                        return;
                    }
                }
            }
        }
    }
    
    private void getBestShovel(final int slot) {
        for (int i = 9; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                if (this.isBestShovel(is) && InvCleanner.shovelSlot != i && !this.isBestWeapon(is)) {
                    if (!Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.shovelSlot).getHasStack()) {
                        this.swap(i, InvCleanner.shovelSlot - 36);
                        this.timer.reset();
                        return;
                    }
                    if (!this.isBestShovel(Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.shovelSlot).getStack())) {
                        this.swap(i, InvCleanner.shovelSlot - 36);
                        this.timer.reset();
                        return;
                    }
                }
            }
        }
    }
    
    private void getBestAxe(final int slot) {
        for (int i = 9; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                if (this.isBestAxe(is) && InvCleanner.axeSlot != i && !this.isBestWeapon(is)) {
                    if (!Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.axeSlot).getHasStack()) {
                        this.swap(i, InvCleanner.axeSlot - 36);
                        this.timer.reset();
                        return;
                    }
                    if (!this.isBestAxe(Wrapper.INSTANCE.player().inventoryContainer.getSlot(InvCleanner.axeSlot).getStack())) {
                        this.swap(i, InvCleanner.axeSlot - 36);
                        this.timer.reset();
                        return;
                    }
                }
            }
        }
    }
    
    private boolean isBestPickaxe(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        final float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                if (this.getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isBestShovel(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        final float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                if (this.getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isBestAxe(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        final float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                if (this.getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !this.isBestWeapon(stack)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private float getToolEffect(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        final String name = item.getTranslationKey();
        final ItemTool tool = (ItemTool)item;
        float value = 1.0f;
        if (item instanceof ItemPickaxe) {
            value = tool.getDestroySpeed(stack, Blocks.STONE.getDefaultState());
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        else if (item instanceof ItemSpade) {
            value = tool.getDestroySpeed(stack, Blocks.DIRT.getDefaultState());
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        else {
            if (!(item instanceof ItemAxe)) {
                return 1.0f;
            }
            value = tool.getDestroySpeed(stack, Blocks.LOG.getDefaultState());
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(32), stack) * 0.0075);
        value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(34), stack) / 100.0);
        return value;
    }
    
    boolean invContainsType(final int type) {
        for (int i = 9; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (item instanceof ItemArmor) {
                    final ItemArmor armor = (ItemArmor)item;
                    if (type == armor.armorType.getIndex()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    static {
        InvCleanner.weaponSlot = 0;
        InvCleanner.pickaxeSlot = 0;
        InvCleanner.axeSlot = 0;
        InvCleanner.shovelSlot = 0;
        InvCleanner.blocksSlot = 0;
        InvCleanner.appleSlot = 0;
        InvCleanner.bowSlot = 0;
    }
}
