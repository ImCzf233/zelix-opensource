package zelix.script;

import java.io.*;
import zelix.utils.*;
import javax.script.*;
import zelix.utils.visual.*;
import net.minecraft.client.*;
import zelix.script.api.*;
import zelix.hack.*;
import zelix.*;
import zelix.managers.*;

public class ZelixScript
{
    private ScriptEngine scriptEngine;
    public String name;
    public String author;
    public String version;
    public String category;
    public ScriptModule scriptModule;
    public Invocable invoke;
    
    public ZelixScript(final File scriptFile) {
        final ScriptEngineManager manager = new ScriptEngineManager(null);
        this.scriptEngine = manager.getEngineByName("nashorn");
        final String scriptContent = FileUtils.readFile(scriptFile);
        this.invoke = (Invocable)this.scriptEngine;
        try {
            this.scriptEngine.eval(scriptContent);
        }
        catch (ScriptException ex) {}
        this.name = (String)this.scriptEngine.get("name");
        this.author = (String)this.scriptEngine.get("author");
        this.version = (String)this.scriptEngine.get("version");
        this.category = (String)this.scriptEngine.get("category");
        String type = (String)this.scriptEngine.get("scriptType");
        if (type == null) {
            type = "Module";
        }
        HackCategory modCategory = null;
        try {
            modCategory = HackCategory.valueOf(this.category);
        }
        catch (Exception e) {
            e.printStackTrace();
            ChatUtils.message("\u52a0\u8f7d\u811a\u672c\"" + scriptFile.getAbsolutePath() + "\"\u5931\u8d25!");
            ChatUtils.message("\u529f\u80fd\u5206\u7c7b: " + this.category + " \u672a\u627e\u5230");
            ChatUtils.message("\u5982\u679ccategory\u586b\u5199\u65e0\u8bef\u8bf7\u68c0\u67e5\u8bed\u6cd5\u9519\u8bef");
            return;
        }
        if ("Module".equals(type)) {
            this.registerModule(this.name, modCategory, this.invoke);
        }
        manager.put("out", System.out);
        manager.put("mc", Minecraft.getMinecraft());
        manager.put("values", new Values(this.scriptModule));
        try {
            this.scriptEngine.eval(scriptContent);
        }
        catch (ScriptException e2) {
            e2.printStackTrace();
            ChatUtils.message("\u52a0\u8f7d\u811a\u672c\"" + scriptFile.getAbsolutePath() + "\"\u5931\u8d25!");
        }
    }
    
    public void registerModule(final String moduleName, final HackCategory category, final Invocable invocable) {
        this.scriptModule = new ScriptModule(moduleName, category, invocable);
        final HackManager hackManager = Core.hackManager;
        HackManager.addPluginHack(this.scriptModule);
    }
}
