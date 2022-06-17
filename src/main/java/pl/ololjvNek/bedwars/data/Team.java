package pl.ololjvNek.bedwars.data;

import lombok.Data;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.enums.GeneratorType;
import pl.ololjvNek.bedwars.enums.TeamColor;
import pl.ololjvNek.bedwars.enums.UpgradeType;
import pl.ololjvNek.bedwars.managers.UserManager;
import pl.ololjvNek.bedwars.utils.ItemBuilder;
import pl.ololjvNek.bedwars.utils.Lang;
import pl.ololjvNek.bedwars.utils.Util;

import java.util.ArrayList;
import java.util.List;

@Data
public class Team {

    private TeamColor teamColor;
    private List<Player> players;
    private org.bukkit.scoreboard.Team sbTeam;
    private Location spawnLocation,shopLocation,upgradeLocation,generatorLocation, bedLocation;
    private int armorUpgradeLevel, weaponUpgradeLevel, furnaceUpgradeLevel, speedUpgradeLevel, bedUpgradeLevel;

    public Team(TeamColor teamColor){
        this.teamColor = teamColor;
        players = new ArrayList<>();
        sbTeam = Main.getSbBedwars().registerNewTeam(teamColor.name());
        sbTeam.setAllowFriendlyFire(false);
        sbTeam.setPrefix(teamColor.getTeamName() + " " + ChatColor.GRAY);
        spawnLocation = null;
        shopLocation = null;
        upgradeLocation = null;
        generatorLocation = null;
        bedLocation = null;
        armorUpgradeLevel = 0;
        weaponUpgradeLevel = 0;
        furnaceUpgradeLevel = 0;
        speedUpgradeLevel = 0;
        bedUpgradeLevel = 0;
    }

    public boolean isBedAlive(){
        return (bedLocation != null && bedLocation.getWorld().getBlockAt(getBedLocation()).getType() == Material.BED_BLOCK);
    }

