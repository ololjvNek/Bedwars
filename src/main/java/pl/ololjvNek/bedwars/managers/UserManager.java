package pl.ololjvNek.bedwars.managers;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.data.User;
import pl.ololjvNek.bedwars.utils.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    @Getter
    private static HashMap<UUID, User> users = new HashMap<>();

    public static User createUser(Player player){
        User u = new User(player.getUniqueId());
        users.put(u.getUUID(), u);
        Logger.info("[UserManager] Created new player " + u.getUUID().toString());
        return u;
    }

    public static User getUser(Player player){
        return users.get(player.getUniqueId());
    }

    public static User getUser(UUID uuid){
        return users.get(uuid);
    }

    public static User getUserByLastName(String lastName){
        for(User u : users.values()){
            if(u.getLastName().equals(lastName)){
                return u;
            }
        }
        return null;
    }

    public static void loadUsers() {
        ResultSet rs = Main.getStore().query("SELECT * FROM `users`");
        try {
            while (rs.next()) {
                User u = new User(rs);
                users.put(u.getUUID(), u);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Logger.info("[UserManager] Loaded " + users.size() + " players");
    }
}
