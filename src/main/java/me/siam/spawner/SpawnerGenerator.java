package me.siam.spawner;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnerGenerator {

    private final SpawnerPlugin plugin;
    private final NamespacedKey mobKey;
    private final NamespacedKey amountKey;
    private final NamespacedKey xpKey;
    private final NamespacedKey lootKey;

    public SpawnerGenerator(SpawnerPlugin plugin) {
        this.plugin = plugin;
        this.mobKey = new NamespacedKey(plugin, "mobType");
        this.amountKey = new NamespacedKey(plugin, "stackAmount");
        this.xpKey = new NamespacedKey(plugin, "storedXP");
        this.lootKey = new NamespacedKey(plugin, "storedLoot");

        startTask();
    }

    private void startTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (var chunk : world.getLoadedChunks()) {
                        // ঠিক করা অংশ: BlockState array নিচ্ছি
                        for (BlockState state : chunk.getTileEntities()) {
                            Block block = state.getBlock(); // BlockState থেকে Block নিচ্ছি
                            
                            if (!(state instanceof CreatureSpawner spawner)) continue;

                            if (!spawner.getPersistentDataContainer().has(mobKey, PersistentDataType.STRING))
                                continue;

                            String mobName = spawner.getPersistentDataContainer().get(mobKey, PersistentDataType.STRING);
                            int stackAmount = spawner.getPersistentDataContainer().get(amountKey, PersistentDataType.INTEGER);

                            // MobType ক্লাস আছে ধরে নিচ্ছি
                            MobType type = MobType.valueOf(mobName);

                            int generatedMobs = stackAmount * 2;

                            int xpStored = spawner.getPersistentDataContainer().getOrDefault(
                                    xpKey, PersistentDataType.INTEGER, 0);

                            int lootStored = spawner.getPersistentDataContainer().getOrDefault(
                                    lootKey, PersistentDataType.INTEGER, 0);

                            xpStored += generatedMobs * type.getXpPerMob();
                            lootStored += generatedMobs;

                            spawner.getPersistentDataContainer().set(xpKey,
                                    PersistentDataType.INTEGER, xpStored);

                            spawner.getPersistentDataContainer().set(lootKey,
                                    PersistentDataType.INTEGER, lootStored);

                            spawner.update();
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 100, 100); // 5 seconds
    }
}
