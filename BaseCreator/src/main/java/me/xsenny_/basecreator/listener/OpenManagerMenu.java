package me.xsenny_.basecreator.listener;

import me.xsenny_.basecreator.BaseCreator;
import me.xsenny_.basecreator.bases.Base;
import me.xsenny_.basecreator.menu.AddMembers;
import me.xsenny_.basecreator.menu.SeeMembers;
import me.xsenny_.basecreator.utils.DeleteBase;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class OpenManagerMenu implements Listener {

    public static HashMap<String, Boolean> isPlayerAdding = new HashMap<>();
    public static HashMap<String, Boolean> isPlayerRemoving = new HashMap<>();
    public static FileConfiguration c = BaseCreator.plugin.getConfig();

    @EventHandler
    public static void onClick(PlayerInteractEvent e){
        Block block = e.getClickedBlock();
        if (block != null){
            if (block.getType() == Material.LECTERN){
                Location location = block.getLocation();
                for (Base base : BaseCreator.baseCollection.getCollection()){
                    if (base.locationIsInRegion(location)){
                        if (base.getOwnerUuid().equals(e.getPlayer().getUniqueId().toString())){
                            openManagerMenu(e.getPlayer());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e){
        if (e.getView().getTitle().equals(c.getString("inventory.manage")) && e.getInventory().getMaxStackSize() == 48){
            Player p = (Player) e.getWhoClicked();
            switch (e.getSlot()){
                case 3:
                    deleteBase(getBase(p));
                    break;
                case 5:
                    openMembersMenu(p);
                    break;
                case 8:
                    p.closeInventory();
            }
            e.setCancelled(true);
        }else if (e.getView().getTitle().equals(c.getString("inventory.members")) && e.getInventory().getMaxStackSize() == 50){
            Player p = (Player) e.getWhoClicked();
            switch (e.getSlot()){
                case 3:
                    addMember(p);
                    break;
                case 5:
                    removeMember(p);
                    break;
                case 8:
                    p.closeInventory();
                    break;
            }
            e.setCancelled(true);
        }
    }

    public static void openManagerMenu(Player p){
        Inventory inventory = Bukkit.createInventory(p, 9, c.getString("inventory.manage"));
        inventory.setMaxStackSize(48);
        ItemStack delete = new ItemStack(Material.TNT);
        ItemMeta delete_meta = delete.getItemMeta();
        delete_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("inventory.manage-items.delete")));
        delete.setItemMeta(delete_meta);
        ItemStack members = new ItemStack(Material.BOOK);
        ItemMeta members_meta = members.getItemMeta();
        members_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("inventory.manage-items.manage")));
        members.setItemMeta(members_meta);
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta close_meta = close.getItemMeta();
        close_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("inventory.manage-items.close")));
        close.setItemMeta(close_meta);

        inventory.setItem(3, delete);
        inventory.setItem(5, members);
        inventory.setItem(8, close);
        p.openInventory(inventory);
    }

    public static void openMembersMenu(Player p){
        Inventory inventory = Bukkit.createInventory(p, 9, c.getString("inventory.members"));
        inventory.setMaxStackSize(50);
        ItemStack add = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta add_meta = add.getItemMeta();
        add_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("inventory.members-items.add")));
        add.setItemMeta(add_meta);

        ItemStack remove = new ItemStack(Material.ARROW);
        ItemMeta remove_meta = remove.getItemMeta();
        remove_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("inventory.members-items.remove")));
        remove.setItemMeta(remove_meta);

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta close_meta = close.getItemMeta();
        close_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("inventory.members-items.close")));
        close.setItemMeta(close_meta);

        inventory.setItem(3, add);
        inventory.setItem(5, remove);
        inventory.setItem(8, close);
        p.openInventory(inventory);
    }

    public static void deleteBase(Base base){
        BukkitTask tast = new DeleteBase(base).runTaskTimer(BaseCreator.plugin, 0L, 20L);
        BaseCreator.baseCollection.getCollection().remove(base);
    }

    public static void removeMember(Player p){
//        isPlayerRemoving.put(p.getUniqueId().toString(), true);
//        p.closeInventory();
//        int i = 0;
//        for (String uuid : getBase(p).getMembers()){
//            OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
//            p.sendMessage("" + i + ": " + op.getName());
//        }
//        p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("remove-a-member")));
        p.closeInventory();
        new SeeMembers(BaseCreator.getPlayerMenuUtility(p)).open();
    }

    public static void addMember(Player p){
        new AddMembers(BaseCreator.getPlayerMenuUtility(p)).open();
    }

    public static Base getBase(Player p){
        for (Base base : BaseCreator.baseCollection.getCollection()){
            if (base.getOwnerUuid().equals(p.getUniqueId().toString())){
                return base;
            }
        }
        return null;
    }

    @EventHandler
    public static void onChat(AsyncPlayerChatEvent e){
        if (isPlayerAdding.containsKey(e.getPlayer().getUniqueId().toString())){
            String message = e.getMessage();
            Player p = Bukkit.getPlayer(message);
            if (p != null && p.isOnline()){
                for (Base base : BaseCreator.baseCollection.getCollection()){
                    if (base.getOwnerUuid().equals(e.getPlayer().getUniqueId().toString())){
                        if (!base.getMembers().contains(p.getUniqueId().toString()) && !p.getUniqueId().toString().equals(e.getPlayer().getUniqueId().toString())){
                            base.getMembers().add(p.getUniqueId().toString());
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("he-was-added").replace("{name}", p.getName())));
                        }else{
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("add-error").replace("{name}", p.getName())));
                        }
                    }
                }
            }else{
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("add-error-2").replace("{name}", e.getMessage())));
            }
            e.setCancelled(true);
            isPlayerAdding.remove(e.getPlayer().getUniqueId().toString());
        }else if (isPlayerRemoving.containsKey(e.getPlayer().getUniqueId().toString())){
            if (!e.getMessage().equals("cancel")){
                try{
                    Integer integer = Integer.parseInt(e.getMessage());
                    getBase(e.getPlayer()).getMembers().remove(getBase(e.getPlayer()).getMembers().get(integer));
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("remove-success")));
                    for (String member : getBase(e.getPlayer()).getMembers()) {
                        System.out.println(member);
                    }
                }catch (NumberFormatException exception){
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("no-number")));
                }catch (IndexOutOfBoundsException ex){
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("number-above")));
                }
            }
            e.setCancelled(true);
            isPlayerRemoving.remove(e.getPlayer().getUniqueId().toString());
        }
    }

}
