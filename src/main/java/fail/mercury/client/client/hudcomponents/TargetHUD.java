package fail.mercury.client.client.hudcomponents;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.gui.hudeditor.HudComponent;
import fail.mercury.client.api.gui.hudeditor.annotation.HudManifest;
import fail.mercury.client.api.util.MathUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.modules.combat.KillAura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

/**
 * @author auto on 3/31/2020
 */
@HudManifest(label = "Target HUD", x = 415, y = 320, width = 140, height = 45)
public class TargetHUD extends HudComponent {

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        if (Mercury.INSTANCE.getModuleManager().find(KillAura.class).isEnabled() && KillAura.target != null && KillAura.target instanceof EntityPlayer) {
            EntityLivingBase entity = KillAura.target;
            NetworkPlayerInfo networkPlayerInfo = mc.getConnection().getPlayerInfo(entity.getUniqueID());

            final String ping = "Ping: " + (Objects.isNull(networkPlayerInfo) ? "0ms" : networkPlayerInfo.getResponseTime() + "ms");

            final String playerName = "Name: " + StringUtils.stripControlCodes(entity.getName());
            RenderUtil.drawBorderedRect2(x, y, width, height, 0.5f, new Color(0, 0, 0, 255).getRGB(), new Color(0, 0, 0, 90).getRGB());
            RenderUtil.drawRect2(x, y, 45, 45, new Color(0, 0, 0).getRGB());
            font.drawStringWithShadow(playerName, x + 46.5, y + 4, -1);
            font.drawStringWithShadow("Distance: " + MathUtil.round(mc.player.getDistance(entity), 2), x + 46.5, y + 12, -1);
            font.drawStringWithShadow(ping, x + 46.5, y + 28, new Color(0x5D5B5C).getRGB());
            font.drawStringWithShadow("Health: " + MathUtil.round(entity.getHealth() / 2, 2), x + 46.5, y + 20, getHealthColor(entity));
            drawFace(x + 0.5, y + 0.5, 8, 8, 8, 8, 44, 44, 64, 64, (AbstractClientPlayer) entity);
            RenderUtil.drawBorderedRect2(x + 46, y + height - 10, 92, 8, 0.5f, new Color(0).getRGB(), new Color(35, 35, 35).getRGB());
            float inc = 91 / entity.getMaxHealth();
            float end = inc * (entity.getHealth() > entity.getMaxHealth() ? entity.getMaxHealth() : entity.getHealth());
            RenderUtil.drawRect2(x + 46.5f, y + height - 9.5f, end, 7, getHealthColor(entity));
        }

    }

    private void drawFace(double x, double y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation skin = target.getLocationSkin();
            Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 1, 1);
            RenderUtil.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(GL11.GL_BLEND);
        } catch (Exception e) {
        }
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 0.75F) | 0xFF000000;
    }

}
