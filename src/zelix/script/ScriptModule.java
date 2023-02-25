package zelix.script;

import zelix.hack.*;
import javax.script.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import zelix.eventapi.event.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.*;

public class ScriptModule extends Hack
{
    private String moduleName;
    private HackCategory category;
    private Invocable invoke;
    
    public ScriptModule(final String moduleName, final HackCategory category, final Invocable invocable) {
        super(moduleName, category);
        this.moduleName = moduleName;
        this.category = category;
        this.invoke = invocable;
    }
    
    @Override
    public void onGuiContainer(final GuiContainerEvent event) {
        try {
            this.invoke.invokeFunction("onGuiContainer", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onGuiOpen(final GuiOpenEvent event) {
        try {
            this.invoke.invokeFunction("onGuiOpen", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onMouse(final MouseEvent event) {
        try {
            this.invoke.invokeFunction("onMouse", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        try {
            this.invoke.invokeFunction("onPlayerTick", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        try {
            this.invoke.invokeFunction("onClientTick", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onCameraSetup(final EntityViewRenderEvent.CameraSetup event) {
        try {
            this.invoke.invokeFunction("onCameraSetup", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onAttackEntity(final AttackEntityEvent event) {
        try {
            this.invoke.invokeFunction("onGuiContainer", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onItemPickup(final EntityItemPickupEvent event) {
        try {
            this.invoke.invokeFunction("onItemPickup", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onProjectileImpact(final ProjectileImpactEvent event) {
        try {
            this.invoke.invokeFunction("onProjectileImpact", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        try {
            this.invoke.invokeFunction("onLivingUpdate", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        try {
            this.invoke.invokeFunction("onRenderWorldLast", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        try {
            this.invoke.invokeFunction("onRenderGameOverlay", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        try {
            this.invoke.invokeFunction("onLeftClickBlock", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onInputUpdate(final InputUpdateEvent event) {
        try {
            this.invoke.invokeFunction("onInputUpdate", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onPlayerEventPre(final EventPlayerPre event) {
        try {
            this.invoke.invokeFunction("onPlayerEventPre", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onPlayerEventPost(final EventPlayerPost event) {
        try {
            this.invoke.invokeFunction("onPlayerEventPost", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onRender3D(final RenderBlockOverlayEvent event) {
        try {
            Minecraft.getMinecraft().player.setSprinting(true);
            this.invoke.invokeFunction("onRender3D", event);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onEnable() {
        try {
            this.invoke.invokeFunction("onEnable", new Object[0]);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @Override
    public void onDisable() {
        try {
            this.invoke.invokeFunction("onDisable", new Object[0]);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException ex) {}
    }
}
