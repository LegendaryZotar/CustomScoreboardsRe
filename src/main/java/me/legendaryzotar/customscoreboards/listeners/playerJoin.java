package me.legendaryzotar.customscoreboards.listeners;

import me.legendaryzotar.customscoreboards.customclasses.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class playerJoin implements Listener {

    @EventHandler
    void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        ScoreboardManager.SetPlayersOnline(Bukkit.getOnlinePlayers().size());
        ScoreboardManager.createScoreboard(p, true);
    }
}