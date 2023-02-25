package zelix.hack.hacks;

import net.minecraft.block.*;
import zelix.hack.*;
import net.minecraft.util.math.*;
import net.minecraft.block.state.*;
import zelix.eventapi.event.*;
import net.minecraft.client.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import java.lang.reflect.*;
import net.minecraft.init.*;
import zelix.utils.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import java.util.*;

public class AutoMLG extends Hack
{
    private double fallStartY;
    private TimerUtils timer;
    private BlockData blockBelowData;
    private boolean nextPlaceWater;
    private boolean nextRemoveWater;
    EventPlayerPre eu;
    private static List<Block> blacklistedBlocks;
    
    public AutoMLG() {
        super("AutoMLG", HackCategory.PLAYER);
        this.fallStartY = 0.0;
        this.timer = new TimerUtils();
        this.nextPlaceWater = false;
        this.nextRemoveWater = false;
    }
    
    @Override
    public void onPlayerEventPre(final EventPlayerPre event) {
        this.eu = event;
        if (!Wrapper.INSTANCE.player().onGround && Wrapper.INSTANCE.player().motionY < 0.0) {
            if (this.fallStartY < Wrapper.INSTANCE.player().posY) {
                this.fallStartY = Wrapper.INSTANCE.player().posY;
            }
            if (this.fallStartY - Wrapper.INSTANCE.player().posY > 2.0) {
                final double x = Wrapper.INSTANCE.player().posX + Wrapper.INSTANCE.player().motionX * 1.25;
                final double y = Wrapper.INSTANCE.player().posY - Wrapper.INSTANCE.player().getEyeHeight();
                final double z = Wrapper.INSTANCE.player().posZ + Wrapper.INSTANCE.player().motionZ * 1.25;
                final BlockPos blockBelow = new BlockPos(x, y, z);
                final IBlockState blockState = Wrapper.INSTANCE.world().getBlockState(blockBelow);
                final IBlockState underBlockState = Wrapper.INSTANCE.world().getBlockState(blockBelow.down());
                if (!Wrapper.INSTANCE.player().isSneaking() && (blockState.getBlock() == Blocks.AIR || blockState.getBlock() == Blocks.SNOW_LAYER || blockState.getBlock() == Blocks.TALLGRASS) && this.timer.hasReached(100.0f)) {
                    this.timer.reset();
                    this.blockBelowData = this.getBlockData(blockBelow);
                    if (this.blockBelowData != null) {
                        this.nextPlaceWater = true;
                        this.nextRemoveWater = false;
                        final float[] rotations = MoveUtils.getRotationsBlock(this.blockBelowData.position, this.blockBelowData.face);
                        this.eu.setYaw(rotations[0]);
                        this.eu.setPitch(rotations[1]);
                    }
                }
            }
        }
        else {
            this.fallStartY = Wrapper.INSTANCE.player().posY;
        }
        if (this.blockBelowData != null && Wrapper.INSTANCE.player().isInWater()) {
            this.nextRemoveWater = true;
            final float[] rotations2 = MoveUtils.getRotationsBlock(this.blockBelowData.position, this.blockBelowData.face);
            this.eu.setYaw(rotations2[0]);
            this.eu.setPitch(rotations2[1]);
        }
    }
    
    @Override
    public void onPlayerEventPost(final EventPlayerPost event) {
        if (this.blockBelowData != null && this.nextPlaceWater) {
            this.placeWater();
        }
        else if (this.blockBelowData != null && this.nextRemoveWater) {
            this.getWaterBack();
        }
    }
    
    private int swapToItem(final int item) {
        try {
            final Field f = Minecraft.class.getDeclaredField(Mapping.rightClickDelayTimer);
            f.setAccessible(true);
            f.set(Wrapper.INSTANCE.mc(), 2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        final int currentItem = Wrapper.INSTANCE.player().inventory.currentItem;
        Wrapper.INSTANCE.player().connection.sendPacket((Packet)new CPacketHeldItemChange(item - 36));
        Wrapper.INSTANCE.player().inventory.currentItem = item - 36;
        this.mc.playerController.updateController();
        return currentItem;
    }
    
    private void placeWater() {
        for (final Map.Entry<Integer, Item> item : this.getHotbarItems().entrySet()) {
            if (item.getValue().equals(Items.WATER_BUCKET)) {
                final int currentItem = this.swapToItem(item.getKey());
                Utils.placeBlockScaffold(this.blockBelowData.position);
                Wrapper.INSTANCE.player().inventory.currentItem = currentItem;
                this.mc.playerController.updateController();
                break;
            }
        }
        this.nextPlaceWater = false;
    }
    
    private void getWaterBack() {
        for (final Map.Entry<Integer, Item> item : this.getHotbarItems().entrySet()) {
            if (item.getValue().equals(Items.BUCKET)) {
                final int currentItem = this.swapToItem(item.getKey());
                Utils.placeBlockScaffold(this.blockBelowData.position);
                Wrapper.INSTANCE.player().inventory.currentItem = currentItem;
                this.mc.playerController.updateController();
                break;
            }
        }
        this.blockBelowData = null;
        this.nextRemoveWater = false;
    }
    
    private HashMap<Integer, Item> getHotbarItems() {
        final HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        for (int i = 36; i < 45; ++i) {
            if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack itemStack = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
                items.put(i, itemStack.getItem());
            }
        }
        return items;
    }
    
    private BlockData getBlockData(final BlockPos pos) {
        return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
    }
    
    static {
        AutoMLG.blacklistedBlocks = Arrays.asList(Blocks.AIR, Blocks.WATER, Blocks.FLOWING_WATER, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.ENCHANTING_TABLE, Blocks.CARPET, Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE, Blocks.IRON_ORE, Blocks.SNOW_LAYER, Blocks.ICE, Blocks.PACKED_ICE, Blocks.COAL_ORE, Blocks.EMERALD_ORE);
    }
}
