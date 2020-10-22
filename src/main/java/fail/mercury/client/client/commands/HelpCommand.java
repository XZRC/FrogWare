package fail.mercury.client.client.commands;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.command.Command;
import fail.mercury.client.api.command.annotation.CommandManifest;
import fail.mercury.client.api.util.ChatUtil;

import java.util.Arrays;

/**
 * @author auto on 2/13/2020
 */
@CommandManifest(label = "Help", description = "Lists Commands.")
public class HelpCommand extends Command {

    @Override
    public void onRun(final String[] args) {
        /*final TextComponentString msg = new TextComponentString("Commands (" + Mercury.INSTANCE.getModuleManager().getRegistry().values().size() + "): ");
        StringBuilder commands = new StringBuilder("Commands (" + Mercury.INSTANCE.getCommandManager().getRegistry().values().size() + "): ");
        Mercury.INSTANCE.getCommandManager().getRegistry().values()
                .forEach(cmd -> msg.appendSibling(new TextComponentString(cmd.getLabel() + "\247r, "))
                .setStyle(new Style()
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(cmd.getDescription().equals("") ? "This Command has no Description." : cmd.getDescription())))));
        mc.ingameGUI.getChatGUI().printChatMessage(msg);*/
        Mercury.INSTANCE.getCommandManager().getRegistry().values()
                .forEach(cmd -> {
                    //String usage = cmd.getUsage();
                    if (Arrays.stream(cmd.getAlias()).count() > 0) {
                        StringBuilder aliases = new StringBuilder();
                        Arrays.stream(cmd.getAlias()).forEach(alias -> aliases.append(alias).append("\247r, "));
                        ChatUtil.print(String.format("%s (%s) - %s", cmd.getLabel(), aliases.toString().substring(0, aliases.length() - 2), cmd.getDescription().equals("") ? "No Description found." : cmd.getDescription()));
                    } else {
                        ChatUtil.print(String.format("%s - %s", cmd.getLabel(), cmd.getDescription().equals("") ? "No Description found." : cmd.getDescription()));
                    }
                });
    }

}
