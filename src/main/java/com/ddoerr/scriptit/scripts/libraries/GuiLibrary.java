package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.mixin.ContainerAccessor;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;

import java.util.List;
import java.util.stream.Collectors;

public class GuiLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry gui = registry.registerLibrary("gui");

        gui.registerFunction("click_slot", this::clickSlot);
        gui.registerFunction("click_button", this::clickButton);

        gui.registerVariable("screen", (name, mc) -> {
            if (mc.currentScreen != null) {
                return mc.currentScreen.getClass().getSimpleName();
            }
            return null;
        });

        gui.registerVariable("slot_count", (name, mc) -> {
            if (mc.currentScreen instanceof ContainerScreen) {
                return ((ContainerScreen<?>)mc.currentScreen).getContainer().slots.size();
            }
            return 0;
        });

        gui.registerVariable("button_count", (name, mc) -> {
            if (mc.currentScreen == null) {
                return 0;
            }
            return (int)mc.currentScreen.children()
                    .stream()
                    .filter(AbstractPressableButtonWidget.class::isInstance)
                    .count();
        });

        gui.registerVariable("active_stack", (name, mc) -> {
            if (mc.currentScreen instanceof ContainerScreen && mc.player.inventory.getCursorStack() != null) {
                return ObjectConverter.convert(mc.player.inventory.getCursorStack());
            }
            return null;
        });
    }

    private Object clickSlot(String name, MinecraftClient minecraft, Object... arguments) {
        if (!(minecraft.currentScreen instanceof ContainerScreen)) {
            return false;
        }

        ContainerScreen<?> containerScreen = (ContainerScreen<?>) minecraft.currentScreen;

        List<Integer> ids;

        if (arguments[0] instanceof List) {
            ids = ((List<?>)arguments[0])
                    .stream()
                    .map(obj -> Integer.parseInt(obj.toString()))
                    .collect(Collectors.toList());
        } else if (arguments[0] instanceof Integer) {
            ids = Lists.newArrayList((int)arguments[0]);
        } else {
            ids = Lists.newArrayList(Integer.parseInt(arguments[0].toString()));
        }

        SlotActionType actionType = SlotActionType.PICKUP;
        if (arguments.length > 1) {
            String type = arguments[1].toString();
            actionType = SlotActionType.valueOf(type);
        }

        int button = 0;
        if (arguments.length > 2 && arguments[2] instanceof Integer) {
            button = (int)arguments[2];
        }

        int progress = 0;

        if (actionType == SlotActionType.QUICK_CRAFT) {
            ((ContainerAccessor)containerScreen).invokeOnMouseClick(null, -999, Container.packClickData(progress, button), actionType);
            button = Container.packClickData(++progress, button);
        }

        for (int id : ids) {
            if (id >= containerScreen.getContainer().slots.size() || id < 0) {
                continue;
            }

            Slot slot = containerScreen.getContainer().getSlot(id);
            ((ContainerAccessor)containerScreen).invokeOnMouseClick(slot, id, button, actionType);
        }

        if (actionType == SlotActionType.QUICK_CRAFT) {
            ((ContainerAccessor)containerScreen).invokeOnMouseClick(null, -999, Container.packClickData(++progress, button), actionType);
        }

        return true;
    }

    private Object clickButton(String name, MinecraftClient minecraft, Object... arguments) {
        List<AbstractPressableButtonWidget> buttons = minecraft.currentScreen.children()
                .stream()
                .filter(AbstractPressableButtonWidget.class::isInstance)
                .map(AbstractPressableButtonWidget.class::cast)
                .collect(Collectors.toList());

        List<Integer> ids;

        if (arguments[0] instanceof List) {
            ids = ((List<?>)arguments[0])
                    .stream()
                    .map(obj -> Integer.parseInt(obj.toString()))
                    .collect(Collectors.toList());
        } else if (arguments[0] instanceof Integer) {
            ids = Lists.newArrayList((int)arguments[0]);
        } else {
            ids = Lists.newArrayList(Integer.parseInt(arguments[0].toString()));
        }

        for (int id : ids) {
            if (id < 0 || id >= buttons.size()) {
                continue;
            }

            MinecraftClient.getInstance().submit(() -> {
                buttons.get(id).onPress();
            });
        }

        return null;
    }
}
