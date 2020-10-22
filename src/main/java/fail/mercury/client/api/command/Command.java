package fail.mercury.client.api.command;

import fail.mercury.client.api.command.annotation.CommandManifest;
import net.minecraft.client.Minecraft;

public class Command {

    public Minecraft mc = Minecraft.getMinecraft();

    private String label;

    private String description;

    private String usage;

    private String[] alias;

    public Command() {
        if (getClass().isAnnotationPresent(CommandManifest.class)) {
            CommandManifest moduleManifest = getClass().getAnnotation(CommandManifest.class);
            this.label = moduleManifest.label();
            this.alias = moduleManifest.aliases();
            this.description = moduleManifest.description();
            this.usage = moduleManifest.usage();
        }
    }

    public void onRun(final String[] s) {
    }
    public String getLabel() {
        return this.label;
    }

    public String getDescription() {
        return this.description;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public String getUsage() {
        return this.usage;
    }
}

