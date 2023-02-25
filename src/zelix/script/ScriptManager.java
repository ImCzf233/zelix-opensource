package zelix.script;

import net.minecraft.client.*;
import java.io.*;
import java.util.*;

public class ScriptManager
{
    public List<ZelixScript> scripts;
    
    public ScriptManager() {
        this.loadScripts();
    }
    
    public void loadScripts() {
        final File scriptDir = new File(Minecraft.getMinecraft().gameDir.getParentFile(), "Plugins");
        final File[] scriptsFiles = scriptDir.listFiles((dir, name) -> name.endsWith(".js"));
        if (scriptsFiles == null) {
            System.out.println("No script found at " + scriptDir.getAbsolutePath() + "!");
            return;
        }
        this.scripts = new ArrayList<ZelixScript>();
        for (final File scriptFile : scriptsFiles) {
            System.out.println("Evaluating " + scriptFile.getAbsolutePath());
            final ZelixScript script = new ZelixScript(scriptFile);
            this.scripts.add(script);
        }
    }
}
