package fail.mercury.client.client.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.command.Command;
import fail.mercury.client.api.command.annotation.CommandManifest;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.util.ChatUtil;

@CommandManifest(label = "Toggle", aliases = {"t"}, description = "Toggles modules.")
public class ToggleCommand extends Command {

    @Override
    public void onRun(final String[] s) {
        boolean found = false;
        if (s.length <= 1) {
            ChatUtil.print("Not enough args.");
            return;
        }
        final Module m = Mercury.INSTANCE.getModuleManager().getAlias(s[1]);
        if (m != null) {
            found = true;
            ChatUtil.print("Toggled " + m.getLabel() + (m.isEnabled() ? ChatFormatting.RED + " OFF" : ChatFormatting.GREEN + " ON") + ChatFormatting.WHITE + "!");
            m.toggle();
        }
        if (!found) {
            ChatUtil.print("Module " + s[1] + " not found.");
        }
    }

}
