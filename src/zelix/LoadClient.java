package zelix;

import AlphaAutoLeak.annotations.*;
import zelix.utils.*;

@native1
public class LoadClient
{
    public static boolean isCheck;
    
    public static void RLoad(final String string, final String string2) {
        try {
            LoadClient.isCheck = true;
            final Class Main = Class.forName(string);
            Main.getField("initCount").setInt(Main.newInstance(), 114514);
            Main.newInstance();
        }
        catch (IllegalAccessException e2) {
            LoadClient.isCheck = false;
            new Cr4sh();
        }
        catch (InstantiationException e3) {
            LoadClient.isCheck = false;
            new Cr4sh();
        }
        catch (ClassNotFoundException e4) {
            LoadClient.isCheck = false;
            Class Main2 = null;
            try {
                LoadClient.isCheck = true;
                Main2 = Class.forName(string2);
                Main2.newInstance();
            }
            catch (ClassNotFoundException ex) {
                LoadClient.isCheck = false;
                new Cr4sh();
            }
            catch (IllegalAccessException ex2) {
                LoadClient.isCheck = false;
                new Cr4sh();
            }
            catch (InstantiationException ex3) {
                LoadClient.isCheck = false;
                new Cr4sh();
            }
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    
    static {
        LoadClient.isCheck = false;
    }
}
