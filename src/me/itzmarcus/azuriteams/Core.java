package me.itzmarcus.azuriteams;

import me.itzmarcus.azuriteams.events.ChatEvent;
import me.itzmarcus.azuriteams.managers.TeamManager;
import me.itzmarcus.azuriteams.utils.MyConfig;
import me.itzmarcus.azuriteams.utils.MyConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by marcus on 25-07-2016.
 */
public class Core extends JavaPlugin {

    public static Economy econ = null;
    TeamManager tm = null;

    public static MyConfigManager myConfigManager;
    public static MyConfig config;
    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            Bukkit.getServer().getConsoleSender().sendMessage("SKAL BRUGE ESSENTIALS");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        myConfigManager = new MyConfigManager(this);
        File f = new File(getDataFolder(), "config.yml");
        if(!f.exists()) {
            config = myConfigManager.getNewConfig("config.yml");
            config.set("teams", "");
            config.saveConfig();
        } else {
            config = myConfigManager.getNewConfig("config.yml");
        }
        tm = new TeamManager();
        tm.loadTeams();
        getCommand("team").setExecutor(new MainCommand());
        registerListeners(new ChatEvent());
    }

    public void onDisable() {
        TeamManager.saveTeams();
        tm = null;
    }

    public void registerListeners(Listener... listeners) {
        for(Listener l : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(l, this);
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
