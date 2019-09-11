package com.ddoerr.scriptit.api.util;

import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.scripts.ScriptBinding;
import com.ddoerr.scriptit.scripts.ScriptBindings;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class KeyBindingHelper {
    public static boolean hasConflict(KeyBinding keyBinding) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        for (KeyBinding other : minecraft.options.keysAll) {
            if (other != keyBinding && other.equals(keyBinding)) {
                return true;
            }
        }

        ScriptBindings scriptBindings = Resolver.getInstance().resolve(ScriptBindings.class);
        for (ScriptBinding scriptBinding : scriptBindings.getAll()) {
            if (scriptBinding.getKeyBinding() != keyBinding && scriptBinding.getKeyBinding().equals(keyBinding)) {
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

    public static KeyBinding create(String id, int keyCode) {
        return new KeyBinding(
                id,
                InputUtil.Type.KEYSYM,
                keyCode,
                "Scripts"
        );
    }
}
