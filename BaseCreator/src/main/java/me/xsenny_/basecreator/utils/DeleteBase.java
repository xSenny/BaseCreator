package me.xsenny_.basecreator.utils;

import me.xsenny_.basecreator.bases.Base;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class DeleteBase extends BukkitRunnable {

    Base base;
    Integer layer;
    public DeleteBase(Base base){
        this.base = base;
        layer = base.getOrigine().getBlockY();
    }

    @Override
    public void run() {
        if (this.layer <= base.getPanaUnde().getBlockY()){
            Boolean first = base.getOrigine().getBlockZ() < base.getPanaUnde().getBlockZ();
            for (int i = base.getOrigine().getBlockX(); i <= base.getPanaUnde().getBlockX(); i++){
                if (first){
                    for (int j = base.getOrigine().getBlockZ(); j <= base.getPanaUnde().getBlockZ(); j++){
                        base.getOrigine().getWorld().getBlockAt(i, this.layer, j).setType(Material.AIR);
                    }
                }else{
                    for (int j = base.getPanaUnde().getBlockZ(); j <= base.getOrigine().getBlockZ(); j++){
                        base.getOrigine().getWorld().getBlockAt(i, this.layer, j).setType(Material.AIR);
                    }
                }
            }
            this.layer += 1;
        }
    }
}
