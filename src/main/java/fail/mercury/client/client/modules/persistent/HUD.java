package fail.mercury.client.client.modules.persistent;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.FullScreenEvent;
import fail.mercury.client.client.events.Render2DEvent;
import fail.mercury.client.client.events.ResizeEvent;
import fail.mercury.client.client.gui.hudeditor.GuiHud;
import net.b0at.api.event.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author auto on 3/30/2020
 */
@ModuleManifest(label = "HUD", aliases = {"Overlay"}, category = Category.VISUAL, persistent = true, hidden = true)
public class HUD extends Module {

    @EventHandler
    public void onRender(Render2DEvent eventRender) {
        if (mc.gameSettings.showDebugInfo || mc.currentScreen instanceof GuiHud) return;
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        Mercury.INSTANCE.getHudManager().getValues().forEach(hudComponent -> {
            if (hudComponent.getX() < 0) {
                hudComponent.setX(0);
            }
            if (hudComponent.getX() + hudComponent.getW() > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
                hudComponent.setX(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - hudComponent.getW());
            }
            if (hudComponent.getY() < 0) {
                hudComponent.setY(0);
            }
            if (hudComponent.getY() + hudComponent.getH() > new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) {
                hudComponent.setY(new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - hudComponent.getH());
            }
            if (hudComponent.isShown()) hudComponent.onDraw(new ScaledResolution(mc));
        });
    }
    @EventHandler
    public void onScreenResize(ResizeEvent event) {
        Mercury.INSTANCE.getHudManager().getValues().forEach(hudComponent -> {
            if (hudComponent.isShown()) {
                hudComponent.onResize(event.getSr());
            }
        });
    }

    @EventHandler
    public void onFullScreen(FullScreenEvent event) {
        Mercury.INSTANCE.getHudManager().getValues().forEach(hudComponent -> {
            if (hudComponent.isShown()) {
                hudComponent.onFullScreen(event.getWidth(), event.getHeight());
            }
        });
    }

}

