package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.util.ObjectConverter;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

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

        namespace.registerVariable("position", (name, minecraft) -> ObjectConverter.convert(minecraft.player.getPos()));

        namespace.registerVariable("yaw", (name, minecraft) -> minecraft.player.yaw % 360);
        namespace.registerVariable("pitch", (name, minecraft) -> minecraft.player.pitch);

        namespace.registerFunction("target", this::target);
        namespace.registerFunction("look", this::look);

        namespace.registerVariable(Hand.MAIN_HAND.toString().toLowerCase(), this::hand);
        namespace.registerVariable(Hand.OFF_HAND.toString().toLowerCase(), this::hand);

        NamespaceRegistry armor = namespace.registerNamespace("armor");
        armor.registerVariable("helmet", this::armor);
        armor.registerVariable("chestplate", this::armor);
        armor.registerVariable("leggings", this::armor);
        armor.registerVariable("boots", this::armor);

        namespace.registerVariable("biome", (name, minecraft) -> ObjectConverter.convert(minecraft.world.getBiome(minecraft.player.getBlockPos())));

        namespace.registerVariable("total_experience", (name, minecraft) -> minecraft.player.totalExperience);
        namespace.registerVariable("experience_progress", (name, minecraft) -> minecraft.player.experienceProgress);
        namespace.registerVariable("experience_level", (name, minecraft) -> minecraft.player.experienceLevel);
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

    Object target(String name, MinecraftClient minecraft, Object... arguments) {
        return ObjectConverter.convert(minecraft.crosshairTarget);
    }

    Object look(String name, MinecraftClient minecraft, Object... arguments) {
        minecraft.player.yaw = ObjectConverter.toFloat(arguments[0]);
        minecraft.player.pitch =  ObjectConverter.toFloat(arguments[1]);

        return null;
    }

    Object hand(String name, MinecraftClient minecraft) {
        Hand hand = Hand.MAIN_HAND;
        if (name.equalsIgnoreCase(Hand.OFF_HAND.toString())) {
            hand = Hand.OFF_HAND;
        }

        return ObjectConverter.convert(minecraft.player.getStackInHand(hand));
    }
}
