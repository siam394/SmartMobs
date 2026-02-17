package me.siam.spawner;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SpawnerGUI implements Listener {

    private final SpawnerPlugin plugin;
    private final Economy econ;

    private final NamespacedKey xpKey;
    private final NamespacedKey lootKey;
    private final NamespacedKey mobKey;

    public SpawnerGUI(SpawnerPlugin plugin, Economy econ) {
        this.plugin = plugin;
        this.econ = econ;

        this.xpKey = new NamespacedKey(plugin, "storedXP");
        this.lootKey = new NamespacedKey(plugin, "storedLoot");
        this.mobKey = new NamespacedKey(plugin, "mobType");
    }

    public void openGUI(Player player, Block block) {

        CreatureSpawner spawner = (CreatureSpawner) block.getState();

        Inventory inv = Bukkit.createInventory(null, 27, "§6Spawner Control");

        int xp = spawner.getPersistentDataContainer().getOrDefault(xpKey, PersistentDataType.INTEGER, 0);
        int loot = spawner.getPersistentDataContainer().getOrDefault(lootKey, PersistentDataType.INTEGER, 0);

        // XP Button
        ItemStack xpItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta xpMeta = xpItem.getItemMeta();
        xpMeta.setDisplayName("§aCollect XP");

        List<String> xpLore = new ArrayList<>();
        xpLore.add("§7Stored XP: §e" + xp);
        xpMeta.setLore(xpLore);

        xpItem.setItemMeta(xpMeta);
        inv.setItem(11, xpItem);

        // Sell Button
        ItemStack sellItem = new ItemStack(Material.EMERALD);
        ItemMeta sellMeta = sellItem.getItemMeta();
        sellMeta.setDisplayName("§6Sell Loot");

        List<String> sellLore = new ArrayList<>();
        sellLore.add("§7Stored Loot: §e" + loot);
        sellMeta.setLore(sellLore);

        sellItem.setItemMeta(sellMeta);
        inv.setItem(15, sellItem);

        player.openInventory(inv);
    }
}
