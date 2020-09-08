package me.legendaryzotar.customscoreboards.listeners;

import me.legendaryzotar.customscoreboards.customclasses.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        ScoreboardManager.createScoreboard(p, true);
    }
}