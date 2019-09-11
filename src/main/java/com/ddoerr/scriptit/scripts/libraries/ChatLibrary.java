package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class ChatLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("chat");

        namespace.registerFunction("send", this::send);
        namespace.registerFunction("log", this::log);
    }

    Object send(String name, MinecraftClient minecraft, Object... arguments) {
        String message = (String)arguments[0];
        minecraft.player.sendChatMessage(message);

        return null;
    }

    Object log(String name, MinecraftClient minecraft, Object... arguments) {
        String message = (String)arguments[0];
        Text text;

        try {
            text = BaseText.Serializer.fromJson(message);
        }
        catch (Exception e) {
            text = new LiteralText(message);
        }

        minecraft.player.addChatMessage(text, false);

        return null;
    }
}
