package zelix.naitve.transformer.transformers;

import zelix.naitve.transformer.*;
import zelix.utils.hooks.visual.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.*;

public class MinecraftTransformer extends ClassTransformer
{
    @Override
    public Class<?> getTargetClass() {
        Class<?> clazz;
        try {
            clazz = Class.forName("net.minecraft.client.Minecraft");
            if (clazz == null) {
                clazz = Class.forName("ave");
            }
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return clazz;
    }
    
    @Override
    public byte[] transform(final byte[] paramArrayOfbyte) {
        ChatUtils.message("Minecraft....");
        final ClassReader cr = new ClassReader(paramArrayOfbyte);
        final ClassNode cn = new ClassNode();
        cr.accept((ClassVisitor)cn, 0);
        final ClassWriter cw = new ClassWriter(cr, 3);
        cn.accept((ClassVisitor)cw);
        return cw.toByteArray();
    }
}
