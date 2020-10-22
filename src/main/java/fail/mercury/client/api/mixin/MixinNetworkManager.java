package fail.mercury.client.api.mixin;

import fail.mercury.client.Mercury;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import fail.mercury.client.client.events.PacketEvent;
import net.minecraft.network.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        PacketEvent event = new PacketEvent(PacketEvent.Type.OUTGOING, packet);
        Mercury.INSTANCE.getEventManager().fireEvent(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        PacketEvent event = new PacketEvent(PacketEvent.Type.INCOMING, packet);
        Mercury.INSTANCE.getEventManager().fireEvent(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}
