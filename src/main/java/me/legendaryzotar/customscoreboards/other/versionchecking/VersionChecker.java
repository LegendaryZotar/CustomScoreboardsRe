package me.legendaryzotar.customscoreboards.other.versionchecking;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionChecker implements Listener {

    //To setup the version checker use setup( main, permissions )
    //main would be your plugin's main class (that extends JavaPlugin)
    //permissions is the string array of permissions that a player should have to be sent the version when they join (only sends if outdated)
    //
    //to use the version checker anywhere just use checkVersion(sender) (VersionChecker.checkVersion(sender))
    //sender would be who sent the command (a CommandSender)
    //alternatively if you'd like to only send the message if its out of date use VersionChecker.checkVersion(sender, true)

    private static JavaPlugin main;
    private static String pluginName;
    private static String version;
    private static String urlPath;

    public static void setup(JavaPlugin main, String url, String[] permissions){
        VersionChecker.main = main;
        pluginName = main.getDescription().getName();
        version = main.getDescription().getVersion();
        urlPath = url;
        Bukkit.getServer().getPluginManager().registerEvents(new VersionChecker(permissions), main);
        checkVersion(main.getServer().getConsoleSender());
    }

    public static void checkVersion(CommandSender sender){
        checkVersion(sender, false);
    }

    public static void checkVersion(CommandSender sender, boolean notifyOnlyIfOutdated) { //Notify if outdated only sends a message if the version is outdated.
        if(!notifyOnlyIfOutdated)
            sendMessage(sender, "&aChecking for a newer version of &b" + pluginName + "&a...");
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try {
                boolean UpToDate = false;
                URL url = new URL(urlPath);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setUseCaches(false);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while((inputLine = in.readLine()) != null){
                    content.append(inputLine);
                }
                in.close();


                String latestVersion = content.toString().trim();

                if(version.equalsIgnoreCase(latestVersion))
                    UpToDate = true;
                con.disconnect();

                if(UpToDate){
                    if(!notifyOnlyIfOutdated)
                        sendMessage(sender, "&aYou are using the latest version of " + pluginName + "! &e(Version: [" + version + "])");
                }else
                    sendMessage(sender, "&cA newer version of &a" + pluginName + "&c is available!" +
                            "\n&eCurrent Version: " + version + "\nLatest Version: " + latestVersion);
            }catch(Exception e){
                sendMessage(sender, "&cAn error has occurred while checking for a new version!\n&eCurrent Version: " + version);
            }
        });
    }

    private static void sendMessage(CommandSender sender, String message){
        if(sender instanceof Player){
            sender.sendMessage("&e["+ pluginName +"] : &r" + message);
        }else
            sender.sendMessage(Format(message));
    }

    private final String[] permissions;

    public VersionChecker(String[] permissions){
        this.permissions = permissions;
    };

    @EventHandler
    void OnPlayerJoin(PlayerJoinEvent e){

        boolean send = false;

        for(String permission : permissions){
            if (e.getPlayer().hasPermission(permission)){
                send = true;
                break;
            }
        }

        if(!send)
            return;

        VersionChecker.checkVersion(e.getPlayer(), true);
    }

    private static String Format(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}