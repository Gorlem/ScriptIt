package com.ddoerr.scriptit.extension.libraries;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.annotations.Setter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.models.ItemModel;
import com.ddoerr.scriptit.models.PositionModel;
import com.ddoerr.scriptit.models.TargetModel;
import com.google.common.collect.Iterables;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerLibrary extends AnnotationBasedModel {
    MinecraftClient minecraft = MinecraftClient.getInstance();

    @Getter
    public float getHealth() {
        return Optional.ofNullable(minecraft.player).map(LivingEntity::getHealth).orElse(0f);
    }

    @Getter
    public int getArmorValue() {
        return Optional.ofNullable(minecraft.player).map(LivingEntity::getArmor).orElse(0);
    }

    @Getter
    public int getHunger() {
        return Optional.ofNullable(minecraft.player).map(p -> p.getHungerManager().getFoodLevel()).orElse(0);
    }

    @Getter
    public float getSaturation() {
        return Optional.ofNullable(minecraft.player).map(p -> p.getHungerManager().getSaturationLevel()).orElse(0f);
    }

    @Getter
    public int getBreath() {
        return Optional.ofNullable(minecraft.player).map(LivingEntity::getAir).orElse(0);
    }

    @Getter
    public GameMode getGamemode() {
        return Optional.ofNullable(minecraft.player)
                .map(p -> p.networkHandler)
                .map(networkHandler -> networkHandler.getPlayerListEntry(minecraft.player.getUuid()))
                .map(PlayerListEntry::getGameMode)
                .orElse(GameMode.NOT_SET);
    }

    @Getter
    public PositionModel getPosition() {
        return PositionModel.From(Optional.ofNullable(minecraft.player)
                .map(Entity::getPos)
                .orElse(Vec3d.ZERO));
    }

    @Getter
    public float getYaw() {
        return Optional.ofNullable(minecraft.player).map(p -> p.yaw % 360).orElse(0f);
    }

    @Setter
    public void setYaw(float yaw) {
        Optional.ofNullable(minecraft.player).ifPresent(p -> p.yaw = yaw);
    }

    @Getter
    public float getPitch() {
        return Optional.ofNullable(minecraft.player).map(p -> p.pitch).orElse(0f);
    }

    @Setter
    public void setPitch(float pitch) {
        Optional.ofNullable(minecraft.player).ifPresent(p -> p.pitch = pitch);
    }

    @Callable
    public TargetModel target() {
        return TargetModel.From(minecraft.crosshairTarget);
    }

    @Getter
    public ItemModel getMainHand() {
        return getHand(Hand.MAIN_HAND);
    }

    @Getter
    public ItemModel getOffHand() {
        return getHand(Hand.OFF_HAND);
    }

    private ItemModel getHand(Hand hand) {
        ItemStack itemStack = Optional.ofNullable(minecraft.player).map(p -> p.getStackInHand(hand)).orElse(ItemStack.EMPTY);
        return ItemModel.From(itemStack);
    }

    @Getter
    public String getBiome() {
        return Optional.ofNullable(minecraft.world)
                .map(w -> w.getBiome(Optional.ofNullable(minecraft.player).map(LivingEntity::getBlockPos).orElse(BlockPos.ORIGIN)))
                .map(b -> b.getName().asFormattedString())
                .orElse(StringUtils.EMPTY);
    }

    @Getter
    public int getTotalExperience() {
        return Optional.ofNullable(minecraft.player).map(p -> p.totalExperience).orElse(0);
    }

    @Getter
    public float getExperienceProgress() {
        return Optional.ofNullable(minecraft.player).map(p -> p.experienceProgress).orElse(0f);
    }

    @Getter
    public int getExperienceLevel() {
        return Optional.ofNullable(minecraft.player).map(p -> p.experienceLevel).orElse(0);
    }

    @Getter
    public Map<String, ItemModel> getArmor() {
        return Optional.ofNullable(minecraft.player)
                .map(p -> {
                    Iterable<ItemStack> armorItems = p.getArmorItems();
                    Map<String, ItemModel> map = new HashMap<>();
                    map.put("helmet", ItemModel.From(Iterables.get(armorItems, 3)));
                    map.put("chestplate", ItemModel.From(Iterables.get(armorItems, 2)));
                    map.put("leggings", ItemModel.From(Iterables.get(armorItems, 1)));
                    map.put("boots", ItemModel.From(Iterables.get(armorItems, 0)));
                    return map;
                }).orElse(Collections.emptyMap());
    }
}
