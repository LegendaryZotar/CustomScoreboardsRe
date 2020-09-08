package me.legendaryzotar.customscoreboards.listeners;

import me.legendaryzotar.customscoreboards.customclasses.ScoreboardManager;
import me.legendaryzotar.customscoreboards.events.ModeToggleEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerModeToggle implements Listener {

    @EventHandler
    void modeToggle(ModeToggleEvent e){
        ScoreboardManager.UpdateScoreboard(e.getPlayer(), false);
    }
}