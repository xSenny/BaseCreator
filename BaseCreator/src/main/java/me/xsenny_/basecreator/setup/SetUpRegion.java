package me.xsenny_.basecreator.setup;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import me.xsenny_.basecreator.BaseCreator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SetUpRegion implements CommandExecutor {

    public static HashMap<String, Boolean> isPlayerInEditMode = new HashMap<>();

    public static HashMap<String, ArrayList<Location>> playerSetUpBase = new HashMap<>();

    public static HashMap<String, Location> playerFirstBase = new HashMap<>();
    public static FileConfiguration c = BaseCreator.plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;
            if (p.hasPermission("basecreator.setup")){
                if (args.length == 0){
                    File file = new File(BaseCreator.plugin.getDataFolder().getAbsolutePath() + "/base.schem");
                    ClipboardFormat format = ClipboardFormats.findByFile(file);

                    try {
                        ClipboardReader reader = format.getReader(new FileInputStream(file));
                        Clipboard clipboard = reader.read();
                        World adaptedWorld = BukkitAdapter.adapt(p.getWorld());
                        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
                        Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ())).ignoreAirBlocks(true).build();
                        p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                        try{
                            Operations.complete(operation);
                            editSession.flushSession();
                        }catch (WorldEditException ex){
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("error")));
                            ex.printStackTrace();
                        }
                        isPlayerInEditMode.put(p.getUniqueId().toString(), true);

                        ArrayList<Location> locations = new ArrayList<>();
                        locations.add(new Location(p.getWorld(), 0, 0, 0));
                        locations.add(new Location(p.getWorld(), 0, 0, 0));

                        playerSetUpBase.put(p.getUniqueId().toString(), locations);
                        playerFirstBase.put(p.getUniqueId().toString(), p.getLocation());

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("start")));

                        ItemStack axe = new ItemStack(Material.NETHERITE_AXE);
                        ItemMeta meta = axe.getItemMeta();
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("items.axe.name")));
                        ArrayList<String> lore = new ArrayList<>();
                        for (int i = 0; i < c.getList("items.axe.lore").size(); i++){
                            lore.add(ChatColor.translateAlternateColorCodes('&',  (String) c.getList("items.axe.lore").get(i)));
                        }
                        meta.setLore(lore);
                        axe.setItemMeta(meta);
                        ItemStack emerald = new ItemStack(Material.EMERALD);
                        ItemMeta meta1 = emerald.getItemMeta();
                        meta1.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString("items.emerald.name")));
                        lore = new ArrayList<>();
                        for (int i = 0; i < c.getList("items.emerald.lore").size(); i++){
                            lore.add(ChatColor.translateAlternateColorCodes('&', (String) c.getList("items.emerald.lore").get(i)));
                        }
                        meta1.setLore(lore);
                        emerald.setItemMeta(meta1);
                        p.getInventory().addItem(axe, emerald);

                        return true;

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else if (args[0].equalsIgnoreCase("exit")){
                    SetUpRegion.isPlayerInEditMode.remove(p.getUniqueId().toString());
                    SetUpRegion.playerFirstBase.remove(p.getUniqueId().toString());
                    SetUpRegion.playerSetUpBase.remove(p.getUniqueId().toString());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("left")));
                }
            }else{
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("no-permission")));
                return true;
            }

        }else{
            System.out.println(ChatColor.translateAlternateColorCodes('&', c.getString("no-console")));
        }



        return true;
    }
}
