package fail.mercury.client.api.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import fail.mercury.client.Mercury;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil implements Util {

    public static final void print(final String text) {
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(String.format(ChatFormatting.WHITE + "[" + ChatFormatting.AQUA +  "%s" + ChatFormatting.WHITE + "]:"+ ChatFormatting.WHITE + " %s", Mercury.INSTANCE.getName().substring(0,1), text)));
    }


}
