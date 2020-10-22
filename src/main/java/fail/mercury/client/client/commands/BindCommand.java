package fail.mercury.client.client.commands;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.command.Command;
import fail.mercury.client.api.command.annotation.CommandManifest;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.util.ChatUtil;
import org.lwjgl.input.Keyboard;

@CommandManifest(label = "Bind", description = "Binds modules to keys.", aliases = {"b"})
public class BindCommand extends Command {

    @Override
    public void onRun(final String[] args) {
        boolean found = false;
        if (args.length <= 1) {
            ChatUtil.print("Not enough args.");
            return;
        }
        final Module m = Mercury.INSTANCE.getModuleManager().getAlias(args[1]);
        if (m != null) {
            found = true;
            m.setBind(Keyboard.getKeyIndex(args[2].toUpperCase()));
            ChatUtil.print("Bound " + m.getLabel() + " to " + args[2]);
        }
        if (!found) {
            ChatUtil.print("Module " + args[1] + " not found.");
        }
    }
}
