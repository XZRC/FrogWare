package fail.mercury.client.api.util;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.*;

import java.util.List;

public class AngleUtil implements Util {


    public static float[] getRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.getEntityBoundingBox().maxY - 4.0;
        return getRotationFromPosition(x, z, y);
    }


    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().player.posX;
        double zDiff = z - Minecraft.getMinecraft().player.posZ;
        double yDiff = y - Minecraft.getMinecraft().player.posY;
        double hypotenuse = Math.hypot(xDiff, zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(yDiff, hypotenuse) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationFromPosition(BlockPos pos) {
        double xDiff = pos.getX() - Minecraft.getMinecraft().player.posX;
        double zDiff = pos.getZ() - Minecraft.getMinecraft().player.posZ;
        double yDiff = pos.getY() - Minecraft.getMinecraft().player.posY;
        double hypotenuse = Math.hypot(xDiff, zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(yDiff, hypotenuse) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static Vec3d resolveBestHitVec(Entity entity, int precision, boolean evadeBlocks) {

        try {
            Vec3d headVec = mc.player.getPositionEyes(1.0f);
            Vec3d bestHitVec = new Vec3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
            Vec3d interpolated = MathUtil.interpolateEntity(entity, mc.getRenderPartialTicks());

            int offset = precision / 3;

            float height = entity.getEyeHeight() / precision;
            float width = (entity.width * 0.5f) / offset;

            for (int offsetY = 0; offsetY <= precision; offsetY++) {
                for (int offsetX = -offset; offsetX <= offset; offsetX++) {
                    for (int offsetZ = -offset; offsetZ <= offset; offsetZ++) {
                        Vec3d possibleVec = new Vec3d(interpolated.x + width * offsetX, interpolated.y + height * offsetY,
                                interpolated.z + width * offsetZ);

                        if (evadeBlocks) {
                            RayTraceResult result = mc.player.getEntityWorld().rayTraceBlocks(headVec,
                                    possibleVec);

                            if(result != null) {
                                continue;
                            }

                        }
                        if (headVec.distanceTo(possibleVec) < headVec.distanceTo(bestHitVec)) {
                            bestHitVec = possibleVec;
                        }
                    }
                }
            }

            if (bestHitVec.x == Double.MAX_VALUE) {
                bestHitVec = null;
            }

            return bestHitVec;
        } catch (Throwable t) {
            t.printStackTrace();
            return entity.getPositionVector();
        }
    }

    public static Entity rayTrace(float yaw, float pitch, double range) {
        Entity entity = mc.getRenderViewEntity();

        if (entity != null && mc.world != null) {
            mc.pointedEntity = null;
            double d0 = range;
            float partialTicks = 1.0F;
            mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d1 = d0;
            Vec3d vec3d = entity.getPositionEyes(partialTicks);

            Vec3d vec31 = mc.player.getVectorForRotation(pitch, yaw);
            Vec3d vec32 = vec3d.addVector(vec31.x * d0, vec31.y * d0, vec31.z * d0);
            Entity pointedEntity = null;
            Vec3d vec33 = null;
            float f = 1.0F;
            List list = mc.world.getEntitiesInAABBexcluding(entity,
                    entity.getEntityBoundingBox().expand(vec31.x * d0, vec31.y * d0, vec31.z * d0)
                            .expand( f, f, f),
                    Predicates.and(EntitySelectors.NOT_SPECTATING, p_apply_1_ -> p_apply_1_ != null && p_apply_1_.canBeCollidedWith()));
            double d2 = d1;

            for (int i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand( f1,  f1, f1);
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec32);

                if (axisalignedbb.intersects(vec3d, vec32)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                        d2 = 0.0D;
                    }
                } else if (raytraceresult != null) {
                    double d3 = vec3d.distanceTo(raytraceresult.hitVec);
                    if (d3 < d2 || d2 == 0.0D) {
                        if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity1.canRiderInteract()) {
                            if (d2 == 0.0D) {
                                pointedEntity = entity1;
                                vec3d = raytraceresult.hitVec;
                            }
                        } else {
                            pointedEntity = entity1;
                            vec3d = raytraceresult.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null)) {
                mc.objectMouseOver = new RayTraceResult(pointedEntity, vec33);

                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    return pointedEntity;
                }
            }
        }
        return null;
    }

    public static float getAngleDifference(float direction, float rotationYaw) {
        float phi = Math.abs(rotationYaw - direction) % 360.0f;
        return (phi > 180.0f) ? (360.0f - phi) : phi;
    }

    public static boolean isEntityInFov(EntityLivingBase entity, double angle) {
        return getAngleDifference(mc.player.rotationYaw, AngleUtil.getRotations(entity)[0]) < angle;
    }

}
