package fail.mercury.client.api.gui.hudeditor;


import com.google.gson.JsonObject;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.gui.hudeditor.annotation.HudManifest;
import fail.mercury.client.api.util.font.CFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class HudComponent {

    public float x, y, width, height, lastX, lastY;
    public String label;
    private boolean shown, dragging, showLabel;
    public Minecraft mc = Minecraft.getMinecraft();
    private ScaledResolution sr = new ScaledResolution(mc);
    public static CFontRenderer font = new CFontRenderer(new Font("Calibri", 0, 20), true, true);

    public HudComponent() {
        if (getClass().isAnnotationPresent(HudManifest.class)) {
            HudManifest hudManifest = getClass().getAnnotation(HudManifest.class);
            this.label = hudManifest.label();
            this.x = hudManifest.x();
            this.y = hudManifest.y();
            this.width = hudManifest.width();
            this.height = hudManifest.height();
            this.shown = hudManifest.shown();
            this.showLabel = hudManifest.showlabel();
        }
    }

    public void onDraw(ScaledResolution scaledResolution) {

    }

    public void onResize(ScaledResolution scaledResolution) {
        if (sr.getScaledWidth() < scaledResolution.getScaledWidth() && getX() > sr.getScaledWidth() - getW() - 20) {
            setX(scaledResolution.getScaledWidth() - getW() - 2);
        }
        if (sr.getScaledHeight() < scaledResolution.getScaledHeight() && getY() > sr.getScaledHeight() - getH() - 20) {
            setY(scaledResolution.getScaledHeight() - getH() - 2);
        }
        if (sr.getScaledHeight() != scaledResolution.getScaledHeight() || sr.getScaledWidth() != scaledResolution.getScaledWidth()) {
            sr = scaledResolution;
        }
    }

    public void onFullScreen(float w, float h) {
        if (sr.getScaledWidth() < w && getX() > sr.getScaledWidth() - getW() - 20) {
            setX(w - (sr.getScaledWidth() - getW()) - 2);
        }
        if (sr.getScaledHeight() < h && getY() > sr.getScaledHeight() - getH() - 20) {
            setY(h - (sr.getScaledHeight() - getH()) - 2);
        }
        if (sr.getScaledHeight() != new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() || sr.getScaledWidth() != new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
            sr = new ScaledResolution(Minecraft.getMinecraft());
        }
    }

    public void save(JsonObject directory) {
        directory.addProperty("x", x);
        directory.addProperty("y", y);
        directory.addProperty("shown", shown);
        if (Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(this) != null) {
            Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(this).forEach(property -> directory.addProperty(property.getLabel(), property.getValue().toString()));
        }
    }

    public void load(JsonObject directory) {
        directory.entrySet().forEach(data -> {
            switch (data.getKey()) {
                case "name":
                    return;
                case "x":
                    setX(data.getValue().getAsInt());
                    return;
                case "y":
                    setY(data.getValue().getAsInt());
                    return;
                case "shown":
                    setShown(data.getValue().getAsBoolean());
                    return;
            }
        });
        if (Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(this) != null) {
            directory.entrySet().forEach(entry -> Mercury.INSTANCE.getPropertyManager().getProperty(this, entry.getKey()).ifPresent(property -> property.setValue(entry.getValue().getAsString())));
        }
    }

    public void init() {
        Mercury.INSTANCE.getPropertyManager().scan(this);
    }


    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getW() {
        return this.width;
    }

    public float getH() {
        return this.height;
    }

    public String getLabel() {
        return this.label;
    }

    public float getLastX() {
        return lastX;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public boolean isLabelShown() {
        return this.showLabel;
    }

    public void setLabelShown(boolean shown) {
        this.showLabel = shown;
    }
}
