package fail.mercury.client.client.gui.click.panel.panels.modules.impl;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.api.util.TimerUtil;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.api.util.TimerUtil;
import fail.mercury.client.client.gui.click.Menu;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;
import me.kix.lotus.property.IProperty;
import me.kix.lotus.property.impl.BooleanProperty;
import me.kix.lotus.property.impl.NumberProperty;
import me.kix.lotus.property.impl.string.impl.ModeStringProperty;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ModuleComponent extends Component {
    private Module module;
    private boolean extended, binding;
    public TimerUtil descTimer = new TimerUtil();

    public ModuleComponent(Module module, float x, float y, float offsetx, float offsety, float w, float h) {
        super(module.getFakeLabel().equals("") ? module.getLabel() : module.getFakeLabel(), x, y, offsetx, offsety, w, h);
        this.module = module;
    }

    @Override
    public void init() {
        super.init();
        if (Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(module) != null) {
            float offsetY = getH();
            for (IProperty property : Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(module)) {
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
        RenderUtil.drawRect2(getX(), getY(), getW(), getH(), hovered ? new Color(0, 0, 0, 200).getRGB() : (this.module.isEnabled() ? new Color(5, 5, 5, 200).getRGB() : new Color(14, 14, 14, 200).getRGB()));
        if (this.module.isEnabled()) {
            RenderUtil.drawRect2(getX(), getY(), 1, getH(), Color.CYAN.darker().getRGB());
        }
        try {
            if (!Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(this.module).isEmpty()) {
                Menu.font.drawStringWithShadow(this.isExtended() ? "-" : "+", (getX() + getW() - 10), getY() + getH() / 2 - Menu.font.getHeight() / 2, new Color(200, 200, 200, 255).getRGB());
            }
        }
        catch (Exception ex) {}
        Menu.font.drawStringWithShadow(isBinding() ? "Press a key..." : getLabel(), getX() + getW() / 2 - Menu.font.getStringWidth(isBinding() ? "Press a key..." : getLabel()) / 2, getY() + getH() / 2 - Menu.font.getHeight() / 2, module.isEnabled() ? -1 : 0xff707070);
        if (hovered) {
            if (!this.module.getDescription().equals("") && descTimer.hasReached(1000)) {
                Menu.font.drawCenteredStringWithShadow(module.getDescription(), mx + 4, my - 5, -1);
            }
        } else {
            descTimer.reset();
        }
        if (isExtended())
            super.drawScreen(mx, my, partialTicks);
    }

    @Override
    public void mouseClicked(int mx, int my, int button) {
        final boolean hovered = MouseUtil.withinBounds(mx, my, getX(), getY(), getW(), getH());
        switch (button) {
            case 0:
                if (hovered && !isBinding())
                    module.setEnabled(!module.isEnabled());
                break;
            case 1:
                if (hovered && !getSubComponents().isEmpty())
                    setExtended(!isExtended());
                break;
            case 2:
                if (hovered)
                    setBinding(!isBinding());
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

    @Override
    public void keyTyped(char character, int key) {
        super.keyTyped(character, key);
        if (isBinding() && module instanceof Module) {
            module.setBind(key == Keyboard.KEY_ESCAPE || key == Keyboard.KEY_SPACE || key == Keyboard.KEY_DELETE ? Keyboard.KEY_NONE : key);
            ChatUtil.print("Bound " + getLabel() + " to " + Keyboard.getKeyName(module.getBind()));
            setBinding(false);
        }
    }

    public Module getModule() {
        return module;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public boolean isBinding() {
        return binding;
    }

    public void setBinding(boolean binding) {
        this.binding = binding;
    }
}
