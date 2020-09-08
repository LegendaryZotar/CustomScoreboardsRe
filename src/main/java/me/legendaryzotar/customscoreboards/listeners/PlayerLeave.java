package me.legendaryzotar.customscoreboards.listeners;

import me.legendaryzotar.customscoreboards.CustomScoreboards;
import me.legendaryzotar.customscoreboards.customclasses.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    void onLeave(PlayerQuitEvent e){
        if(ScoreboardManager.players.contains(e.getPlayer())){
            ScoreboardManager.players.remove(e.getPlayer());
            ScoreboardManager.LastUsed.remove(e.getPlayer().getUniqueId());
        }
        if(ScoreboardManager.QueuedUpdates.contains(e.getPlayer()))
            ScoreboardManager.QueuedUpdates.remove(e.getPlayer());

        Bukkit.getScheduler().scheduleSyncDelayedTask(CustomScoreboards.getInstance(), () -> {
            ScoreboardManager.scoreBoardUpdater();
        }, 1L);
    }
}