package fail.mercury.client.client.events;

import net.b0at.api.event.Event;
import net.b0at.api.event.types.EventTiming;
import net.minecraft.entity.Entity;

public class AttackEvent extends Event {

    private Entity entity;
    private EventTiming timing;

    public AttackEvent(EventTiming timing, Entity entity) {
        this.timing = timing;
        this.entity = entity;
    }

    public EventTiming getTiming() {
        return this.timing;
    }

    public Entity getEntity() {
        return entity;
    }
}
