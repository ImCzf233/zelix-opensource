package zelix.otherhacks.net.wurstclient.forge.utils;

import net.minecraft.client.entity.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;

public class EntityFakePlayer extends EntityOtherPlayerMP
{
    public EntityFakePlayer() {
        super((World)WMinecraft.getWorld(), WMinecraft.getPlayer().getGameProfile());
        this.copyLocationAndAnglesFrom((Entity)WMinecraft.getPlayer());
        this.field_71071_by.copyInventory(WMinecraft.getPlayer().inventory);
        this.getDataManager().set(EntityPlayer.PLAYER_MODEL_FLAG, WMinecraft.getPlayer().getDataManager().get(EntityPlayer.PLAYER_MODEL_FLAG));
        this.field_70759_as = WMinecraft.getPlayer().rotationYawHead;
        this.field_70761_aq = WMinecraft.getPlayer().renderYawOffset;
        this.field_71094_bP = this.posX;
        this.field_71095_bQ = this.posY;
        this.field_71085_bR = this.posZ;
        WMinecraft.getWorld().addEntityToWorld(this.getEntityId(), (Entity)this);
    }
    
    public void resetPlayerPosition() {
        WMinecraft.getPlayer().setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }
    
    public void despawn() {
        WMinecraft.getWorld().removeEntityFromWorld(this.getEntityId());
    }
}
