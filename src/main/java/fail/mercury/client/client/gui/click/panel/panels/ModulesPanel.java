package fail.mercury.client.client.gui.click.panel.panels;

import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.gui.click.panel.Panel;
import fail.mercury.client.client.gui.click.panel.panels.modules.frame.Frame;
import fail.mercury.client.client.gui.click.panel.panels.modules.frame.impl.CategoryFrame;
import fail.mercury.client.client.gui.click.panel.panels.modules.frame.impl.HudFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

/**
 * @author auto on 2/25/2020
 */
public class ModulesPanel extends Panel {

    public static ArrayList<Frame> frames;

    public ModulesPanel() {
        super("Modules");
        frames = new ArrayList<>();
        int x = 2;
        int y = 20;
        for (Category moduleCategory : Category.values()) {
            frames.add(new CategoryFrame(moduleCategory, x, y, 120, 18));
            if (x + 225 >= new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
                x = 2;
                y += 20;
            } else x += 125;
        }
        frames.add(new HudFrame(x, y, 120, 18));
        frames.forEach(Frame::init);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        frames.forEach(frame -> frame.drawScreen(mouseX, mouseY, partialTicks));

    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        frames.forEach(frame -> frame.mouseClicked(mouseX, mouseY, mouseButton));

    }

    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        frames.forEach(frame -> frame.keyTyped(typedChar, keyCode));

    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        frames.forEach(frame -> frame.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    public void onGuiClosed() {
        frames.forEach(frame -> {
            frame.onGuiClosed();
            frame.setDragging(false);
        });
    }

}
