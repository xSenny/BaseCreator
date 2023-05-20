package me.xsenny_.basecreator.listener;

import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public static void onPlayerLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if (OpenManagerMenu.getBase(p) != null){
            OpenManagerMenu.getBase(p).setDateOfCreation(System.currentTimeMillis());
        }
    }

}
