package fail.mercury.client.client.gui.click.panel.panels.modules.frame;

import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;
import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.client.gui.click.panel.panels.modules.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

public class Frame {
    private float x, y, lastx, lasty, w, h;
    private String label;
    private boolean dragging, extended;
    private ArrayList<Component> components = new ArrayList<>();

    public Frame(String label, float x, float y, float w, float h) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void init() {
        components.forEach(Component::init);
    }

    public void moved(float x, float y) {
        this.x = x;
        this.y = y;
        components.forEach(component -> component.moved(x, y));
    }

    public void drawScreen(int mx, int my, float partialTicks) {
        if (isDragging()) {
            setX(mx + getLastx());
            setY(my + getLasty());
            moved(getX(), getY());
        }
        if (getX() < 0) {
            setX(0);
            moved(getX(), getY());
        }
        if (getX() + getW() > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
            setX(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - getW());
            moved(getX(), getY());
        }
        if (getY() < 0) {
            setY(0);
            moved(getX(), getY());
        }
        if (getY() + getH() > new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) {
            setY(new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - getH());
            moved(getX(), getY());
        }
        if (isExtended())
            components.forEach(component -> component.drawScreen(mx, my, partialTicks));
    }

    public void mouseClicked(int mx, int my, int button) {
        final boolean hovered = MouseUtil.withinBounds(mx, my, getX(), getY(), getW(), getH());
        switch (button) {
            case 0:
                if (hovered) {
                    setDragging(true);
                    setLastx(getX() - mx);
                    setLasty(getY() - my);
                }
                break;
            case 1:
                if (hovered)
                    setExtended(!isExtended());
                break;
            default:
                break;
        }
        if (isExtended())
            components.forEach(component -> component.mouseClicked(mx, my, button));
    }

    public void mouseReleased(int mx, int my, int button) {
        switch (button) {
            case 0:
                if (isDragging()) {
                    setDragging(false);
                }
                break;
            default:
                break;
        }
        if (isExtended())
            components.forEach(component -> component.mouseReleased(mx, my, button));
    }

    public void onGuiClosed() {
        components.forEach(component -> component.onGuiClosed());
    }

    public void keyTyped(char character, int key) {
        if (isExtended())
            components.forEach(component -> component.keyTyped(character, key));
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public float getLastx() {
        return lastx;
    }

    public void setLastx(float lastx) {
        this.lastx = lastx;
    }

    public float getLasty() {
        return lasty;
    }

    public void setLasty(float lasty) {
        this.lasty = lasty;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }
}
