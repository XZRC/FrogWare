package fail.mercury.client.api.util;

/**
 * @author auto on 4/6/2020
 */
public class Rotation {

    private float yaw, pitch;
    private boolean active;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Rotation add(float yaw, float pitch) {
        this.yaw += yaw;
        this.pitch += pitch;
        return this;
    }

    public Rotation subtract(float yaw, float pitch) {
        this.yaw -= yaw;
        this.pitch -= pitch;
        return this;
    }

    public float getYaw() {
        return yaw;
    }

    public Rotation setYaw(float yaw) {
        active = true;
        this.yaw = yaw;
        return this;
    }

    public float getPitch() {
        return pitch;
    }

    public Rotation setPitch(float pitch) {
        active = true;
        this.pitch = pitch;
        return this;
    }

    public boolean isActive() {
        return active;
    }
}

