package com.ddoerr.scriptit.widgets;

import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import spinnery.client.BaseRenderer;
import spinnery.widget.*;

import java.text.Normalizer;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class KeyBindingButtonWidget extends WButton {
    private InputUtil.KeyCode keyCode = InputUtil.UNKNOWN_KEYCODE;

    private Consumer<InputUtil.KeyCode> onChange;

    public void setKeyCode(InputUtil.KeyCode keyCode) {
        this.keyCode = keyCode;
        setLabel(new LiteralText(getFormattedKey()));
    }

    public void setOnChange(Consumer<InputUtil.KeyCode> onChange) {
        this.onChange = onChange;
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isLowered()) {
            keyCode = InputUtil.Type.MOUSE.createFromCode(mouseButton);
            setLowered(false);

            if (onChange != null) {
                onChange.accept(keyCode);
            }
        } else {
            super.onMouseClicked(mouseX, mouseY, mouseButton);
        }

        setLabel(new LiteralText(getFormattedKey()));
    }

    @Override
    public void tick() {
        // do not trigger `tick` from `WButton`
    }

    @Override
    public void onKeyPressed(int keyPressed, int character, int keyModifier) {
        super.onKeyPressed(keyPressed, character, keyModifier);

        if (isLowered()) {
            keyCode = InputUtil.getKeyCode(keyPressed, character);

            setLowered(false);
            setLabel(new LiteralText(getFormattedKey()));

            if (onChange != null) {
                onChange.accept(keyCode);
            }
        }
    }

    public String getFormattedKey() {
        if (keyCode == null) {
            return Formatting.DARK_RED + "???";
        }

        if (isLowered()) {
            return Formatting.WHITE + "> " + Formatting.YELLOW + KeyBindingHelper.getKeyCodeName(keyCode) + Formatting.WHITE + " <";
        }

        if (KeyBindingHelper.hasConflict(keyCode)) {
            return Formatting.RED + KeyBindingHelper.getKeyCodeName(keyCode);
        }

        return KeyBindingHelper.getKeyCodeName(keyCode);
    }
}

