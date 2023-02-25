package zelix.hack.hacks;

import java.util.*;
import net.minecraft.network.*;
import zelix.hack.*;
import zelix.value.*;
import zelix.utils.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;

public class PingSpoof extends Hack
{
    public NumberValue delay;
    public ArrayList<Packet> packets;
    public boolean csend;
    TimerUtils timer;
    
    public PingSpoof() {
        super("PingSpoof", HackCategory.PLAYER);
        this.delay = new NumberValue("Delay", 1000.0, 100.0, 5000.0);
        this.packets = new ArrayList<Packet>();
        this.timer = new TimerUtils();
        this.addValue(this.delay);
    }
    
    public void sendPacket() {
    }
    
    @Override
    public void onDisable() {
        this.packets.forEach(p -> Wrapper.INSTANCE.sendPacket(p));
        this.packets.clear();
        this.csend = false;
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!this.packets.isEmpty() && this.timer.hasReached((float)(Object)this.delay.getValue()) && this.packets.size() >= 7) {
            this.csend = true;
            this.packets.forEach(p -> Wrapper.INSTANCE.sendPacket(p));
            this.packets.clear();
            this.csend = false;
            this.timer.reset();
        }
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (!(packet instanceof CPacketKeepAlive)) {
            return true;
        }
        if (this.csend) {
            return true;
        }
        this.packets.add((Packet)packet);
        return false;
    }
}
