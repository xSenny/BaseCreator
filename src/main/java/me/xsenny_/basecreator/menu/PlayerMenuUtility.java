package me.xsenny_.basecreator.menu;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private Player owner;
    //store the player that will be killed so we can access him in the next menu
    private String playerToRemove;
    private String uuidToRemove;

    public PlayerMenuUtility(Player p) {
        this.owner = p;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getPlayerToRemove() {
        return playerToRemove;
    }

    public void setPlayerToRemove(String playerToRemove) {
        this.playerToRemove = playerToRemove;
    }
}
