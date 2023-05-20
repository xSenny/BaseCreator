package me.xsenny_.basecreator.create;

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
import me.xsenny_.basecreator.bases.Base;
import me.xsenny_.basecreator.listener.OpenManagerMenu;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;


public class CreateBase implements Listener {


    public static HashMap<Player, Boolean> hasCalled = new HashMap<>();
    public static BaseCreator plugin = BaseCreator.plugin;

    @EventHandler
    public static void onInteract(PlayerInteractEvent e){
        if (e.getHand() != EquipmentSlot.HAND) { // * if the hand used is NOT the main hand:
            return; // do not progress past this point  |
        }
        if (hasCalled.get(e.getPlayer()) == null || !hasCalled.get(e.getPlayer())){
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
                Player p = e.getPlayer();
                if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getItemMeta() != null){
                    if (p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(BaseCreator.plugin, "paper"), PersistentDataType.INTEGER)){
                        if (p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(BaseCreator.plugin, "paper"), PersistentDataType.INTEGER) == 20 && e.getClickedBlock() != null){
                            if (OpenManagerMenu.getBase(p) == null){
                                File file = new File(BaseCreator.plugin.getDataFolder().getAbsolutePath() + "/base.schem");
                                ClipboardFormat format = ClipboardFormats.findByFile(file);

                                try {
                                    ClipboardReader reader = format.getReader(new FileInputStream(file));
                                    Clipboard clipboard = reader.read();

                                    e.setCancelled(true);

                                    int inaltime = clipboard.getDimensions().getY();
                                    int latime = clipboard.getDimensions().getX();
                                    int lungime = clipboard.getDimensions().getZ();
                                    FileConfiguration c = plugin.getConfig();
                                    int x1 = c.getInt("changes.x1");
                                    int x2 = c.getInt("changes.x2");
                                    int y1 = c.getInt("changes.y1");
                                    int y2 = c.getInt("changes.y2");
                                    int z1 = c.getInt("changes.z1");
                                    int z2 = c.getInt("changes.z2");
                                    for (int i =e.getClickedBlock().getX() + x1; i <= e.getClickedBlock().getX() + x2; i++) {
                                        for (int j = e.getClickedBlock().getY() + y1; j <= e.getClickedBlock().getY() + y2; j++) {
                                            for (int k = e.getClickedBlock().getZ() + z1; k <= e.getClickedBlock().getZ() + z2; k++) {
                                                if (p.getWorld().getBlockAt(new Location(p.getWorld(), i, j, k)).getType() != Material.AIR && p.getWorld().getBlockAt(new Location(p.getWorld(), i, j, k)).getType() != Material.GRASS && p.getWorld().getBlockAt(i, j, k).getType() != Material.TALL_GRASS) {
                                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("no-space").replace("{lun}", "" + lungime).replace("{lat}", ""+latime).replace("{ina}", "" + inaltime)));
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    for (int i =e.getClickedBlock().getX() + x1; i <= e.getClickedBlock().getX() + x2; i++) {
                                        for (int k = e.getClickedBlock().getZ() + z1; k <= e.getClickedBlock().getZ() + z2; k++) {
                                            if (p.getWorld().getBlockAt(new Location(p.getWorld(), i, e.getClickedBlock().getY(), k)).getType() == Material.AIR) {
                                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("create-ground")));
                                                return;
                                            }
                                        }
                                    }
                                    Location start = e.getClickedBlock().getLocation();
                                    start.setX(start.getBlockX() + x1);
                                    start.setY(start.getBlockY() + y1);
                                    start.setZ(start.getBlockZ() + z1);
                                    if (areSpatiu(start)){
                                        Location loc = new Location(e.getClickedBlock().getWorld(), e.getClickedBlock().getX() + x2, e.getClickedBlock().getY() + y2, e.getClickedBlock().getZ() + z2);
                                        Base base = new Base(start, loc, p.getUniqueId().toString());
                                        BaseCreator.baseCollection.getCollection().add(base);

                                        World adaptedWorld = BukkitAdapter.adapt(p.getWorld());
                                        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);

                                        Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(e.getClickedBlock().getX(), e.getClickedBlock().getY(), e.getClickedBlock().getZ())).ignoreAirBlocks(true).build();
                                        p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                                        try{
                                            Operations.complete(operation);
                                            editSession.flushSession();
                                        }catch (WorldEditException ex){
                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("error")));
                                            ex.printStackTrace();
                                        }


                                    }else{
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', c.getString("no-distance")));
                                    }

                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }else{
                                Base base = OpenManagerMenu.getBase(p);
                                FileConfiguration c = plugin.getConfig();
                                p.sendMessage(c.getString("already-have-base").replace("{x}", String.valueOf(base.getOrigine().getBlockX())).replace("{y}", String.valueOf(base.getOrigine().getBlockY())).replace("{z}", String.valueOf(base.getOrigine().getBlockZ())).replace("{world}", base.getOrigine().getWorld().getName()));
                            }
                        }
                    }
                }
            }
            hasCalled.put(e.getPlayer(), true);
        }else{
            hasCalled.put(e.getPlayer(), false);
        }
    }

    public static boolean areSpatiu(Location location){
        int distanta = 20;
        for (Base base : BaseCreator.baseCollection.getCollection()){
            if (Math.abs(location.getBlockX() - base.getOrigine().getBlockX()) > distanta || Math.abs(location.getBlockZ() - base.getOrigine().getBlockZ()) > distanta){
                if (Math.abs(location.getBlockX() - base.getPanaUnde().getBlockX()) > distanta || Math.abs(location.getBlockZ() - base.getPanaUnde().getBlockZ()) > distanta){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }

}
