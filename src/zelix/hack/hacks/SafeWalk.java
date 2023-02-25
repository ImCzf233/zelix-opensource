package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;

public class SafeWalk extends Hack
{
    BooleanValue sneak;
    NumberValue edgeSpeed;
    ModeValue mode;
    
    public SafeWalk() {
        super("SafeWalk", HackCategory.MOVEMENT);
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Eagle", true), new Mode("Blatant", false), new Mode("StopAtEdge", false) });
        this.edgeSpeed = new NumberValue("EdgeSpeed", 0.0, 0.0, 0.1);
        this.addValue(this.mode);
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        double motionX = player.motionX;
        final double motionY = player.motionY;
        double motionZ = player.motionZ;
        if (player.onGround && this.mode.getMode("Blatant").isToggled()) {
            final double v = 1.0;
            while (motionX != 0.0 && Wrapper.INSTANCE.world().getCollisionBoxes((Entity)player, player.getEntityBoundingBox().offset(motionX, -1.0, 0.0)).isEmpty()) {
                if (motionX < v && motionX >= -v) {
                    motionX = 0.0;
                }
                else if (motionX > 0.0) {
                    motionX -= v;
                }
                else {
                    motionX += v;
                }
            }
            while (motionZ != 0.0 && Wrapper.INSTANCE.world().getCollisionBoxes((Entity)player, player.getEntityBoundingBox().offset(0.0, -1.0, motionZ)).isEmpty()) {
                if (motionZ < v && motionZ >= -v) {
                    motionZ = 0.0;
                }
                else if (motionZ > 0.0) {
                    motionZ -= v;
                }
                else {
                    motionZ += v;
                }
            }
            while (motionX != 0.0 && motionZ != 0.0 && Wrapper.INSTANCE.world().getCollisionBoxes((Entity)player, player.getEntityBoundingBox().offset(motionX, -1.0, motionZ)).isEmpty()) {
                motionX = ((motionX < v && motionX >= -v) ? 0.0 : ((motionX > 0.0) ? (motionX -= v) : (motionX += v)));
                if (motionZ < v && motionZ >= -v) {
                    motionZ = 0.0;
                }
                else if (motionZ > 0.0) {
                    motionZ -= v;
                }
                else {
                    motionZ += v;
                }
            }
            event.player.motionX = motionX;
            event.player.motionY = motionY;
            event.player.motionZ = motionZ;
        }
        if ((getBlockUnderPlayer((EntityPlayer)player) instanceof BlockAir || getBlockUnderPlayer((EntityPlayer)player) instanceof BlockLiquid) && player.onGround && player.getLookVec().y < -0.6660000085830688) {
            if (this.mode.getMode("Eagle").isToggled()) {
                KeyBinding.setKeyBindState(Wrapper.INSTANCE.mc().gameSettings.keyBindSneak.getKeyCode(), true);
            }
            else if (this.mode.getMode("StopAtEdge").isToggled()) {
                player.motionX = ((player.motionX > 0.0) ? 1 : -1) * this.edgeSpeed.getValue();
                player.motionZ = ((player.motionZ > 0.0) ? 1 : -1) * this.edgeSpeed.getValue();
            }
        }
        else if (this.mode.getMode("Eagle").isToggled()) {
            KeyBinding.setKeyBindState(Wrapper.INSTANCE.mc().gameSettings.keyBindSneak.getKeyCode(), false);
        }
        super.onPlayerTick(event);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return Wrapper.INSTANCE.world().getBlockState(pos).getBlock();
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer player) {
        return getBlock(new BlockPos(player.posX, player.posY - 1.0, player.posZ));
    }
}
