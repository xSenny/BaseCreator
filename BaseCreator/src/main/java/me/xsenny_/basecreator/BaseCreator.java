package me.xsenny_.basecreator;

import me.kodysimpson.simpapi.menu.MenuManager;
import me.xsenny_.basecreator.bases.Base;
import me.xsenny_.basecreator.bases.BaseCollection;
import me.xsenny_.basecreator.bases.BaseMethods;
import me.xsenny_.basecreator.create.CreateBase;
import me.xsenny_.basecreator.listener.Interact;
import me.xsenny_.basecreator.listener.OpenManagerMenu;
import me.xsenny_.basecreator.listener.PlayerLeave;
import me.xsenny_.basecreator.menu.MenuListener;
import me.xsenny_.basecreator.menu.PlayerMenuUtility;
import me.xsenny_.basecreator.setup.SetUpListener;
import me.xsenny_.basecreator.setup.SetUpRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public final class BaseCreator extends JavaPlugin {

    public static BaseCreator plugin;
    public static BaseCollection baseCollection;
    public static HashMap<Player, PlayerMenuUtility> playerMenuUtilityHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
        } else {
            System.out.println("Could not find World Edit! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        File file = new File(plugin.getDataFolder().getAbsolutePath() + "/bases.json");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BaseMethods.loadBases();

        long secs = getConfig().getLong("delete.time") * 1000;



        if (baseCollection == null){
            baseCollection = new BaseCollection(new ArrayList<>());
        }

        if (baseCollection.getCollection() == null){
            baseCollection = new BaseCollection(new ArrayList<>());
        }

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (!baseCollection.getCollection().isEmpty() && baseCollection.getCollection() != null){
                for (Base base : baseCollection.getCollection()){
                    if (System.currentTimeMillis() - base.getDateOfCreation() >= secs && !Bukkit.getOfflinePlayer(UUID.fromString(base.getOwnerUuid())).isOnline()){
                        OpenManagerMenu.deleteBase(base);
                    }
                }
            }
        }, 0L, 200L);

        getServer().getPluginManager().registerEvents(new SetUpListener(), this);
        getCommand("setup").setExecutor(new SetUpRegion());

        getServer().getPluginManager().registerEvents(new CreateBase(), this);
        getServer().getPluginManager().registerEvents(new Interact(), this);
        getServer().getPluginManager().registerEvents(new OpenManagerMenu(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);

        MenuManager.setup(getServer(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        BaseMethods.saveBase(baseCollection);
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p){
        if (playerMenuUtilityHashMap.containsKey(p)){
            return playerMenuUtilityHashMap.get(p);
        }
        PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility(p);
        playerMenuUtilityHashMap.put(p, playerMenuUtility);
        return playerMenuUtility;
    }

}
