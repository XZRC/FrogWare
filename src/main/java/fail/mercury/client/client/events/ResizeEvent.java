package fail.mercury.client.client.events;

import net.b0at.api.event.Event;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author auto on 3/30/2020
 */
public class ResizeEvent extends Event {
    private ScaledResolution sr;

    public ResizeEvent(ScaledResolution sr) {
        this.sr = sr;
    }

    public ScaledResolution getSr() {
        return sr;
    }
}
