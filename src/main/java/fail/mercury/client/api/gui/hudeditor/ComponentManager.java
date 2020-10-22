package fail.mercury.client.api.gui.hudeditor;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fail.mercury.client.api.manager.type.HashMapManager;
import fail.mercury.client.client.hudcomponents.ArrayList;
import fail.mercury.client.client.hudcomponents.Speed;
import fail.mercury.client.client.hudcomponents.TargetHUD;
import fail.mercury.client.client.hudcomponents.Watermark;

import java.io.*;

public class ComponentManager extends HashMapManager<String, HudComponent> {

    private File directory;

    public ComponentManager(File directory) {
        this.directory = directory;
        if (!directory.exists())
            directory.mkdir();
    }

    @Override
    public void load() {
        super.load();
        register(new Watermark(), new ArrayList(), new TargetHUD(), new Speed());
        getRegistry().values().forEach(HudComponent::init);
        loadComponents();
    }

    @Override
    public void unload() {
        saveComponents();
    }


    public void register(HudComponent... components) {
        for (HudComponent component : components) {
            if (component.getLabel() != null)
                include(component.getLabel().toLowerCase(), component);
        }
    }

    public void saveComponents() {
        if (getValues().isEmpty()) {
            directory.delete();
        }
        File[] files = directory.listFiles();
        if (!directory.exists()) {
            directory.mkdir();
        } else if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        getValues().forEach(comp -> {
            File file = new File(directory, comp.getLabel() + ".json");
            JsonObject node = new JsonObject();
            comp.save(node);
            if (node.entrySet().isEmpty()) {
                return;
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                return;
            }
            try (Writer writer = new FileWriter(file)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(node));
            } catch (IOException e) {
                file.delete();
            }
        });
        files = directory.listFiles();
        if (files == null || files.length == 0) {
            directory.delete();
        }
    }

    public void loadComponents() {
        getValues().forEach(comp -> {
            final File file = new File(directory, comp.getLabel() + ".json");
            if (!file.exists()) {
                return;
            }
            try (Reader reader = new FileReader(file)) {
                JsonElement node = new JsonParser().parse(reader);
                if (!node.isJsonObject()) {
                    return;
                }
                comp.load(node.getAsJsonObject());
            } catch (IOException e) {
            }
        });
    }

    public HudComponent find(Class<? extends HudComponent> clazz) {
        return getValues().stream().filter(m -> m.getClass() == clazz).findFirst().orElse(null);
    }

    public HudComponent find(String find) {
        HudComponent m = pull(find.replaceAll(" ", ""));
        if (pull(find.replaceAll(" ", "")) != null)
            m = pull(find.replaceAll(" ", ""));
        return m;
    }

}
