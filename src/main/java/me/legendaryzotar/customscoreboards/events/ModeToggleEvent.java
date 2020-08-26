package me.legendaryzotar.customscoreboards.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ModeToggleEvent extends Event {

    final private Player player;
    private static final HandlerList handlers = new HandlerList();

    public ModeToggleEvent(Player player){
        this.player = player;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public Player getPlayer(){
        return player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}