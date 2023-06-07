package me.xsenny_.basecreator.menu;

import me.kodysimpson.simpapi.heads.SkullCreator;
import me.xsenny_.basecreator.BaseCreator;
import me.xsenny_.basecreator.bases.BaseMethods;
import me.xsenny_.basecreator.listener.OpenManagerMenu;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class SeeMembers extends PaginatedMenu{
    public SeeMembers(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }
    public static FileConfiguration c = BaseCreator.plugin.getConfig();

    @Override
    public String getMenuName() {
        return ChatColor.translateAlternateColorCodes('&', c.getString("remove-member.list.title"));
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        ArrayList<String> players = OpenManagerMenu.getBase(p).getMembers();

        if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
            playerMenuUtility.setPlayerToRemove(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(BaseCreator.plugin, "uuid"), PersistentDataType.STRING));
            new RemoveConfirmMenu(playerMenuUtility).open();
        }else if (e.getCurrentItem().getType().equals(Material.BARRIER)) {

            //close inventory
            p.closeInventory();

        }else if(e.getCurrentItem().getType().equals(Material.DARK_OAK_BUTTON)){
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(c.getString("left-button"))){
                if (page == 0){
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("first-page")));
                }else{
                    page = page - 1;
                    super.open();
                }
            }else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(c.getString("right-button"))){
                if (!((index + 1) >= players.size())){
                    page = page + 1;
                    super.open();
                }else{
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("last-page")));
                }
            }
        }
    }

    @Override
    public void setMenuItems() {

        addMenuBorder();

        ArrayList<String> uuids = OpenManagerMenu.getBase(playerMenuUtility.getOwner()).getMembers();
        if (uuids != null && !uuids.isEmpty()){
            for (int i =0; i < getMaxItemsPerPage(); i++){
                index = getMaxItemsPerPage() * page + i;
                if (index >= uuids.size()) break;
                if (uuids.get(index) != null){
                    OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(uuids.get(index)));
                    ItemStack head = SkullCreator.itemFromUuid(UUID.fromString(uuids.get(index)));
                    ItemMeta meta = head.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("remove-member.list.item.name")).replace("{player}", op.getName()));
                    ArrayList<String> lore = new ArrayList<>();
                    for (String s : (List<String>) c.getList("remove-member.list.item.lore")){
                        lore.add(ChatColor.translateAlternateColorCodes('&', s.replace("{name}", op.getName())));
                    }
                    meta.setLore(lore);
                    meta.getPersistentDataContainer().set(new NamespacedKey(BaseCreator.plugin, "uuid"), PersistentDataType.STRING, uuids.get(index));
                    head.setItemMeta(meta);
                    inventory.addItem(head);
                }
            }
        }
        ////////////////////////


    }

}
