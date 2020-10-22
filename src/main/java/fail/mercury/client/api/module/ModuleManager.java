package fail.mercury.client.api.module;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fail.mercury.client.api.util.Util;
import fail.mercury.client.client.modules.combat.*;
import fail.mercury.client.client.modules.misc.*;
import fail.mercury.client.client.modules.movement.*;
import fail.mercury.client.client.modules.player.*;
import fail.mercury.client.client.modules.visual.*;
import fail.mercury.client.api.manager.type.HashMapManager;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.modules.combat.*;
import fail.mercury.client.client.modules.misc.*;
import fail.mercury.client.client.modules.movement.*;
import fail.mercury.client.client.modules.persistent.Commands;
import fail.mercury.client.client.modules.persistent.HUD;
import fail.mercury.client.client.modules.persistent.KeyBinds;
import fail.mercury.client.client.modules.player.*;
import fail.mercury.client.client.modules.visual.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager extends HashMapManager<String, Module> implements Util {

    private File directory;

    public ModuleManager(File directory) {
        this.directory = directory;
        if (!directory.exists())
            directory.mkdir();
    }

    @Override
    public void load() {
        super.load();
        register(new Commands(), new KeyBinds(), new Sprint(), new Speed(), new Velocity(), new KillAura(), new Flight(), new Timer()
                , new NoRotate(), new NoFall(), new VisualRange(), new Criticals(), new DiscordRPC()
                , new AutoFish(), new ChatSuffix(), new Phase(), new Revive(), new Freecam(), new Crasher()
                , new AntiCrash(), new AutoReply(), new FurryChat(), new FastBow(), new FastPlace(), new AutoTotem()
                , new AutoWeb(), new Surround(), new AntiHunger(), new GodMode(), new AntiChainPop(), new NoPush(), new Step()
                , new TotemDetector(), new CaptchaSolver(), new Jesus(), new EntityESP(), new Handshake(), new AntiVoid(), new FullBright()
                , new PacketCancel(), new SpeedMine(), new KeepBreak(), new BanWave(), new ArmorBreaker(), new HoleESP()
                , new HitEffects(), new Scaffold(), new HUD(), new Translator(), new ScreenMove(), new Location());
        getRegistry().values().forEach(Module::init);
        this.loadCheats();
    }

    public List<Module> getToggles() {
        List<Module> toggleableModules = new ArrayList<>();
        for(Module module : getValues()) {
            if(module instanceof Module && !module.isPersistent())
                toggleableModules.add(module);
        }
        return toggleableModules;
    }


    @Override
    public void unload() {
        this.saveCheats();
    }

    public Module getAlias(final String name) {
        for (final Module f : this.getRegistry().values()) {
            if (f.getLabel().equalsIgnoreCase(name)) {
                return f;
            }
            String[] alias;
            for (int length = (alias = f.getAlias()).length, i = 0; i < length; ++i) {
                final String s = alias[i];
                if (s.equalsIgnoreCase(name)) {
                    return f;
                }
            }
        }
        return null;
    }

    public ArrayList<Module> getModsInCategory(final Category category) {
        final ArrayList<Module> mods = new ArrayList<>();
        for (final Module m : this.getRegistry().values()) {
            if (m.getCategory().equals(category)) {
                mods.add(m);
            }
        }
        return mods;
    }

    public void register(Module... modules) {
        for (Module cheat : modules) {
            if (cheat.getLabel() != null)
            include(cheat.getLabel().toLowerCase(), cheat);
        }
    }

    public Module find(Class<? extends Module> clazz) {
        return getValues().stream().filter(m -> m.getClass() == clazz).findFirst().orElse(null);
    }

    public Module find(String find) {
        Module m = pull(find.replaceAll(" ", ""));
        if (pull(find.replaceAll(" ", "")) != null)
            m = pull(find.replaceAll(" ", ""));
        if (getAlias(find) != null)
            m = getAlias(find);
        return m;
    }

    public void saveCheats() {
        if (getRegistry().values().isEmpty()) {
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
        getRegistry().values().forEach(module -> {
            File file = new File(directory, module.getLabel() + ".json");
            JsonObject node = new JsonObject();
            module.save(node);
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

    public void loadCheats() {
        getRegistry().values().forEach(module -> {
            if (module.isPersistent())
                module.setEnabled(true);
            final File file = new File(directory, module.getLabel() + ".json");
            if (!file.exists()) {
                return;
            }
            try (Reader reader = new FileReader(file)) {
                JsonElement node = new JsonParser().parse(reader);
                if (!node.isJsonObject()) {
                    return;
                }
                module.load(node.getAsJsonObject());
            } catch (IOException e) {
            }
        });
    }


}
