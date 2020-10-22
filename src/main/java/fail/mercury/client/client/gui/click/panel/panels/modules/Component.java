package fail.mercury.client.client.gui.click.panel.panels.modules;

import java.util.ArrayList;

public class Component {
    private float x, y,offsetx,offsety, w, h;
    private String label;
    private ArrayList<Component> subComponents = new ArrayList<>();
    public Component(String label, float x, float y,float offsetx,float offsety, float w, float h) {
        this.label = label;
        this.x = x + offsetx;
        this.y = y + offsety;
        this.w = w;
        this.h = h;
        this.offsetx = offsetx;
        this.offsety = offsety;
    }

    public void init() {
        subComponents.forEach(Component::init);
    }

    public void moved(float x, float y) {
        this.x = x + offsetx;
        this.y = y + offsety;
        subComponents.forEach(subComponents->subComponents.moved(getX(),getY()));
    }

    public void onGuiClosed() {
        subComponents.forEach(subComponents->subComponents.onGuiClosed());
    }

    public void drawScreen(int mx, int my, float partialTicks) {
        subComponents.forEach(subComponents->subComponents.drawScreen(mx,my,partialTicks));
    }

    public void mouseClicked(int mx, int my, int button) {
        subComponents.forEach(subComponents->subComponents.mouseClicked(mx,my,button));
    }

    public void mouseReleased(int mx, int my, int button) {
        subComponents.forEach(subComponents->subComponents.mouseReleased(mx,my,button));
    }

    public void keyTyped(char character,int key) {
        subComponents.forEach(subComponents->subComponents.keyTyped(character,key));
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

    public float getOffsetx() {
        return offsetx;
    }

    public void setOffsetx(float offsetx) {
        this.offsetx = offsetx;
    }

    public float getOffsety() {
        return offsety;
    }

    public void setOffsety(float offsety) {
        this.offsety = offsety;
    }

    public ArrayList<Component> getSubComponents() {
        return subComponents;
    }
}
