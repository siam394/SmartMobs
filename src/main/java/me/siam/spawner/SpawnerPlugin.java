package me.siam.spawner;

import org.bukkit.plugin.java.JavaPlugin;

public final class SpawnerPlugin extends JavaPlugin {

    private static SpawnerPlugin instance;
    private SpawnerItem spawnerItem;

    @Override
    public void onEnable() {

        instance = this;

        // Initialize spawner item system
        spawnerItem = new SpawnerItem(this);

        getLogger().info("SpawnerPlugin Enabled Successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SpawnerPlugin Disabled.");
    }

    public static SpawnerPlugin getInstance() {
        return instance;
    }

    public SpawnerItem getSpawnerItem() {
        return spawnerItem;
    }
}
