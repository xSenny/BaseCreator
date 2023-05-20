package me.xsenny_.basecreator.menu;

import me.kodysimpson.simpapi.heads.SkullCreator;
import me.xsenny_.basecreator.BaseCreator;
import me.xsenny_.basecreator.bases.Base;
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

public class AddMembers extends PaginatedMenu{

    public static FileConfiguration c = BaseCreator.plugin.getConfig();

    public AddMembers(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ChatColor.translateAlternateColorCodes('&', c.getString("add-members.title"));
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        ArrayList<Player> players = new ArrayList<Player>(getServer().getOnlinePlayers());

        if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
            OpenManagerMenu.getBase(p).getMembers().add(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(BaseCreator.plugin, "uuid"), PersistentDataType.STRING));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("add-members.successfully").replace("{player}", Bukkit.getOfflinePlayer(UUID.fromString(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(BaseCreator.plugin, "uuid"), PersistentDataType.STRING))).getName())));
            new AddMembers(playerMenuUtility).open();
        }else if (e.getCurrentItem().getType().equals(Material.BARRIER)) {

            //close inventory
            p.closeInventory();

        }else if(e.getCurrentItem().getType().equals(Material.DARK_OAK_BUTTON)){
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', c.getString("left-button")))){
                if (page == 0){
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("first-page")));
                }else{
                    page = page - 1;
                    super.open();
                }
            }else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', c.getString("right-button")))){
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

        ArrayList<Player> uuids = new ArrayList<Player>(Bukkit.getOnlinePlayers());
        if (uuids != null && !uuids.isEmpty()){
            for (int i =0; i < getMaxItemsPerPage(); i++){
                index = getMaxItemsPerPage() * page + i;
                if (index >= uuids.size()) break;
                if (uuids.get(index) != null){
                    if (!OpenManagerMenu.getBase(playerMenuUtility.getOwner()).getMembers().contains(uuids.get(index).getUniqueId().toString()) && !uuids.get(index).equals(playerMenuUtility.getOwner())){
                        Player p = uuids.get(index);
                        ItemStack head = SkullCreator.itemFromUuid(uuids.get(index).getUniqueId());
                        ItemMeta meta = head.getItemMeta();
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("add-members.item.name").replace("{player}", p.getName())));
                        ArrayList<String> lore = new ArrayList<>();
                        for (String s : (List<String>) c.getList("add-members.item.lore")){
                            lore.add(ChatColor.translateAlternateColorCodes('&', s.replace("{name}", p.getName())));
                        }
                        meta.setLore(lore);
                        meta.getPersistentDataContainer().set(new NamespacedKey(BaseCreator.plugin, "uuid"), PersistentDataType.STRING, uuids.get(index).getUniqueId().toString());
                        head.setItemMeta(meta);
                        inventory.addItem(head);
                    }
                }
            }
        }
        ////////////////////////


    }
}
