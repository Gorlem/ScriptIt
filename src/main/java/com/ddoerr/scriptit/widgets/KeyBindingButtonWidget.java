package com.ddoerr.scriptit.widgets;

import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;

public class KeyBindingButtonWidget extends AbstractPressableButtonWidget {
    private KeyBinding keyBinding;
    boolean isCurrentlyReassigning = false;

    public KeyBindingButtonWidget(int x, int y, int width, int height, KeyBinding keyBinding) {
        super(x, y, width, height, "");

        this.keyBinding = keyBinding;
        setMessage(getFormattedKey());
    }

    @Override
    public void onPress() {
        isCurrentlyReassigning = true;
    }

    @Override
    public boolean keyPressed(int int_1, int int_2, int int_3) {
        if (!isCurrentlyReassigning) {
            return super.keyPressed(int_1, int_2, int_3);
        }

        InputUtil.KeyCode keyCode = InputUtil.getKeyCode(int_1, int_2);
        keyBinding.setKeyCode(keyCode);
        KeyBinding.updateKeysByCode();

        isCurrentlyReassigning = false;
        return true;
    }

    public String getFormattedKey() {
        if (isCurrentlyReassigning) {
            return Formatting.WHITE + "> " + Formatting.YELLOW + keyBinding.getLocalizedName() + Formatting.WHITE + " <";
        }

        if (KeyBindingHelper.hasConflict(keyBinding)) {
            return Formatting.RED + keyBinding.getLocalizedName();
        }

        return keyBinding.getLocalizedName();
    }

    @Override
    public void render(int int_1, int int_2, float float_1) {
        setMessage(getFormattedKey());

        super.render(int_1, int_2, float_1);
    }
}
