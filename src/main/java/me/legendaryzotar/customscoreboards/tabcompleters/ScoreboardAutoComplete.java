package me.legendaryzotar.customscoreboards.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAutoComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            if(args.length == 1) {
                if (sender.hasPermission("sc.reloadconfig")) {
                    List<String> temp = new ArrayList();
                    temp.add("Reload");
                    temp.add("Version");
                    temp.add("Toggle");
                    return SmartComplete(args[args.length - 1], temp);
                }if (sender.hasPermission("sc.version")){
                    List<String> temp = new ArrayList();
                    temp.add("Version");
                    temp.add("Toggle");
                    return SmartComplete(args[args.length - 1], temp);
                }else{
                    List<String> temp = new ArrayList();
                    temp.add("Toggle");
                    return SmartComplete(args[args.length - 1], temp);
                }
            }else
                return new ArrayList();
        }else
        {
            if(args.length == 1){
                List<String> temp = new ArrayList();
                temp.add("Reload");
                temp.add("Version");
                return SmartComplete(args[args.length - 1], temp);
            }else
                return new ArrayList();
        }
    }

    List<String> SmartComplete(String Arg, List<String> list) {
        List<String> temp = new ArrayList();
        for (String item : list) {
            if (item.toUpperCase().startsWith(Arg.toUpperCase()))
                temp.add(item);
        }
        return temp;
    }
}