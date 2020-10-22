package fail.mercury.client.api.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.api.manager.type.HashMapManager;
import fail.mercury.client.client.commands.*;
import fail.mercury.client.client.commands.*;
import me.kix.lotus.property.impl.BooleanProperty;
import me.kix.lotus.property.impl.NumberProperty;
import me.kix.lotus.property.impl.string.StringProperty;
import me.kix.lotus.property.impl.string.impl.ModeStringProperty;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.HashMap;

public class CommandManager extends HashMapManager<String, Command> {

    private HashMap<String, Command> aliasMap = new HashMap<>();

    @Override
    public void load() {
        super.load();
        initialize();
    }

    public void initialize() {
        register(new ToggleCommand(), new BindCommand(), new ModulesCommand(), new FriendCommand()
                , new HClipCommand(), new SexCommand(), new HelpCommand(), new MobOwnerCommand());
    }

    public HashMap<String, Command> getAliasMap() {
        return this.aliasMap;
    }

    public void register(Command... commands) {
        for (Command command : commands) {
            include(command.getLabel().toLowerCase(), command);
            if (command.getAlias().length > 0) {
                for (String com : command.getAlias()) {
                    aliasMap.put(com.toLowerCase(), command);
                }
            }
        }
    }

    public void dispatch(final String s) {
        final String[] command = s.split(" ");
        if (command.length > 1) {
            Module m = Mercury.INSTANCE.getModuleManager().find(command[0]);
            if (m != null) {
                if (command[1].equals("help")) {
                    try {
                        ChatUtil.print(m.getLabel() + "'s available properties (" + Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(m).size() + ") are:");
                        Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(m).forEach(prop -> ChatUtil.print(prop.getLabel() + ": " + prop.getValue()));
                    } catch (NullPointerException ex) {
                        ChatUtil.print("This module has no properties.");
                    }
                    ChatUtil.print(m.getLabel() + " is bound to " + Keyboard.getKeyName(m.getBind()) + ".");
                    if (!m.getDescription().equals(""))
                        ChatUtil.print("Desc: " + m.getDescription());
                    return;
                }
                if (command.length >= 2) {
                    Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(m).stream()
                            .filter(property -> command[1].equalsIgnoreCase(property.getLabel()))
                            .forEach(property -> {
                                if (property instanceof BooleanProperty) {
                                    BooleanProperty booleanProperty = (BooleanProperty) property;
                                    booleanProperty.setValue(!booleanProperty.getValue());
                                    ChatUtil.print(booleanProperty.getLabel() + " has been " + (booleanProperty.getValue() ? "\247aenabled\2477" : "\247cdisabled\2477") + " for " + m.getLabel() + ".");
                                } else if (property instanceof ModeStringProperty) {
                                    ModeStringProperty modeProperty = (ModeStringProperty) property;
                                    if (command.length >= 3) {
                                        modeProperty.setValue(command[2]);
                                        ChatUtil.print(modeProperty.getLabel() + " has been set to " + modeProperty.getValue() + " for " + ChatFormatting.AQUA + m.getLabel() + ChatFormatting.WHITE + ".");
                                    } else if (command.length >= 2) {
                                        ChatUtil.print(m.getLabel() + "'s available " + modeProperty.getLabel() + "s are:");
                                        Arrays.stream(modeProperty.getModes()).forEach(prop -> ChatUtil.print(modeProperty.getValue().toLowerCase().equals(prop.toLowerCase()) ? ChatFormatting.GRAY + prop : prop));
                                    }
                                } else if (property instanceof NumberProperty) {
                                    NumberProperty numberProperty = (NumberProperty) property;
                                    if (command.length >= 3) {
                                        try {
                                            numberProperty.setValue(command[2]);
                                            ChatUtil.print(numberProperty.getLabel() + " has been set to " + numberProperty.getValue() + " for " + ChatFormatting.AQUA + m.getLabel() + ChatFormatting.WHITE + ".");
                                        } catch (NumberFormatException ex) {
                                            ChatUtil.print(command[2] + " is not a number.");
                                        }
                                    } else {
                                        ChatUtil.print("Not enough arguments to change property.");
                                    }
                                } else if (property instanceof StringProperty) {
                                    StringProperty stringProperty = (StringProperty) property;
                                    if (command.length >= 3) {
                                        stringProperty.setValue(s.substring((m.getLabel().length() + stringProperty.getLabel().length() + 2)));
                                        ChatUtil.print(stringProperty.getLabel() + " has been set to "+ ChatFormatting.GRAY + stringProperty.getValue() + ChatFormatting.WHITE + " for " + ChatFormatting.AQUA + m.getLabel() + ChatFormatting.WHITE + ".");
                                    } else {
                                        ChatUtil.print("Not enough arguments to change property.");
                                    }
                                } /*else {
                                    if (command.length >= 3) {
                                        property.setValue(command[2]);
                                        ChatUtil.print(property.getLabel() + " has been set to " + property.getValue() + " for " + m.getLabel() + ".");
                                    } else {
                                        ChatUtil.print("Not enough arguments to change property.");
                                    }
                                }*/
                            });
                } else {
                    ChatUtil.print("Property not found! Do ." + command[0] + " help for list of properties.");
                }
            }
        }
        Command c = getRegistry().get(command[0]);
        if (c != null) {
            c.onRun(command);
        }
        Command cc = getAliasMap().get(command[0]);
        if (cc != null) {
            cc.onRun(command);
        }

    }
}
