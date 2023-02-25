package zelix;

import zelix.utils.hooks.visual.font.*;
import zelix.managers.*;
import net.minecraftforge.common.*;
import zelix.utils.system.*;
import net.minecraftforge.fml.common.*;
import zelix.script.*;
import zelix.utils.resourceloader.*;

public class Core
{
    public static final String MODID = "rsaaaaaa";
    public static final String NAME = "Zelix";
    public static final String VERSION = "1.3.0";
    public static final String MCVERSION = "1.12.2";
    public static int initCount;
    public static HackManager hackManager;
    public static FileManager fileManager;
    public static EventsHandler eventsHandler;
    public static CapeManager capeManager;
    public static FontLoaders fontLoaders;
    public static FontManager fontManager;
    public static String UN;
    public static String UP;
    public static String Love;
    
    public Core() {
        this.Sort_Verify();
    }
    
    public static void main(final String[] args) {
    }
    
    public void Sort_Verify() {
        if (Core.initCount > 0) {
            return;
        }
        Core.hackManager = new HackManager();
        Core.fileManager = new FileManager();
        Core.eventsHandler = new EventsHandler();
        Core.capeManager = new CapeManager();
        Nan0EventRegister.register(MinecraftForge.EVENT_BUS, Core.eventsHandler);
        Nan0EventRegister.register(FMLCommonHandler.instance().bus(), Core.eventsHandler);
        try {
            new ScriptManager();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            Strings.loadTranslation();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    static {
        Core.initCount = 0;
        Core.UN = "Zelix Cracked By St. Lillian's Pseudo Academy Team CUBK&Sakai";
        Core.UP = "Zelix Cracked By St. Lillian's Pseudo Academy Team CUBK&Sakai";
    }
}
