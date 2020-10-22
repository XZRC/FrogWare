package fail.mercury.client.client.modules.combat;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.AngleUtil;
import fail.mercury.client.api.util.InventoryUtil;
import fail.mercury.client.api.util.MathUtil;
import fail.mercury.client.api.util.MotionUtil;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.b0at.api.event.types.EventTiming;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author auto on 2/6/2020
 */
@ModuleManifest(label = "Surround", category = Category.COMBAT, aliases = {"NoCrystal", "FeetPlace"}, description = "Automatically surrounds you with bedrock/obsidian")
public class Surround extends Module {

    @Property("Toggle")
    public boolean toggle = false;

    @Property("Swing")
    public boolean swing = false;

    @Property("Rotate")
    public boolean rotate = false;

    @Property("Replenish")
    public boolean replenish = false;

    @Property("Center")
    public boolean center = false;

    public boolean centered;
    public int counter;

    @Override
    public void onDisable() {
        if (centered && center)
            centered = false;
        counter = 0;
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getTiming().equals(EventTiming.PRE)) {
            if (InventoryUtil.getItemCount(mc.player.inventoryContainer, Item.getItemFromBlock(Blocks.OBSIDIAN)) == 0 || !mc.player.onGround)
                return;
            final Vec3d vec = MathUtil.interpolateEntity(mc.player, mc.getRenderPartialTicks());
            final BlockPos playerPos = new BlockPos(vec.x, vec.y, vec.z);
            if (center) {
                if (!centered) {
                    mc.player.setPosition(playerPos.getX() + 0.5, playerPos.getY(), playerPos.getZ() + 0.5);
                    centered = true;
                }
                if (MotionUtil.isMoving(mc.player))
                    centered = false;
            }
            if (center && mc.player.posX != playerPos.getX() + 0.5 && mc.player.posZ != playerPos.getZ() + 0.5)
                return;
            int lastSlot;
            int slot;
            if (InventoryUtil.getItemSlot(mc.player.inventoryContainer, Item.getItemFromBlock(Blocks.OBSIDIAN)) < 36 && replenish) {
                InventoryUtil.swap(InventoryUtil.getItemSlot(mc.player.inventoryContainer, Item.getItemFromBlock(Blocks.OBSIDIAN)), 44);
            }
            slot = InventoryUtil.getItemSlotInHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN));
            lastSlot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = slot;
            mc.playerController.updateController();
            final BlockPos[] positions = {playerPos.north(), playerPos.south(), playerPos.east(), playerPos.west()};
            if (this.canPlace(positions[0])) {
                this.place(playerPos, EnumFacing.NORTH, event);
            }
            if (this.canPlace(positions[1])) {
                this.place(playerPos, EnumFacing.SOUTH, event);
            }
            if (this.canPlace(positions[2])) {
                this.place(playerPos, EnumFacing.EAST, event);
            }
            if (this.canPlace(positions[3])) {
                this.place(playerPos, EnumFacing.WEST, event);
            }
            if (this.canPlace(positions[0])) {
                this.place(positions[0], EnumFacing.UP, event);
            }
            if (this.canPlace(positions[1])) {
                this.place(positions[1], EnumFacing.UP, event);
            }
            if (this.canPlace(positions[2])) {
                this.place(positions[2], EnumFacing.UP, event);
            }
            if (this.canPlace(positions[3])) {
                this.place(positions[3], EnumFacing.UP, event);
            }
           // if (!mc.player.inventory.getStackInSlot(lastSlot).getItem().equals(Item.getItemFromBlock(Blocks.OBSIDIAN)))
            mc.player.inventory.currentItem = lastSlot;
            mc.playerController.updateController();
            if (toggle)
                this.toggle();
        }
    }

    public boolean canPlace(BlockPos pos) {
        final Block block = mc.world.getBlockState(pos).getBlock();
        return (block instanceof BlockAir || block instanceof BlockLiquid) && mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).isEmpty();
    }
   // public boolean canBeClicked(final BlockPos pos) {
   //     return mc.world.getBlockState(pos).getBlock().canCollideCheck(mc.world.getBlockState(pos), false);
   // }


    public void place(BlockPos pos, EnumFacing direction, UpdateEvent event) {
        float[] rotations = AngleUtil.getRotationFromPosition(pos.getX() + 0.5, pos.getZ() + 0.5, pos.add(0, 0, 0).getY() - mc.player.getEyeHeight());
        if (rotate) {
            event.getRotation().setYaw(rotations[0]);
            event.getRotation().setPitch(rotations[1]);
        }
        if (mc.playerController.processRightClickBlock(mc.player, mc.world, pos.add(0, -1, 0), direction, new Vec3d(Math.random(), Math.random(), Math.random()), EnumHand.MAIN_HAND) != EnumActionResult.FAIL) {
            if (swing) mc.player.swingArm(EnumHand.MAIN_HAND);
            else  mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
    }

}
