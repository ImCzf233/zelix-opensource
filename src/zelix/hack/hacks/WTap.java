package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.event.entity.player.*;
import zelix.utils.*;
import net.minecraft.entity.*;

public class WTap extends Hack
{
    private long lastStep;
    
    public WTap() {
        super("WTap", HackCategory.COMBAT);
        this.lastStep = -1L;
    }
    
    @Override
    public void onAttackEntity(final AttackEntityEvent event) {
        if (!(event.getEntity() instanceof EntityLivingBase) || this.lastStep + 500L > System.currentTimeMillis()) {
            return;
        }
        if (Wrapper.INSTANCE.player().moveForward <= 0.0f) {
            return;
        }
        final EntityLivingBase entity = (EntityLivingBase)event.getEntity();
        if (entity.moveStrafing == 0.0f && Wrapper.INSTANCE.player().getDistance((Entity)entity) <= 3.1f) {
            float yaw = Wrapper.INSTANCE.player().rotationYaw;
            if (Wrapper.INSTANCE.player().moveForward < 0.0f) {
                yaw += 180.0f;
            }
            if (Wrapper.INSTANCE.player().moveStrafing > 0.0f) {
                yaw -= 90.0f * ((Wrapper.INSTANCE.player().moveForward > 0.0f) ? 0.5f : ((Wrapper.INSTANCE.player().moveForward < 0.0f) ? -0.5f : 1.0f));
            }
            if (Wrapper.INSTANCE.player().moveStrafing < 0.0f) {
                yaw += 90.0f * ((Wrapper.INSTANCE.player().moveForward > 0.0f) ? 0.5f : ((Wrapper.INSTANCE.player().moveForward < 0.0f) ? -0.5f : 1.0f));
            }
            final double x = -Math.cos((yaw + 90.0f) * 3.141592653589793 / 180.0) * 0.2;
            final double z = -Math.sin((yaw + 90.0f) * 3.141592653589793 / 180.0) * 0.2;
            if (Math.abs(Wrapper.INSTANCE.player().motionX) <= Math.abs(x)) {
                Wrapper.INSTANCE.player().motionX = x;
            }
            if (Math.abs(Wrapper.INSTANCE.player().motionZ) <= Math.abs(z)) {
                Wrapper.INSTANCE.player().motionZ = z;
            }
            this.lastStep = System.currentTimeMillis();
        }
    }
}
