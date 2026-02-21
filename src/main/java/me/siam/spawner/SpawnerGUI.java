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

        if (!setupEconomy()) {
            getLogger().severe("Vault or Economy not found! Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("spawner").setExecutor(new SpawnerCommand(this));
        Bukkit.getPluginManager().registerEvents(new SpawnerListener(this), this);

        getLogger().info("SmartSpawner Enabled!");
    }

    public static SpawnerPlugin getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return economy;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) return false;

        economy = rsp.getProvider();
        return economy != null;
    }
}
