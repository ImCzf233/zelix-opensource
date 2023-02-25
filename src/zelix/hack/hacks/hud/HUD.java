package zelix.hack.hacks.hud;

import net.minecraft.client.*;
import java.text.*;
import net.minecraft.util.*;
import java.awt.image.*;
import zelix.utils.visual.*;
import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.client.event.*;
import java.awt.*;
import zelix.*;
import zelix.utils.hooks.visual.*;
import zelix.managers.*;
import zelix.hack.hacks.*;
import zelix.hack.hacks.xray.gui.*;
import zelix.utils.*;
import zelix.utils.tenacityutils.render.*;
import zelix.gui.clickguis.N3ro.Utils.*;
import java.util.*;
import net.minecraft.client.renderer.*;
import zelix.utils.hooks.visual.font.render.*;
import net.minecraft.client.gui.*;
import net.minecraft.inventory.*;
import net.minecraft.client.gui.inventory.*;
import zelix.utils.hooks.visual.font.*;

public class HUD extends Hack
{
    public static Minecraft mc;
    public BooleanValue effects;
    public BooleanValue SessionInfo;
    public BooleanValue Inentory;
    public BooleanValue notification;
    public BooleanValue MUSICPLAYER;
    public BooleanValue ListRainBow;
    public BooleanValue ListBackRainBow;
    public BooleanValue OraginRainBow;
    public BooleanValue ListBackVis;
    public BooleanValue ListBoxAlpha;
    public ModeValue HUDmode;
    SimpleDateFormat sdf;
    public static FontManager font;
    public ColorUtils c;
    private ResourceLocation logo;
    int rainbowTick;
    NumberValue invx;
    NumberValue ListBackTick;
    NumberValue ListOnUP;
    NumberValue ListAlpha;
    int red;
    int green;
    int blue;
    NumberValue r;
    NumberValue g;
    NumberValue b;
    Boolean create;
    ModeValue rainbowmode;
    NumberValue invy;
    public BufferedImage trayIcon;
    public SimpleDateFormat formatter;
    int rainbowcount;
    private Opacity hue;
    
