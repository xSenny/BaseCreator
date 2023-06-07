package me.xsenny_.basecreator.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GsonLocation {

    public String world;
    public int x;
    public int y;
    public int z;


    public GsonLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GsonLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }
}
