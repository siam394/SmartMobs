package me.siam.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerListener implements Listener {

    private final SpawnerPlugin plugin;
    private final NamespacedKey mobKey;
    private final NamespacedKey amountKey;

    public SpawnerListener(SpawnerPlugin plugin) {
        this.plugin = plugin;
        this.mobKey = new NamespacedKey(plugin, "mobType");
        this.amountKey = new NamespacedKey(plugin, "stackAmount");
    }

    @EventHandler
    public void onSpawnerPlace(BlockPlaceEvent event) {

        ItemStack item = event.getItemInHand();

        if (item.getType() != Material.SPAWNER) return;
        if (!(item.getItemMeta() instanceof BlockStateMeta meta)) return;

        if (!meta.getPersistentDataContainer().has(mobKey, PersistentDataType.STRING)) {
            // Natural spawner convert
            Block block = event.getBlockPlaced();
            CreatureSpawner spawner = (CreatureSpawner) block.getState();

            spawner.getPersistentDataContainer().set(mobKey,
                    PersistentDataType.STRING,
                    spawner.getSpawnedType().name());

            spawner.getPersistentDataContainer().set(amountKey,
                    PersistentDataType.INTEGER,
                    1);

            spawner.update();

            return;
        }

        String mobName = meta.getPersistentDataContainer().get(mobKey, PersistentDataType.STRING);
        int itemAmount = meta.getPersistentDataContainer().get(amountKey, PersistentDataType.INTEGER);

        Block block = event.getBlockPlaced();
        CreatureSpawner placedSpawner = (CreatureSpawner) block.getState();

        placedSpawner.getPersistentDataContainer().set(mobKey,
                PersistentDataType.STRING,
                mobName);

        placedSpawner.getPersistentDataContainer().set(amountKey,
                PersistentDataType.INTEGER,
                itemAmount);

        placedSpawner.update();

        // STACK CHECK
        Block below = block.getRelative(0, -1, 0);

        if (below.getType() == Material.SPAWNER) {

            CreatureSpawner belowSpawner = (CreatureSpawner) below.getState();

            if (belowSpawner.getPersistentDataContainer().has(mobKey, PersistentDataType.STRING)) {

                String belowMob = belowSpawner.getPersistentDataContainer().get(mobKey, PersistentDataType.STRING);
                int belowAmount = belowSpawner.getPersistentDataContainer().get(amountKey, PersistentDataType.INTEGER);

                if (belowMob.equalsIgnoreCase(mobName)) {

                    int newAmount = belowAmount + itemAmount;

                    if (newAmount > 100) {
                        event.getPlayer().sendMessage("§cMax stack limit is 100!");
                        return;
                    }

                    belowSpawner.getPersistentDataContainer().set(amountKey,
                            PersistentDataType.INTEGER,
                            newAmount);

                    belowSpawner.update();

                    block.setType(Material.AIR);

                    event.getPlayer().sendMessage("§aSpawner stacked! Total: §e" + newAmount);
                }
            }
        }
    }
}
