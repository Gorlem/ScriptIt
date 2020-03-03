package com.ddoerr.scriptit.scripts.libraries;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.*;

public class ObjectConverter {
    public static Map<String, Object> convert(Biome biome) {
        Map<String, Object> map = new HashMap<>();

        map.put("id", Registry.BIOME.getId(biome));
        map.put("name", biome.getName().getString());
        map.put("category", biome.getCategory());

        return map;
    }

    public static Map<String, Object> convert(ItemStack stack) {
        Map<String, Object> map = new HashMap<>();

        map.put("amount", stack.getCount());
        map.put("max_amount", stack.getMaxCount());
        map.put("cooldown", stack.getCooldown());
        map.put("damage", stack.getDamage());
        map.put("max_damage", stack.getMaxDamage());
        map.put("repair_cost", stack.getRepairCost());
        map.put("max_usetime", stack.getMaxUseTime());
        map.put("rarity", stack.getRarity().toString());
        map.put("enchantments", convert(EnchantmentHelper.getEnchantments(stack)));
        map.put("name", stack.getName().getString());
        map.put("is_enchantable", stack.isEnchantable());
        map.put("is_food", stack.isFood());
        map.put("is_damageable", stack.isDamageable());
        map.put("is_stackable", stack.isStackable());
        map.put("id", stack.getItem().toString());

        return map;
    }

    public static List<Map<String, Object>> convert(Map<Enchantment, Integer> enchantments) {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();

            Map<String, Object> table = new HashMap<>();
            table.put("name", enchantment.getName(level).getString());
            table.put("level", level);
            table.put("min_level", enchantment.getMinimumLevel());
            table.put("max_level", enchantment.getMaximumLevel());
            table.put("is_cursed", enchantment.isCursed());
            table.put("is_treasure", enchantment.isTreasure());

            list.add(table);
        }

        return list;
    }

    public static Map<String, Object> convert(BlockState blockState) {
        Map<String, Object> map = new HashMap<>();

        Block block = blockState.getBlock();
        Item item = block.asItem();
        map.put("item", ObjectConverter.convert(item.getStackForRender()));

        Collection<Property<?>> properties = blockState.getProperties();
        Map<String, String> props = new HashMap<>();

        for (Property property : properties) {
            Comparable comparable = blockState.get(property);
            props.put(property.getName(), comparable.toString());
        }

        map.put("properties", props);

        return map;
    }

    public static Map<String, Object> convert(Entity entity) {
        Map<String, Object> map = new HashMap<>();

        map.put("name", entity.getEntityName());
        map.put("uuid", entity.getUuidAsString());

        return map;
    }

    public static Map<String, Object> convert(Vec3d vector) {
        Map<String, Object> map = new HashMap<>();

        map.put("x", vector.getX());
        map.put("y", vector.getY());
        map.put("z", vector.getZ());

        return map;
    }
}
