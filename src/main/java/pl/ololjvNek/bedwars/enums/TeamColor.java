package pl.ololjvNek.bedwars.enums;

import lombok.Getter;
import org.bukkit.ChatColor;
import pl.ololjvNek.bedwars.utils.Lang;

public enum TeamColor {

    RED(ChatColor.RED + Lang.PREFIX_RED),
    BLUE(ChatColor.BLUE + Lang.PREFIX_BLUE),
    YELLOW(ChatColor.YELLOW + Lang.PREFIX_YELLOW),
    GREEN(ChatColor.GREEN + Lang.PREFIX_GREEN),
    PURPLE(ChatColor.DARK_PURPLE + Lang.PREFIX_PURPLE),
    GRAY(ChatColor.GRAY + Lang.PREFIX_GRAY),
    AQUA(ChatColor.AQUA + Lang.PREFIX_AQUA),
    WHITE(ChatColor.WHITE + Lang.PREFIX_WHITE);

    @Getter public final String teamName;

    private TeamColor(String name){
        teamName = name;
    }


}
