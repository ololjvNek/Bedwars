package pl.ololjvNek.bedwars.managers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.data.Bedwars;
import pl.ololjvNek.bedwars.data.Generator;
import pl.ololjvNek.bedwars.enums.EventType;
import pl.ololjvNek.bedwars.enums.GeneratorType;
import pl.ololjvNek.bedwars.utils.DataUtil;
import pl.ololjvNek.bedwars.utils.Lang;
import pl.ololjvNek.bedwars.utils.TimeUtil;
import pl.ololjvNek.bedwars.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EventManager {

    @Getter private Bedwars bedwars = Main.getBedwarsGame();
    @Getter private EventType startedEventType;
    @Getter @Setter private long nextEvent = 0L, eventTime = 0L;

    @Getter private EnderDragon enderDragon;
    @Getter private final List<Zombie> zombieList;

    public EventManager(){
        zombieList = new ArrayList<>();
        setNextEvent(System.currentTimeMillis()+ TimeUtil.MINUTE.getTime(5));
        getBedwars().setEventType(EventType.values()[ThreadLocalRandom.current().nextInt(EventType.values().length)]);
        new BukkitRunnable(){
            public void run(){
                if(getNextEvent() != 0L && System.currentTimeMillis() > getNextEvent()){
                    startEvent(getBedwars().getEventType());
                }
                if(getStartedEventType() == EventType.DRAGON){
                    if(getEventTime() != 0L && System.currentTimeMillis() > getEventTime()){
                        getBedwars().setEventType(EventType.values()[ThreadLocalRandom.current().nextInt(EventType.values().length)]);
                        setNextEvent(System.currentTimeMillis()+ TimeUtil.MINUTE.getTime(5));
                        setEventTime(0L);
                        startedEventType = null;
                        if(getEnderDragon() != null){
                            getEnderDragon().remove();
                        }
                    }
                }else if(getStartedEventType() == EventType.ZOMBIE){
                    if(getEventTime() != 0L && getEventTime() > System.currentTimeMillis()){
                        for(Generator generator : getBedwars().getGenerators()){
                            if(generator.getType() != GeneratorType.FURNACE){
                                if(getZombieList().size() < (getBedwars().getPlayers().size()*6)){
                                    Zombie zombie = (Zombie) generator.getLocation().getWorld().spawnEntity(generator.getLocation(), EntityType.ZOMBIE);
                                    zombie.setMaxHealth(50);
                                    zombie.setHealth(50);
                                    zombie.setCustomName(Util.fixColors(getStartedEventType().getName()));
                                    zombie.setCustomNameVisible(true);
                                    getZombieList().add(zombie);
                                }
                            }
                        }
                    }else if(getEventTime() != 0L && System.currentTimeMillis() > getEventTime()){
                        getBedwars().setEventType(EventType.values()[ThreadLocalRandom.current().nextInt(EventType.values().length)]);
                        setNextEvent(System.currentTimeMillis()+ TimeUtil.MINUTE.getTime(5));
                        setEventTime(0L);
                        getBedwars().getLobby().getWorld().setTime(0);
                        startedEventType = null;
                        for(Zombie zombie : getZombieList()){
                            zombie.remove();
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(), 20L, 20L);
    }



    public void startEvent(EventType eventType){
        setNextEvent(0L);
        switch (eventType){
            case DRAGON:
                startedEventType = eventType;
                setEventTime(System.currentTimeMillis()+TimeUtil.MINUTE.getTime(eventType.getEventTime()));
                EnderDragon dragon = (EnderDragon) getBedwars().getLobby().getWorld().spawnEntity(getBedwars().getLobby().clone().subtract(0, 7, 0), EntityType.ENDER_DRAGON);
                dragon.setHealth(100);
                dragon.setMaxHealth(100);
                dragon.setCustomName(Util.fixColors(eventType.getName()));
                enderDragon = dragon;
                Util.sendMessage(getBedwars().getPlayers(), Lang.EVENT_MESSAGES_STARTEVENT.replace("{EVENT}", getStartedEventType().getName()));
                break;
            case ZOMBIE:
                getBedwars().getLobby().getWorld().setTime(14000);
                startedEventType = eventType;
                setEventTime(System.currentTimeMillis()+TimeUtil.MINUTE.getTime(eventType.getEventTime()));
                Util.sendMessage(getBedwars().getPlayers(), Lang.EVENT_MESSAGES_STARTEVENT.replace("{EVENT}", getStartedEventType().getName()));
                break;
        }
    }
}
