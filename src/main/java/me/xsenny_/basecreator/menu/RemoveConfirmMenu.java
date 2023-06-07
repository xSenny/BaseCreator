package me.xsenny_.basecreator.menu;

import me.xsenny_.basecreator.BaseCreator;
import me.xsenny_.basecreator.listener.OpenManagerMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RemoveConfirmMenu extends Menu{
    public RemoveConfirmMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }
    public static FileConfiguration c = BaseCreator.plugin.getConfig();

    @Override
    public String getMenuName() {
        return ChatColor.translateAlternateColorCodes('&', c.getString("remove-member.confirm.title").replace("{name}", Bukkit.getOfflinePlayer(UUID.fromString(playerMenuUtility.getPlayerToRemove())).getName()));
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        switch (e.getCurrentItem().getType()){
            case EMERALD:
                //they pressed yes, kill player
                e.getWhoClicked().closeInventory();
//                playerMenuUtility.getPlayerToKill().setHealth(0.0); //grab the data from the previous menu
//                e.getWhoClicked().sendMessage(ChatColor.RED + "HE DEAD!");
                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("remove-member.confirm.successfully").replace("{player}", Bukkit.getOfflinePlayer(UUID.fromString(playerMenuUtility.getPlayerToRemove())).getName())));
                OpenManagerMenu.getBase(playerMenuUtility.getOwner()).getMembers().remove(playerMenuUtility.getPlayerToRemove());
                new SeeMembers(playerMenuUtility).open();

                break;
            case BARRIER:

                //go back to the previous menu
                new SeeMembers(playerMenuUtility).open();

                break;
        }

    }

    @Override
    public void setMenuItems() {

        ItemStack yes = new ItemStack(Material.EMERALD, 1);
        ItemMeta yes_meta = yes.getItemMeta();
        yes_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("remove-member.confirm.da.name")));
        ArrayList<String> yes_lore = new ArrayList<>();
        for (String s : (List<String> ) c.getList("remove-member.confirm.da.lore")){
            yes_lore.add(ChatColor.translateAlternateColorCodes('&', s.replace("{name}", Bukkit.getOfflinePlayer(UUID.fromString(playerMenuUtility.getPlayerToRemove())).getName())));
        }
        yes_meta.setLore(yes_lore);
        yes.setItemMeta(yes_meta);
        ItemStack no = new ItemStack(Material.BARRIER, 1);
        ItemMeta no_meta = no.getItemMeta();
        no_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("remove-member.confirm.nu.name")));
        no.setItemMeta(no_meta);

        inventory.setItem(3, yes);
        inventory.setItem(5, no);

        setFillerGlass();

    }

}
