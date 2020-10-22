package fail.mercury.client.client.hudcomponents;

import fail.mercury.client.api.gui.hudeditor.HudComponent;
import fail.mercury.client.api.gui.hudeditor.annotation.HudManifest;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * @author auto on 4/12/2020
 */
@HudManifest(label = "Speed", x = 2, y = 10, width = 58, height = 18, showlabel = false)
public class Speed extends HudComponent {

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        if (mc.world == null || mc.player == null)
            return;
        float x = getX();
        float y = getY();
        final DecimalFormat df = new DecimalFormat("#.#");

        final double deltaX = mc.player.posX - mc.player.prevPosX;
        final double deltaZ = mc.player.posZ - mc.player.prevPosZ;
        final float tickRate = (mc.timer.tickLength / 1000.0f);

        final String bps = "BPS: " + df.format((MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) / tickRate));

        font.drawStringWithShadow(bps, x, y, Color.WHITE.getRGB());
    }

}
