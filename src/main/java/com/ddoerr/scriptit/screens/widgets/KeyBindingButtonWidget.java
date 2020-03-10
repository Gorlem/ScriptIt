package com.ddoerr.scriptit.screens.widgets;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import spinnery.widget.*;
import spinnery.widget.api.WFocusedMouseListener;

import java.util.function.Consumer;

@WFocusedMouseListener
public class KeyBindingButtonWidget extends WButton {
    private InputUtil.KeyCode keyCode = InputUtil.UNKNOWN_KEYCODE;

    private Consumer<InputUtil.KeyCode> onChange;

    public void setKeyCode(InputUtil.KeyCode keyCode) {
        this.keyCode = keyCode;
        setLabel(getFormattedKey(keyCode));
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

        setLabel(getFormattedKey(keyCode));
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
            setLabel(getFormattedKey(keyCode));

            if (onChange != null) {
                onChange.accept(keyCode);
            }
        }
    }

    public Text getFormattedKey(InputUtil.KeyCode keyCode) {
        if (keyCode == null) {
            return new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "keybinding.unknown").toString());
        }

        String translationKey;

        if (isLowered()) {
            translationKey = "keybinding.active";
        } else if (KeyBindingHelper.hasConflict(keyCode)) {
            translationKey = "keybinding.conflict";
        } else {
            translationKey = "keybinding.default";
        }

        return new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, translationKey).toString(), KeyBindingHelper.getKeyCodeName(keyCode));
    }
}

