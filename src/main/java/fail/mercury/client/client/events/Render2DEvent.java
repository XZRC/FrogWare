package fail.mercury.client.client.events;

import net.b0at.api.event.Event;

public class Render2DEvent extends Event {
    private final float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}

