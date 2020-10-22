package fail.mercury.client.client.events;

import fail.mercury.client.api.util.Location;
import fail.mercury.client.api.util.Rotation;
import net.b0at.api.event.Event;
import net.b0at.api.event.types.EventTiming;

public class UpdateEvent extends Event {

    private EventTiming timing;
    private Location location;
    private Rotation rotation;

    public UpdateEvent(EventTiming timing, Location location, Rotation rotation) {
        this.timing = timing;
        this.location = location;
        this.rotation = rotation;
    }

    public EventTiming getTiming() {
        return this.timing;
    }

    public Location getLocation() {
        return this.location;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
