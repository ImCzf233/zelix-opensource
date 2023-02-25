package zelix.hack.hacks;

import java.lang.reflect.*;
import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.managers.*;
import net.minecraft.item.*;
import zelix.utils.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.relauncher.*;

public class FastPlace extends Hack
{
    BooleanValue onBlock;
    NumberValue speed;
    public static final Field rightClickDelayTimerField;
    
    public FastPlace() {
        super("FastPlace", HackCategory.PLAYER);
        this.onBlock = new BooleanValue("BlockOnly", Boolean.valueOf(false));
        this.speed = new NumberValue("Speed", 0.0, 0.0, 4.0);
        this.addValue(this.onBlock, this.speed);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (HackManager.getHack("Scaffold").isToggled()) {
            try {
                FastPlace.rightClickDelayTimerField.set(this.mc, 4);
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (this.onBlock.getValue()) {
            if (!(Wrapper.INSTANCE.player().getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock)) {
                if (!(Wrapper.INSTANCE.player().getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemBlock)) {
                    return;
                }
            }
            try {
                final int c = (int)(Object)this.speed.getValue();
                if (c == 0) {
                    FastPlace.rightClickDelayTimerField.set(this.mc, 0);
                }
                else {
                    if (c == 4) {
                        return;
                    }
                    final int d = FastPlace.rightClickDelayTimerField.getInt(this.mc);
                    if (d == 4) {
                        FastPlace.rightClickDelayTimerField.set(this.mc, c);
                    }
                }
            }
            catch (IllegalAccessException | IndexOutOfBoundsException ex) {}
        }
        else {
            try {
                final int c = (int)(Object)this.speed.getValue();
                if (c == 0) {
                    FastPlace.rightClickDelayTimerField.set(this.mc, 0);
                }
                else {
                    if (c == 4) {
                        return;
                    }
                    final int d = FastPlace.rightClickDelayTimerField.getInt(this.mc);
                    if (d == 4) {
                        FastPlace.rightClickDelayTimerField.set(this.mc, c);
                    }
                }
            }
            catch (IllegalAccessException ex2) {}
            catch (IndexOutOfBoundsException ex3) {}
        }
    }
    
    static {
        rightClickDelayTimerField = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "field_71467_ac", "rightClickDelayTimer" });
        if (FastPlace.rightClickDelayTimerField != null) {
            FastPlace.rightClickDelayTimerField.setAccessible(true);
        }
    }
}
