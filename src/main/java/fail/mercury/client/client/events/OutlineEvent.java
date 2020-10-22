package fail.mercury.client.client.events;

import net.b0at.api.event.Event;
import net.minecraft.entity.Entity;

/**
 * @author auto on 2/29/2020
 */
public class OutlineEvent extends Event {

    private Entity entity;

    public OutlineEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
