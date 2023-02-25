package zelix.naitve.agent.instrument.impl;

import zelix.naitve.agent.instrument.*;
import zelix.naitve.agent.*;

public class InstrumentationImpl implements Instrumentation
{
    @Override
    public ClassTransformer[] getTransformers() {
        return Access.getTransformersAsArray();
    }
    
    @Override
    public native Class<?>[] getAllLoadedClasses();
    
    @Override
    public native Class<?>[] getLoadedClasses(final ClassLoader p0);
    
    @Override
    public native int reTransformClasses(final Class<?>[] p0);
    
    @Override
    public void addTransformer(final ClassTransformer classTransformer) {
        Access.addTransformer(classTransformer);
    }
}
