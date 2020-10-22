package fail.mercury.client.client.gui.click.panel.panels;

import fail.mercury.client.client.gui.click.Menu;
import fail.mercury.client.client.gui.click.panel.Panel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * @author auto on 2/16/2020
 */
public class MainPanel extends Panel {

    public MainPanel() {
        super("Home");
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        Menu.font.drawStringWithShadow("lol", 20, 20, -1);
        ScaledResolution res = new ScaledResolution(mc);
        Gui.drawRect((res.getScaledWidth() / 2) - 150, 20, (res.getScaledWidth() / 2) + 150, 500, new Color(0, 0, 0, 137).getRGB());
    }
}


