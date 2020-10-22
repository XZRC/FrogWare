package fail.mercury.client.client.events;

import fail.mercury.client.api.util.Rotation;
import net.b0at.api.event.Event;

/**
 * @author auto on 4/6/2020
 */
public class RotationEvent extends Event {

    private Rotation rotation, prevRotation;
    private float renderYawOffset, prevRenderYawOffset;


    public RotationEvent(Rotation rotation, Rotation prevRotation, float renderYawOffset, float prevRenderYawOffset) {
        this.rotation = rotation;
        this.prevRotation = prevRotation;
        this.renderYawOffset = renderYawOffset;
        this.prevRenderYawOffset = prevRenderYawOffset;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public Rotation getPrevRotation() {
        return prevRotation;
    }

    public float getRenderYawOffset() {
        return renderYawOffset;
    }

    public float getPrevRenderYawOffset() {
        return prevRenderYawOffset;
    }

    public void setRenderYawOffset(float renderYawOffset) {
        this.renderYawOffset = renderYawOffset;
    }

    public void setPrevRenderYawOffset(float prevRenderYawOffset) {
        this.prevRenderYawOffset = prevRenderYawOffset;
    }
}
