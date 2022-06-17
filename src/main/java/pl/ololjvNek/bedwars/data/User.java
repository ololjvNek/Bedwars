package pl.ololjvNek.bedwars.data;

import lombok.Data;
import org.bukkit.Bukkit;
import pl.ololjvNek.bedwars.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Data
public class User {

    private UUID UUID;
    private String lastName, armor;
    private int coins;
    private Team team;

    public User(UUID uuid){
        UUID = uuid;
        lastName = Bukkit.getPlayer(getUUID()).getName();
        coins = 0;
        team = null;
        armor = "DEFAULT";
        insert();
    }

    public User(ResultSet rs) throws SQLException{
        UUID = java.util.UUID.fromString(rs.getString("uuid"));
        lastName = rs.getString("lastName");
        coins = rs.getInt("coins");
        team = null;
        armor = "DEFAULT";
    }

    public void insert(){
        Main.getStore().update("INSERT INTO `users`(`id`, `uuid`, `lastName`, `coins`) VALUES (NULL, '" + getUUID().toString() + "', '" + getLastName() + "', '" + getCoins() + "');");
    }

    public void update(){
        Main.getStore().update("UPDATE `users` SET `coins` = '" + getCoins() + "' WHERE `uuid` = '" + getUUID().toString() + "';");
    }
}
