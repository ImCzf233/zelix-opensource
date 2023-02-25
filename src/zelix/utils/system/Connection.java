package zelix.utils.system;

import zelix.*;
import zelix.utils.hooks.visual.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import zelix.utils.*;
import zelix.eventapi.event.*;
import zelix.eventapi.*;
import zelix.eventapi.events.*;
import io.netty.channel.*;

public class Connection extends ChannelDuplexHandler
{
    private EventsHandler eventHandler;
    
    public Connection(final EventsHandler eventHandler) {
        this.eventHandler = eventHandler;
        try {
            final ChannelPipeline pipeline = Wrapper.INSTANCE.mc().getConnection().getNetworkManager().channel().pipeline();
            pipeline.addBefore("packet_handler", "PacketHandler", (ChannelHandler)this);
        }
        catch (Exception exception) {
            ChatUtils.error("Connection: Error on attaching");
        }
    }
    
    public void channelRead(final ChannelHandlerContext ctx, final Object packet) throws Exception {
        if (((Packet)packet) instanceof CPacketPlayer.PositionRotation) {
            final CPacketPlayer.PositionRotation packetPlayer = (CPacketPlayer.PositionRotation)packet;
            final double x = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "x", "field_149479_a");
            final double y = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "y", "field_149477_b");
            final double z = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "z", "field_149478_c");
            final float yaw = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "yaw", "field_149476_e");
            final float pitch = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "pitch", "field_149473_f");
            final boolean onGroud = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "onGround", "field_149474_g");
            final EventPlayerPre eventPlayerPre = new EventPlayerPre(x, y, z, yaw, pitch, Wrapper.INSTANCE.player().isSneaking(), onGroud);
            EventManager.call(eventPlayerPre);
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getX()), "x", "field_149479_a");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getY()), "y", "field_149477_b");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getZ()), "z", "field_149478_c");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Float.valueOf(eventPlayerPre.getYaw()), "yaw", "field_149476_e");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Float.valueOf(eventPlayerPre.getPitch()), "pitch", "field_149473_f");
        }
        if (!this.eventHandler.onPacket(packet, Side.IN)) {
            return;
        }
        super.channelRead(ctx, packet);
    }
    
    public void write(final ChannelHandlerContext ctx, final Object packet, final ChannelPromise promise) throws Exception {
        if (((Packet)packet) instanceof CPacketPlayer.PositionRotation) {
            final CPacketPlayer.PositionRotation packetPlayer = (CPacketPlayer.PositionRotation)packet;
            final double x = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "x", "field_149479_a");
            final double y = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "y", "field_149477_b");
            final double z = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "z", "field_149478_c");
            final float yaw = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "yaw", "field_149476_e");
            final float pitch = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "pitch", "field_149473_f");
            final boolean onGroud = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "onGround", "field_149474_g");
            final EventPlayerPre eventPlayerPre = new EventPlayerPre(x, y, z, yaw, pitch, Wrapper.INSTANCE.player().isSneaking(), onGroud);
            EventManager.call(eventPlayerPre);
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getX()), "x", "field_149479_a");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getY()), "y", "field_149477_b");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getZ()), "z", "field_149478_c");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Float.valueOf(eventPlayerPre.getYaw()), "yaw", "field_149476_e");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Float.valueOf(eventPlayerPre.getPitch()), "pitch", "field_149473_f");
        }
        if (!this.eventHandler.onPacket(packet, Side.OUT)) {
            return;
        }
        super.write(ctx, packet, promise);
    }
    
    public void processPlayerPacket(final CPacketPlayer.PositionRotation packetPlayer) {
    }
    
    public enum Side
    {
        IN, 
        OUT;
    }
}
