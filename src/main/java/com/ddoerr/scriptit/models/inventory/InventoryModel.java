package com.ddoerr.scriptit.models.inventory;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.models.ItemModel;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public abstract class InventoryModel extends AnnotationBasedModel {
    private static Map<Predicate<Screen>, BiFunction<Screen, PlayerInventory, InventoryModel>> inventories = new LinkedHashMap<>();

    static {
        inventories.put(CreativeInventoryScreen.class::isInstance, CreativeInventoryModel::new);
        inventories.put(ContainerScreen.class::isInstance, ContainerInventoryModel::new);
        inventories.put(screen -> true, DefaultInventoryModel::new);
    }

    private static InventoryModel noInventoryModel = new NoInventoryModel();

    public static InventoryModel From(Screen screen, PlayerInventory inventory) {
        return inventories.entrySet().stream()
                .filter(e -> e.getKey().test(screen))
                .findFirst()
                .map(e -> e.getValue().apply(screen, inventory))
                .orElse(noInventoryModel);
    }

    public abstract void renderTooltip(int mouseX, int mouseY);

    @Getter
    public abstract List<ItemModel> getSlots();

    @Callable
    public abstract ItemModel slot(int slot);

    @Getter
    public abstract int getSlotCount();

    @Getter
    public abstract ItemModel getActiveStack();


    @Callable
    public void click(int slot) {
        click(slot, ClickType.WholeStack);
    }
    @Callable
    public void click(int slot, ClickType clickType) {
        click(slot, clickType.button);
    }
    @Callable
    public abstract void click(int slot, int button);

    @Callable
    public void drop() {
        drop(ClickType.WholeStack);
    }
    @Callable
    public void drop(ClickType clickType) {
        drop(clickType.button);
    }
    @Callable
    public abstract void drop(int button);

    @Callable
    public void drop(int slot, ClickType clickType) {
        drop(slot, clickType.button == 0 ? 1 : 0);
    }
    @Callable
    public abstract void drop(int slot, int button);

    @Callable
    public abstract void move(int slot);

    @Callable
    public abstract void clone(int slot);

    @Callable
    public abstract void swap(int slot, int button);

    @Callable
    public void distribute(List<Integer> slots, ClickType clickType) {
        distribute(slots, clickType.button);
    }

    @Callable
    public abstract void distribute(List<Integer> slots, int button);

    public enum ClickType {
        WholeStack(0),
        Evenly(0),
        Left(0),
        HalfStack(1),
        SingleItem(1),
        Right(1);

        protected final int button;
        ClickType(int button) {
            this.button = button;
        }
    }

    private static class NoInventoryModel extends InventoryModel {
        @Override
        public void renderTooltip(int mouseX, int mouseY) {}
        @Override
        public List<ItemModel> getSlots() { return Collections.emptyList(); }
        @Override
        public ItemModel slot(int slot) { return ItemModel.From(ItemStack.EMPTY); }
        @Override
        public int getSlotCount() { return 0; }
        @Override
        public ItemModel getActiveStack() { return ItemModel.From(ItemStack.EMPTY); }
        @Override
        public void click(int slot, int button) {}
        @Override
        public void drop(int button) {}
        @Override
        public void drop(int slot, int button) {}
        @Override
        public void move(int slot) {}
        @Override
        public void clone(int slot) {}
        @Override
        public void swap(int slot, int button) {}
        @Override
        public void distribute(List<Integer> slots, int button) {}
    }
}
