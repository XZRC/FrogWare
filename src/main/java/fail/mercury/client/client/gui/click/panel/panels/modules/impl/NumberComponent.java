package fail.mercury.client.client.gui.click.panel.panels.modules.impl;

import fail.mercury.client.api.util.MathUtil;
import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;
import fail.mercury.client.api.util.MathUtil;
import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.gui.click.Menu;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;
import me.kix.lotus.property.impl.NumberProperty;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class NumberComponent extends Component {
    private NumberProperty numberProperty;
    private boolean sliding;

    public NumberComponent(NumberProperty numberProperty, float x, float y, float offsetx, float offsety, float w, float h) {
        super(numberProperty.getLabel(), x, y, offsetx, offsety, w, h);
        this.numberProperty = numberProperty;
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
        float length = MathHelper.floor((((Number) numberProperty.getValue()).floatValue() - numberProperty.getMinimum().floatValue()) / (numberProperty.getMaximum().floatValue() - numberProperty.getMinimum().floatValue()) * (getW() - 1));
        RenderUtil.drawRect2(getX(), getY(), getW(), getH(), new Color(5, 5, 5, 200).getRGB());
        RenderUtil.drawRect2(getX() + .5f, getY() + getH() - 1.5f, length, 1, Color.WHITE.getRGB());
        Menu.font.drawStringWithShadow(getLabel() + ": " + numberProperty.getValue(), getX() + 2, getY() + getH() / 2 - Menu.font.getHeight() / 2 + 1,  -1);
        if (sliding) {
            if (numberProperty.getValue() instanceof Float) {
                float preval = ((mx - getX()) * (numberProperty.getMaximum().floatValue() - numberProperty.getMinimum().floatValue()) / getW() + numberProperty.getMinimum().floatValue());
                numberProperty.setValue(MathUtil.roundFloat(preval, 2));
            } else if (numberProperty.getValue() instanceof Integer) {
                int preval = (int) ((mx - getX()) * (numberProperty.getMaximum().intValue() - numberProperty.getMinimum().intValue()) / getW() + numberProperty.getMinimum().intValue());
                numberProperty.setValue(preval);
            } else if (numberProperty.getValue() instanceof Double) {
                double preval = ((mx - getX()) * (numberProperty.getMaximum().doubleValue() - numberProperty.getMinimum().doubleValue()) / getW() + numberProperty.getMinimum().doubleValue());
                numberProperty.setValue(MathUtil.roundDouble(preval, 2));
            }
        }

    }

    @Override
    public void mouseClicked(int mx, int my, int button) {
        super.mouseClicked(mx, my, button);
        if ((MouseUtil.withinBounds(mx, my, getX(), getY(), getW(), getH())) && button == 0) sliding = true;
    }

    @Override
    public void mouseReleased(int mx, int my, int button) {
        super.mouseReleased(mx, my, button);
        if (sliding) sliding = false;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (sliding) sliding = false;
    }
}
