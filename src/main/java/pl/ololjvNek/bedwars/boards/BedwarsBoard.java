package pl.ololjvNek.bedwars.boards;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.entity.Player;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.data.Team;
import pl.ololjvNek.bedwars.utils.DataUtil;
import pl.ololjvNek.bedwars.utils.Lang;
import pl.ololjvNek.bedwars.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class BedwarsBoard {

    public static void sendBoard(Player player){
        BPlayerBoard board = Netherboard.instance().getBoard(player);
        if(board == null){
            board = Netherboard.instance().createBoard(player, Util.fixColors("&e&lBEDWARS"));
        }

        List<String> list = new ArrayList<>();
        if(Main.getEventManager().getStartedEventType() == null){
            list.add(Util.fixColors(Lang.EVENT_BOARD_NEXTEVENT.replace("{EVENT}", Main.getBedwarsGame().getEventType().getName())));
            list.add(Util.fixColors("  &a" + DataUtil.secondsToString(Main.getEventManager().getNextEvent())));
        }else{
            list.add(Util.fixColors(Lang.EVENT_BOARD_EVENTUNDERWAY.replace("{EVENT}", Main.getEventManager().getStartedEventType().getName())));
            list.add(Util.fixColors("  &a" + DataUtil.secondsToString(Main.getEventManager().getEventTime())));
        }
        list.add(Util.fixColors("&2"));
        for(Team team : Main.getBedwarsGame().getTeams()){
            list.add("  " + team.getTeamColor().getTeamName() +Util.fixColors(" &a" + (team.isBedAlive() ? "✔" : (team.getPlayers().size() != 0 ? team.getPlayers().size() : "&4✖"))));
        }
        board.setAll(list.toArray(new String[list.size()]));
    }
}
