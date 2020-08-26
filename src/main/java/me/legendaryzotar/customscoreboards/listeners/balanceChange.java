package me.legendaryzotar.customscoreboards.listeners;

import me.legendaryzotar.customscoreboards.CustomScoreboards;
import me.legendaryzotar.customscoreboards.customclasses.ScoreboardManager;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class balanceChange implements Listener {

    @EventHandler
    void onBalanceChange(UserBalanceUpdateEvent e) {
        if (ScoreboardManager.players.contains(e.getPlayer())) {
            ScoreboardManager.Balances.put(e.getPlayer().getUniqueId(), ScoreboardManager.GetBalance(e.getPlayer(), e.getNewBalance().doubleValue()));
            ScoreboardManager.UpdateScoreboard(e.getPlayer(), false);
            Bukkit.getScheduler().runTaskLater(CustomScoreboards.getInstance(), () -> ScoreboardManager.UpdateScoreboard(e.getPlayer(), false), ScoreboardManager.minDelay / 50);
        }
    }

}