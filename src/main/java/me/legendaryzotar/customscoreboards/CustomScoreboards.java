package me.legendaryzotar.customscoreboards;

import me.legendaryzotar.customscoreboards.commands.sc;
import me.legendaryzotar.customscoreboards.commands.unistevenisawesome;
import me.legendaryzotar.customscoreboards.customclasses.ScoreboardManager;
import me.legendaryzotar.customscoreboards.listeners.*;
import me.legendaryzotar.customscoreboards.other.Utils;
import me.legendaryzotar.customscoreboards.other.Vault;
import me.legendaryzotar.customscoreboards.other.versionchecking.VCPlayerJoin;
import me.legendaryzotar.customscoreboards.other.versionchecking.VersionChecker;
import me.legendaryzotar.customscoreboards.tabcompleters.scComp;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomScoreboards extends JavaPlugin{

    private static CustomScoreboards instance = null;

    public void onEnable() {

        instance = this;

        saveDefaultConfig();
        addMissingConfig();
        Vault vault = new Vault(this);

        ScoreboardManager.Instance();

        getCommand("sc").setExecutor(new sc());
        getCommand("unistevenisawesome").setExecutor(new unistevenisawesome());
        getCommand("sc").setTabCompleter(new scComp());

        getServer().getPluginManager().registerEvents(new playerJoin(), this);
        getServer().getPluginManager().registerEvents(new playerLeave(), this);
        getServer().getPluginManager().registerEvents(new balanceChange(), this);
        getServer().getPluginManager().registerEvents(new AutoSellToggle(), this);
        getServer().getPluginManager().registerEvents(new VCPlayerJoin(), this);

        getServer().getConsoleSender().sendMessage(Utils.Format("&aCustomScoreboards &e[" + getDescription().getVersion() + "]&a has been Enabled!"));

        VersionChecker.setup(this, "https://raw.githubusercontent.com/LegendaryZotar/LegendaryZotarVersions/master/minecraft-plugins/CustomScoreboards", new String[] {"sc.version", "sc.reloadconfig"});
    }

    public static CustomScoreboards getInstance(){
        return instance;
    }

    public void onDisable() {
        getServer().getConsoleSender().sendMessage(Utils.Format("&cCustomScoreboards &e[" + getDescription().getVersion() + "]&c has been Disabled!"));
    }

    void addMissingConfig() {
        boolean update = false;
        if (!getConfig().contains("ASONMODE", true)) {
            getConfig().set("ASONMODE", "&aOn");
            update = true;
        }
        if (!getConfig().contains("ASOFFMODE", true)) {
            getConfig().set("ASOFFMODE", "&cOff");
            update = true;
        }
        if (!getConfig().contains("StartUpDelay", true)) {
            getConfig().set("StartUpDelay", 10);
            update = true;
        }
        if (!getConfig().contains("MinUpdateDelay", true)) {
            getConfig().set("MinUpdateDelay", 250);
            update = true;
        }

        String LatestHeader = "# %PLAYERNAME% Returns player name.\n" +
                "# %PLAYERSONLINE% Returns amount of players online.\n" +
                "# %MAXPLAYERS% Returns max number of players that can be on the server.\n" +
                "# %BALANCE% Returns the balance of the player.\n" +
                "# %RANK% Returns the players rank.\n" +
                "# %ASMODE% Displays configured On/Off message for AutoSell mode.\n" +
                "#Lines cannot be longer than 40 letters, (using things like %BALANCE% doesn't count 9, rather it counts as the players money (50.0 is 4).\n" +
                "#Color codes such as &a work.\n" +
                "#StartUpDelay is the delay between the plugin getting enabled and the plugin starting its Automatic Work, Adjust this if your encounter errors on start up, Default is 10 (Seconds).\n" +
                "#The [Title] under Scoreboard is the title of the scoreboard, Using the server name is recommended.\n" +
                "The MinUpdateDelay defines the minimum interval between each update, if it is not met, it will queue the update. Default is 250 (MilliSeconds, so 4 times a second.)";

        if(getConfig().options().header() != null){
            if(!getConfig().options().header().equals(LatestHeader)){
                update = true;
                getConfig().options().header(LatestHeader);
            }
        }else{
            getConfig().options().header(LatestHeader);
            update = true;
        }

        if(update) {
            saveConfig();
            getServer().getConsoleSender().sendMessage(Utils.Format("&aUpdated CustomScoreboard Config to version &e[" + getServer().getVersion() + "]&a!" ));
        }
    }
}