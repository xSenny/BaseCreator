package me.xsenny_.basecreator.listener;

import me.xsenny_.basecreator.BaseCreator;
import me.xsenny_.basecreator.bases.Base;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class Interact implements Listener {

    public static HashMap<Player, Boolean> wasInteracted = new HashMap<>();
    public static BaseCreator plugin = BaseCreator.plugin;
    public static FileConfiguration c = plugin.getConfig();


    @EventHandler
    public static void onInteract(PlayerInteractEvent e){
        if (e.getClickedBlock() != null){
            Location location = e.getClickedBlock().getLocation();
            if (wasInteracted.get(e.getPlayer()) == null || !wasInteracted.get(e.getPlayer())){
                for (Base base : BaseCreator.baseCollection.getCollection()){
                    if (base.locationIsInRegion(location)){
                        if (!base.getOwnerUuid().equals(e.getPlayer().getUniqueId().toString()) && !base.getMembers().contains(e.getPlayer().getUniqueId().toString()) && !e.getPlayer().isOp()){
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("cannot-interact")));
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
                wasInteracted.put(e.getPlayer(), true);
            }else{
                wasInteracted.put(e.getPlayer(), false);
            }
        }
    }
    @EventHandler
    public static void onInteractWithEntity(PlayerInteractAtEntityEvent e){
        Location location = e.getClickedPosition().toLocation(e.getPlayer().getWorld());
        for (Base base : BaseCreator.baseCollection.getCollection()){
            if (base.locationIsInRegion(location)){
                if (!base.getOwnerUuid().equals(e.getPlayer().getUniqueId().toString()) && !base.getMembers().contains(e.getPlayer().getUniqueId().toString()) && !e.getPlayer().isOp()){
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("cannot-interact")));
                    e.setCancelled(true);
                }
            }
        }
    }

}
