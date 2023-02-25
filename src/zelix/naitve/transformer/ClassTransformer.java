package zelix.naitve.transformer;

public abstract class ClassTransformer
{
    public abstract Class<?> getTargetClass();
    
    public abstract byte[] transform(final byte[] p0);
}
