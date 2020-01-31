package com.ddoerr.scriptit.widgets;

import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import spinnery.widget.*;

import java.text.Normalizer;
import java.util.Map;
import java.util.Objects;

public class KeyBindingButtonWidget extends WButton {
    private InputUtil.KeyCode keyCode = InputUtil.UNKNOWN_KEYCODE;

    public KeyBindingButtonWidget(WPosition position, WSize size, WInterface linkedInterface) {
        super(position, size, linkedInterface);

        setLabel(new LiteralText(getFormattedKey()));
    }

    public KeyBindingButtonWidget(WPosition position, WSize size, WInterface linkedInterface, InputUtil.KeyCode keyCode) {
        super(position, size, linkedInterface);

        this.keyCode = keyCode;
        setLabel(new LiteralText(getFormattedKey()));
    }

    public static Theme of(Map<String, String> rawTheme) {
        return WButton.of(rawTheme);
    }

    public void setKeyCode(InputUtil.KeyCode keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isLowered()) {
            keyCode = InputUtil.Type.MOUSE.createFromCode(mouseButton);
            setLowered(false);
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
        }
    }

    public String getFormattedKey() {
        if (keyCode == null) {
            return Formatting.DARK_RED + "???";
        }

        if (isLowered()) {
            return Formatting.WHITE + "> " + Formatting.YELLOW + getKeyCodeName(keyCode) + Formatting.WHITE + " <";
        }

        if (KeyBindingHelper.hasConflict(keyCode)) {
            return Formatting.RED + getKeyCodeName(keyCode);
        }

        return getKeyCodeName(keyCode);
    }

    private String getKeyCodeName(InputUtil.KeyCode keyCode) {
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
