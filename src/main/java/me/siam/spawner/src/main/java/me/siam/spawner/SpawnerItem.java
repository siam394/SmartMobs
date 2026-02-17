package me.siam.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.CreatureSpawner;

import java.util.ArrayList;
import java.util.List;

public class SpawnerItem {

    private final JavaPlugin plugin;
    private final NamespacedKey mobKey;
    private final NamespacedKey amountKey;

    public SpawnerItem(JavaPlugin plugin) {
        this.plugin = plugin;
        this.mobKey = new NamespacedKey(plugin, "mobType");
        this.amountKey = new NamespacedKey(plugin, "stackAmount");
    }

    public ItemStack createSpawner(MobType type, int amount) {

        if (amount > 100) amount = 100;

        ItemStack item = new ItemStack(Material.SPAWNER);
        BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();

        CreatureSpawner spawner = (CreatureSpawner) meta.getBlockState();
        spawner.setSpawnedType(type.getEntityType());
        meta.setBlockState(spawner);

        meta.getPersistentDataContainer().set(mobKey, PersistentDataType.STRING, type.name());
        meta.getPersistentDataContainer().set(amountKey, PersistentDataType.INTEGER, amount);

        meta.setDisplayName("§6" + formatName(type.name()) + " Spawner");

        List<String> lore = new ArrayList<>();
        lore.add("§7Stack Amount: §e" + amount);
        lore.add("§7Max Stack: §c100");
        lore.add(" ");
        lore.add("§aRight Click to manage");

        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    public boolean isCustomSpawner(ItemStack item) {
        if (item == null || item.getType() != Material.SPAWNER) return false;
        if (!(item.getItemMeta() instanceof BlockStateMeta meta)) return false;

        return meta.getPersistentDataContainer().has(mobKey, PersistentDataType.STRING);
    }

    public MobType getMobType(ItemStack item) {
        BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
        String mobName = meta.getPersistentDataContainer().get(mobKey, PersistentDataType.STRING);
        return MobType.valueOf(mobName);
    }

    public int getStackAmount(ItemStack item) {
        BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
        return meta.getPersistentDataContainer().get(amountKey, PersistentDataType.INTEGER);
    }

    private String formatName(String name) {
        return name.toLowerCase().replace("_", " ");
    }
}
