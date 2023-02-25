package zelix.script.api;

import zelix.hack.*;
import zelix.utils.visual.*;
import zelix.value.*;

public class Values
{
    private Hack mod;
    
    public Values(final Hack mod) {
        this.mod = mod;
    }
    
    public NumberValue addNumberValue(final String name, final double value, final double min, final double max) {
        final NumberValue num = new NumberValue(name, value, min, max);
        try {
            this.mod.addValue(num);
        }
        catch (Exception e) {
            e.printStackTrace();
            ChatUtils.message("\u6dfb\u52a0\u53c2\u6570\u5931\u8d25");
        }
        return num;
    }
    
    public BooleanValue addBooleanValue(final String name, final boolean state) {
        final BooleanValue bool = new BooleanValue(name, Boolean.valueOf(state));
        try {
            this.mod.addValue(bool);
        }
        catch (Exception e) {
            e.printStackTrace();
            ChatUtils.message("\u6dfb\u52a0\u53c2\u6570\u5931\u8d25");
        }
        return bool;
    }
    
    public ModeValue addModeValue(final String name, final String[] values, final String value) {
        final Mode[] modes = new Mode[values.length];
        for (int i = 0; i <= values.length; ++i) {
            if (values[i].equals(value)) {
                modes[i] = new Mode(values[i], true);
            }
            else {
                modes[i] = new Mode(values[i], false);
            }
        }
        final ModeValue mode = new ModeValue(name, modes);
        try {
            this.mod.addValue(mode);
        }
        catch (Exception e) {
            e.printStackTrace();
            ChatUtils.message("\u6dfb\u52a0\u53c2\u6570\u5931\u8d25");
        }
        return mode;
    }
}
