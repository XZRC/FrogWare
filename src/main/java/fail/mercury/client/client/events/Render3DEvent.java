package fail.mercury.client.client.events;

import net.b0at.api.event.Event;

public class Render3DEvent extends Event {

    private float partialTicks;

    public Render3DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
