package fail.mercury.client.client.hudcomponents;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.gui.hudeditor.HudComponent;
import fail.mercury.client.api.gui.hudeditor.annotation.HudManifest;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * @author auto on 3/30/2020
 */
@HudManifest(label = "Watermark", x = 2, y = 2, width = 58, height = 18, showlabel = false)
public class Watermark extends HudComponent {

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        if (mc.world == null || mc.player == null)
            return;
        float x = getX();
        float y = getY();
        //if (getX() > scaledResolution.getScaledHeight())
       //     x = getX() + getW()
        font.drawStringWithShadow(Mercury.NAME, x, y, Color.WHITE.getRGB());
    }

}
