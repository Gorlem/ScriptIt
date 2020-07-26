package com.ddoerr.scriptit.extension.libraries;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.mixin.ActiveMouseButtonAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class KeyboardLibrary extends AnnotationBasedModel implements Tickable {
    private MinecraftClient minecraft;
    private static List<InputUtil.KeyCode> keyCodes = new ArrayList<>();

    public KeyboardLibrary(MinecraftClient minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void tick() {
        for (InputUtil.KeyCode keyCode : keyCodes) {
            KeyBinding.setKeyPressed(keyCode, false);
        }

        keyCodes.clear();
    }

    @Getter
    public boolean getControl() {
        return Screen.hasControlDown();
    }

    @Getter
    public boolean getShift() {
        return Screen.hasShiftDown();
    }

    @Getter
    public boolean getAlt() {
        return Screen.hasAltDown();
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

    @Callable
    public void toggle(String bindingName, boolean toggleDown) {
        KeyBinding keyBinding = getKeyBind(bindingName);

        if (keyBinding == null) {
            return;
        }

        InputUtil.KeyCode keyCode = InputUtil.fromName(keyBinding.getName());

        if (toggleDown) {
            KeyBinding.onKeyPressed(keyCode);
        }

        KeyBinding.setKeyPressed(keyCode, toggleDown);
    }

    @Callable
    public void toggle(String bindingName) {
        KeyBinding keyBinding = getKeyBind(bindingName);

        if (keyBinding == null) {
            return;
        }

        boolean toggleDown = !keyBinding.isPressed();

        InputUtil.KeyCode keyCode = InputUtil.fromName(keyBinding.getName());

        if (toggleDown) {
            KeyBinding.onKeyPressed(keyCode);
        }

        KeyBinding.setKeyPressed(keyCode, toggleDown);
    }

    @Callable
    public void once(String bindingName) {
        KeyBinding keyBinding = getKeyBind(bindingName);

        if (keyBinding == null) {
            return;
        }

        InputUtil.KeyCode keyCode = InputUtil.fromName(keyBinding.getName());

        KeyBinding.onKeyPressed(keyCode);
        KeyBinding.setKeyPressed(keyCode, true);

        keyCodes.add(keyCode);
    }

    @Callable
    public boolean isKeyDown(String keyName) {
        InputUtil.KeyCode code = InputUtil.fromName(keyName);

        if (code.getCategory() == InputUtil.Type.KEYSYM) {
            return InputUtil.isKeyPressed(minecraft.getWindow().getHandle(), code.getKeyCode());
        } else if (code.getCategory() == InputUtil.Type.MOUSE) {
            return ((ActiveMouseButtonAccessor)minecraft.mouse).getActiveButton() == code.getKeyCode();
        }

        return false;
    }

    @Callable
    public boolean isKeyDown(int keyCode) {
        return InputUtil.isKeyPressed(minecraft.getWindow().getHandle(), keyCode);
    }
}
