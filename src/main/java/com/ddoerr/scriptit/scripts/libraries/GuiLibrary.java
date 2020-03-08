package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.util.ObjectConverter;
import com.ddoerr.scriptit.util.buttons.ButtonHelper;
import com.ddoerr.scriptit.util.slots.SlotHelper;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.container.SlotActionType;

import java.util.List;
import java.util.stream.Collectors;

public class GuiLibrary implements LibraryInitializer {
    private ButtonHelper buttonHelper = new ButtonHelper();
    private SlotHelper slotHelper = new SlotHelper();

    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry gui = registry.registerLibrary("gui");

        gui.registerFunction("click_slot", this::clickSlot);
        gui.registerFunction("click_button", this::clickButton);
        gui.registerFunction("find_slot", this::findSlot);
        gui.registerFunction("get_item", this::getSlotContent);

        gui.registerVariable("screen", (name, mc) -> {
            if (mc.currentScreen != null) {
                return mc.currentScreen.getClass().getSimpleName();
            }
            return null;
        });

        gui.registerVariable("slot_count", (name, mc) -> slotHelper.getAmount(mc.currentScreen));
        gui.registerVariable("button_count", (name, mc) -> buttonHelper.getAmount(mc.currentScreen));

        gui.registerVariable("active_stack", (name, mc) -> {
            if (mc.currentScreen instanceof ContainerScreen && mc.player.inventory.getCursorStack() != null) {
                return ObjectConverter.convert(mc.player.inventory.getCursorStack());
            }
            return null;
        });
    }

    private Object clickSlot(String name, MinecraftClient minecraft, Object... arguments) {
        List<Integer> ids;

        if (arguments[0] instanceof List) {
            ids = ((List<?>)arguments[0])
                    .stream()
                    .map(ObjectConverter::toInteger)
                    .collect(Collectors.toList());
        } else {
            ids = Lists.newArrayList(ObjectConverter.toInteger(arguments[0]));
        }

        SlotActionType actionType = SlotActionType.PICKUP;
        if (arguments.length > 1) {
            actionType = ObjectConverter.toEnum(SlotActionType.class, arguments[1]);
        }

        int button = 0;
        if (arguments.length > 2) {
            button = ObjectConverter.toInteger(arguments[2]);
        }

        int progress = 0;

        if (actionType == SlotActionType.QUICK_CRAFT) {
            slotHelper.click(minecraft.currentScreen, -999, Container.packClickData(progress, button), actionType);
            button = Container.packClickData(++progress, button);
        }

        for (int id : ids) {
            slotHelper.click(minecraft.currentScreen, id, button, actionType);
        }

        if (actionType == SlotActionType.QUICK_CRAFT) {
            slotHelper.click(minecraft.currentScreen, -999, Container.packClickData(++progress, button), actionType);
        }

        return true;
    }

    private Object clickButton(String name, MinecraftClient minecraft, Object... arguments) {
        List<Integer> ids;

        if (arguments[0] instanceof List) {
            ids = ((List<?>)arguments[0])
                    .stream()
                    .map(ObjectConverter::toInteger)
                    .collect(Collectors.toList());
        } else {
            ids = Lists.newArrayList(ObjectConverter.toInteger(arguments[0]));
        }

        for (int id : ids) {
            buttonHelper.click(minecraft.currentScreen, id);
        }

        return null;
    }

    private Object findSlot(String name, MinecraftClient minecraft, Object... arguments) {
        String id = ObjectConverter.toString(arguments[0]);
        return slotHelper.findSlot(minecraft.currentScreen, id);
    }

    private Object getSlotContent(String name, MinecraftClient minecraft, Object... arguments) {
        int index = ObjectConverter.toInteger(arguments[0]);
        return ObjectConverter.convert(slotHelper.getSlotContent(minecraft.currentScreen, index));
    }
}
