package fail.mercury.client.client.gui.hudeditor;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.client.gui.click.Menu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;

/**
 * @author auto on 3/30/2020
 */
public class GuiHud extends GuiScreen {
    private ScaledResolution scaledResolution = null;

    @Override
    public void drawScreen(int mx, int my, float p_drawScreen_3_) {
        super.drawScreen(mx, my, p_drawScreen_3_);
        if (scaledResolution == null) scaledResolution = new ScaledResolution(mc);
        Mercury.INSTANCE.getHudManager().getValues().forEach(hudComponent -> {
            if (hudComponent.isDragging()) {
                hudComponent.setX(mx + hudComponent.getLastX());
                hudComponent.setY(my + hudComponent.getLastY());
            }
            if (hudComponent.getX() < 0) {
                hudComponent.setX(0);
            }
            if (hudComponent.getX() + hudComponent.getW() > new ScaledResolution(mc).getScaledWidth()) {
                hudComponent.setX(new ScaledResolution(mc).getScaledWidth() - hudComponent.getW());
            }
            if (hudComponent.getY() < 0) {
                hudComponent.setY(0);
            }
            if (hudComponent.getY() + hudComponent.getH() > new ScaledResolution(mc).getScaledHeight()) {
                hudComponent.setY(new ScaledResolution(mc).getScaledHeight() - hudComponent.getH());
            }
            //RenderUtil.drawRect2(0, scaledResolution.getScaledHeight() / 2 - 0.5f, scaledResolution.getScaledWidth(), 1, 0xff000000);
            //RenderUtil.drawRect2(scaledResolution.getScaledWidth() / 2 - 0.5f, 0, 1, scaledResolution.getScaledHeight(), 0xff000000);
            if (hudComponent.isShown()) hudComponent.onDraw(new ScaledResolution(mc));

            RenderUtil.drawRect2(hudComponent.getX(), hudComponent.getY(), hudComponent.getW(), hudComponent.getH(), hudComponent.isDragging() ? 0x95000000 : 0x80000000);

            if (hudComponent.isLabelShown()) Menu.font.drawStringWithShadow(hudComponent.getLabel(), hudComponent.getX() + hudComponent.getW() / 2 - mc.fontRenderer.getStringWidth(hudComponent.getLabel()) / 2, hudComponent.getY() + hudComponent.getH() / 2 - mc.fontRenderer.FONT_HEIGHT / 2, new Color(255, 255, 255, 83).getRGB());
        });
    }

    @Override
    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
        super.keyTyped(p_keyTyped_1_, p_keyTyped_2_);
    }

    @Override
    protected void mouseClicked(int mx, int my, int p_mouseClicked_3_) throws IOException {
        super.mouseClicked(mx, my, p_mouseClicked_3_);
        Mercury.INSTANCE.getHudManager().getValues().forEach(hudComponent -> {
            final boolean hovered = MouseUtil.withinBounds(mx, my, hudComponent.getX(), hudComponent.getY(), hudComponent.getW(), hudComponent.getH());
            switch (p_mouseClicked_3_) {
                case 0:
                    if (hovered) {
                        hudComponent.setDragging(true);
                        hudComponent.setLastX(hudComponent.getX() - mx);
                        hudComponent.setLastY(hudComponent.getY() - my);
                    }
                    break;
                case 1:
                    if (hovered)
                        hudComponent.setShown(!hudComponent.isShown());
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void mouseReleased(int mx, int my, int p_mouseReleased_3_) {
        super.mouseReleased(mx, my, p_mouseReleased_3_);
        Mercury.INSTANCE.getHudManager().getValues().forEach(hudComponent -> {
            if (p_mouseReleased_3_ == 0) {
                if (hudComponent.isDragging()) {
                    hudComponent.setDragging(false);
                }
            }
        });
    }

    @Override
    public void onGuiClosed() {
        Mercury.INSTANCE.getHudManager().getValues().forEach(hudComponent -> {
            if (hudComponent.isDragging()) {
                hudComponent.setDragging(false);
            }
        });
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

