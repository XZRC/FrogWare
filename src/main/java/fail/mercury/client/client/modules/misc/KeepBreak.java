package fail.mercury.client.client.modules.misc;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.client.events.PacketEvent;
import net.b0at.api.event.EventHandler;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

/**
 * @author auto on 2/29/2020
 */
@ModuleManifest(label = "KeepBreak", fakelabel = "Keep Break", aliases = {"NoAbortBreaking"}, category = Category.MISC)
public class KeepBreak extends Module {

    private final ArrayList<BlockPos> blocksAborted = new ArrayList<>();

    @EventHandler
    public void onPacket(PacketEvent event) {
        if(event.getType().equals(PacketEvent.Type.OUTGOING)) {
            if(event.getPacket() instanceof CPacketPlayerDigging) {
                ChatUtil.print("f");
                if(((CPacketPlayerDigging) event.getPacket()).getAction().equals(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK)) {
                    blocksAborted.add(((CPacketPlayerDigging) event.getPacket()).getPosition());
                    event.setCancelled(true);
                }
                if(((CPacketPlayerDigging) event.getPacket()).getAction().equals(CPacketPlayerDigging.Action.START_DESTROY_BLOCK) && blocksAborted.contains(((CPacketPlayerDigging) event.getPacket()).getPosition())) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
