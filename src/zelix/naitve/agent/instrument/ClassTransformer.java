package zelix.naitve.agent.instrument;

import java.security.*;

public interface ClassTransformer
{
    byte[] transform(final ClassLoader p0, final String p1, final Class<?> p2, final ProtectionDomain p3, final byte[] p4);
}
