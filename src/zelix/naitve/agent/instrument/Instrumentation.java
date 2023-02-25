package zelix.naitve.agent.instrument;

public interface Instrumentation
{
    Class<?>[] getAllLoadedClasses();
    
    ClassTransformer[] getTransformers();
    
    int reTransformClasses(final Class<?>[] p0);
    
    Class<?>[] getLoadedClasses(final ClassLoader p0);
    
    void addTransformer(final ClassTransformer p0);
}
