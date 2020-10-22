package fail.mercury.client.client.modules.visual;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.MathUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.events.OutlineEvent;
import fail.mercury.client.client.events.Render3DEvent;
import fail.mercury.client.client.modules.combat.KillAura;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

@ModuleManifest(label = "EntityESP", fakelabel = "Entity ESP", aliases = {"PlayerESP"}, category = Category.VISUAL)
public class EntityESP extends Module {

    @Property("Mode")
    @Mode({"Box", "Outline"})
    public static String mode = "Box";

    @Property("Radius")
    @Clamp(maximum = "5")
    public float radiusValue = 0.1f;

    @Property("Box")
    public static boolean box = true;

    @Property("Outline")
    public static boolean outline = true;

    @Property("Players")
    public static boolean players = true;

    @Property("Mobs")
    public static boolean mobs = true;

    @Property("Items")
    public static boolean items = true;

    @Property("Invisibles")
    public static boolean invisibles = true;

    @Property("TargetESP")
    public static boolean targetESP = false;

    @Property("CustomColor")
    public static boolean color = false;
    @Property("Red")
    @Clamp(minimum = "0", maximum = "255")
    public int red = 255;

    @Property("Green")
    @Clamp(minimum = "0", maximum = "255")
    public int green = 255;

    @Property("Blue")
    @Clamp(minimum = "0", maximum = "255")
    public int blue = 255;

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        mc.world.loadedEntityList.forEach(entity -> {
            if (doesQualify(entity)) {
                Color clr = getColor(entity);
                if (mode.equalsIgnoreCase("box")) {
                    Vec3d vec = MathUtil.interpolateEntity(entity, event.getPartialTicks());
                    double posX = vec.x - mc.getRenderManager().renderPosX;
                    double posY = vec.y - mc.getRenderManager().renderPosY;
                    double posZ = vec.z - mc.getRenderManager().renderPosZ;
                    float alpha = 40F;
                    if (box) {
                        if (entity instanceof EntityLivingBase) {
                            EntityLivingBase ent = (EntityLivingBase) entity;
                            if (ent.hurtTime > 0)
                                alpha += 10;
                        }
                        RenderUtil.drawESP(new AxisAlignedBB(0.0, 0.0, 0.0, entity.width, entity.height, entity.width).offset(posX - entity.width / 2, posY, posZ - entity.width / 2), clr.getRed(), clr.getGreen(), clr.getBlue(), alpha);
                    }
                    if (outline)
                        RenderUtil.drawESPOutline(new AxisAlignedBB(0.0, 0.0, 0.0, entity.width, entity.height, entity.width).offset(posX - entity.width / 2, posY, posZ - entity.width / 2), clr.getRed(), clr.getGreen(), clr.getBlue(), 255f, 1f);
                }
                /*if (mode.equalsIgnoreCase("outline")) {
                    mc.renderGlobal.entityOutlineShader.listShaders.forEach(shader -> {
                        ShaderUniform radius = shader.getShaderManager().getShaderUniform("Radius");
                        if (radius != null)
                            radius.set(radiusValue);
                    });
                    if (clr.getAlpha() != 0) {
                        GL11.glPushMatrix();
                        RenderUtil.glEnableOutlineMode(clr);
                        mc.getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
                        RenderUtil.glDisableOutlineMode();
                        GL11.glPopMatrix();
                    }
                }*/
            }
            });
    }

    @EventHandler
    public void onOutline(OutlineEvent event) {
        if (mode.equalsIgnoreCase("outline")) {
             mc.renderGlobal.entityOutlineShader.listShaders.forEach(shader -> {
                ShaderUniform radius = shader.getShaderManager().getShaderUniform("Radius");
                if(radius != null)
                    radius.set(radiusValue);
            });
            mc.world.loadedEntityList.forEach(entity -> {
                if (doesQualify(entity)) {
                    if (event.getEntity() == entity)
                        event.setCancelled(true);
                }
            });
        }
    }

    public Color getColor(Entity entity) {
        if (color) {
            return new Color(red, green, blue);
        }
        if (Mercury.INSTANCE.getFriendManager().isFriend(entity.getName()))
            return new Color(155, 100, 180);
        if (entity.isSneaking()) 
            return new Color(192, 58, 100);
        if (entity instanceof EntityItem)
            return new Color(116, 255, 253);
        if (Mercury.INSTANCE.getModuleManager().find(KillAura.class).isEnabled() && KillAura.target != null && KillAura.target.equals(entity) && targetESP)
            return new Color(233, 11, 0);
        if (entity instanceof EntityPlayer)
            return new Color(255, 63, 43);
        return new Color(255, 125, 40);
    }

    private static boolean doesQualify(Entity entity) {
        return entity != null && entity.isEntityAlive() && RenderUtil.isInViewFrustrum(entity) && entity != mc.player
                && ((entity instanceof EntityPlayer && players) || (entity instanceof EntityItem && items) || (entity instanceof EntityAnimal
                || entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityVillager) && mobs)
                && (!entity.isInvisible() || invisibles);
    }

}
