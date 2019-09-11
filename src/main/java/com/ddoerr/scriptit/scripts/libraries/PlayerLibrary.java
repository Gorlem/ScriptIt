package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.*;

public class PlayerLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("player");

        namespace.registerVariable("health", (name, minecraft) -> minecraft.player.getHealth());
        namespace.registerVariable("hunger", (name, minecraft) -> minecraft.player.getHungerManager().getFoodLevel());
        namespace.registerVariable("saturation", (name, minecraft) -> minecraft.player.getHungerManager().getSaturationLevel());
        namespace.registerVariable("armor", (name, minecraft) -> minecraft.player.getArmor());
        namespace.registerVariable("breath", (name, minecraft) -> minecraft.player.getBreath());
        namespace.registerVariable("gamemode", (name, minecraft) -> minecraft.getNetworkHandler().getPlayerListEntry(minecraft.player.getUuid()).getGameMode().getName());

        namespace.registerVariable("x", (name, minecraft) -> minecraft.player.x);
        namespace.registerVariable("y", (name, minecraft) -> minecraft.player.y);
        namespace.registerVariable("z", (name, minecraft) -> minecraft.player.z);

        namespace.registerVariable("yaw", (name, minecraft) -> minecraft.player.yaw % 360);
        namespace.registerVariable("pitch", (name, minecraft) -> minecraft.player.pitch);

        namespace.registerFunction("hit", this::hit);
        namespace.registerFunction("look", this::look);

        namespace.registerVariable("mainhand", this::hand);
        namespace.registerVariable("offhand", this::hand);

        NamespaceRegistry armor = namespace.registerNamespace("armor");
        armor.registerVariable("helmet", this::armor);
        armor.registerVariable("chestplate", this::armor);
        armor.registerVariable("leggings", this::armor);
        armor.registerVariable("boots", this::armor);

        namespace.registerVariable("biome", (name, minecraft) -> convertBiome(minecraft.world.getBiome(minecraft.player.getBlockPos())));
    }

    private static Map<String, Object> convertBiome(Biome biome) {
        Map<String, Object> table = new HashMap<>();

        table.put("id", Registry.BIOME.getId(biome));
        table.put("name", biome.getName().getString());
        table.put("category", biome.getCategory());

        return table;
    }

    private static Map<String, Object> convertItemStack(ItemStack stack) {
        Map<String, Object> table = new HashMap<>();

        table.put("amount", stack.getCount());
        table.put("maxamount", stack.getMaxCount());
        table.put("cooldown", stack.getCooldown());
        table.put("damage", stack.getDamage());
        table.put("maxdamage", stack.getMaxDamage());
        table.put("repaircost", stack.getRepairCost());
        table.put("maxusetime", stack.getMaxUseTime());
        table.put("rarity", stack.getRarity().toString());
        table.put("enchantments", convertEnchantments(stack));
        table.put("name", stack.getName().getString());
        table.put("isenchantable", stack.isEnchantable());
        table.put("isfood", stack.isFood());
        table.put("isdamageable", stack.isDamageable());
        table.put("isstackable", stack.isStackable());
        table.put("id", stack.getItem().toString());

        return table;
    }

    private static List<Map<String, Object>> convertEnchantments(ItemStack stack) {
        List<Map<String, Object>> enchantments = new ArrayList<>();

        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();

            Map<String, Object> table = new HashMap<>();
            table.put("name", enchantment.getName(level).getString());
            table.put("level", level);
            table.put("minlevel", enchantment.getMinimumLevel());
            table.put("maxlevel", enchantment.getMaximumLevel());
            table.put("iscursed", enchantment.isCursed());
            table.put("istreasure", enchantment.isTreasure());

            enchantments.add(table);
        }

        return enchantments;
    }

    Object armor(String name, MinecraftClient minecraft) {
        int index = -1;
        switch (name) {
            case "helmet":
                index = 3;
                break;
            case "chestplate":
                index = 2;
                break;
            case "leggings":
                index = 1;
                break;
            case "boots":
                index = 0;
                break;
        }

        ItemStack itemStack = ((List<ItemStack>) minecraft.player.getArmorItems()).get(index);
        return convertItemStack(itemStack);
    }

    Object hit(String name, MinecraftClient minecraft, Object... arguments) {
        Map<String, Object> map = new HashMap<>();

        if (minecraft.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult)minecraft.hitResult).getBlockPos();
            BlockState blockState = minecraft.world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            Item item = block.asItem();
            map.put("item", convertItemStack(item.getStackForRender()));

            Collection<Property<?>> properties = blockState.getProperties();
            Map<String, String> props = new HashMap<>();

            for (Property property : properties) {
                Comparable comparable = blockState.get(property);
                props.put(property.getName(), comparable.toString());
            }
            map.put("properties", props);
        }

        if (minecraft.hitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult)minecraft.hitResult).getEntity();

            Map<String, String> names = new HashMap<>();
            names.put("name", entity.getEntityName());
            map.put("entity", names);
        }

        return map;
    }

    Object look(String name, MinecraftClient minecraft, Object... arguments) {
        minecraft.player.yaw = (float)arguments[0];
        minecraft.player.pitch = (float)arguments[0];

        return null;
    }

    Object hand(String name, MinecraftClient minecraft) {
        Hand hand = Hand.MAIN_HAND;
        if (name == "offhand") {
            hand = Hand.OFF_HAND;
        }

        return convertItemStack(minecraft.player.getStackInHand(hand));
    }
}
