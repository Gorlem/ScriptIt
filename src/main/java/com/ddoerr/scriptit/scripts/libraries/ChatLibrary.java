package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.mixin.MessagesAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("chat");

        namespace.registerFunction("send", this::send);
        namespace.registerFunction("log", this::log);
        namespace.registerFunction("clear", (name, mc, args) -> {
            mc.inGameHud.getChatHud().clear(false);
            return null;
        });

        namespace.registerVariable("messages", (name, mc) -> ((MessagesAccessor)mc.inGameHud.getChatHud())
                .getMessages()
                .stream()
                .map(line -> line.getText().asFormattedString())
                .collect(Collectors.toList()));

        namespace.registerVariable("history", (name, mc) -> mc.inGameHud.getChatHud().getMessageHistory());
    }

    Object send(String name, MinecraftClient minecraft, Object... arguments) {
        String message = (String)arguments[0];
        if (minecraft.player != null) {
            minecraft.player.sendChatMessage(message);
        }

        return null;
    }

    Object log(String name, MinecraftClient minecraft, Object... arguments) {
        Text text = new LiteralText("");
        if (arguments[0] instanceof String) {
            String message = (String)arguments[0];

            try {
                text = BaseText.Serializer.fromJson(message);
            }
            catch (Exception e) {
                text = new LiteralText(message);
            }
        } else if (arguments[0] instanceof Number) {
            Number number = (Number)arguments[0];
            text = new LiteralText(number.toString());
        } else if (arguments[0] instanceof Iterable) {
            Iterable<?> iterable = (Iterable<?>)arguments[0];
            StringBuilder message = new StringBuilder("Iterable: ");

            for (Object object : iterable) {
                message.append("\n").append(object.toString());
            }

            text = new LiteralText(message.toString());
        } else if (arguments[0] instanceof Map) {
            Map<?, ?> map = (Map<?, ?>)arguments[0];

            StringBuilder message = new StringBuilder("Map: ");

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                message.append("\n")
                        .append(entry.getKey().toString())
                        .append(" = ")
                        .append(entry.getValue().toString());
            }

            text = new LiteralText(message.toString());
        }

        if (minecraft.player != null) {
            minecraft.player.addChatMessage(text, false);
        }

        return null;
    }
}
