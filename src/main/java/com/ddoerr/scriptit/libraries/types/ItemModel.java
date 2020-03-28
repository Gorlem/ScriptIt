package com.ddoerr.scriptit.libraries.types;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.stream.Collectors;

public class ItemModel extends AnnotationBasedModel {
    public static ItemModel From(ItemStack item) {
        ItemModel itemModel = new ItemModel();
        itemModel.itemStack = item;
        return itemModel;
    }

    private ItemStack itemStack;

    @Getter
    public int getAmount() {
        return itemStack.getCount();
    }

    @Getter
    public int getMaxAmount() {
        return itemStack.getMaxCount();
    }

    @Getter
    public int getCooldown() {
        return itemStack.getCooldown();
    }

    @Getter
    public int getDamage() {
        return itemStack.getDamage();
    }

    @Getter
    public int getMaxDamage() {
        return itemStack.getMaxDamage();
    }

    @Getter
    public int getRepairCost() {
        return itemStack.getRepairCost();
    }

    @Getter
    public int getMaxUsetime() {
        return itemStack.getMaxUseTime();
    }

    @Getter
    public Rarity getRarity() {
        return itemStack.getRarity();
    }

    @Getter
    public List<EnchantmentModel> getEnchantments() {
        return EnchantmentHelper.getEnchantments(itemStack)
                .entrySet()
                .stream()
                .map(EnchantmentModel::From)
                .collect(Collectors.toList());
    }

    @Getter
    public String getName() {
        return itemStack.getName().asFormattedString();
    }

    @Getter
    public String getId() {
        return itemStack.getItem().toString();
    }

    @Getter
    public boolean getIsEnchantable() {
        return itemStack.isEnchantable();
    }

    @Getter
    public boolean getIsFood() {
        return itemStack.isFood();
    }

    @Getter
    public boolean getIsDamageable() {
        return itemStack.isDamageable();
    }

    @Getter boolean getIsStackable() {
        return itemStack.isStackable();
    }
}
