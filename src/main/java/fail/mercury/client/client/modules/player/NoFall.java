package fail.mercury.client.client.modules.player;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import fail.mercury.client.client.events.UpdateEvent;
import net.b0at.api.event.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(label = "NoFall", category = Category.PLAYER, fakelabel = "No Fall")
public class NoFall extends Module {

    private int teleportId;
    private List<CPacketPlayer> packets = new ArrayList<>();

    //TODO: Make this easier to use


    @Override
    public void onEnable() {
        super.onEnable();
            if (mc.world != null) {
                this.teleportId = 0;
                this.packets.clear();
                final CPacketPlayer bounds = new CPacketPlayer.Position(mc.player.posX, mc.player.posY <= 10 ? 255 : 1, mc.player.posZ, mc.player.onGround);
                this.packets.add(bounds);
                mc.player.connection.sendPacket(bounds);
            }
    }


    @EventHandler
    public void onUpdate(UpdateEvent event) {
            if (this.teleportId <= 0) {
                final CPacketPlayer bounds = new CPacketPlayer.Position(mc.player.posX, mc.player.posY <= 10 ? 255 : 1, mc.player.posZ, mc.player.onGround);
                this.packets.add(bounds);
                mc.player.connection.sendPacket(bounds);
                return;
            }
            double posY = -0.00000001;
            if (mc.player.fallDistance > 1.5){
                mc.player.setVelocity(0,0,0);
                for (int i = 0; i <= 3; i++) {
                        mc.player.setVelocity(0, posY - 0.0625 * i, 0);
                        move(0, posY - 0.0625 * i, 0);
                    }
                }
            }

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (mc.player != null && mc.player.fallDistance > 1.5) {
            switch (event.getType()) {
                case INCOMING:
                    if (event.getPacket() instanceof SPacketPlayerPosLook) {
                        final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
                        if (Minecraft.getMinecraft().player.isEntityAlive() && Minecraft.getMinecraft().world.isBlockLoaded(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ)) && !(Minecraft.getMinecraft().currentScreen instanceof GuiDownloadTerrain)) {
                            if (this.teleportId <= 0) {
                                this.teleportId = packet.getTeleportId();
                            } else {
                                event.setCancelled(true);
                            }
                        }
                    }
                    break;
                case OUTGOING:
                    if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
                        event.setCancelled(true);
                    }
                    if (event.getPacket() instanceof CPacketPlayer) {
                        final CPacketPlayer packet = (CPacketPlayer) event.getPacket();
                        if (packets.contains(packet)) {
                            packets.remove(packet);
                            return;
                        }
                        event.setCancelled(true);
                    }
                    break;
            }
        }
    }

    private void move(double x, double y, double z) {
        final Minecraft mc = Minecraft.getMinecraft();
        final CPacketPlayer pos = new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z, mc.player.onGround);
        this.packets.add(pos);
        mc.player.connection.sendPacket(pos);

        final CPacketPlayer bounds = new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY <= 10 ? 255 : 1, mc.player.posZ + z, mc.player.onGround);
        this.packets.add(bounds);
        mc.player.connection.sendPacket(bounds);

        this.teleportId++;
        mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportId - 1));
        mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportId));
        mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportId + 1));
    }


}