    public HUD() {
        super("HUD", HackCategory.VISUAL);
        this.sdf = new SimpleDateFormat("HH:mm");
        this.c = new ColorUtils();
        this.logo = new ResourceLocation("logo.png");
        this.rainbowTick = 0;
        this.create = false;
        this.formatter = new SimpleDateFormat("HH:mm:ss");
        this.hue = new Opacity(0);
        this.setToggled(true);
        this.setShow(false);
        this.r = new NumberValue("Red", 255.0, 0.0, 255.0);
        this.g = new NumberValue("Green", 255.0, 0.0, 255.0);
        this.b = new NumberValue("Blue", 255.0, 0.0, 255.0);
        this.effects = new BooleanValue("Effects", Boolean.valueOf(false));
        this.SessionInfo = new BooleanValue("SessionInfo", Boolean.valueOf(false));
        this.Inentory = new BooleanValue("Inentory", Boolean.valueOf(false));
        this.invx = new NumberValue("InvX", 10.0, 0.0, 100.0);
        this.invy = new NumberValue("InvY", 20.0, 0.0, 100.0);
        this.notification = new BooleanValue("Notification", Boolean.valueOf(false));
        this.HUDmode = new ModeValue("Mode", new Mode[] { new Mode("Normal", true), new Mode("OTC", false), new Mode("Logo", false) });
        this.ListBackRainBow = new BooleanValue("New ListBackRainBow", Boolean.valueOf(false));
        this.ListBackTick = new NumberValue("ListBackTick", 80.0, -60.0, 500.0);
        this.ListOnUP = new NumberValue("ListOnUP", 1.5, 1.5, 10.0);
        this.OraginRainBow = new BooleanValue("OraginRainBow", Boolean.valueOf(false));
        this.ListBackVis = new BooleanValue("ListBackVis", Boolean.valueOf(false));
        this.ListBoxAlpha = new BooleanValue("isListBoxAlpha", Boolean.valueOf(true));
        this.ListAlpha = new NumberValue("ListAlpha", 0.35, 0.1, 1.0);
        this.rainbowmode = new ModeValue("ListColor", new Mode[] { new Mode("Old", true), new Mode("New", false), new Mode("Palette", false) });
        this.addValue(this.r, this.g, this.b, this.effects, this.invx, this.invy, this.Inentory, this.HUDmode, this.rainbowmode, this.ListBackRainBow, this.ListBackTick, this.ListOnUP, this.OraginRainBow, this.ListBackVis, this.ListBoxAlpha, this.ListAlpha);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public String getDescription() {
        return "Heads-Up Display.";
    }
    
    @Override
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        this.red = (int)(Object)this.r.getValue();
        this.green = (int)(Object)this.g.getValue();
        this.blue = (int)(Object)this.b.getValue();
        int rainbowTickc = 0;
        float h1 = this.hue.getOpacity();
        if (this.notification.getValue()) {}
        if (this.Inentory.getValue()) {
            this.drawInventory();
        }
        if (this.HUDmode.getMode("Logo").isToggled()) {
            RenderUtils.drawImage(this.logo, 2, 2, 100, 100);
        }
        else if (this.HUDmode.getMode("Normal").isToggled()) {
            final TTFFontRenderer font = HUD.font.getFont("SFR 11");
            final String text = "Z";
            final float x2 = 2.0f;
            final float y2 = 2.0f;
            final ColorUtils c = this.c;
            final int red = ColorUtils.rainbow().getRed();
            final ColorUtils c2 = this.c;
            final int green = ColorUtils.rainbow().getGreen();
            final ColorUtils c3 = this.c;
            font.drawStringWithShadow(text, x2, y2, new Color(red, green, ColorUtils.rainbow().getBlue()).getRGB());
            HUD.font.getFont("SFR 11").drawStringWithShadow("elix 1.3.0(" + this.sdf.format(new Date()) + ")", HUD.font.getFont("SFR 11").getStringWidth("Z") + 6, 2.0f, -1);
        }
        else if (this.HUDmode.getMode("OTC").isToggled()) {
            final String test = "AutoWidthTest";
            final String serverip = HUD.mc.isSingleplayer() ? "singleplayer" : (HUD.mc.getCurrentServerData().serverIP.contains(":") ? HUD.mc.getCurrentServerData().serverIP : (HUD.mc.getCurrentServerData().serverIP + ":25565"));
            final String info = "Zelix | " + Core.UN + " | " + Minecraft.getDebugFPS() + " fps | " + serverip + " | " + this.formatter.format(new Date());
            GuiRenderUtils.drawRect(5.0f, 5.0f, HUD.font.getFont("SFR 5").getWidth(info) + 4.0f, 13.0f, new Color(40, 40, 40));
            GuiRenderUtils.drawRoundedRect(5.0f, 5.0f, HUD.font.getFont("SFR 5").getWidth(info) + 4.0f, 1.0f, 1.0f, new Color(255, 191, 0).getRGB(), 1.0f, new Color(255, 191, 0).getRGB());
            HUD.font.getFont("SFR 5").drawStringWithShadow(info, 7.0f, 7.0f, Colors.WHITE.c);
        }
        else if (this.HUDmode.getMode("GameSense").isToggled()) {}
        final ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        int index = (int)(Object)this.invx.getValue();
        final int index_right = (int)(Object)this.invy.getValue();
        int DrawColor = -1;
        final ArrayList<Hack> hacks = new ArrayList<Hack>();
        hacks.addAll(HackManager.hacks);
        final double x = Wrapper.INSTANCE.player().posX;
        final double y = Wrapper.INSTANCE.player().posY;
        final double z = Wrapper.INSTANCE.player().posZ;
        final String coords = String.format("¡ì7X: ¡ìf%s ¡ì7Y: ¡ìf%s ¡ì7Z: ¡ìf%s", RenderUtils.DF((float)x, 1), RenderUtils.DF((float)y, 1), RenderUtils.DF((float)z, 1));
        final boolean isChatOpen = Wrapper.INSTANCE.mc().currentScreen instanceof GuiChat;
        final int heightCoords = isChatOpen ? (sr.getScaledHeight() - 25) : (sr.getScaledHeight() - 10);
        final int colorRect = ColorUtils.color(0.0f, 0.0f, 0.0f, 0.0f);
        final int colorRect2 = ColorUtils.color(0.0f, 0.0f, 0.0f, 0.5f);
        final int heightFPS = isChatOpen ? (sr.getScaledHeight() - 37) : (sr.getScaledHeight() - 22);
        this.hue.interp(256.0f, 2.0);
        if (this.hue.getOpacity() > 255.0f) {
            this.hue.setOpacity(0.0f);
        }
        if (!ClickGui.language.getMode("Chinese").isToggled()) {
            for (final Hack h2 : HackManager.getSortedHacks3()) {
                if (this.rainbowcount > 100) {
                    this.rainbowcount = 12;
                }
                if (h1 > 255.0f) {
                    h1 = 0.0f;
                }
                String modeName = "";
                if (!h2.isToggled()) {
                    continue;
                }
                if (++rainbowTickc > 100) {
                    rainbowTickc = 0;
                }
                if (h2.getName().equals("Xray")) {
                    modeName = modeName + " ¡ì7" + GuiOverlay.XrayStr;
                }
                for (final Value value : h2.getValues()) {
                    if (value instanceof ModeValue) {
                        final ModeValue modeValue = (ModeValue)value;
                        if (!modeValue.getModeName().equals("Mode")) {
                            continue;
                        }
                        for (final Mode mode : modeValue.getModes()) {
                            if (mode.isToggled()) {
                                modeName = modeName + " ¡ì7" + mode.getName();
                            }
                        }
                    }
                }
                final int yPos = 3;
                int xPos = 4;
                xPos = 6;
                final Color rai = Color.getHSBColor(h1 / 255.0f, 0.5f, 0.8f);
                final Color m_Backrainbow = new Color(Color.HSBtoRGB((float)(Minecraft.getMinecraft().player.ticksExisted / 50.0 - Math.sin(rainbowTickc / this.ListBackTick.getValue() * this.ListOnUP.getValue())) % 1.0f, 1.0f, 1.0f));
                if (this.rainbowmode.getMode("Old").isToggled()) {
                    DrawColor = new Color(Color.HSBtoRGB((float)(Minecraft.getMinecraft().player.ticksExisted / 50.0 - Math.sin(rainbowTickc / 40.0 * 1.4)) % 1.0f, 1.0f, 1.0f)).getRGB();
                }
                else if (this.rainbowmode.getMode("New").isToggled()) {
                    DrawColor = new Color(rai.getRed(), 50, 120).getRGB();
                }
                else {
                    DrawColor = Palette.fade(new Color(this.red, this.green, this.blue)).getRGB();
                }
                if (this.ListBackVis.getValue()) {
                    if (this.ListBoxAlpha.getValue()) {
                        final float alphaAnimation = 1.0f;
                        final int SDrawColor = ColorUtil.applyOpacity(new Color(10, 10, 10), (float)(this.ListAlpha.getValue() * alphaAnimation)).getRGB();
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() - HUD.font.getFont("SFB 6").getWidth(h2.getName() + modeName) - index_right - 4.0f, index + 1.6, sr.getScaledWidth() - index_right, index + 13.6, 0.0f, 0, SDrawColor);
                    }
                    else if (this.ListBackRainBow.getValue()) {
                        if (this.OraginRainBow.getValue()) {
                            RenderUtil.drawBorderedRect(sr.getScaledWidth() - HUD.font.getFont("SFB 6").getWidth(h2.getName() + modeName) - index_right - 4.0f, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, ColorUtils.rainbow((int)(this.ListOnUP.getValue() * 10.0)), ColorUtils.rainbow((int)(this.ListOnUP.getValue() * 10.0)));
                        }
                        else {
                            RenderUtil.drawBorderedRect(sr.getScaledWidth() - HUD.font.getFont("SFB 6").getWidth(h2.getName() + modeName) - index_right - 4.0f, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, m_Backrainbow.getRGB(), m_Backrainbow.getRGB());
                        }
                    }
                    else {
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() - HUD.font.getFont("SFB 6").getWidth(h2.getName() + modeName) - index_right - 4.0f, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, -14869217, -14869217);
                    }
                }
                HUD.font.getFont("SFB 6").drawStringWithShadow(h2.getName() + modeName, sr.getScaledWidth() - HUD.font.getFont("SFB 6").getWidth(h2.getName() + modeName) - index_right - 2.0f, index + 2, DrawColor);
                index += (int)(HUD.font.getFont("SFB 6").getHeight(h2.getName()) + 3.0f);
                h1 += 25.0f;
                this.rainbowcount += 12;
            }
        }
        else {
            for (final Hack h2 : HackManager.getSortedHacks()) {
                String modeName = "";
                if (h1 > 255.0f) {
                    h1 = 0.0f;
                }
                if (!h2.isToggled()) {
                    continue;
                }
                if (++rainbowTickc > 100) {
                    rainbowTickc = 0;
                }
                if (h2.getName().equals("Xray") && !GuiOverlay.XrayStr.equals("")) {
                    modeName = modeName + " ¡ì7" + GuiOverlay.XrayStr;
                }
                for (final Value value : h2.getValues()) {
                    if (value instanceof ModeValue) {
                        final ModeValue modeValue = (ModeValue)value;
                        if (!modeValue.getModeName().equals("Mode")) {
                            continue;
                        }
                        for (final Mode mode : modeValue.getModes()) {
                            if (mode.isToggled()) {
                                modeName = modeName + " ¡ì7" + mode.getName();
                            }
                        }
                    }
                }
                final int yPos = 18;
                int xPos = 4;
                xPos = 6;
                final Color rai = Color.getHSBColor(h1 / 255.0f, 0.5f, 0.8f);
                final Color m_Backrainbow = new Color(Color.HSBtoRGB((float)(Minecraft.getMinecraft().player.ticksExisted / 50.0 - Math.sin(rainbowTickc / this.ListBackTick.getValue() * this.ListOnUP.getValue())) % 1.0f, 1.0f, 1.0f));
                if (this.rainbowmode.getMode("Old").isToggled()) {
                    DrawColor = new Color(Color.HSBtoRGB((float)(Minecraft.getMinecraft().player.ticksExisted / 50.0 - Math.sin(rainbowTickc / 40.0 * 1.4)) % 1.0f, 1.0f, 1.0f)).getRGB();
                }
                else if (this.rainbowmode.getMode("New").isToggled()) {
                    DrawColor = new Color(rai.getRed(), 50, 120).getRGB();
                }
                else {
                    DrawColor = Palette.fade(new Color(this.red, this.green, this.blue), 2, 1).getRGB();
                }
                if (this.ListBackVis.getValue()) {
                    if (this.ListBoxAlpha.getValue()) {
                        final float alphaAnimation = 1.0f;
                        final int SDrawColor = ColorUtil.applyOpacity(new Color(10, 10, 10), (float)(this.ListAlpha.getValue() * alphaAnimation)).getRGB();
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(h2.getRenderName() + modeName) - index_right - 4, index + 1.6, sr.getScaledWidth() - index_right, index + 13.6, 0.0f, 0, SDrawColor);
                    }
                    else if (this.ListBackRainBow.getValue()) {
                        if (this.OraginRainBow.getValue()) {
                            RenderUtil.drawBorderedRect(sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(h2.getRenderName() + modeName) - index_right - 4, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, ColorUtils.rainbow((int)(this.ListOnUP.getValue() * 10.0)), ColorUtils.rainbow((int)(this.ListOnUP.getValue() * 10.0)));
                        }
                        else {
                            RenderUtil.drawBorderedRect(sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(h2.getRenderName() + modeName) - index_right - 4, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, m_Backrainbow.getRGB(), m_Backrainbow.getRGB());
                        }
                    }
                    else {
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(h2.getRenderName() + modeName) - index_right - 4, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, -14869217, -14869217);
                    }
                }
                Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(h2.getRenderName() + modeName, (float)(sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(h2.getRenderName() + modeName) - index_right - 2), (float)(index + 2), DrawColor);
                index += Wrapper.INSTANCE.fontRenderer().FONT_HEIGHT + 3;
                h1 += 25.0f;
            }
        }
        super.onRenderGameOverlay(event);
    }
    
    public void drawInventory() {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        final int x = 5;
        final int y = 22;
        Gui.drawRect(x, y, x + 167, y + 73, new Color(29, 29, 29, 255).getRGB());
        Gui.drawRect(x + 1, y + 13, x + 166, y + 72, new Color(40, 40, 40, 255).getRGB());
        final FontLoaders fontLoaders = Core.fontLoaders;
        FontLoaders.default16.drawString("Your Inventory", x + 3, y + 3, -1, true);
        boolean hasStacks = false;
        for (int i1 = 9; i1 < Wrapper.INSTANCE.player().inventoryContainer.inventorySlots.size() - 9; ++i1) {
            final Slot slot = Wrapper.INSTANCE.player().inventoryContainer.inventorySlots.get(i1);
            if (slot.getHasStack()) {
                hasStacks = true;
            }
            final int j = slot.xPos;
            final int k = slot.yPos;
            HUD.mc.getRenderItem().renderItemAndEffectIntoGUI(slot.getStack(), x + j - 4, y + k - 68);
            HUD.mc.getRenderItem().renderItemOverlayIntoGUI(Wrapper.INSTANCE.fontRenderer(), slot.getStack(), x + j - 4, y + k - 68, (String)null);
        }
        if (HUD.mc.currentScreen instanceof GuiInventory) {
            final CFontRenderer default16 = FontLoaders.default16;
            final String text = "Already in inventory";
            final int n = x + 83;
            final FontLoaders fontLoaders2 = Core.fontLoaders;
            default16.drawString(text, n - FontLoaders.default16.getStringWidth("Already in inventory") / 2, y + 36, -1, true);
        }
        else if (!hasStacks) {
            final FontLoaders fontLoaders3 = Core.fontLoaders;
            final CFontRenderer default17 = FontLoaders.default16;
            final String text2 = "Empty...";
            final int n2 = x + 83;
            final FontLoaders fontLoaders4 = Core.fontLoaders;
            default17.drawString(text2, n2 - FontLoaders.default16.getStringWidth("Empty...") / 2, y + 36, -1, true);
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();
    }
    
    static {
        HUD.mc = Wrapper.INSTANCE.mc();
        HUD.font = new FontManager();
    }
}
