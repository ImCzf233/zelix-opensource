package zelix.naitve.agent.utils;

import java.lang.reflect.*;
import java.util.*;

public final class Reflections
{
    public static Method getMethod(final Class<?> aClass, final String name) {
        try {
            for (final Method method : aClass.getMethods()) {
                if (method.getName().equals(name)) {
                    return method;
                }
            }
            return null;
        }
        catch (Throwable throwable) {
            JavaUtil.printStacktrace(throwable);
            return null;
        }
    }
    
    public static Object invokeMethod(final Method method, final Object instance, final Object... args) {
        try {
            if (method == null) {
                System.err.println("Method == null, args: " + Arrays.toString(args));
                return null;
            }
            return method.invoke(instance, args);
        }
        catch (Throwable throwable) {
            JavaUtil.printStacktrace(throwable);
            return null;
        }
    }
}
