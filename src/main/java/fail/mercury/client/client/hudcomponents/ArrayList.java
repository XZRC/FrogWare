package fail.mercury.client.client.hudcomponents;

import com.mojang.realmsclient.gui.ChatFormatting;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.gui.hudeditor.HudComponent;
import fail.mercury.client.api.gui.hudeditor.annotation.HudManifest;
import fail.mercury.client.api.module.Module;
import me.kix.lotus.property.annotations.Property;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.PotionEffect;

import java.awt.*;
import java.util.Comparator;

/**
 * @author auto on 3/30/2020
 */
@HudManifest(label = "Array List", x = 2, y = 22, width = 70, height = 18)
public class ArrayList extends HudComponent {
    @Property("Rainbow")
    public boolean rainbow = false;

    @Property("Effects")
    public boolean effects = false;

    private java.util.ArrayList<Module> sorted = new java.util.ArrayList<>(Mercury.INSTANCE.getModuleManager().getToggles());

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        if (mc.world == null || mc.player == null)
            return;
        boolean compensateEffect = effects && ((getY() + getH() / 2) > scaledResolution.getScaledHeight() / 2) && ((getX() + getW() / 2) > scaledResolution.getScaledWidth() / 2) && mc.player.getActivePotionEffects().size() > 0;
        float compensatedY = 0;
        if (compensateEffect) {
            boolean bad = false, good = false;
            for (PotionEffect effect : mc.player.getActivePotionEffects()) {
                if (effect.getPotion().isBadEffect()) {
                    bad = true;
                } else {
                    good = true;
                }
            }
            if (good)
                compensatedY += 25;
            if (bad)
                compensatedY += 25;
        }
        float y = getY() + (compensateEffect ? compensatedY : 0);
        sorted.sort(Comparator.comparingDouble(m -> -font.getStringWidth(getLabel(m))));
        for (Module module : sorted) {
            if (module.isEnabled() && !module.isHidden()) {
                font.drawStringWithShadow(getLabel(module), getX() + ((getX() + getW() / 2) > scaledResolution.getScaledWidth() / 2 ? (getW() - font.getStringWidth(getLabel(module))) : 0), y + ((getY() + getH() / 2) > scaledResolution.getScaledHeight() / 2 ? getH() - font.getHeight() : 0), rainbow ? getRainbow(6000, (int) (y * 30), 0.85f): -1);
                y += ((getY() + getH() / 2) > scaledResolution.getScaledHeight() / 2) ? -(font.getHeight() + 1) : font.getHeight() + 1;
            }
        }
    }
    public static int getRainbow(int speed, int offset,float s) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, s, 1f).getRGB();
    }

    public String getLabel(Module module) {
        StringBuilder string = new StringBuilder((module.getFakeLabel() != null && module.getFakeLabel().equals("")) ? module.getLabel() : module.getFakeLabel());
        if (module.getSuffix() != null && !module.getSuffix().equals(""))
            string.append(" ").append(ChatFormatting.GRAY + module.getSuffix());
        return string.toString();
    }
}