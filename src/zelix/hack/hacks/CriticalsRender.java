package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.event.entity.player.*;
import zelix.utils.*;
import net.minecraft.util.*;

public class CriticalsRender extends Hack
{
    public NumberValue crackSize;
    
    public CriticalsRender() {
        super("MoreParticle", HackCategory.VISUAL);
        this.setChinese("\u66f4\u591a\u7c92\u5b50");
        this.crackSize = new NumberValue("Critical", 1.0, 0.0, 5.0);
        this.addValue(this.crackSize);
    }
    
    @Override
    public void onAttackEntity(final AttackEntityEvent e) {
        for (int i = 0; i < this.crackSize.getValue() && e.getTarget() != null; ++i) {
            Wrapper.INSTANCE.mc().effectRenderer.emitParticleAtEntity(e.getTarget(), EnumParticleTypes.CRIT_MAGIC);
        }
    }
}
