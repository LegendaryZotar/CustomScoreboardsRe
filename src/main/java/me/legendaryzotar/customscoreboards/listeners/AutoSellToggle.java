package me.legendaryzotar.customscoreboards.listeners;

import me.legendaryzotar.customscoreboards.customclasses.ScoreboardManager;
import me.legendaryzotar.customscoreboards.events.ModeToggleEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AutoSellToggle implements Listener {

    @EventHandler
    void onToggle(ModeToggleEvent e){
        Player p = e.getPlayer();
        if(ScoreboardManager.players.contains(p)){
            ScoreboardManager.UpdateScoreboard(p, false);
        }
    }
}