package fail.mercury.client.api.tickrate;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.manager.IManager;
import fail.mercury.client.client.events.PacketEvent;
import net.b0at.api.event.EventHandler;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;

public final class TickRateManager implements IManager {

    private long prevTime;
    private float[] ticks = new float[20];
    private int currentTick;

    @Override
    public void load() {
        this.prevTime = -1;

        for (int i = 0, len = this.ticks.length; i < len; i++) {
            this.ticks[i] = 0.0f;
        }

        Mercury.INSTANCE.getEventManager().registerListener(this);
    }
    public float getTickRate() {
        int tickCount = 0;
        float tickRate = 0.0f;

        for (int i = 0; i < this.ticks.length; i++) {
            final float tick = this.ticks[i];

            if (tick > 0.0f) {
                tickRate += tick;
                tickCount++;
            }
        }

        return MathHelper.clamp((tickRate / tickCount), 0.0f, 20.0f);
    }

    @Override
    public void unload() {
        Mercury.INSTANCE.getEventManager().deregisterListener(this);
    }

    @EventHandler
    public void receivePacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.INCOMING)) {
            if (event.getPacket() instanceof SPacketTimeUpdate) {
                if (this.prevTime != -1) {
                    this.ticks[this.currentTick % this.ticks.length] = MathHelper.clamp((20.0f / ((float) (System.currentTimeMillis() - this.prevTime) / 1000.0f)), 0.0f, 20.0f);
                    this.currentTick++;
                }

                this.prevTime = System.currentTimeMillis();
            }
        }
    }
}