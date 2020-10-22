package fail.mercury.client.client.gui.click.panel.panels.modules.impl;

import fail.mercury.client.Mercury;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.gui.hudeditor.HudComponent;
import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.gui.click.Menu;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;
import me.kix.lotus.property.IProperty;
import me.kix.lotus.property.impl.BooleanProperty;
import me.kix.lotus.property.impl.NumberProperty;
import me.kix.lotus.property.impl.string.impl.ModeStringProperty;

import java.awt.*;

/**
 * @author auto on 3/30/2020
 */
public class HudGuiComponent extends Component {
    private HudComponent hudComponent;
    private boolean extended;

    public HudGuiComponent(HudComponent hudComponent, float x, float y, float offsetx, float offsety, float w, float h) {
        super(hudComponent.getLabel(), x, y, offsetx, offsety, w, h);
        this.hudComponent = hudComponent;
    }

    @Override
    public void init() {
        super.init();
        if (Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(hudComponent) != null) {
            float offsetY = getH();
            for (IProperty property : Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(hudComponent)) {
                if (property.getValue() instanceof Boolean) {
                    getSubComponents().add(new BooleanComponent((BooleanProperty) property, getX(), getY(),0, offsetY, getW(), getH()));
                    offsetY += 15;
                }
                if (property.getValue() instanceof Number) {
                    getSubComponents().add(new NumberComponent((NumberProperty) property, getX(), getY(),0, offsetY, getW(), getH()));
                    offsetY += 15;
                }
                if (property instanceof ModeStringProperty) {
                    getSubComponents().add(new ModeComponent((ModeStringProperty) property, getX(), getY(),0, offsetY, getW(), getH()));
                    offsetY += 15;
                }
            }
        }
    }

    @Override
    public void moved(float x, float y) {
        super.moved(x, y);
    }

    @Override
    public void drawScreen(int mx, int my, float partialTicks) {
        final boolean hovered = MouseUtil.withinBounds(mx, my, getX(), getY(), getW(), getH());
        RenderUtil.drawRect2(getX(), getY(), getW(), getH(), hovered ? new Color(0, 0, 0, 200).getRGB() : (this.hudComponent.isShown() ? new Color(5, 5, 5, 200).getRGB() : new Color(14, 14, 14, 200).getRGB()));
        if (this.hudComponent.isShown()) {
            RenderUtil.drawRect2(getX(), getY(), 1, getH(), Color.CYAN.darker().getRGB());
        }

        try {
            if (!Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(this.hudComponent).isEmpty()) {
                fail.mercury.client.client.gui.click.Menu.font.drawStringWithShadow(this.isExtended() ? "-" : "+", (getX() + getW() - 10), getY() + getH() / 2 - Menu.font.getHeight() / 2, new Color(200, 200, 200, 255).getRGB());
            }
        } catch (Exception ex) {}
        Menu.font.drawStringWithShadow(getLabel(), getX() + getW() / 2 - Menu.font.getStringWidth(getLabel()) / 2, getY() + getH() / 2 - Menu.font.getHeight() / 2, hudComponent.isShown() ? -1 : 0xff707070);
        if (isExtended())
            super.drawScreen(mx, my, partialTicks);
    }

    @Override
    public void mouseClicked(int mx, int my, int button) {
        final boolean hovered = MouseUtil.withinBounds(mx, my, getX(), getY(), getW(), getH());
        switch (button) {
            case 0:
                if (hovered)
                    hudComponent.setShown(!hudComponent.isShown());
                break;
            case 1:
                if (hovered && !getSubComponents().isEmpty())
                    setExtended(!isExtended());
                break;
            default:
                break;
        }
        if (isExtended())
            super.mouseClicked(mx, my, button);
    }

    @Override
    public void mouseReleased(int x, int y, int button) {
        if (isExtended())
            super.mouseReleased(x, y, button);
    }

    public HudComponent getHudComponent() {
        return hudComponent;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }
}

