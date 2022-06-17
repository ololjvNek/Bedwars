package pl.ololjvNek.bedwars.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.data.Shop;
import pl.ololjvNek.bedwars.data.User;
import pl.ololjvNek.bedwars.enums.ShopCategory;
import pl.ololjvNek.bedwars.enums.TeamColor;
import pl.ololjvNek.bedwars.managers.ShopManager;
import pl.ololjvNek.bedwars.managers.UserManager;
import pl.ololjvNek.bedwars.utils.ItemBuilder;
import pl.ololjvNek.bedwars.utils.ItemUtil;
import pl.ololjvNek.bedwars.utils.Lang;
import pl.ololjvNek.bedwars.utils.Util;

public class ShopProvider implements InventoryProvider {

    public static SmartInventory INVENTORY = SmartInventory.builder().manager(Main.getInventoryManager()).provider(new ShopProvider()).title(Util.fixColors("&8            * &9SKLEP &8*")).size(6, 9).build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        int row = 0;
        int slot = 0;
        final User u = UserManager.getUser(player);
        for(int i = 0; i < 8; i++){
            ItemBuilder ib = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte)15).setName(Util.fixColors("&8&l########"));
            inventoryContents.set(1, i, ClickableItem.empty(ib.toItemStack()));
        }
        for(ShopCategory shopCategory : ShopCategory.values()){
            ItemBuilder ib = new ItemBuilder(shopCategory.getGuiItem()).setName(Util.fixColors(shopCategory.getName()));
            inventoryContents.set(row, slot, ClickableItem.of(ib.toItemStack(), e -> {
                for(int i = 2; i < 6; i++){
                    inventoryContents.fillRow(i, ClickableItem.empty(new ItemStack(Material.AIR)));
                }
                int row2 = 2;
                int slot2 = 0;
                for(Shop shop : ShopManager.getShopsWithCategory(shopCategory)){
                    ItemBuilder shopBuilder = new ItemBuilder(shop.getGuiItem(), shop.getGuiAmount(), (shop.getGuiItem() == Material.WOOL ? getColorTeam(u.getTeam().getTeamColor()) : shop.getGuiData())).setName(Util.fixColors(shop.getGuiName())).setLore("", Util.fixColors("  &8>> &7Koszt: &6" + Util.replaceItemStackToString(shop.getCost())), Util.fixColors("  &8>> &7Kliknij, aby zakupic!")).addUnsafeEnchantments(shop.getEnchantments());
                    inventoryContents.set(row2, slot2, ClickableItem.of(shopBuilder.toItemStack(), ee->{
                        if(ItemUtil.playerHasItemAndRemove(player, shop.getCost())){
                            Util.sendMessage(player, Lang.MESSAGES_SUCCESSBUY);
                            if(shopCategory == ShopCategory.ARMOR){
                                switch (shop.getItem().getType()){
                                    case CHAINMAIL_BOOTS:
                                        u.setArmor("CHAIN");
                                        u.getTeam().equipPlayer(player);
                                        return;
                                    case IRON_BOOTS:
                                        u.setArmor("IRON");
                                        u.getTeam().equipPlayer(player);
                                        return;
                                    case DIAMOND_BOOTS:
                                        u.setArmor("DIAMOND");
                                        u.getTeam().equipPlayer(player);
                                        return;
                                }
                            }
                            player.getInventory().addItem(new ItemBuilder(shop.getItem()).setDurability((shop.getGuiItem() == Material.WOOL ? getColorTeam(u.getTeam().getTeamColor()) : shop.getGuiData())).toItemStack());
                        }else{
                            player.closeInventory();
                            Util.sendMessage(player, Lang.MESSAGES_CANTAFFORD);
                        }
                    }));
                    row2++;
                    if(row2 >= 6){
                        row2 = 2;
                        slot2++;
                    }
                }
            }));
            slot++;
        }
    }

    public byte getColorTeam(TeamColor teamColor){
        switch (teamColor){
            case BLUE:
                return (byte)11;
            case RED:
                return (byte)14;
            case AQUA:
                return (byte)3;
            case GREEN:
                return (byte)5;
            case WHITE:
                return (byte)0;
            case PURPLE:
                return (byte)10;
            case YELLOW:
                return (byte)4;
            case GRAY:
                return (byte)7;
        }
        return (byte)0;
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
