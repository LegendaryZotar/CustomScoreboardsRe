package me.legendaryzotar.customscoreboards.commands;

import me.legendaryzotar.customscoreboards.other.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UniStevenIsAwesomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(Utils.Format("&aYes he is!"));
        return true;
    }
}