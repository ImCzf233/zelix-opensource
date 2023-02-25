package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraft.client.settings.*;
import zelix.eventapi.event.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.world.*;
import net.minecraftforge.client.event.*;
import zelix.utils.hooks.visual.*;
import org.lwjgl.input.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.item.*;
import net.minecraft.block.state.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import java.util.*;
import zelix.utils.*;
import net.minecraft.block.*;

public class Scaffold extends Hack
{
    public ModeValue mode;
    public TimerUtils timer;
    public BlockData blockData;
    boolean isBridging;
    BlockPos blockDown;
    public static float[] facingCam;
    float startYaw;
    float startPitch;
    BooleanValue noSprint;
    BooleanValue safeWalk;
    BooleanValue test;
    EventPlayerPre ep;
    float yaw;
    float pitch;
    public int godBridgeTimer;
    TimerUtils timer1;
    
    public Scaffold() {
        super("Scaffold", HackCategory.PLAYER);
        this.isBridging = false;
        this.blockDown = null;
        this.startYaw = 0.0f;
        this.startPitch = 0.0f;
        this.ep = null;
        this.timer1 = new TimerUtils();
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("AAC", false), new Mode("Simple", true), new Mode("GodBridge", false) });
        this.noSprint = new BooleanValue("NoSprint", Boolean.valueOf(false));
        this.safeWalk = new BooleanValue("SafeWalk", Boolean.valueOf(false));
        this.test = new BooleanValue("DEBUG", Boolean.valueOf(false));
        this.addValue(this.mode, this.noSprint, this.safeWalk, this.test);
        this.timer = new TimerUtils();
    }
    
    @Override
    public String getDescription() {
        return "Automatically places blocks below your feet.";
    }
    
    @Override
    public void onDisable() {
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketEntityAction((Entity)Wrapper.INSTANCE.player(), CPacketEntityAction.Action.STOP_SNEAKING));
        Scaffold.facingCam = null;
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        this.blockDown = null;
        Scaffold.facingCam = null;
        this.isBridging = false;
        this.startYaw = 0.0f;
        this.startPitch = 0.0f;
        this.timer1.reset();
        if (this.mode.getMode("AAC").isToggled() && Wrapper.INSTANCE.mcSettings().keyBindBack.isKeyDown()) {
            KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindBack.getKeyCode(), false);
        }
        super.onEnable();
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void onCameraSetup(final EntityViewRenderEvent.CameraSetup event) {
        if (this.mode.getMode("Simple").isToggled() && this.mode.getMode("AAC").isToggled() && event.getEntity() == Wrapper.INSTANCE.player()) {
            if (this.startYaw == 0.0f || this.startPitch == 0.0f) {
                return;
            }
            event.setYaw(this.startYaw);
            event.setPitch(this.startPitch - 70.0f);
            Scaffold.facingCam = new float[] { this.startYaw - 180.0f, this.startPitch - 70.0f };
        }
        super.onCameraSetup(event);
    }
    
    @Override
    public void onPlayerEventPost(final EventPlayerPost event) {
        event.setYaw(this.yaw);
        event.setPitch(this.pitch);
    }
    
    @Override
    public void onPlayerEventPre(final EventPlayerPre event) {
        this.ep = event;
        final BlockPos underPos = new BlockPos(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY - 1.0, Wrapper.INSTANCE.player().posZ);
        final Block underBlock = Wrapper.INSTANCE.world().getBlockState(underPos).getBlock();
        final BlockData blockdata = this.getBlockData(underPos);
        this.yaw = this.aimAtLocation(blockdata.position.getX(), blockdata.position.getY(), blockdata.position.getZ(), blockdata.face)[0];
        this.pitch = this.aimAtLocation(blockdata.position.getX(), blockdata.position.getY(), blockdata.position.getZ(), blockdata.face)[1];
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        if (this.mode.getMode("AAC").isToggled()) {
            this.AAC();
            this.godBridgeTimer = 0;
        }
        else if (this.mode.getMode("Simple").isToggled()) {
            this.ep.setYaw(this.yaw);
            this.ep.setPitch(this.pitch);
            Wrapper.INSTANCE.player().setRotationYawHead(this.yaw);
            Wrapper.INSTANCE.player().renderYawOffset = this.yaw;
            if (this.test.getValue()) {
                ChatUtils.message("True:" + Integer.toString((int)Wrapper.INSTANCE.player().rotationPitch) + "  Server:" + Integer.toString((int)this.ep.getPitch()));
                ChatUtils.message("True:" + Integer.toString((int)Wrapper.INSTANCE.player().rotationYaw) + "  Server:" + Integer.toString((int)this.ep.getYaw()));
            }
            if (this.safeWalk.getValue() && (SafeWalk.getBlockUnderPlayer((EntityPlayer)player) instanceof BlockAir || SafeWalk.getBlockUnderPlayer((EntityPlayer)player) instanceof BlockLiquid) && player.onGround && player.getLookVec().y < -0.666) {
                player.motionX = 0.0;
                player.motionZ = 0.0;
            }
            this.Simple();
            this.godBridgeTimer = 0;
        }
        else if (this.mode.getMode("GodBridge").isToggled()) {
            this.GodBridge();
        }
        if (this.noSprint.getValue()) {
            Wrapper.INSTANCE.player().setSprinting(false);
        }
        if (this.safeWalk.getValue() && SafeWalk.getBlockUnderPlayer((EntityPlayer)player) instanceof BlockAir && player.onGround && player.motionY < 0.1) {
            player.motionX = 0.0;
            player.motionZ = 0.0;
        }
        super.onClientTick(event);
    }
    
    private float[] aimAtLocation(final double x, final double y, final double z, final EnumFacing facing) {
        final EntitySnowball temp = new EntitySnowball((World)Wrapper.INSTANCE.world());
        temp.field_70165_t = x + 0.5;
        temp.field_70163_u = y - 2.5;
        temp.field_70161_v = z + 0.5;
        final EntitySnowball entitySnowball4;
        final EntitySnowball entitySnowball = entitySnowball4 = temp;
        entitySnowball4.field_70165_t += facing.getDirectionVec().getX() * 0.23;
        final EntitySnowball entitySnowball5;
        final EntitySnowball entitySnowball2 = entitySnowball5 = temp;
        entitySnowball5.field_70163_u += facing.getDirectionVec().getY() * 0.23;
        final EntitySnowball entitySnowball6;
        final EntitySnowball entitySnowball3 = entitySnowball6 = temp;
        entitySnowball6.field_70161_v += facing.getDirectionVec().getZ() * 0.23;
        return this.aimAtLocation(temp.field_70165_t, temp.field_70163_u, temp.field_70161_v);
    }
    
    private float[] aimAtLocation(final double positionX, final double positionY, final double positionZ) {
        final double x = positionX - Wrapper.INSTANCE.player().posX;
        final double y = positionY - Wrapper.INSTANCE.player().posY;
        final double z = positionZ - Wrapper.INSTANCE.player().posZ;
        final double distance = MathHelper.sqrt(x * x + z * z);
        return new float[] { (float)(Math.atan2(z, x) * 180.0 / 3.14) - 90.0f, (float)(-(Math.atan2(y, distance) * 180.0 / 3.14)) };
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (this.blockDown != null) {
            RenderUtils.drawBlockESP(this.blockDown, 1.0f, 1.0f, 1.0f);
            if (this.mode.getMode("AAC").isToggled()) {
                BlockPos blockDown2 = new BlockPos((Entity)Wrapper.INSTANCE.player()).down();
                BlockPos blockDown3 = new BlockPos((Entity)Wrapper.INSTANCE.player()).down();
                if (Wrapper.INSTANCE.player().getHorizontalFacing() == EnumFacing.EAST) {
                    blockDown2 = new BlockPos((Entity)Wrapper.INSTANCE.player()).down().west();
                    blockDown3 = new BlockPos((Entity)Wrapper.INSTANCE.player()).down().west(2);
                }
                else if (Wrapper.INSTANCE.player().getHorizontalFacing() == EnumFacing.NORTH) {
                    blockDown2 = new BlockPos((Entity)Wrapper.INSTANCE.player()).down().south();
                    blockDown3 = new BlockPos((Entity)Wrapper.INSTANCE.player()).down().south(2);
                }
                else if (Wrapper.INSTANCE.player().getHorizontalFacing() == EnumFacing.SOUTH) {
                    blockDown2 = new BlockPos((Entity)Wrapper.INSTANCE.player()).down().north();
                    blockDown3 = new BlockPos((Entity)Wrapper.INSTANCE.player()).down().north(2);
                }
                else if (Wrapper.INSTANCE.player().getHorizontalFacing() == EnumFacing.WEST) {
                    blockDown2 = new BlockPos((Entity)Wrapper.INSTANCE.player()).down().east();
                    blockDown3 = new BlockPos((Entity)Wrapper.INSTANCE.player()).down().east(2);
                }
                RenderUtils.drawBlockESP(blockDown2, 1.0f, 0.0f, 0.0f);
                RenderUtils.drawBlockESP(blockDown3, 1.0f, 0.0f, 0.0f);
            }
        }
        super.onRenderWorldLast(event);
    }
    
    void GodBridge() {
        if (this.godBridgeTimer > 0) {
            --this.godBridgeTimer;
        }
        if (Wrapper.INSTANCE.world() == null || Wrapper.INSTANCE.player() == null) {
            return;
        }
        final WorldClient world = Wrapper.INSTANCE.world();
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        final RayTraceResult movingObjectPosition = player.rayTrace((double)Wrapper.INSTANCE.controller().getBlockReachDistance(), 1.0f);
        boolean isKeyUseDown = false;
        final int keyCode = Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode();
        if (keyCode >= 0) {
            isKeyUseDown = Keyboard.isKeyDown(keyCode);
        }
        else {
            isKeyUseDown = Mouse.isButtonDown(keyCode + 100);
        }
        if (movingObjectPosition != null && movingObjectPosition.typeOfHit == RayTraceResult.Type.BLOCK && movingObjectPosition.sideHit == EnumFacing.UP && isKeyUseDown) {
            final ItemStack itemstack = player.inventory.getCurrentItem();
            final int i = (itemstack != null) ? itemstack.getMaxStackSize() : 0;
            if (itemstack != null && itemstack.getItem() instanceof ItemBlock) {
                final ItemBlock itemblock = (ItemBlock)itemstack.getItem();
                if (!itemblock.canPlaceBlockOnSide((World)world, movingObjectPosition.getBlockPos(), movingObjectPosition.sideHit, (EntityPlayer)player, itemstack)) {
                    final BlockPos blockPos = movingObjectPosition.getBlockPos();
                    final IBlockState blockState = world.getBlockState(blockPos);
                    final AxisAlignedBB axisalignedbb = blockState.getBlock().getSelectedBoundingBox(BlockUtils.getState(blockPos), (World)world, blockPos);
                    if (axisalignedbb == null || world.isAirBlock(blockPos)) {
                        return;
                    }
                    Vec3d targetVec3 = null;
                    final Vec3d eyeVec3 = player.getPositionEyes(1.0f);
                    final double x1 = axisalignedbb.minX;
                    final double x2 = axisalignedbb.maxX;
                    final double y1 = axisalignedbb.minY;
                    final double y2 = axisalignedbb.maxY;
                    final double z1 = axisalignedbb.minZ;
                    final double z2 = axisalignedbb.maxZ;
                    class Data implements Comparable<Data>
                    {
                        public BlockPos blockPos = blockPos.west();
                        public EnumFacing enumFacing = EnumFacing.WEST;
                        public double cost = xCost;
                        
                        public Data(final EnumFacing enumFacing, final double cost) {
                        }
                        
                        @Override
                        public int compareTo(final Data data) {
                            return (this.cost - data.cost > 0.0) ? -1 : ((this.cost - data.cost < 0.0) ? 1 : 0);
                        }
                    }
                    final List<Data> list = new ArrayList<Data>();
                    if (x1 > eyeVec3.x || eyeVec3.x > x2 || y1 > eyeVec3.y || eyeVec3.y > y2 || z1 > eyeVec3.z || eyeVec3.z > z2) {
                        final double xCost = Math.abs(eyeVec3.x - 0.5 * (axisalignedbb.minX + axisalignedbb.maxX));
                        final double yCost = Math.abs(eyeVec3.y - 0.5 * (axisalignedbb.minY + axisalignedbb.maxY));
                        final double zCost = Math.abs(eyeVec3.z - 0.5 * (axisalignedbb.minZ + axisalignedbb.maxZ));
                        final double sumCost = xCost + yCost + zCost;
                        if (eyeVec3.x < x1) {
                            list.add(new Data(EnumFacing.WEST));
                        }
                        else if (eyeVec3.x > x2) {
                            list.add(new Data(EnumFacing.EAST));
                        }
                        if (eyeVec3.z < z1) {
                            list.add(new Data(EnumFacing.NORTH));
                        }
                        else if (eyeVec3.z > z2) {
                            list.add(new Data(EnumFacing.SOUTH));
                        }
                        Collections.sort(list);
                        final double border = 0.05;
                        double x3 = MathHelper.clamp(eyeVec3.x, x1 + border, x2 - border);
                        double y3 = MathHelper.clamp(eyeVec3.y, y1 + border, y2 - border);
                        double z3 = MathHelper.clamp(eyeVec3.z, z1 + border, z2 - border);
                        for (final Data data : list) {
                            if (!world.isAirBlock(data.blockPos)) {
                                continue;
                            }
                            if (data.enumFacing == EnumFacing.WEST || data.enumFacing == EnumFacing.EAST) {
                                x3 = MathHelper.clamp(eyeVec3.x, x1, x2);
                            }
                            else if (data.enumFacing == EnumFacing.UP || data.enumFacing == EnumFacing.DOWN) {
                                y3 = MathHelper.clamp(eyeVec3.y, y1, y2);
                            }
                            else {
                                z3 = MathHelper.clamp(eyeVec3.z, z1, z2);
                            }
                            targetVec3 = new Vec3d(x3, y3, z3);
                            break;
                        }
                        if (targetVec3 != null) {
                            final double d0 = targetVec3.x - eyeVec3.x;
                            final double d2 = targetVec3.y - eyeVec3.y;
                            final double d3 = targetVec3.z - eyeVec3.z;
                            final double d4 = MathHelper.sqrt(d0 * d0 + d3 * d3);
                            final float f = (float)(MathHelper.atan2(d3, d0) * 180.0 / 3.141592653589793) - 90.0f;
                            final float f2 = (float)(-(MathHelper.atan2(d2, d4) * 180.0 / 3.141592653589793));
                            final float f3 = player.rotationYaw;
                            final float f4 = player.rotationPitch;
                            player.rotationYaw = f;
                            player.rotationPitch = f2;
                            final RayTraceResult movingObjectPosition2 = player.rayTrace((double)Wrapper.INSTANCE.controller().getBlockReachDistance(), 1.0f);
                            if (movingObjectPosition2.typeOfHit == RayTraceResult.Type.BLOCK && movingObjectPosition2.getBlockPos().getX() == blockPos.getX() && movingObjectPosition2.getBlockPos().getY() == blockPos.getY() && movingObjectPosition2.getBlockPos().getZ() == blockPos.getZ()) {
                                if (Wrapper.INSTANCE.controller().processRightClickBlock(player, Wrapper.INSTANCE.world(), blockPos, movingObjectPosition2.sideHit, movingObjectPosition2.hitVec, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS) {
                                    player.swingArm(EnumHand.MAIN_HAND);
                                }
                                if (itemstack != null) {
                                    if (itemstack.getMaxStackSize() == 0) {
                                        player.inventory.mainInventory.set(player.inventory.currentItem, (Object)null);
                                    }
                                    else if (itemstack.getMaxStackSize() != i || Wrapper.INSTANCE.controller().isInCreativeMode()) {
                                        Wrapper.INSTANCE.mc().entityRenderer.itemRenderer.resetEquippedProgress(EnumHand.MAIN_HAND);
                                    }
                                }
                            }
                            player.rotationYaw = f3;
                            player.rotationPitch = f4;
                            final double pitchDelta = 2.5;
                            final double targetPitch = 75.5;
                            if (targetPitch - pitchDelta < player.rotationPitch && player.rotationPitch < targetPitch + pitchDelta) {
                                double mod = player.rotationYaw % 45.0;
                                if (mod < 0.0) {
                                    mod += 45.0;
                                }
                                final double delta = 5.0;
                                if (mod < delta) {
                                    final EntityPlayerSP entityPlayerSP = player;
                                    entityPlayerSP.rotationYaw -= (float)mod;
                                    player.rotationPitch = (float)targetPitch;
                                }
                                else if (45.0 - mod < delta) {
                                    final EntityPlayerSP entityPlayerSP2 = player;
                                    entityPlayerSP2.rotationYaw += (float)(45.0 - mod);
                                    player.rotationPitch = (float)targetPitch;
                                }
                            }
                            ReflectionHelper.setPrivateValue((Class)Minecraft.class, (Object)Wrapper.INSTANCE.mc(), (Object)new Integer(1), new String[] { "rightClickDelayTimer", "field_71467_ac" });
                            this.godBridgeTimer = 10;
                        }
                    }
                }
            }
        }
    }
    
    void AAC() {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        int oldSlot = -1;
        if (!this.check()) {
            if (this.isBridging) {
                KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindSneak.getKeyCode(), BlockUtils.isBlockMaterial(new BlockPos((Entity)player).down(), Blocks.AIR));
                this.isBridging = false;
                if (oldSlot != -1) {
                    player.inventory.currentItem = oldSlot;
                }
            }
            this.startYaw = 0.0f;
            this.startPitch = 0.0f;
            Scaffold.facingCam = null;
            this.blockDown = null;
            return;
        }
        this.startYaw = Wrapper.INSTANCE.player().rotationYaw;
        this.startPitch = Wrapper.INSTANCE.player().rotationPitch;
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindLeft.getKeyCode(), false);
        this.blockDown = new BlockPos((Entity)player).down();
        float r1 = new Random().nextFloat();
        if (r1 == 1.0f) {
            --r1;
        }
        final int newSlot = this.findSlotWithBlock();
        if (newSlot == -1) {
            return;
        }
        oldSlot = player.inventory.currentItem;
        player.inventory.currentItem = newSlot;
        player.rotationPitch = Utils.updateRotation(player.rotationPitch, 82.0f - r1, 15.0f);
        final int currentCPS = Utils.random(3, 4);
        if (this.timer.isDelay(1000 / currentCPS)) {
            RobotUtils.clickMouse(1);
            Utils.swingMainHand();
            this.timer.setLastMS();
        }
        this.isBridging = true;
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindSneak.getKeyCode(), BlockUtils.isBlockMaterial(new BlockPos((Entity)player).down(), Blocks.AIR));
    }
    
    void Simple() {
        this.blockDown = new BlockPos((Entity)Wrapper.INSTANCE.player()).down();
        if (!BlockUtils.getBlock(this.blockDown).getMaterial(BlockUtils.getBlock(this.blockDown).getDefaultState()).isReplaceable()) {
            return;
        }
        final int newSlot = this.findSlotWithBlock();
        if (newSlot == -1) {
            return;
        }
        final int oldSlot = Wrapper.INSTANCE.inventory().currentItem;
        Wrapper.INSTANCE.inventory().currentItem = newSlot;
        if (this.timer1.hasReached(130.0f)) {
            Utils.placeBlockScaffold(this.blockDown);
            Wrapper.INSTANCE.inventory().currentItem = oldSlot;
            this.timer1.reset();
        }
    }
    
    public int findSlotWithBlock() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.INSTANCE.inventory().getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                final Block block = Block.getBlockFromItem(stack.getItem()).getDefaultState().getBlock();
                if (block.isFullBlock(BlockUtils.getBlock(this.blockDown).getDefaultState()) && block != Blocks.SAND && block != Blocks.GRAVEL) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    boolean check() {
        final RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        final ItemStack stack = player.inventory.getCurrentItem();
        return object != null && stack != null && object.typeOfHit == RayTraceResult.Type.BLOCK && player.rotationPitch > 70.0f && player.onGround && !player.isOnLadder() && !player.isInLava() && !player.isInWater() && Wrapper.INSTANCE.mcSettings().keyBindBack.isKeyDown();
    }
    
    private boolean isPosSolid(final BlockPos pos) {
        final Block block = Wrapper.INSTANCE.world().getBlockState(pos).getBlock();
        return (block.getMaterial((IBlockState)null).isSolid() || !block.isTranslucent((IBlockState)null) || block.isFullCube((IBlockState)null) || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial((IBlockState)null).isLiquid() && !(block instanceof BlockContainer);
    }
    
    public float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Wrapper.INSTANCE.player().posX;
        final double zDiff = z - Wrapper.INSTANCE.player().posZ;
        final double yDiff = y - Wrapper.INSTANCE.player().posY - 1.2;
        final double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    private BlockData getBlockData(final BlockPos pos) {
        if (this.isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos2 = pos.add(-1, 0, 0);
        if (this.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos3 = pos.add(1, 0, 0);
        if (this.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos4 = pos.add(0, 0, 1);
        if (this.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos5 = pos.add(0, 0, -1);
        if (this.isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos6 = pos.add(-2, 0, 0);
        if (this.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos7 = pos.add(2, 0, 0);
        if (this.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos8 = pos.add(0, 0, 2);
        if (this.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos9 = pos.add(0, 0, -2);
        if (this.isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos10 = pos.add(0, -1, 0);
        if (this.isPosSolid(pos10.add(0, -1, 0))) {
            return new BlockData(pos10.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos10.add(-1, 0, 0))) {
            return new BlockData(pos10.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos10.add(1, 0, 0))) {
            return new BlockData(pos10.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos10.add(0, 0, 1))) {
            return new BlockData(pos10.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos10.add(0, 0, -1))) {
            return new BlockData(pos10.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos11 = pos10.add(1, 0, 0);
        if (this.isPosSolid(pos11.add(0, -1, 0))) {
            return new BlockData(pos11.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos11.add(-1, 0, 0))) {
            return new BlockData(pos11.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos11.add(1, 0, 0))) {
            return new BlockData(pos11.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos11.add(0, 0, 1))) {
            return new BlockData(pos11.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos11.add(0, 0, -1))) {
            return new BlockData(pos11.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos12 = pos10.add(-1, 0, 0);
        if (this.isPosSolid(pos12.add(0, -1, 0))) {
            return new BlockData(pos12.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos12.add(-1, 0, 0))) {
            return new BlockData(pos12.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos12.add(1, 0, 0))) {
            return new BlockData(pos12.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos12.add(0, 0, 1))) {
            return new BlockData(pos12.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos12.add(0, 0, -1))) {
            return new BlockData(pos12.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos13 = pos10.add(0, 0, 1);
        if (this.isPosSolid(pos13.add(0, -1, 0))) {
            return new BlockData(pos13.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos13.add(-1, 0, 0))) {
            return new BlockData(pos13.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos13.add(1, 0, 0))) {
            return new BlockData(pos13.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos13.add(0, 0, 1))) {
            return new BlockData(pos13.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos13.add(0, 0, -1))) {
            return new BlockData(pos13.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos14 = pos10.add(0, 0, -1);
        if (this.isPosSolid(pos14.add(0, -1, 0))) {
            return new BlockData(pos14.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos14.add(-1, 0, 0))) {
            return new BlockData(pos14.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos14.add(1, 0, 0))) {
            return new BlockData(pos14.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos14.add(0, 0, 1))) {
            return new BlockData(pos14.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos14.add(0, 0, -1))) {
            return new BlockData(pos14.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }
    
    public static float[] getRotations(final BlockPos block, final EnumFacing face) {
        final double x = block.getX() + 0.5 - Wrapper.INSTANCE.player().posX + face.getXOffset() / 2.0;
        final double z = block.getZ() + 0.5 - Wrapper.INSTANCE.player().posZ + face.getZOffset() / 2.0;
        final double y = block.getY() + 0.5;
        final double d1 = Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight() - y;
        final double d2 = MathHelper.sqrt(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 720.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    static {
        Scaffold.facingCam = null;
    }
    
    private class BlockData
    {
        public BlockPos position;
        public EnumFacing face;
        
        private BlockData(final BlockPos position, final EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}
