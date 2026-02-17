package me.siam.spawner;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum MobType {

    IRON_GOLEM(EntityType.IRON_GOLEM, Material.IRON_INGOT, 10, 15.0),
    BLAZE(EntityType.BLAZE, Material.BLAZE_ROD, 8, 12.0),
    ZOMBIE(EntityType.ZOMBIE, Material.ROTTEN_FLESH, 5, 4.0),
    SKELETON(EntityType.SKELETON, Material.BONE, 5, 4.0),
    CREEPER(EntityType.CREEPER, Material.GUNPOWDER, 6, 6.0),
    ENDERMAN(EntityType.ENDERMAN, Material.ENDER_PEARL, 7, 10.0),
    SPIDER(EntityType.SPIDER, Material.STRING, 4, 3.0),
    WITCH(EntityType.WITCH, Material.REDSTONE, 9, 14.0),
    GUARDIAN(EntityType.GUARDIAN, Material.PRISMARINE_CRYSTALS, 8, 11.0),
    PIGLIN(EntityType.PIGLIN, Material.GOLD_NUGGET, 6, 7.0),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, Material.COAL, 10, 20.0),
    MAGMA_CUBE(EntityType.MAGMA_CUBE, Material.MAGMA_CREAM, 7, 9.0);

    private final EntityType entityType;
    private final Material dropMaterial;
    private final int xpPerMob;
    private final double sellPrice;

    MobType(EntityType entityType, Material dropMaterial, int xpPerMob, double sellPrice) {
        this.entityType = entityType;
        this.dropMaterial = dropMaterial;
        this.xpPerMob = xpPerMob;
        this.sellPrice = sellPrice;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Material getDropMaterial() {
        return dropMaterial;
    }

    public int getXpPerMob() {
        return xpPerMob;
    }

    public double getSellPrice() {
        return sellPrice;
    }
}
