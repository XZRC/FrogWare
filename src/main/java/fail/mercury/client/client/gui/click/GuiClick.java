package fail.mercury.client.client.gui.click;

import fail.mercury.client.api.util.MouseUtil;
import fail.mercury.client.api.util.RenderUtil;
import fail.mercury.client.api.util.font.CFontRenderer;
import fail.mercury.client.client.gui.click.panel.Panel;
import fail.mercury.client.client.gui.click.panel.panels.*;
import fail.mercury.client.client.gui.click.panel.panels.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author auto on 2/16/2020
 */
public class GuiClick extends GuiScreen {

    //public static int posX, posY, lastPosX, lastPosY, width, height;
    public boolean dragging;
    public List<Panel> panels = new ArrayList<>();
    public Panel currentPanel;
    public static CFontRenderer font = new CFontRenderer(new Font("Calibri", 0, 20), true, true);

    public void init() {
        GL11.glColor4f(1, 1, 1, 1);
       // posX = 4;
       // posY = 100;
       // width = 400;
        //height = 250;
        //lastPosX = 2;
        //lastPosY = 2;
        panels.add(currentPanel = new MainPanel());
        panels.add(new ModulesPanel());
        panels.add(new FriendsPanel());
        panels.add(new ConfigPanel());
        panels.add(new SettingsPanel());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        /*if (dragging) {
            this.posX = mouseX + this.lastPosX;
            this.posY = mouseY + this.lastPosY;
        }*/
        /*RenderUtil.drawBorderedRect(posX, posY, posX + width, posY + height, 2, new Color(25,25,25).getRGB(), new Color(17,15,16,150).getRGB());
        Gui.drawRect(posX + 2, posY + 2, posX + width - 2, posY + height - 2, new Color(25,25,25).getRGB());
        RenderUtil.drawRoundedRect(posX + 40, posY + 25, posX + width - 100, posY + 35, 1, new Color(36, 36, 36).getRGB());
        Gui.drawRect(posX + 2, posY + 40, posX + width - 2, posY + height - 2, new Color(29,29,29).getRGB());

        currentPanel.draw(posX + 2, posY + 40, posX + width - 2, posY + height - 2);*/
        ScaledResolution res = new ScaledResolution(mc);
        int x = 0;
        for (Panel panel : panels) {
            float renderX = (res.getScaledWidth() / 2) + x - (panels.size() * 20);
            if (panel == currentPanel) {
                RenderUtil.drawBorderedRect(renderX - 2f, 2, renderX + font.getStringWidth(panel.getLabel()) + 1f, font.getHeight() + 3f, 1, new Color(0, 0, 0, 255).getRGB(), new Color(0, 0, 0, 137).getRGB());
            }
            font.drawStringWithShadow(panel.getLabel(), renderX, 3, -1);
            x += font.getStringWidth(panel.getLabel()) + 10;
        }
        currentPanel.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        ScaledResolution res = new ScaledResolution(mc);
        int x = 0;
        for (Panel panel : panels) {
            float renderX = (res.getScaledWidth() / 2) + x - (panels.size() * 20);
            if (MouseUtil.withinBounds(mouseX, mouseY, renderX - 2f, 2, renderX + font.getStringWidth(panel.getLabel()) + 1f, font.getHeight() + 3f)) {
                currentPanel = panel;
            }
            x += font.getStringWidth(panel.getLabel()) + 10;
        }
        /*if (MouseUtil.withinBounds(mouseX, mouseY, posX, posY, posX + width, posY + 20)) {
            if (mouseButton == 0) {
                dragging = true;
                this.lastPosX = (posX - mouseX);
                this.lastPosY = (posY - mouseY);
            }
        }*/
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        /*if (mouseButton == 0) {
            dragging = false;
        }*/
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (key == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().player.closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

}
