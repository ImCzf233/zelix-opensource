package zelix.utils;

import net.minecraft.util.math.*;
import net.minecraft.entity.*;

public class RotationUtils
{
    public static Rotation serverRotation;
    
    public static float getYawChange(final float yaw, final double posX, final double posZ) {
        final double deltaX = posX - Wrapper.INSTANCE.player().posX;
        final double deltaZ = posZ - Wrapper.INSTANCE.player().posZ;
        double yawToEntity = 0.0;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        }
        else if (deltaZ != 0.0) {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapDegrees(-(yaw - (float)yawToEntity));
    }
    
    public static float getPitchChange(final float pitch, final Entity entity, final double posY) {
        final double deltaX = entity.posX - Wrapper.INSTANCE.player().posX;
        final double deltaZ = entity.posZ - Wrapper.INSTANCE.player().posZ;
        final double deltaY = posY - 2.2 + entity.getEyeHeight() - Wrapper.INSTANCE.player().posY;
        final double distanceXZ = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapDegrees(pitch - (float)pitchToEntity) - 2.5f;
    }
    
    static {
        RotationUtils.serverRotation = new Rotation(0.0f, 0.0f);
    }
    
    public static class Rotation
    {
        public float yaw;
        public float pitch;
        
        public Rotation(final float yaw, final float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }
        
        public float getYaw() {
            return this.yaw;
        }
        
        public float getPitch() {
            return this.pitch;
        }
    }
}
