package fail.mercury.client.client.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import fail.mercury.client.api.command.Command;
import fail.mercury.client.api.command.annotation.CommandManifest;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.api.util.MotionUtil;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.BlockPos;

@CommandManifest(label = "hclip", description = "Teleports you forward",aliases = {"h"})
public class HClipCommand extends Command {

    //TODO: figure out why this doesn't work
    @Override
    public void onRun(final String[] args) {
        if (args.length <= 1) {
            ChatUtil.print("Not enough args.");
            return;
        }
        boolean bypass = args.length > 2 && args[2].equalsIgnoreCase("bypass");
        try {
            final double blocks = Double.parseDouble(args[1]);
            boolean ground = mc.player.onGround;
            double[] dir = MotionUtil.forward(blocks);
            Block block = mc.world.getBlockState(new BlockPos(mc.player.posX + dir[0], mc.player.posY - 0.1, mc.player.posZ + dir[1])).getBlock();
            //ground = !(block instanceof BlockAir || block instanceof BlockLiquid);
            if (bypass) {
                for (double x = 0.0625; x < blocks; x += 0.262) {
                    double[] dir2 = MotionUtil.forward(x);
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + dir2[0], mc.player.posY, mc.player.posZ + dir2[1], ground));
                    mc.player.setPositionAndUpdate(mc.player.posX + dir2[0], mc.player.posY, mc.player.posZ + dir2[1]);
                }
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, 0, mc.player.posZ + mc.player.motionZ, ground));
                ChatUtil.print("Zoomed " + blocks + " blocks (Bypass).");

            } else {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + dir[0], mc.player.posY, mc.player.posZ + dir[1], ground));
                mc.player.setPositionAndUpdate(mc.player.posX + dir[0], mc.player.posY, mc.player.posZ + dir[1]);
                ChatUtil.print("Zoomed " + blocks + " blocks.");
            }
        } catch (NumberFormatException ex) {
            ChatUtil.print("Invalid number " + ChatFormatting.GRAY + args[1]);
        }
    }
}