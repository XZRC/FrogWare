package fail.mercury.client;

import fail.mercury.client.api.command.CommandManager;
import fail.mercury.client.api.friend.FriendManager;
import fail.mercury.client.api.gui.hudeditor.ComponentManager;
import fail.mercury.client.api.module.ModuleManager;
import fail.mercury.client.api.profile.ProfileManager;
import fail.mercury.client.api.smalltext.SmallTextManager;
import fail.mercury.client.api.tickrate.TickRateManager;
import fail.mercury.client.api.translate.TranslationManager;
import fail.mercury.client.api.util.Util;
import me.kix.lotus.property.manage.PropertyManager;
import net.b0at.api.event.Event;
import net.b0at.api.event.EventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.File;

public class Mercury {

    public static Mercury INSTANCE = new Mercury();

    public static final String NAME = "Mercury";
    public static final String VERSION = "2.1";

    private final PropertyManager propertyManager = new PropertyManager();

    private EventManager<Event> eventManager = new EventManager<>(Event.class);

    private ModuleManager moduleManager;

    private ComponentManager componentManager;

    private CommandManager commandManager = new CommandManager();

    private FriendManager friendManager = new FriendManager();

    private TickRateManager tickRateManager = new TickRateManager();

    private ProfileManager profileManager = new ProfileManager();

    private SmallTextManager smallTextManager = new SmallTextManager();

    private TranslationManager translationManager = new TranslationManager();

    private File directory = new File(Util.mc.mcDataDir, NAME);

    public static final Logger log = LogManager.getLogger("Mercury");

    public void init() {
        try {

            if (!directory.exists())
                directory.mkdir();
            String title = String.format("%s %s | 1.12.2", getName(), getVersion());
            Display.setTitle(title);
            propertyManager.load();
            moduleManager = new ModuleManager(new File(directory, "modules"));
            moduleManager.load();
            componentManager = new ComponentManager(new File(directory, "hud"));
            componentManager.load();
            friendManager.setDirectory(new File(directory, "friends.json"));
            friendManager.load();
            commandManager.load();
            smallTextManager.load();
            profileManager.load();
            tickRateManager.load();
            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        moduleManager.unload();
        componentManager.unload();
        friendManager.unload();
        tickRateManager.unload();
    }

    public String getName() {
        return NAME;
    }

    public String getVersion() {
        return VERSION;
    }

    public File getDirectory() {
        return this.directory;
    }

    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    public EventManager<Event> getEventManager() {
        return this.eventManager;
    }

    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public ComponentManager getHudManager() {
        return this.componentManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public FriendManager getFriendManager() {
        return this.friendManager;
    }

    public TickRateManager getTickRateManager() {
        return this.tickRateManager;
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }

    public SmallTextManager getSmallTextManager() {
        return this.smallTextManager;
    }

    public TranslationManager getTranslationManager() {
        return this.translationManager;
    }

}
