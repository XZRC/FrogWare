package fail.mercury.client.client.events;

import net.b0at.api.event.Event;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {

    private Packet packet;
    private Type type;

    public PacketEvent(Type type, final Packet packet) {
        this.type = type;
        this.packet = packet;
    }

    public Type getType() {
        return this.type;
    }

    public void setPacket(final Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public enum Type {
        INCOMING,
        OUTGOING
    }

}
