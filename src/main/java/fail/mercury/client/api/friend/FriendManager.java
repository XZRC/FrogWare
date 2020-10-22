package fail.mercury.client.api.friend;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import fail.mercury.client.api.manager.type.ListManager;

import java.io.*;
import java.util.ArrayList;

public class FriendManager extends ListManager<Friend> {

    @Override
    public void load() {
        if (!directory.exists()) {
            try {
                directory.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadFriends();
    }

    @Override
    public void unload() {
        saveFriends();
    }

    private File directory;

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void saveFriends() {
        if (directory.exists()) {
            try (Writer writer = new FileWriter(directory)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(getRegistry()));
            } catch (IOException e) {
                directory.delete();
            }
        }
     }

    public void loadFriends() {
        if (!directory.exists()) {
            return;
        }
        try (FileReader inFile = new FileReader(directory)) {
            setRegistry(new GsonBuilder().setPrettyPrinting().create().fromJson(inFile, new TypeToken<ArrayList<Friend>>() {
            }.getType()));

            if (getRegistry() == null)
                setRegistry(new ArrayList<>());

        } catch (Exception ignored) {
        }
    }

    public void addFriend(String name) {
        include(new Friend(name));
    }

    public void addFriend(String name, String alias) {
        include(new Friend(name, alias));
    }

    public Friend getFriend(String ign) {
        for (Friend friend : getRegistry()) {
            if (friend.getName().equalsIgnoreCase(ign)) {
                return friend;
            }
        }
        return null;
    }

    public boolean isFriend(String ign) {
        return getFriend(ign) != null;
    }

    public void clearFriends() {
        clear();
    }

    public void removeFriend(String name) {
        Friend f = getFriend(name);
        if (f != null) {
            remove(f);
        }
    }
}

