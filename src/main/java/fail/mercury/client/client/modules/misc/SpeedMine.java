package fail.mercury.client.client.modules.misc;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.DamageBlockEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * @author auto on 2/29/2020
 */
@ModuleManifest(label = "SpeedMine", aliases = {"FastBreak"}, fakelabel = "Speed Mine", category = Category.MISC, description = "Mines blocks faster.")
public class SpeedMine extends Module {

    @Property("Packet")
    public boolean packet = true;

    @Property("Damage")
    public boolean damage = false;

    @Property("Effect")
    public boolean effect = false;

    @Override
    public void onDisable() {
            mc.player.removePotionEffect(MobEffects.HASTE);
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        mc.playerController.blockHitDelay = 0;
        if (effect) {
            PotionEffect effect = new PotionEffect(MobEffects.HASTE, 80950, 1, false, false);
            mc.player.addPotionEffect(new PotionEffect(effect));
        }
    }

    @EventHandler
    public void onBreak(DamageBlockEvent event) {
        if (canBreak(event.getPos())) {
            if (packet) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
                event.setCancelled(true);
            }
            if (damage) {
                if (mc.playerController.curBlockDamageMP >= 0.7f) {
                    mc.playerController.curBlockDamageMP = 1.0f;
                }
            }
            if (effect) {
            }
        }
    }
    private boolean canBreak(BlockPos pos) {
        final IBlockState blockState = mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, mc.world, pos) != -1;
    }


}
