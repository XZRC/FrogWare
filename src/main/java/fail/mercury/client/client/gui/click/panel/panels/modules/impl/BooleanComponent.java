package fail.mercury.client.client.gui.click.panel.panels.modules.impl;

import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.gui.click.Menu;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;

import me.kix.lotus.property.impl.BooleanProperty;

import java.awt.*;

public class BooleanComponent extends Component {
    private BooleanProperty booleanProperty;
    public BooleanComponent(BooleanProperty booleanProperty, float x, float y, float offsetx, float offsety, float w, float h) {
        super(booleanProperty.getLabel(), x, y, offsetx, offsety, w, h);
        this.booleanProperty = booleanProperty;
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
        Menu.font.drawStringWithShadow(getLabel(), getX() + 2, getY() + getH() / 2 - Menu.font.getHeight() / 2, booleanProperty.getValue() ? -1 : 0xff707070);
       /* RenderUtil.drawRect2(getX() + getW() - 12, getY() + 3, 10, 12, new Color(140, 140, 140, 200).getRGB());
        if (booleanProperty.getValue()) {
            RenderUtil.drawRect2(getX() + getW() - 11, getY() + 2, 9,13, Color.cyan.brighter().getRGB());
        }*/

        RenderUtil.drawRect(getX() + getW() - 13, getY() + 4, getX() + getW() - 4, getY() + 13, new Color(140, 140, 140, 200).getRGB());
        if (booleanProperty.getValue()) {
            RenderUtil.drawRect(getX() + getW() - 12, getY() + 5, getX() + getW() - 5,getY() + 12, Color.cyan.brighter().getRGB());
        }
    }


    @Override
    public void mouseClicked(int mx, int my, int button) {
        super.mouseClicked(mx, my, button);
        final boolean hovered = MouseUtil.withinBounds(mx, my, getX(), getY(), getW(), getH());
        if (button == 0 && hovered) {
            booleanProperty.setValue(!booleanProperty.getValue());
        }
    }
}
