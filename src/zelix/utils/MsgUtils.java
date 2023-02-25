package zelix.utils;

import java.util.*;
import zelix.gui.Notification.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;

public enum MsgUtils
{
    INSTANCE;
    
    public static ArrayList<Notification> notifications;
    
    public static void sendNotification(final String message, final int type) {
        MsgUtils.notifications.add(new Notification(message, type));
    }
    
    public void drawNotifications() {
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        final double lastY;
        double startY = lastY = res.getScaledHeight() - 50;
        for (int i = 0; i < MsgUtils.notifications.size(); ++i) {
            final Notification not = MsgUtils.notifications.get(i);
            if (not.shouldDelete()) {
                MsgUtils.notifications.remove(i);
            }
            else if (MsgUtils.notifications.size() > 10) {
                MsgUtils.notifications.remove(i);
            }
            not.draw(startY, lastY);
            startY -= not.getHeight() + 3.0;
        }
    }
    
    static {
        MsgUtils.notifications = new ArrayList<Notification>();
    }
}
