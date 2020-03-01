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
        namespace.registerVariable("breath", (name, minecraft) -> minecraft.player.getAir());
        namespace.registerVariable("gamemode", (name, minecraft) -> minecraft.getNetworkHandler().getPlayerListEntry(minecraft.player.getUuid()).getGameMode().getName());

        namespace.registerVariable("x", (name, minecraft) -> minecraft.player.getX());
        namespace.registerVariable("y", (name, minecraft) -> minecraft.player.getY());
        namespace.registerVariable("z", (name, minecraft) -> minecraft.player.getZ());

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

        namespace.registerVariable("biome", (name, minecraft) -> ObjectConverter.convert(minecraft.world.getBiome(minecraft.player.getBlockPos())));
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
        return ObjectConverter.convert(itemStack);
    }

    Object hit(String name, MinecraftClient minecraft, Object... arguments) {
        Map<String, Object> map = new HashMap<>();

        HitResult target = minecraft.crosshairTarget;

        if (target == null) {
            return null;
        }

        if (target.getType() == HitResult.Type.BLOCK && target instanceof BlockHitResult) {
            BlockPos blockPos = ((BlockHitResult) target).getBlockPos();
            BlockState blockState = minecraft.world.getBlockState(blockPos);
            map.put("block", ObjectConverter.convert(blockState));
        }

        if (target.getType() == HitResult.Type.ENTITY &&  target instanceof EntityHitResult) {
            Entity entity = ((EntityHitResult) target).getEntity();
            map.put("entity", ObjectConverter.convert(entity));
        }

        map.put("position", ObjectConverter.convert(target.getPos()));
        map.put("type", target.getType().toString());

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

        return ObjectConverter.convert(minecraft.player.getStackInHand(hand));
    }
}
