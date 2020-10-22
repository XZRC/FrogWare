package fail.mercury.client.client.events;

import net.b0at.api.event.Event;

public class KeypressEvent extends Event {

    private final int key;

    public KeypressEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
