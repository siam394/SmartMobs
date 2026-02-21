package me.siam.spawner;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

    public SpawnerGUI(SpawnerPlugin plugin) {
        this.plugin = plugin;
        this.econ = SpawnerPlugin.getInstance().getEconomy()

        this.xpKey = new NamespacedKey(plugin, "storedXP");
        this.lootKey = new NamespacedKey(plugin, "storedLoot");
        this.mobKey = new NamespacedKey(plugin, "mobType");
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        Block block = event.getClickedBlock();

        if (!(block.getState() instanceof CreatureSpawner spawner)) return;

        if (!spawner.getPersistentDataContainer().has(mobKey, PersistentDataType.STRING))
            return;

        event.setCancelled(true);

        openGUI(event.getPlayer(), block);
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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getView().getTitle().equals("§6Spawner Control")) {

            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();

            if (clicked == null) return;

            Block target = player.getTargetBlockExact(5);
            if (target == null) return;

            if (!(target.getState() instanceof CreatureSpawner spawner)) return;

            // XP Collect
            if (clicked.getType() == Material.EXPERIENCE_BOTTLE) {

                int xp = spawner.getPersistentDataContainer()
                        .getOrDefault(xpKey, PersistentDataType.INTEGER, 0);

                if (xp <= 0) {
                    player.sendMessage("§cNo XP stored!");
                    return;
                }

                player.giveExp(xp);

                spawner.getPersistentDataContainer().set(xpKey,
                        PersistentDataType.INTEGER, 0);
                spawner.update();

                player.sendMessage("§aCollected XP: §e" + xp);
                player.closeInventory();
            }

            // Sell Loot
            if (clicked.getType() == Material.EMERALD) {

                int loot = spawner.getPersistentDataContainer()
                        .getOrDefault(lootKey, PersistentDataType.INTEGER, 0);

                if (loot <= 0) {
                    player.sendMessage("§cNo loot stored!");
                    return;
                }

                String mobName = spawner.getPersistentDataContainer()
                        .get(mobKey, PersistentDataType.STRING);

                MobType type = MobType.valueOf(mobName);

                double totalMoney = loot * type.getSellPrice();

                econ.depositPlayer(player, totalMoney);

                spawner.getPersistentDataContainer().set(lootKey,
                        PersistentDataType.INTEGER, 0);
                spawner.update();

                player.sendMessage("§aSold Loot for §6$" + totalMoney);
                player.closeInventory();
            }
        }
    }
}
