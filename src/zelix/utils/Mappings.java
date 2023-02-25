package zelix.utils;

import net.minecraft.client.*;
import net.minecraftforge.fml.relauncher.*;

public class Mappings
{
    public static String timer;
    public static String anti;
    public static String isInWeb;
    public static String registerReloadListener;
    public static String session;
    public static String yaw;
    public static String pitch;
    public static String rightClickDelayTimer;
    public static String getPlayerInfo;
    public static String playerTextures;
    public static String currentGameType;
    public static String connection;
    public static String blockHitDelay;
    public static String curBlockDamageMP;
    public static String isHittingBlock;
    public static String onUpdateWalkingPlayer;
    
    public static boolean isNotObfuscated() {
        try {
            return Minecraft.class.getDeclaredField("instance") != null;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    private static boolean isMCP() {
        try {
            return ReflectionHelper.findField((Class)Minecraft.class, new String[] { "theMinecraft" }) != null;
        }
        catch (Exception var1) {
            return false;
        }
    }
    
    static {
        Mappings.timer = (isMCP() ? "timer" : "field_71428_T");
        Mappings.anti = (isMCP() ? "MovementInput" : "field_71158_b");
        Mappings.isInWeb = (isMCP() ? "isInWeb" : "field_70134_J");
        Mappings.registerReloadListener = (isMCP() ? "registerReloadListener" : "func_110542_a");
        Mappings.session = (isNotObfuscated() ? "session" : "field_71449_j");
        Mappings.yaw = (isNotObfuscated() ? "yaw" : "field_149476_e");
        Mappings.pitch = (isNotObfuscated() ? "pitch" : "field_149473_f");
        Mappings.rightClickDelayTimer = (isNotObfuscated() ? "rightClickDelayTimer" : "field_71467_ac");
        Mappings.getPlayerInfo = (isNotObfuscated() ? "getPlayerInfo" : "func_175155_b");
        Mappings.playerTextures = (isNotObfuscated() ? "playerTextures" : "field_187107_a");
        Mappings.currentGameType = (isNotObfuscated() ? "currentGameType" : "field_78779_k");
        Mappings.connection = (isNotObfuscated() ? "connection" : "field_78774_b");
        Mappings.blockHitDelay = (isNotObfuscated() ? "blockHitDelay" : "field_78781_i");
        Mappings.curBlockDamageMP = (isNotObfuscated() ? "curBlockDamageMP" : "field_78770_f");
        Mappings.isHittingBlock = (isNotObfuscated() ? "isHittingBlock" : "field_78778_j");
        Mappings.onUpdateWalkingPlayer = (isNotObfuscated() ? "onUpdateWalkingPlayer" : "func_175161_p");
    }
}
