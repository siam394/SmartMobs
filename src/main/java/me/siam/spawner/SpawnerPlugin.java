package me.siam.spawner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SpawnerPlugin extends JavaPlugin {

    private static SpawnerPlugin instance;
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;

        // Setup Vault Economy
        if (!setupEconomy()) {
            getLogger().severe("Vault or Economy provider not found! Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Register Command
        getCommand("spawner").setExecutor(new SpawnerCommand(this));

        // Register Events
        Bukkit.getPluginManager().registerEvents(new SpawnerListener(this), this);

        getLogger().info("SmartSpawner Enabled Successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SmartSpawner Disabled!");
    }

    public static SpawnerPlugin getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return economy;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return economy != null;
    }
}
