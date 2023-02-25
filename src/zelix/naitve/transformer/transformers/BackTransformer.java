package zelix.naitve.transformer.transformers;

import zelix.naitve.transformer.*;

public final class BackTransformer extends ClassTransformer
{
    private final Class<?> targetClass;
    private final byte[] bytes;
    
    public BackTransformer(final Class<?> targetClass, final byte[] bytes) {
        this.targetClass = targetClass;
        this.bytes = bytes;
    }
    
    @Override
    public Class<?> getTargetClass() {
        return this.targetClass;
    }
    
    @Override
    public byte[] transform(final byte[] bytes) {
        return this.bytes;
    }
}
