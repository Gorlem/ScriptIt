package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.util.ObjectConverter;
import com.google.common.collect.Iterables;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.Collections;
import java.util.Optional;

public class PlayerLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("player");

        namespace.registerVariable("health", (name, minecraft) -> Optional.ofNullable(minecraft.player).map(LivingEntity::getHealth).orElse(0f));
        namespace.registerVariable("hunger", (name, minecraft) -> Optional.ofNullable(minecraft.player).map(p -> p.getHungerManager().getFoodLevel()).orElse(0));
        namespace.registerVariable("saturation", (name, minecraft) -> Optional.ofNullable(minecraft.player).map(p -> p.getHungerManager().getSaturationLevel()).orElse(0f));
        namespace.registerVariable("breath", (name, minecraft) -> Optional.ofNullable(minecraft.player).map(LivingEntity::getAir).orElse(0));
        namespace.registerVariable("gamemode", (name, minecraft) -> Optional.ofNullable(minecraft.player)
                .map(p -> p.networkHandler)
                .map(networkHandler -> networkHandler.getPlayerListEntry(minecraft.player.getUuid()))
                .map(PlayerListEntry::getGameMode)
                .orElse(GameMode.NOT_SET)
                .getName());

        namespace.registerVariable("position", (name, minecraft) -> ObjectConverter.convert(Optional.ofNullable(minecraft.player)
                .map(Entity::getPos)
                .orElse(Vec3d.ZERO)));

        namespace.registerVariable("yaw", (name, minecraft) -> Optional.ofNullable(minecraft.player).map(p -> p.yaw % 360).orElse(0f));
        namespace.registerVariable("pitch", (name, minecraft) -> Optional.ofNullable(minecraft.player).map(p -> p.pitch).orElse(0f));

        namespace.registerFunction("target", this::target);
        namespace.registerFunction("look", this::look);

        namespace.registerVariable(Hand.MAIN_HAND.toString().toLowerCase(), this::hand);
        namespace.registerVariable(Hand.OFF_HAND.toString().toLowerCase(), this::hand);

        NamespaceRegistry armor = namespace.registerNamespace("armor");
        armor.registerVariable("value",(name, minecraft) -> Optional.ofNullable(minecraft.player).map(LivingEntity::getArmor).orElse(0));
        armor.registerVariable("helmet", this::armor);
        armor.registerVariable("chestplate", this::armor);
        armor.registerVariable("leggings", this::armor);
        armor.registerVariable("boots", this::armor);

        namespace.registerVariable("biome", (name, minecraft) -> Optional.ofNullable(minecraft.world)
                .map(w -> w.getBiome(Optional.ofNullable(minecraft.player).map(LivingEntity::getBlockPos).orElse(BlockPos.ORIGIN))));

        namespace.registerVariable("total_experience", (name, minecraft) -> Optional.ofNullable(minecraft.player).map(p -> p.totalExperience).orElse(0));
        namespace.registerVariable("experience_progress", (name, minecraft) -> Optional.ofNullable(minecraft.player).map(p -> p.experienceProgress).orElse(0f));
        namespace.registerVariable("experience_level", (name, minecraft) -> Optional.ofNullable(minecraft.player).map(p -> p.experienceLevel).orElse(0));
    }

    Object armor(String name, MinecraftClient minecraft) {
        int index;
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
                default:
                index = 0;
                break;
        }

        ItemStack itemStack = Optional.ofNullable(minecraft.player)
                .map(PlayerEntity::getArmorItems)
                .map(i -> Iterables.get(i, index))
                .orElse(ItemStack.EMPTY);
        return ObjectConverter.convert(itemStack);
    }

    Object target(String name, MinecraftClient minecraft, Object... arguments) {
        return Optional.ofNullable(minecraft.crosshairTarget).map(ObjectConverter::convert).orElse(Collections.emptyMap());
    }

    Object look(String name, MinecraftClient minecraft, Object... arguments) {
        if (minecraft.player == null) {
            return null;
        }

        minecraft.player.yaw = ObjectConverter.toFloat(arguments[0]);
        minecraft.player.pitch =  ObjectConverter.toFloat(arguments[1]);

        return null;
    }

    Object hand(String name, MinecraftClient minecraft) {
        Hand hand;
        if (name.equalsIgnoreCase(Hand.MAIN_HAND.toString())) {
            hand = Hand.MAIN_HAND;
        } else {
            hand = Hand.OFF_HAND;
        }

        ItemStack stack = Optional.ofNullable(minecraft.player).map(p -> p.getStackInHand(hand)).orElse(ItemStack.EMPTY);
        return ObjectConverter.convert(stack);
    }
}
