package zelix.hack.hacks;

import net.minecraft.entity.*;
import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.event.entity.player.*;
import zelix.utils.*;
import net.minecraft.entity.effect.*;

public class DiedwithLightning extends Hack
{
    public NumberValue a;
    Entity eb;
    int delay;
    
    public DiedwithLightning() {
        super("LightningGOD", HackCategory.PLAYER);
        this.delay = 0;
        this.setChinese("\u96f7\u7535\u6cd5\u738b");
        this.a = new NumberValue("Delay", 5.0, 1.0, 10.0);
        this.addValue(this.a);
    }
    
    @Override
    public void onAttackEntity(final AttackEntityEvent e) {
        if (e.getTarget() != null) {
            ++this.delay;
            this.eb = e.getTarget();
            if (this.delay >= (int)(Object)this.a.getValue()) {
                Wrapper.INSTANCE.world().spawnEntity((Entity)new EntityLightningBolt(Wrapper.INSTANCE.player().getEntityWorld(), this.eb.posX, this.eb.posY, this.eb.posZ, false));
                this.delay = 0;
            }
        }
    }
}
