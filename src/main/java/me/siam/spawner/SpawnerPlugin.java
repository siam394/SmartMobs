package me.siam.spawner;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpawnerPlugin extends JavaPlugin {

    private static SpawnerPlugin instance;
    private SpawnerItem spawnerItem;
    private static Economy econ;

    @Override
    public void onEnable() {

        instance = this;

        if (!setupEconomy()) {
            getLogger().severe("Vault not found! Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        spawnerItem = new SpawnerItem(this);

        Bukkit.getPluginManager().registerEvents(new SpawnerListener(this), this);

        new SpawnerGenerator(this);

        getLogger().info("SpawnerPlugin Enabled Successfully!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager()
                .getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static SpawnerPlugin getInstance() {
        return instance;
    }

    public SpawnerItem getSpawnerItem() {
        return spawnerItem;
    }
}