    public void equipPlayer(Player player){
        final User u = UserManager.getUser(player);
        ItemBuilder boots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(dyeColorFromTeamColor(getTeamColor())).setInfinityDurability();
        ItemBuilder leg = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(dyeColorFromTeamColor(getTeamColor())).setInfinityDurability();
        ItemBuilder chest = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(dyeColorFromTeamColor(getTeamColor())).setInfinityDurability();
        ItemBuilder hel = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(dyeColorFromTeamColor(getTeamColor())).setInfinityDurability();
        ItemBuilder sword = new ItemBuilder(Material.WOOD_SWORD).setInfinityDurability();
        switch (u.getArmor()){
            case "DEFAULT":
                boots = new ItemBuilder(Material.LEATHER_BOOTS).setInfinityDurability();
                leg = new ItemBuilder(Material.LEATHER_LEGGINGS).setInfinityDurability();
                chest = new ItemBuilder(Material.LEATHER_CHESTPLATE).setInfinityDurability();
                hel = new ItemBuilder(Material.LEATHER_HELMET).setInfinityDurability();
                break;
            case "CHAIN":
                boots = new ItemBuilder(Material.CHAINMAIL_BOOTS).setInfinityDurability();
                leg = new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setInfinityDurability();
                break;
            case "IRON":
                boots = new ItemBuilder(Material.IRON_BOOTS).setInfinityDurability();
                leg = new ItemBuilder(Material.IRON_LEGGINGS).setInfinityDurability();
                break;
            case "DIAMOND":
                boots = new ItemBuilder(Material.DIAMOND_BOOTS).setInfinityDurability();
                leg = new ItemBuilder(Material.DIAMOND_LEGGINGS).setInfinityDurability();
                break;
        }
        if(player.getGameMode() == GameMode.ADVENTURE){
            player.getInventory().clear();
        }
        player.getInventory().setArmorContents(new ItemStack[] {boots.toItemStack(),leg.toItemStack(),chest.toItemStack(),hel.toItemStack()});
        if(!player.getInventory().contains(sword.toItemStack())){
            player.getInventory().addItem(sword.toItemStack());
        }
        if(getArmorUpgradeLevel() > 0){
            if(player.getInventory().getBoots() != null && (player.getInventory().getBoots().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) == null || player.getInventory().getBoots().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) < getArmorUpgradeLevel())){
                final ItemMeta im = player.getInventory().getBoots().getItemMeta();
                im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, getArmorUpgradeLevel(), true);
                player.getInventory().getBoots().setItemMeta(im);
            }
            if(player.getInventory().getLeggings() != null && (player.getInventory().getLeggings().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) == null || player.getInventory().getLeggings().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) < getArmorUpgradeLevel())){
                final ItemMeta im = player.getInventory().getLeggings().getItemMeta();
                im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, getArmorUpgradeLevel(), true);
                player.getInventory().getLeggings().setItemMeta(im);
            }
        }
    }

    public Color dyeColorFromTeamColor(TeamColor color){
        switch (color){
            case GRAY:
                return Color.GRAY;
            case YELLOW:
                return Color.YELLOW;
            case PURPLE:
                return Color.PURPLE;
            case WHITE:
                return Color.WHITE;
            case GREEN:
                return Color.GREEN;
            case AQUA:
                return Color.AQUA;
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
        }
        return null;
    }

    public void speedUpgrade(){
        new BukkitRunnable(){
            public void run(){
                for(final Player player : getPlayers()){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99, getSpeedUpgradeLevel()-1));
                }
            }
        }.runTask(Main.getPlugin());
    }

    public void weaponUpgrade(){
        new BukkitRunnable(){
            public void run(){
                for(final Player inTeam : getPlayers()){
                    for(ItemStack is : inTeam.getInventory().getContents()){
                        if(is != null && EnchantmentTarget.WEAPON.includes(is)){
                            if(is.getEnchantments() != null && (is.getEnchantments().get(Enchantment.DAMAGE_ALL) == null || is.getEnchantments().get(Enchantment.DAMAGE_ALL) < getWeaponUpgradeLevel())){
                                final ItemMeta im = is.getItemMeta();
                                im.addEnchant(Enchantment.DAMAGE_ALL, getWeaponUpgradeLevel(), true);
                                is.setItemMeta(im);
                            }
                        }
                    }
                }
            }
        }.runTask(Main.getPlugin());
    }

    public void armorUpgrade(){
        new BukkitRunnable(){
            public void run(){
                for(final Player inTeam : getPlayers()){
                    if(inTeam.getInventory().getBoots() != null && (inTeam.getInventory().getBoots().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) == null || inTeam.getInventory().getBoots().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) < getArmorUpgradeLevel())){
                        final ItemMeta im = inTeam.getInventory().getBoots().getItemMeta();
                        im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, getArmorUpgradeLevel(), true);
                        inTeam.getInventory().getBoots().setItemMeta(im);
                    }
                    if(inTeam.getInventory().getLeggings() != null && (inTeam.getInventory().getLeggings().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) == null || inTeam.getInventory().getLeggings().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) < getArmorUpgradeLevel())){
                        final ItemMeta im = inTeam.getInventory().getLeggings().getItemMeta();
                        im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, getArmorUpgradeLevel(), true);
                        inTeam.getInventory().getLeggings().setItemMeta(im);
                    }
                }
            }
        }.runTask(Main.getPlugin());
    }

    public void respawnPlayer(Player player){
        for(Player inGame : Main.getBedwarsGame().getPlayers()){
            inGame.hidePlayer(player);
        }
        player.teleport(Main.getBedwarsGame().getLobby().clone().subtract(0, 7, 0));
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);
        new BukkitRunnable(){
            int i = 5;
            public void run(){
                if(i > 0){
                    Util.sendTitle(player, Lang.MESSAGES_YOUDEAD);
                    Util.sendSubTitle(player, Lang.MESSAGES_RESPAWNIN.replace("{TIME}", String.valueOf(i)));
                }else{
                    equipPlayer(player);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.teleport(getSpawnLocation());
                    for(Player inGame : Main.getBedwarsGame().getPlayers()){
                        inGame.showPlayer(player);
                    }
                    this.cancel();
                }
                i--;
            }
        }.runTaskTimer(Main.getPlugin(), 20L, 20L);
    }

    public void addArmorUpgradeLevel(int index){
        armorUpgradeLevel += index;
    }

    public void addWeaponUpgradeLevel(int index){
        weaponUpgradeLevel += index;
    }

    public void addFurnaceUpgradeLevel(int index){
        furnaceUpgradeLevel += index;
    }

    public void addSpeedUpgradeLevel(int index){
        speedUpgradeLevel += index;
    }

    public void addBedUpgradeLevel(int index){
        bedUpgradeLevel += index;
    }

    public ItemStack getCostToUpgrade(UpgradeType upgradeType){
        ItemStack[] costs = Util.parseItemStackLevelCosts(Main.getGlobalConfig().getConfig().getString("settings.upgrade." + upgradeType.name().toLowerCase() + ".levelCosts"));
        try{
            if(costs[getLevelFromUpgradeType(upgradeType)+1] == null){
                return costs[getLevelFromUpgradeType(upgradeType)];
            }
        }catch (ArrayIndexOutOfBoundsException outOfBoundsException){
            return costs[getLevelFromUpgradeType(upgradeType)];
        }
        return costs[getLevelFromUpgradeType(upgradeType)+1];
    }

    public void addLevelToUpgradeType(UpgradeType upgradeType, int index){
        switch (upgradeType){
            case ARMOR:
                addArmorUpgradeLevel(index);
                for(final Player inTeam : getPlayers()){
                    if(inTeam.getInventory().getBoots() != null && (inTeam.getInventory().getBoots().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) == null || inTeam.getInventory().getBoots().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) < getArmorUpgradeLevel())){
                        ItemMeta im = inTeam.getInventory().getBoots().getItemMeta();
                        im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, getArmorUpgradeLevel(), true);
                        inTeam.getInventory().getBoots().setItemMeta(im);
                    }
                    if(inTeam.getInventory().getLeggings() != null && (inTeam.getInventory().getLeggings().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) == null || inTeam.getInventory().getLeggings().getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) < getArmorUpgradeLevel())){
                        ItemMeta im = inTeam.getInventory().getLeggings().getItemMeta();
                        im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, getArmorUpgradeLevel(), true);
                        inTeam.getInventory().getLeggings().setItemMeta(im);
                    }
                }
                break;
            case WEAPON:
                addWeaponUpgradeLevel(index);
                break;
            case FURNACE:
                addFurnaceUpgradeLevel(index);
                Main.getBedwarsGame().searchForFurnace(GeneratorType.FURNACE, getTeamColor()).upgradeFurnace();
                break;
            case SPEED:
                addSpeedUpgradeLevel(index);
                break;
            case BED:
                addBedUpgradeLevel(index);
                break;
        }
    }

    public int getLevelFromUpgradeType(UpgradeType upgradeType){
        switch (upgradeType){
            case ARMOR:
                return getArmorUpgradeLevel();
            case WEAPON:
                return getWeaponUpgradeLevel();
            case FURNACE:
                return getFurnaceUpgradeLevel();
            case SPEED:
                return getSpeedUpgradeLevel();
            case BED:
                return getBedUpgradeLevel();
        }
        return 404;
    }

    public void setupShop(){
        Villager villager = (Villager) getShopLocation().getWorld().spawnEntity(getShopLocation(), EntityType.VILLAGER);
        Util.setTeg(villager, "PersistenceRequired", 1);
        Util.setTeg(villager, "NoAI", 1);
        Util.setTeg(villager, "Silent", 1);
        Util.setTeg(villager, "Invulnerable", 1);
        villager.setAdult();
        villager.setAgeLock(true);
        ((LivingEntity)villager).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 255));
        villager.setCustomName(Util.fixColors(Main.getGlobalConfig().getConfig().getString("settings.shops.villagername")));
        villager.setCustomNameVisible(true);
        villager.setRemoveWhenFarAway(false);
    }

    public void setupUpgrade(){
        Villager villager = (Villager) getUpgradeLocation().getWorld().spawnEntity(getUpgradeLocation(), EntityType.VILLAGER);
        Util.setTeg(villager, "PersistenceRequired", 1);
        Util.setTeg(villager, "NoAI", 1);
        Util.setTeg(villager, "Silent", 1);
        Util.setTeg(villager, "Invulnerable", 1);
        villager.setAdult();
        villager.setAgeLock(true);
        ((LivingEntity)villager).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 255));
        villager.setCustomName(Util.fixColors(Main.getGlobalConfig().getConfig().getString("settings.upgrade.villagername")));
        villager.setCustomNameVisible(true);
        villager.setRemoveWhenFarAway(false);
    }

    public void addToTeam(Player player){
        players.add(player);
        UserManager.getUser(player).setTeam(this);
        sbTeam.addPlayer(player);
    }

    public void removeFromTeam(Player player){
        players.remove(player);
        sbTeam.removePlayer(player);
    }

}
