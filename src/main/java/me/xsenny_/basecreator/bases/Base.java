package me.xsenny_.basecreator.bases;

import me.xsenny_.basecreator.utils.GsonLocation;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;

public class Base {

    private GsonLocation origine;
    private GsonLocation panaUnde;
    private String ownerUuid;
    private UUID worldUniqueId;
    private Long dateOfCreation;
    private ArrayList<String> members;


    private double maxX;
    private double maxY;
    private double maxZ;
    private double minX;
    private double minY;
    private double minZ;



    public Base(Location origine, Location panaUnde, String ownerUuid) {
        this.origine = new GsonLocation(origine);
        this.panaUnde = new GsonLocation(panaUnde);
        this.ownerUuid = ownerUuid;
        Location firstPoint = origine;
        Location secondPoint = panaUnde;
        worldUniqueId = firstPoint.getWorld().getUID();

        members = new ArrayList<>();
        dateOfCreation = System.currentTimeMillis();

        maxX = Math.max(firstPoint.getX(), secondPoint.getX());
        maxY = Math.max(firstPoint.getY(), secondPoint.getY());
        maxZ = Math.max(firstPoint.getZ(), secondPoint.getZ());

        minX = Math.min(firstPoint.getX(), secondPoint.getX());
        minY = Math.min(firstPoint.getY(), secondPoint.getY());
        minZ = Math.min(firstPoint.getZ(), secondPoint.getZ());
    }

    public Location getOrigine() {
        return origine.toLocation();
    }

    public void setOrigine(Location origine) {
        this.origine = new GsonLocation(origine);
    }

    public Location getPanaUnde() {
        return panaUnde.toLocation();
    }

    public void setPanaUnde(Location panaUnde) {
        this.panaUnde = new GsonLocation(panaUnde);
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }


    public boolean locationIsInRegion(Location loc) {
        return loc.getWorld().getUID().equals(worldUniqueId)
                && loc.getX() >= (int) minX - 1 && loc.getX() <= (int) maxX + 1
                && loc.getY() >= (int) minY - 1 && loc.getY() <= (int) maxY + 1
                && loc.getZ() >= (int) minZ - 1 && loc.getZ() <= (int) maxZ + 1;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
    public Long getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Long dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
