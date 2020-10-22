package fail.mercury.client.client.commands;

import fail.mercury.client.api.command.Command;
import fail.mercury.client.api.command.annotation.CommandManifest;
import fail.mercury.client.api.util.ChatUtil;

/**
 * @author auto on 4/3/2020
 */
@CommandManifest(label = "Translate", description = "Translates outgoing messages.")
public class TranslateCommand extends Command {

    @Override
    public void onRun(final String[] s) {
        if (s.length <= 3) {
            ChatUtil.print("Not enough args.");
            return;
        }
        String messageToTranslate = s[3];

    }
    
}
