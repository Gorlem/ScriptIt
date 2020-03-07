package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.mixin.ActiveMouseButtonAccessor;
import com.ddoerr.scriptit.util.ObjectConverter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class KeyboardLibrary implements LibraryInitializer, Tickable {
    private static List<InputUtil.KeyCode> keyCodes = new ArrayList<>();

    @Override
    public void onInitialize(LibraryRegistry registry) {
        registry.registerTickable(this);

        NamespaceRegistry namespace = registry.registerLibrary("keyboard");
        namespace.registerFunction("toggle", this::toggle);
        namespace.registerFunction("once", this::once);
        namespace.registerFunction("is_key_pressed", this::isKeyDown);

        namespace.registerVariable("control", (name, minecraft) -> Screen.hasControlDown());
        namespace.registerVariable("shift", (name, minecraft) -> Screen.hasShiftDown());
        namespace.registerVariable("alt", (name, minecraft) -> Screen.hasAltDown());
    }

    @Override
    public void tick() {
        for (InputUtil.KeyCode keyCode : keyCodes) {
            KeyBinding.setKeyPressed(keyCode, false);
        }

        keyCodes.clear();
    }

    private static KeyBinding getKeyBind(String name) {
        KeyBinding keyBinding = null;

        for (KeyBinding entry : MinecraftClient.getInstance().options.keysAll) {
            if (entry.getId().equalsIgnoreCase("key." + name)) {
                keyBinding = entry;
                break;
            }
        }

        return keyBinding;
    }

    private Object toggle(String name, MinecraftClient minecraft, Object... arguments) {
        String bindingName = ObjectConverter.toString(arguments[0]);
        KeyBinding keyBinding = getKeyBind(bindingName);

        if (keyBinding == null)
            return null;

        boolean toggleDown = true;

        if (arguments.length == 1) {
            toggleDown = !keyBinding.isPressed();
        } else {
            toggleDown = ObjectConverter.toBoolean(arguments[1]);
        }

        InputUtil.KeyCode keyCode = InputUtil.fromName(keyBinding.getName());

        if (toggleDown)
            KeyBinding.onKeyPressed(keyCode);

        KeyBinding.setKeyPressed(keyCode, toggleDown);

        return toggleDown;
    }

    private Object once(String name, MinecraftClient minecraft, Object... arguments) {
        String bindingName = ObjectConverter.toString(arguments[0]);
        KeyBinding keyBinding = getKeyBind(bindingName);

        if (keyBinding == null)
            return null;

        InputUtil.KeyCode keyCode = InputUtil.fromName(keyBinding.getName());

        KeyBinding.onKeyPressed(keyCode);
        KeyBinding.setKeyPressed(keyCode, true);

        keyCodes.add(keyCode);

        return true;
    }

    private Object isKeyDown(String name, MinecraftClient minecraft, Object... arguments) {
        if (arguments[0] instanceof Number) {
            return InputUtil.isKeyPressed(minecraft.getWindow().getHandle(), ObjectConverter.toInteger(arguments[0]));
        } else if (arguments[0] instanceof String) {
            InputUtil.KeyCode code = InputUtil.fromName(ObjectConverter.toString(arguments[0]));

            if (code.getCategory() == InputUtil.Type.KEYSYM) {
                return InputUtil.isKeyPressed(minecraft.getWindow().getHandle(), code.getKeyCode());
            } else if (code.getCategory() == InputUtil.Type.MOUSE) {
                return ((ActiveMouseButtonAccessor)minecraft.mouse).getActiveButton() == code.getKeyCode();
            }
        }

        return false;
    }
}
