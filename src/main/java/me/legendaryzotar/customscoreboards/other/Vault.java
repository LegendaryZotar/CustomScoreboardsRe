package me.legendaryzotar.customscoreboards.other;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Vault {

    private static Economy economy = null;
    private static Permission permission = null;
    private static Chat chat = null;
    private static JavaPlugin plugin;

    public Vault(JavaPlugin plugin) {
        Vault.plugin = plugin;
        if (!setupEconomy()) {
            plugin.getLogger().severe("- Disabled due to no Vault and/or Economy dependency found!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        if(!setupPermissions()){
            plugin.getLogger().severe("- Disabled due to no Vault and/or Permission dependency found!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        if(!setupChat()){
            plugin.getLogger().severe("- Disabled due to no Vault and/or Chat dependency found!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static Permission getPermission() { return permission; }

    public static Chat getChat() { return chat; }

}