package com.ddoerr.scriptit.api.util;

import com.ddoerr.scriptit.ScriptContainer;
import com.ddoerr.scriptit.Scripts;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.mixin.KeyBindingAccessor;
import com.ddoerr.scriptit.triggers.KeybindingTrigger;
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

        Scripts scriptBindings = Resolver.getInstance().resolve(Scripts.class);
        for (ScriptContainer scriptBinding : scriptBindings.getAll(Scripts.KEYBIND_CATEGORY)) {
            if (!(scriptBinding.getTrigger() instanceof  KeybindingTrigger)) {
                continue;
            }

            KeyBinding otherKeyBinding = ((KeybindingTrigger)scriptBinding.getTrigger()).getKeyBinding();
            if (otherKeyBinding != keyBinding && otherKeyBinding.equals(keyBinding)) {
                return true;
            }
        }

        return false;
    }

    public static KeyBinding create(Identifier identifier, int keyCode) {
        return FabricKeyBinding.Builder.create(
                identifier,
                InputUtil.Type.KEYSYM,
                keyCode,
                "Scripts"
        ).build();
    }
    public static KeyBinding create(Identifier identifier) {
        return create(identifier, -1);
    }

    public static KeyBinding create(String id, int keyCode) {
        return new KeyBinding(
                id,
                InputUtil.Type.KEYSYM,
                keyCode,
                "Scripts"
        );
    }

    public static void remove(KeyBinding keyBinding) {
        Map<String, KeyBinding> keyBindings = KeyBindingAccessor.getKeysById();
        keyBindings.remove(keyBinding.getId());
        KeyBinding.updateKeysByCode();
    }
}
