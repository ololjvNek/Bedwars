package pl.ololjvNek.bedwars.enums;

import lombok.Getter;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.utils.Lang;

public enum EventType {


    DRAGON(Lang.EVENT_PREFIX_DRAGON, Main.getGlobalConfig().getConfig().getInt("settings.event.dragon.time")),
    ZOMBIE(Lang.EVENT_PREFIX_ZOMBIE, Main.getGlobalConfig().getConfig().getInt("settings.event.zombie.time"));

    @Getter private final String name;
    @Getter private final int eventTime;

    EventType(String name, int eventTime){
        this.name = name;
        this.eventTime = eventTime;
    }

}
