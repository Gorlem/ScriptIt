package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.mixin.MessagesAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class ChatLibrary extends AnnotationBasedModel {
    private MinecraftClient minecraft;

    public ChatLibrary(MinecraftClient minecraft) {
        this.minecraft = minecraft;
    }

    @Callable
    public void send(String message) {
        if (minecraft.player != null) {
            minecraft.player.sendChatMessage(message);
        }
    }

    @Callable
    public void log(ContainedValue value) {
        String message = value.format();
        Text text;
        try {
            text = BaseText.Serializer.fromJson(message);
        }
        catch (Exception e) {
            text = new LiteralText(message);
        }

        if (minecraft.player != null) {
            minecraft.player.addChatMessage(text, false);
        }
    }

    @Callable
    public void clear() {
        minecraft.inGameHud.getChatHud().clear(false);
    }

    @Getter
    public List<String> getHistory() {
        return minecraft.inGameHud.getChatHud().getMessageHistory();
    }

    @Getter
    public List<String> getMessages() {
        return ((MessagesAccessor)minecraft.inGameHud.getChatHud())
                .getMessages()
                .stream()
                .map(line -> line.getText().asFormattedString())
                .collect(Collectors.toList());
    }
}
