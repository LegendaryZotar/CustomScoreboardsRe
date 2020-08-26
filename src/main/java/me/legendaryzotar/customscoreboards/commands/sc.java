package me.legendaryzotar.customscoreboards.commands;

import me.legendaryzotar.customscoreboards.customclasses.ScoreboardManager;
import me.legendaryzotar.customscoreboards.other.Utils;
import me.legendaryzotar.customscoreboards.other.versionchecking.VersionChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class sc implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("Reload")) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("sc.reloadconfig")) {
                        ScoreboardManager.reloadConfig();
                        ScoreboardManager.scoreBoardUpdater();
                        sender.sendMessage(Utils.Format("&aSuccessfully reloaded config!"));
                    } else
                        sender.sendMessage(Utils.Format("&cIncorrect Usage! &e/sc Toggle"));
                } else {
                    ScoreboardManager.reloadConfig();
                }
                return true;
            } else if(args[0].equalsIgnoreCase("Version")){
                if(sender.hasPermission("sc.version") || sender.hasPermission("sc.reloadconfig")){
                    VersionChecker.checkVersion(sender);
                    return true;
                }
            }else if (args[0].equalsIgnoreCase("Toggle")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (ScoreboardManager.players.contains(p)) {
                        ScoreboardManager.players.remove(p);
                        if(ScoreboardManager.QueuedUpdates.contains(p))
                            ScoreboardManager.QueuedUpdates.remove(p);
                        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                        p.sendMessage(Utils.Format("&aScoreboard has been toggled off!"));
                    } else {
                        ScoreboardManager.createScoreboard(p, false);
                        p.sendMessage(Utils.Format("&aScoreboard has been toggled on!"));
                    }
                } else
                    sender.sendMessage(Utils.Format("&cOnly players can execute that command!"));
                return true;
            }
        }
        if (sender instanceof Player) {
            if (sender.hasPermission("sc.reloadconfig")) {
                sender.sendMessage(Utils.Format("&cIncorrect Usage! &e/sc [Toggle | Reload | Version]"));
            } else if(sender.hasPermission("sc.version")){
                sender.sendMessage(Utils.Format("&cIncorrect Usage! &e/sc [Toggle | Version]"));
            }else
                sender.sendMessage(Utils.Format("&cIncorrect Usage! &e/sc Toggle"));
        } else
            sender.sendMessage(Utils.Format("&cIncorrect Usage! &e/sc [Reload | Version]"));
        return true;
    }
}