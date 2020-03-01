package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.mixin.ContainerAccessor;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;

import java.util.List;
import java.util.stream.Collectors;

public class GuiLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry gui = registry.registerLibrary("gui");

        gui.registerFunction("clickslot", this::clickSlot);

        gui.registerVariable("screen", (name, mc) -> {
            if (mc.currentScreen != null) {
                return mc.currentScreen.getClass().getSimpleName();
            }
            return null;
        });

        gui.registerVariable("activestack", (name, mc) -> {
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
            Slot slot = containerScreen.getContainer().getSlot(id);
            ((ContainerAccessor)containerScreen).invokeOnMouseClick(slot, id, button, actionType);
        }

        if (actionType == SlotActionType.QUICK_CRAFT) {
            ((ContainerAccessor)containerScreen).invokeOnMouseClick(null, -999, Container.packClickData(++progress, button), actionType);
        }

        return true;
    }
}
