package me.legendaryzotar.customscoreboards.other.versionchecking;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VCPlayerJoin implements Listener {

    @EventHandler
    void OnPlayerJoin(PlayerJoinEvent e){
        if(e.getPlayer().hasPermission("sc.version") || e.getPlayer().hasPermission("sc.reloadconfig")){ //Insert Permission(s) Here
            VersionChecker.checkVersion(e.getPlayer(), true);
        }
    }
}