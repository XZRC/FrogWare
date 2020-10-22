package fail.mercury.client.client.commands;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.command.Command;
import fail.mercury.client.api.command.annotation.CommandManifest;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.command.Command;
import fail.mercury.client.api.command.annotation.CommandManifest;
import fail.mercury.client.api.util.ChatUtil;

/**
 * @author auto on 2/2/2020
 */
@CommandManifest(label = "Friend", description = "Add/Remove/List friends.", aliases = {"f"})
public class FriendCommand extends Command {

    @Override
    public void onRun(final String[] args) {
        if (args.length <= 1) {
            ChatUtil.print("Not enough args.");
            return;
        }
        switch (args[1]) {
            case "add":
                if (args.length > 1) {
                    if (Mercury.INSTANCE.getFriendManager().isFriend(args[2])) {
                        ChatUtil.print(args[2] + " is already your friend.");
                        return;
                    }
                    if (args.length < 4) {
                        ChatUtil.print("Added " + args[2] + " to your friends list without an alias.");
                        Mercury.INSTANCE.getFriendManager().addFriend(args[2]);
                    } else {
                        ChatUtil.print("Added " + args[2] + " to your friends list with the alias " + args[3] + ".");
                        Mercury.INSTANCE.getFriendManager().addFriend(args[2], args[3]);
                    }
                }
                break;
            case "del":
            case "remove":
                if (args.length > 1) {
                    if (!Mercury.INSTANCE.getFriendManager().isFriend(args[2])) {
                        ChatUtil.print(args[2] + " is not your friend.");
                        return;
                    }
                    if (Mercury.INSTANCE.getFriendManager().isFriend(args[2])) {
                        ChatUtil.print("Removed " + args[2] + " from your friends list.");
                        Mercury.INSTANCE.getFriendManager().removeFriend(args[2]);
                    }
                }
                break;
            case "clear":
                if (Mercury.INSTANCE.getFriendManager().getRegistry().isEmpty()) {
                    ChatUtil.print("Your friends list is already empty.");
                    return;
                }
                ChatUtil.print("Your have cleared your friends list. Friends removed: " + Mercury.INSTANCE.getFriendManager().getRegistry().size());
                Mercury.INSTANCE.getFriendManager().clearFriends();
                break;
            case "list":
                if (Mercury.INSTANCE.getFriendManager().getRegistry().isEmpty()) {
                    ChatUtil.print("Your friends list is empty.");
                    return;
                }
                ChatUtil.print("Your current friends are: ");
                Mercury.INSTANCE.getFriendManager().getRegistry().forEach(friend -> {
                    ChatUtil.print("Username: " + friend.getName() + (friend.getAlias() != null ? (" - Alias: " + friend.getAlias()) : ""));
                });
                break;
        }
    }
}
