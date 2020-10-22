package fail.mercury.client.client.gui.click.panel.panels.modules.frame.impl;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.gui.hudeditor.HudComponent;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;
import fail.mercury.client.client.gui.click.panel.panels.modules.impl.HudGuiComponent;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.gui.hudeditor.HudComponent;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.gui.click.Menu;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;
import fail.mercury.client.client.gui.click.panel.panels.modules.impl.HudGuiComponent;
import fail.mercury.client.client.gui.click.panel.panels.modules.frame.Frame;

import java.awt.*;


/**
 * @author auto on 3/30/2020
 */
public class HudFrame extends Frame {
    public HudFrame(float x, float y, float w, float h) {
        super("Hud Components", x, y, w, h);
    }

    @Override
    public void init() {
        float offsetY = getH() - 2f;
        for (HudComponent hudComponent : Mercury.INSTANCE.getHudManager().getValues()) {
            getComponents().add(new HudGuiComponent(hudComponent, getX(), getY(), 2, offsetY, getW() - 4, 15));
            offsetY += 15;
        }
        super.init();
    }

    @Override
    public void moved(float x, float y) {
        super.moved(x, y);
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        RenderUtil.drawRect2(getX() - 1, getY() - 2, getW() + 3, getH(), new Color(45, 45, 45).getRGB());
        fail.mercury.client.client.gui.click.Menu.font.drawStringWithShadow(getLabel(), getX() + getW() / 2 - fail.mercury.client.client.gui.click.Menu.font.getStringWidth(getLabel()) / 2, (getY() - 2) + getH() / 2 - Menu.font.getHeight() / 2, -1);
        super.drawScreen(x, y, partialTicks);
        if (isExtended())
            resetHeights();
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
    }


    @Override
    public void mouseReleased(int x, int y, int button) {
        super.mouseReleased(x, y, button);
    }

    @Override
    public void keyTyped(char character, int key) {
        super.keyTyped(character, key);
    }

    private void resetHeights() {
        float offsetY = getH() - 2f;
        for (fail.mercury.client.client.gui.click.panel.panels.modules.Component component : getComponents()) {
            component.setOffsety(offsetY);
            component.moved(getX(), getY());
            if (component instanceof HudGuiComponent) {
                if (((HudGuiComponent) component).isExtended()) {
                    for (fail.mercury.client.client.gui.click.panel.panels.modules.Component component1 : component.getSubComponents()) {
                        offsetY += component1.getH();
                    }
                }
            }
            offsetY += component.getH();
        }
    }

    private float getHeight() {
        float offsetY = 0;
        for (fail.mercury.client.client.gui.click.panel.panels.modules.Component component : getComponents()) {
            if (component instanceof HudGuiComponent) {
                if (((HudGuiComponent) component).isExtended()) {
                    for (Component component1 : component.getSubComponents()) {
                        offsetY += component1.getH();
                    }
                }
            }
            offsetY += component.getH();
        }
        return offsetY;
    }
}

