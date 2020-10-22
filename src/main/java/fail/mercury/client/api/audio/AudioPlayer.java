package fail.mercury.client.api.audio;

import fail.mercury.client.api.util.Util;
import net.minecraft.util.ResourceLocation;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class AudioPlayer implements Util {

    static Clip clip;
    ResourceLocation location;

    public AudioPlayer(ResourceLocation location) {
        if (clip == null) {
            try {
                clip = AudioSystem.getClip();
                this.location = location;
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    public void play() {
        if (clip != null && location != null) {
            try {
                InputStream in = mc.getResourceManager().getResource(location).getInputStream();
                System.out.println(mc.getResourceManager().getResource(location).getInputStream());
                clip.open(AudioSystem.getAudioInputStream(in));
                clip.start();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                e.printStackTrace();
            }

        }
    }

    public void stop() {
        if (clip != null && location != null && clip.isActive()) {
            clip.stop();
        }
    }

    public void close() {
        if (clip != null && location != null && clip.isOpen()) {
            clip.close();
        }
    }

    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

}
