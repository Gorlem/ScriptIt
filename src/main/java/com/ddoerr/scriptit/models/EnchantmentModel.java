package com.ddoerr.scriptit.models;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import net.minecraft.enchantment.Enchantment;

import java.util.Map;

public class EnchantmentModel extends AnnotationBasedModel {
    public static EnchantmentModel From(Map.Entry<Enchantment, Integer> entry) {
        EnchantmentModel enchantmentModel = new EnchantmentModel();
        enchantmentModel.enchantment = entry.getKey();
        enchantmentModel.level = entry.getValue();
        return enchantmentModel;
    }

    private Enchantment enchantment;
    @Getter
    public int level;

    @Getter
    public String getName() {
        return enchantment.getName(level).asFormattedString();
    }

    @Getter
    public int getMinLevel() {
        return enchantment.getMinimumLevel();
    }

    @Getter
    public int getMaxLevel() {
        return enchantment.getMaximumLevel();
    }

    @Getter
    public boolean getIsCursed() {
        return enchantment.isCursed();
    }

    @Getter
    public boolean getIsTreasure() {
        return enchantment.isTreasure();
    }
}
