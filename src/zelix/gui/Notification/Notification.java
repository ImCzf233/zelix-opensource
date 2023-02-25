package zelix.gui.Notification;

import zelix.hack.hacks.automine.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import zelix.utils.hooks.visual.font.*;
import java.awt.*;
import zelix.utils.hooks.visual.*;
import net.minecraft.client.gui.*;

public class Notification
{
    private final String message;
    private final TimeHelper timer;
    private final int color;
    private final ResourceLocation image;
    private final long stayTime;
    Minecraft mc;
    private double lastY;
    private double posY;
    private double width;
    private double height;
    private double animationX;
    private int imageWidth;
    public CFontRenderer fr;
    
    public Notification(final String message, final int type) {
        this.mc = Minecraft.getMinecraft();
        this.fr = FontLoaders.default16;
        this.message = message;
        (this.timer = new TimeHelper()).reset();
        this.width = this.fr.getStringWidth(message) + 20;
        this.height = 20.0;
        this.animationX = this.width;
        this.stayTime = 2000L;
        this.imageWidth = 16;
        this.posY = -1.0;
        this.image = new ResourceLocation((type == 0) ? "SUCCESS.png" : "ERROR.png");
        this.color = Colors.DARKGREY.c;
    }
    
    public static int reAlpha(final int n, final float n2) {
        final Color color = new Color(n);
        return new Color(0.003921569f * color.getRed(), 0.003921569f * color.getGreen(), 0.003921569f * color.getBlue(), n2).getRGB();
    }
    
    public void draw(final double getY, final double lastY) {
        this.width = this.fr.getStringWidth(this.message) + 45;
        this.height = 22.0;
        this.imageWidth = 11;
        this.lastY = lastY;
        this.animationX = RenderUtils.getAnimationState(this.animationX, this.isFinished() ? this.width : 0.0, Math.max(this.isFinished() ? 200.0 : 30.0, Math.abs(this.animationX - (this.isFinished() ? this.width : 0.0)) * 20.0) * 0.3);
        if (this.posY == -1.0) {
            this.posY = getY;
        }
        else {
            this.posY = RenderUtils.getAnimationState(this.posY, getY, 200.0);
        }
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        final int x1 = (int)(res.getScaledWidth() - this.width + this.animationX);
        final int x2 = (int)(res.getScaledWidth() + this.animationX);
        int y1 = (int)this.posY - 22;
        final int y2 = (int)(y1 + this.height);
        RenderUtils.drawRect(x1 + 30, y1, x2, y2, new Color(255, 255, 255).getRGB());
        RenderUtils.drawRoundRect(x1, y1, x2, y2, 1, new Color(255, 255, 255, 120).getRGB());
        RenderUtils.drawImage(this.image, (int)(x1 + (this.height - this.imageWidth) / 2.0) - 1, y1 + (int)((this.height - this.imageWidth) / 2.0), this.imageWidth, this.imageWidth);
        ++y1;
        this.fr.drawString(this.message, x1 + 25, (float)(y1 + this.height / 4.0) + 2.0f, new Color(230, 150, 165).getRGB());
    }
    
    public boolean shouldDelete() {
        return this.isFinished() && this.animationX >= this.width;
    }
    
    private boolean isFinished() {
        return this.timer.isDelayComplete(this.stayTime) && this.posY == this.lastY;
    }
    
    public double getHeight() {
        return this.height;
    }
    
    public enum Type
    {
        SUCCESS, 
        ERROR;
    }
}
