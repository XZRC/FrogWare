package fail.mercury.client.api.util;

/**
 * @author auto on 2/16/2020
 */
public class MouseUtil {

    public static boolean withinBounds(int mouseX, int mouseY, float x, float y, float width, float height) {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));
    }

}
