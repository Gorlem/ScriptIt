package com.ddoerr.scriptit.api.util;

import com.ddoerr.scriptit.bus.KeyBindingBusExtension;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.mixin.KeyBindingAccessor;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import java.util.Map;

public class KeyBindingHelper {
    public static boolean hasConflict(KeyBinding keyBinding) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        for (KeyBinding other : minecraft.options.keysAll) {
            if (other != keyBinding && other.equals(keyBinding)) {
                return true;
            }
        }

        KeyBindingBusExtension keyBindingBusExtension = Resolver.getInstance().resolve(KeyBindingBusExtension.class);
        for (KeyBinding otherKeyBinding : keyBindingBusExtension.getKeyBindings()) {
            if (otherKeyBinding != keyBinding && otherKeyBinding.equals(keyBinding)) {
                return true;
            }
        }

        return false;
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
}
