package zelix.hack.hacks.HytUtils;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.event.entity.player.*;
import zelix.utils.*;
import net.minecraft.client.entity.*;

public class KeepSprint extends Hack
{
    public BooleanValue onlyForward;
    
    public KeepSprint() {
        super("KeepSprint", HackCategory.HYT_UTILS);
        this.onlyForward = new BooleanValue("OnlyForward", Boolean.valueOf(false));
        this.addValue(this.onlyForward);
    }
    
    @Override
    public void onAttackEntity(final AttackEntityEvent event) {
        if (event.getTarget() != Wrapper.INSTANCE.player() || event.getTarget() == null || Wrapper.INSTANCE.player().movementInput.moveForward < 0.0f || Wrapper.INSTANCE.player().hurtTime > 0) {
            return;
        }
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        player.motionX *= 0.6;
        final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
        player2.motionZ *= 0.6;
        Wrapper.INSTANCE.player().setSprinting(true);
    }
}
