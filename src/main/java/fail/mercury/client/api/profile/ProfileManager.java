package fail.mercury.client.api.profile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fail.mercury.client.api.manager.type.HashMapManager;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.UUID;

public class ProfileManager extends HashMapManager<String, UUID> {

    private static final String NAME = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String PROFILE = "https://sessionserver.mojang.com/session/minecraft/profile/%s";

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void unload() {
        getRegistry().clear();
    }

    public UUID getUUID(final String name) {
        if (getRegistry().containsKey(name)) {
            return getRegistry().get(name);
        }
        try {
            final Reader uuidReader = new InputStreamReader(
                    new URL(String.format(NAME, name)).openStream());
            final JsonObject jsonObject = new JsonParser().parse(uuidReader).getAsJsonObject();
            String unfomatted = jsonObject.get("id").getAsString();
            String formatted = "";
            for (final int length : new int[] { 8, 4, 4, 4, 12 }) {
                formatted += "-";
                for (int i = 0; i < length; ++i) {
                    formatted += unfomatted.charAt(0);
                    unfomatted = unfomatted.substring(1);
                }
            }
            formatted = formatted.substring(1);
            final UUID uuid = UUID.fromString(formatted);
            getRegistry().put(name, uuid);
            return uuid;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String getName(final UUID uuid) {
        try {
            if (getRegistry().containsValue(uuid)) {
                return getRegistry().entrySet().stream().filter(entry -> uuid == entry.getValue())
                        .findFirst().get().getKey();
            }
        } catch (Exception ex) {
        }
        try {
            final Reader uuidReader = new InputStreamReader(
                    new URL(String.format(PROFILE,
                            uuid.toString().replaceAll("-", ""))).openStream());
            final JsonObject jsonObject = new JsonParser().parse(uuidReader).getAsJsonObject();
            final String name = jsonObject.get("name").getAsString();
            getRegistry().put(name, uuid);
            return name;
        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }
}
