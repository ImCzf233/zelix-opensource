package zelix.hack.hacks;

import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import zelix.hack.*;
import net.minecraftforge.event.entity.player.*;
import zelix.utils.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.util.math.*;
import net.minecraftforge.client.event.*;
import java.awt.*;
import net.minecraft.client.*;
import zelix.utils.hooks.visual.*;
import net.minecraft.client.network.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;

public class EnemyInfo extends Hack
{
    private ResourceLocation hpbar;
    private boolean show;
    private String enemyNickname;
    private double enemyHp;
    private double enemyDistance;
    private Entity entity;
    private EntityPlayer entityPlayer;
    private int hpbarwidth;
    
    public EnemyInfo() {
        super("TargetHUD", HackCategory.VISUAL);
        this.hpbar = new ResourceLocation("healthbar.png");
    }
    
    @Override
    public void onAttackEntity(final AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPlayer) {
            this.entityPlayer = (EntityPlayer)event.getTarget();
            this.enemyNickname = this.entityPlayer.getName();
            this.enemyHp = this.entityPlayer.getHealth();
            this.enemyDistance = this.entityPlayer.getDistance((Entity)Wrapper.INSTANCE.mc().player);
            this.show = true;
        }
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent e) {
        final RayTraceResult objectMouseOver = Wrapper.INSTANCE.mc().objectMouseOver;
        if (objectMouseOver != null) {
            if (objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                this.entity = objectMouseOver.entityHit;
                if (this.entity instanceof EntityPlayer) {
                    this.entityPlayer = (EntityPlayer)this.entity;
                    this.enemyNickname = this.entityPlayer.getName();
                    this.enemyHp = this.entityPlayer.getHealth();
                    this.enemyDistance = this.entityPlayer.getDistance((Entity)Wrapper.INSTANCE.mc().player);
                    this.show = true;
                }
            }
            else {
                this.show = false;
            }
        }
        else {
            this.show = false;
        }
    }
    
    @Override
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        if (this.entityPlayer == null) {
            return;
        }
        final NetworkPlayerInfo playerinfo = Wrapper.INSTANCE.mc().getConnection().getPlayerInfo(this.entityPlayer.getUniqueID());
        final int max = (int)this.entityPlayer.getMaxHealth();
        int color;
        if (this.entityPlayer.getHealth() >= max - 4) {
            color = new Color(120, 205, 10).getRGB();
        }
        else if (this.entityPlayer.getHealth() >= max - 8) {
            color = new Color(180, 205, 10).getRGB();
        }
        else if (this.entityPlayer.getHealth() >= max - 12) {
            color = new Color(180, 155, 10).getRGB();
        }
        else if (this.entityPlayer.getHealth() >= max - 16) {
            color = new Color(200, 100, 10).getRGB();
        }
        else {
            color = new Color(230, 50, 10).getRGB();
        }
        final int width = (int)(100.0f * (this.entityPlayer.getHealth() * 5.0f / 100.0f));
        final RenderGameOverlayEvent.ElementType type = event.getType();
        event.getType();
        if (!type.equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            return;
        }
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final FontRenderer fr = Wrapper.INSTANCE.fontRenderer();
        if (this.show && Wrapper.INSTANCE.mc().world != null && Wrapper.INSTANCE.mc().player != null) {
            RenderUtils.drawRect(sr.getScaledWidth() / 2 + 170, sr.getScaledHeight() / 2 + 20, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 76, new Color(75, 75, 75).getRGB());
            Gui.drawRect(sr.getScaledWidth() / 2 + 170, sr.getScaledHeight() / 2 + 20, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 76, new Color(0, 0, 0, 110).getRGB());
            this.drawHead(playerinfo.getLocationSkin(), 50, 50);
            fr.drawStringWithShadow(this.enemyNickname, (float)(sr.getScaledWidth() / 2 + 60), (float)(sr.getScaledWidth() / 2 - 65), -1);
            RenderUtils.drawRect(sr.getScaledWidth() / 2 + 60, sr.getScaledHeight() / 2 + 40, sr.getScaledWidth() / 2 + 160, sr.getScaledHeight() / 2 + 50, new Color(80, 80, 80).getRGB());
            RenderUtils.drawRect(sr.getScaledWidth() / 2 + 60, sr.getScaledHeight() / 2 + 40, sr.getScaledWidth() / 2 + 60 + width, sr.getScaledHeight() / 2 + 50, color);
            fr.drawStringWithShadow("HP:" + Integer.toString((int)this.entityPlayer.getHealth()) + "/" + Integer.toString(max), (float)(sr.getScaledWidth() / 2 + 60), (float)(sr.getScaledHeight() / 2 + 55), -1);
        }
    }
    
    private void drawHead(final ResourceLocation skin, final int width, final int height) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        Wrapper.INSTANCE.mc().getTextureManager().bindTexture(skin);
        RenderUtils.drawScaledCustomSizeModalRect(sr.getScaledWidth() / 2 + 3, sr.getScaledHeight() / 2 + 23, 8.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
    }
    
    public static void drawEntityOnScreen(final int posX, final int posY, final int scale, final float mouseX, final float mouseY, final EntityLivingBase ent) {
        if (Wrapper.INSTANCE.mc().world != null && Wrapper.INSTANCE.mc().player != null) {
            GlStateManager.enableColorMaterial();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)posX, (float)posY, 50.0f);
            GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
            GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
            final float f = ent.renderYawOffset;
            final float f2 = ent.rotationYaw;
            final float f3 = ent.rotationPitch;
            final float f4 = ent.prevRotationYawHead;
            final float f5 = ent.rotationYawHead;
            GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-(float)Math.atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
            ent.renderYawOffset = (float)Math.atan(mouseX / 40.0f) * 20.0f;
            ent.rotationYaw = (float)Math.atan(mouseX / 40.0f) * 40.0f;
            ent.rotationPitch = -(float)Math.atan(mouseY / 40.0f) * 20.0f;
            ent.rotationYawHead = ent.rotationYaw;
            ent.prevRotationYawHead = ent.rotationYaw;
            GlStateManager.translate(0.0f, 0.0f, 0.0f);
            final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setPlayerViewY(180.0f);
            rendermanager.setRenderShadow(false);
            rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
            rendermanager.setRenderShadow(true);
            ent.renderYawOffset = f;
            ent.rotationYaw = f2;
            ent.rotationPitch = f3;
            ent.prevRotationYawHead = f4;
            ent.rotationYawHead = f5;
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }
}
