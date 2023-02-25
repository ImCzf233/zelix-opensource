package zelix.naitve.transformer;

import zelix.naitve.agent.instrument.impl.*;
import zelix.naitve.transformer.transformers.*;
import zelix.utils.hooks.visual.*;
import java.util.*;

public class TransformManager
{
    public static final TransformManager INSTANCE;
    private static final InstrumentationImpl instrumentationImpl;
    private static List<ClassTransformer> transformers;
    private static List<BackTransformer> backTransformers;
    private static boolean transformed;
    
    public final InstrumentationImpl getInstrumentationImpl() {
        return TransformManager.instrumentationImpl;
    }
    
    public final List<ClassTransformer> getTransformers() {
        return TransformManager.transformers;
    }
    
    public final void setTransformers(final List<ClassTransformer> transformer) {
        TransformManager.transformers = transformer;
    }
    
    public final List<BackTransformer> getBackTransformers() {
        return TransformManager.backTransformers;
    }
    
    public final void setBackTransformers(final List<BackTransformer> v) {
        TransformManager.backTransformers = v;
    }
    
    public final boolean getTransformed() {
        return TransformManager.transformed;
    }
    
    public final void setTransformed(final boolean value) {
        TransformManager.transformed = value;
    }
    
    public final void init() {
        if (TransformManager.transformed) {
            throw new RuntimeException("Classes has been transformed");
        }
        TransformManager.transformers.add(new MinecraftTransformer());
        this.doTransform();
    }
    
    public final void reTransform() {
        if (TransformManager.transformed) {
            throw new RuntimeException("Classes has been transformed");
        }
        this.doTransform();
    }
    
    public final void doTransform() {
        for (final ClassTransformer classTransformer : TransformManager.transformers) {
            final Class[] arrayOfClass = { classTransformer.getTargetClass() };
            ChatUtils.message(arrayOfClass[0].getCanonicalName());
            final int error = TransformManager.instrumentationImpl.reTransformClasses(arrayOfClass);
            ChatUtils.message("Transformed Class " + classTransformer.getTargetClass().getCanonicalName() + ' ' + error);
        }
    }
    
    public static final byte[] transform(final Class clazz, final byte[] original_class_bytes) {
        ChatUtils.warning("transform");
        if (clazz == null || clazz.getCanonicalName().equals("null")) {
            ChatUtils.warning("null class");
            return original_class_bytes;
        }
        if (original_class_bytes == null) {
            return null;
        }
        byte[] classBytes = new byte[0];
        if (!TransformManager.transformed) {
            for (final ClassTransformer transformer : TransformManager.transformers) {
                if (transformer.getTargetClass() == clazz) {
                    classBytes = transformer.transform(original_class_bytes);
                    TransformManager.backTransformers.add(new BackTransformer(clazz, original_class_bytes));
                }
            }
        }
        else {
            for (final BackTransformer transformer2 : TransformManager.backTransformers) {
                if (transformer2.getTargetClass() == clazz) {
                    classBytes = transformer2.transform(original_class_bytes);
                }
            }
        }
        return classBytes;
    }
    
    static {
        INSTANCE = new TransformManager();
        instrumentationImpl = new InstrumentationImpl();
        TransformManager.transformers = new ArrayList<ClassTransformer>();
        TransformManager.backTransformers = new ArrayList<BackTransformer>();
    }
}
