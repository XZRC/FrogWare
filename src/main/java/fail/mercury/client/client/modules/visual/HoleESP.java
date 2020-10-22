package fail.mercury.client.client.modules.visual;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.MathUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.events.Render3DEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author auto on 3/21/2020
 */
@ModuleManifest(label = "HoleESP", category = Category.VISUAL, fakelabel = "Hole ESP", aliases = "holes", description = "Renders holes that would protect you from crystal explosions.")
public class HoleESP extends Module {

    public final List<BlockPos> positions = new ArrayList<>();

    @Property("Red")
    @Clamp(minimum = "0", maximum = "255")
    public int red = 200;

    @Property("Green")
    @Clamp(minimum = "0", maximum = "255")
    public int green = 117;

    @Property("Blue")
    @Clamp(minimum = "0", maximum = "255")
    public int blue = 255;

    @Property("Alpha")
    @Clamp(minimum = "0", maximum = "255")
    public int alpha = 119;

    @Property("Distance")
    @Clamp(minimum = "0", maximum = "255")
    public int distance = 5;

    @Override
    public void onToggle() {
        positions.clear();
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        Vec3i pos = MathUtil.interpolateEntityInt(mc.player, mc.getRenderPartialTicks());
        for (int x = pos.getX() - distance; x <pos.getX() + distance; x++) {
            for (int z = pos.getZ() - distance; z < pos.getZ() + distance; z++) {
                for (int y = pos.getY(); y > pos.getY() - 4; y--) {
                    final BlockPos blockPos = new BlockPos(x, y, z);
                    if (isHole(blockPos)) {
                        positions.add(blockPos);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRender(Render3DEvent event) {
        Color clr = new Color(red, green, blue, alpha);
        for (BlockPos position : positions) {
            final AxisAlignedBB bb = new AxisAlignedBB(
                    position.getX() - mc.getRenderManager().viewerPosX,
                    position.getY() - mc.getRenderManager().viewerPosY,
                    position.getZ() - mc.getRenderManager().viewerPosZ,
                    position.getX() + 1 - mc.getRenderManager().viewerPosX,
                    position.getY() + 1 - mc.getRenderManager().viewerPosY,
                    position.getZ() + 1 - mc.getRenderManager().viewerPosZ);
            RenderUtil.drawESP(bb, clr.getRed(), clr.getGreen(), clr.getBlue(), clr.getAlpha());
        }
    }

    public boolean isHole(BlockPos pos) {
        final BlockPos[] touchingBlocks = new BlockPos[]{
                pos.north(), pos.south(), pos.east(), pos.west()
        };
        int validHorizontalBlocks = 0;
        for (BlockPos touching : touchingBlocks) {
            final IBlockState touchingState = mc.world.getBlockState(touching);
            if ((touchingState.getBlock() != Blocks.AIR) && touchingState.isFullBlock()) {
                validHorizontalBlocks++;
            }
        }
        if (validHorizontalBlocks < 4)
            return false;

        if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR) {
            return false;
        }
        if (this.positions.contains(pos)) {
            return false;
        }
        return true;
    }
}
