package fail.mercury.client.client.gui.click.panel.panels.modules.impl;

import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;
import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.gui.click.Menu;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;
import me.kix.lotus.property.impl.string.impl.ModeStringProperty;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class ModeComponent extends Component {
    private ModeStringProperty ModeStringProperty;
    public ModeComponent(ModeStringProperty ModeStringProperty, float x, float y, float offsetx, float offsety, float w, float h) {
        super(ModeStringProperty.getLabel(), x, y, offsetx, offsety, w, h);
        this.ModeStringProperty = ModeStringProperty;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void moved(float x, float y) {
        super.moved(x, y);
    }

    @Override
    public void drawScreen(int mx, int my, float partialTicks) {
        super.drawScreen(mx, my, partialTicks);
        RenderUtil.drawRect2(getX(), getY(), getW(), getH(), new Color(5, 5, 5, 200).getRGB());
        Menu.font.drawStringWithShadow(StringUtils.capitalize(getLabel()) + ": " + StringUtils.capitalize(ModeStringProperty.getValue().toLowerCase()), getX() + 2, getY() + getH() / 2 - Menu.font.getHeight() / 2,  -1);
    }

    @Override
    public void mouseClicked(int mx, int my, int button) {
        super.mouseClicked(mx, my, button);
        final boolean hovered = MouseUtil.withinBounds(mx, my, getX(), getY(), getW(), getH());
        if (button == 0 && hovered) {
            ModeStringProperty.increment();
        }
        if (button == 1 && hovered) {
            ModeStringProperty.decrement();
        }
    }
}
