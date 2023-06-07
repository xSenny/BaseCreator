package me.xsenny_.basecreator.setup;

import me.xsenny_.basecreator.BaseCreator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;

public class SetUpListener implements Listener {

    public static BaseCreator plugin = BaseCreator.plugin;
    public static FileConfiguration c = plugin.getConfig();

    @EventHandler
    public static void onInterract(PlayerInteractEvent e){
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player p = e.getPlayer();
            if (SetUpRegion.isPlayerInEditMode.containsKey(p.getUniqueId().toString()) && SetUpRegion.isPlayerInEditMode.get(p.getUniqueId().toString())){
                if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() == Material.NETHERITE_AXE){
                    if (e.getClickedBlock() == null){
                    }else{
                        ArrayList<Location> locations = SetUpRegion.playerSetUpBase.get(p.getUniqueId().toString());
                        locations.set(0, e.getClickedBlock().getLocation());
                        SetUpRegion.playerSetUpBase.put(p.getUniqueId().toString(), locations);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("position.first")));
                    }
                }else if (p.getInventory().getItemInMainHand().getType() == Material.EMERALD){
                    ArrayList<Location> locations = SetUpRegion.playerSetUpBase.get(p.getUniqueId().toString());
                    if (isNotLocationNull(locations.get(0)) && isNotLocationNull(locations.get(1))){
                        Location initial = SetUpRegion.playerFirstBase.get(p.getUniqueId().toString());
                        Location first = SetUpRegion.playerSetUpBase.get(p.getUniqueId().toString()).get(0);
                        Location second = SetUpRegion.playerSetUpBase.get(p.getUniqueId().toString()).get(1);
                        int x1, x2, y1, y2, z1, z2;
                        x1 = first.getBlockX() - initial.getBlockX();
                        x2 = second.getBlockX() - initial.getBlockX();
                        y1 = first.getBlockY() - initial.getBlockY();
                        y2 = second.getBlockY() - initial.getBlockY();
                        z1 = first.getBlockZ() - initial.getBlockZ();
                        z2 = second.getBlockZ() - initial.getBlockZ();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("position.saved")));
                        FileConfiguration c = plugin.getConfig();
                        c.set("changes.x1", x1);
                        c.set("changes.x2", x2);
                        c.set("changes.y1", y1);
                        c.set("changes.y2", y2);
                        c.set("changes.z1", z1);
                        c.set("changes.z2", z2);
                        SetUpRegion.playerSetUpBase.remove(e.getPlayer().getUniqueId().toString());
                        SetUpRegion.playerFirstBase.remove(p.getUniqueId().toString());
                        SetUpRegion.isPlayerInEditMode.remove(p.getUniqueId().toString());
                        plugin.saveConfig();
                    }else{
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("position.error")));
                    }
                }
            }
        }else if (e.getAction() == Action.LEFT_CLICK_BLOCK){
            Player p = e.getPlayer();
            if (SetUpRegion.isPlayerInEditMode.containsKey(p.getUniqueId().toString()) && SetUpRegion.isPlayerInEditMode.get(p.getUniqueId().toString())){
                if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() == Material.NETHERITE_AXE){
                    if (e.getClickedBlock() == null){
                    }else{
                        ArrayList<Location> locations = SetUpRegion.playerSetUpBase.get(p.getUniqueId().toString());
                        locations.set(1, e.getClickedBlock().getLocation());
                        SetUpRegion.playerSetUpBase.put(p.getUniqueId().toString(), locations);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("position.second")));
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    public static boolean isNotLocationNull(Location loc){
        return !(loc.getBlockX() == 0 && loc.getBlockY() == 0 && loc.getBlockZ() == 0);
    }

}
