package zelix.gui.clickguis.settings;

import java.awt.*;
import zelix.utils.hooks.visual.*;

public class list
{
    public static int x;
    public static int y;
    public static int x1;
    public static int y1;
    
    public static void draw(final int x, final int y) {
        list.x1 = x;
        list.y1 = y;
        RenderUtils.drawRect(list.x1, list.y1, 20.0f, 15.0f, Color.BLACK.getRGB());
    }
    
    public void move(final int mousex, final int mousey) {
        draw(mousex, mousey);
    }
}
