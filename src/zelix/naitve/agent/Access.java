package zelix.naitve.agent;

import zelix.naitve.agent.instrument.*;
import java.security.*;
import java.util.concurrent.atomic.*;
import zelix.naitve.agent.utils.*;
import java.util.*;

public class Access
{
    private static final Set<ClassTransformer> transformers;
    private static ClassTransformer[] transformersArray;
    private static boolean modified;
    
    public static void addTransformer(final ClassTransformer classTransformer) {
        Access.transformers.add(classTransformer);
        Access.modified = true;
    }
    
    public static byte[] transformClass(final ClassLoader loader, final String className, final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain, final byte[] classfileBuffer) {
        if (classBeingRedefined == null) {
            return classfileBuffer;
        }
        final AtomicReference<byte[]> atomicReference = new AtomicReference<byte[]>();
        final byte[] newByteArray;
        final AtomicReference<byte[]> atomicReference2;
        Access.transformers.forEach(classTransformer -> {
            newByteArray = classTransformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
            if (Objects.isNull(atomicReference2.get())) {
                atomicReference2.set(newByteArray);
                return;
            }
            else if (JavaUtil.equals(atomicReference2.get(), newByteArray)) {
                return;
            }
            else {
                atomicReference2.set(newByteArray);
                return;
            }
        });
        return atomicReference.get();
    }
    
    public static ClassTransformer[] getTransformersAsArray() {
        if (Access.modified) {
            Access.modified = false;
            return Access.transformersArray = Access.transformers.toArray(new ClassTransformer[0]);
        }
        return (Access.transformersArray == null) ? (Access.transformersArray = Access.transformers.toArray(new ClassTransformer[0])) : Access.transformersArray;
    }
    
    static {
        transformers = new HashSet<ClassTransformer>();
    }
}
