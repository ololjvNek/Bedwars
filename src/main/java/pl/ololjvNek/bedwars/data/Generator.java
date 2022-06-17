package pl.ololjvNek.bedwars.data;

import lombok.Data;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.enums.GeneratorType;
import pl.ololjvNek.bedwars.utils.DataUtil;
import pl.ololjvNek.bedwars.utils.Lang;
import pl.ololjvNek.bedwars.utils.TimeUtil;
import pl.ololjvNek.bedwars.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Data
public class Generator {

    private List<String> generatorInfo = Main.getGlobalConfig().getConfig().getStringList("settings.generators.info");

    private Location location;
    private List<ItemStack> toDrop;
    private int level, dropTime;
    private GeneratorType type;
    private long interval, upgrade;
    private Team team;
    private ScheduledExecutorService executorService;
    private List<ArmorStand> savedInformations;

    public Generator(Location location, List<ItemStack> toDrop, long interval, GeneratorType type){
        this.location = location;
        this.toDrop = toDrop;
        this.interval = interval;
        this.level = 0;
        this.dropTime = 0;
        this.upgrade = System.currentTimeMillis()+ TimeUtil.MINUTE.getTime(1);
        this.type = type;
        this.savedInformations = new ArrayList<>();
        this.team = null;
        info();
    }

    public void addLevel(int index){
        this.level += index;
    }

    public void addDropTime(int index) {this.dropTime += index;}

    public void drop(){
        new BukkitRunnable(){
            public void run(){
                if(System.currentTimeMillis() > getUpgrade() && getUpgrade() != 0L){
                    setUpgrade(0L);
                    upgrade();
                }
                for(ItemStack toDrop : getToDrop()){
                    Item item = getLocation().getWorld().dropItemNaturally(getLocation().clone().add(0, 0.5, 0), toDrop);
                    item.setVelocity(item.getVelocity().zero());
                }
                updateInfo();
            }
        }.runTask(Main.getPlugin());
    }

    public void upgradeFurnace(){
        addLevel(1);
        setInterval(getInterval()-Main.getGlobalConfig().getConfig().getLong("settings.generators.intervalcut." + getType().toString()));
        if(getLevel() == 1){
            setToDrop(Arrays.asList(new ItemStack(Material.IRON_INGOT, 2), new ItemStack(Material.GOLD_INGOT, 1)));
        }else if(getLevel() == 2){
            setToDrop(Arrays.asList(new ItemStack(Material.IRON_INGOT, 3), new ItemStack(Material.GOLD_INGOT, 2), new ItemStack(Material.DIAMOND, 1)));
        }
        deactivate();
        new BukkitRunnable(){
            public void run(){
                activate();
            }
        }.runTaskLater(Main.getPlugin(), 10L);
    }

    public void furnaceDrop(){
        new BukkitRunnable(){
            public void run(){
                if(savedInformations.size() != 0){
                    for(ArmorStand armorStand : savedInformations){
                        armorStand.remove();
                    }
                    savedInformations.clear();
                }
                for(ItemStack toDrop : getToDrop()){
                    Item item = getLocation().getWorld().dropItemNaturally(getLocation().clone().add(0, 0.5, 0), toDrop);
                    item.setVelocity(item.getVelocity().zero());
                }
            }
        }.runTask(Main.getPlugin());
    }

    public void upgrade(){
        Util.spawnFireworks(getLocation(), 1);
        addLevel(1);
        setUpgrade(System.currentTimeMillis()+TimeUtil.MINUTE.getTime(getLevel()*2));
        setInterval(getInterval()-Main.getGlobalConfig().getConfig().getLong("settings.generators.intervalcut." + getType().toString()));
        deactivate();
        new BukkitRunnable(){
            public void run(){
                activate();
            }
        }.runTaskLater(Main.getPlugin(), 10L);
        //message
    }

    public void updateInfo(){
        List<String> i = new ArrayList<>(getGeneratorInfo());
        Collections.reverse(i);
        int var = 0;
        for(ArmorStand armorStand : getSavedInformations()){
            armorStand.setCustomName(Util.fixColors(replaceVariables(i.get(var))));
            var++;
        }
    }

    public void info(){
        final Location toSpawn = getLocation();
        List<String> i = new ArrayList<>(getGeneratorInfo());
        Collections.reverse(i);
        for(String info : i){
            ArmorStand armorStand = (ArmorStand) getLocation().getWorld().spawnEntity(toSpawn, EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCustomName(Util.fixColors(replaceVariables(info)));
            armorStand.setCustomNameVisible(true);
            getSavedInformations().add(armorStand);
            toSpawn.add(0, 0.3, 0);
        }
    }

    public String replaceVariables(String string){
        double interval = (double) (getInterval()/1000);
        String s = string.replace("{TYPE}", (getType() != null ? generatorName(getType()) : "ERROR"));
        s = s.replace("{UPGRADE}", DataUtil.secondsToString(getUpgrade()));
        s = s.replace("{INTERVAL}", String.valueOf(Util.round(interval, 2)));
        s = s.replace("{LEVEL}", String.valueOf(getLevel()));
        return s;
    }

    public static String generatorName(GeneratorType generatorType){
        switch (generatorType){
            case IRON:
                return Lang.GENERATORS_IRON;
            case GOLD:
                return Lang.GENERATORS_GOLD;
            case DIAMOND:
                return Lang.GENERATORS_DIAMOND;
            case EMERALD:
                return Lang.GENERATORS_EMERALD;
            case FURNACE:
                return "";
        }
        return "ERROR";
    }

    public void deactivate(){
        if(executorService != null){
            executorService.shutdown();
        }
        executorService = null;
    }

    public void activate(){
        executorService = Executors.newScheduledThreadPool(1);

        if(getType() == GeneratorType.FURNACE){
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        furnaceDrop();
                    } catch (Exception e) {
                        e.printStackTrace(); // Or better, use next line if you have configured a logger:
                        System.out.println("Exception in scheduled task" + e);
                    }
                }
            }, getInterval(), getInterval(), TimeUnit.MILLISECONDS);
        }else{
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        drop();
                    } catch (Exception e) {
                        e.printStackTrace(); // Or better, use next line if you have configured a logger:
                        System.out.println("Exception in scheduled task" + e);
                    }
                }
            }, getInterval(), getInterval(), TimeUnit.MILLISECONDS);
        }

        System.out.println("Scheduling executor with interval " + getInterval() + ": " + executorService.toString());
    }

}
