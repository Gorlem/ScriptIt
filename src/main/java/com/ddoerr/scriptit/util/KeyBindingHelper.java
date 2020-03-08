package com.ddoerr.scriptit.util;

import com.ddoerr.scriptit.mixin.KeyBindingAccessor;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.mixin.client.keybinding.KeyCodeAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import java.util.Map;

public class KeyBindingHelper {
    public static boolean hasConflict(InputUtil.KeyCode keyCode) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        for (KeyBinding keyBinding : minecraft.options.keysAll) {
            if (hasKeyBindingKeyCode(keyBinding, keyCode)) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasKeyBindingKeyCode(KeyBinding keyBinding, InputUtil.KeyCode keyCode) {
        return ((KeyCodeAccessor)keyBinding).getKeyCode().getName().equals(keyCode.getName());
    }

    public static KeyBinding create(Identifier identifier, InputUtil.KeyCode keyCode) {
        return FabricKeyBinding.Builder.create(
                identifier,
                keyCode.getCategory(),
                keyCode.getKeyCode(),
                "Scripts"
        ).build();
    }
    public static KeyBinding create(Identifier identifier) {
        return create(identifier, InputUtil.UNKNOWN_KEYCODE);
    }

    public static KeyBinding create(String id, InputUtil.KeyCode keyCode) {
        return new KeyBinding(
                id,
                keyCode.getCategory(),
                keyCode.getKeyCode(),
                "Scripts"
        );
    }

    public static void remove(KeyBinding keyBinding) {
        Map<String, KeyBinding> keyBindings = KeyBindingAccessor.getKeysById();
        keyBindings.remove(keyBinding.getId());
        KeyBinding.updateKeysByCode();
    }


    // Copied from KeyBinding::getLocalizedName
    public static String getKeyCodeName(InputUtil.KeyCode keyCode) {
        String result = null;

        switch(keyCode.getCategory()) {
            case KEYSYM:
                result = InputUtil.getKeycodeName(keyCode.getKeyCode());
                break;
            case SCANCODE:
                result = InputUtil.getScancodeName(keyCode.getKeyCode());
                break;
            case MOUSE:
                String translated = I18n.translate(keyCode.getName());
                result = translated.equals(keyCode.getName()) ? I18n.translate(InputUtil.Type.MOUSE.getName(), keyCode.getKeyCode() + 1) : translated;
                break;
        }

        return result == null ? I18n.translate(keyCode.getName()) : result;
    }
}
