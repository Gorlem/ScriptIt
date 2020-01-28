package com.ddoerr.scriptit.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class ScreenWithPopup extends Screen {
    private Popup popup;

    protected ScreenWithPopup(Text text_1) {
        super(text_1);
    }

    Popup getPopup() {
        return popup;
    }

    public void openPopup(Popup popup) {
        this.popup = popup;
        MinecraftClient minecraft = MinecraftClient.getInstance();
        popup.init(minecraft, minecraft.getWindow().getScaledWidth(), minecraft.getWindow().getScaledHeight());
    }

    public void clearPopup() {
        popup = null;
    }

    public boolean hasPopup() {
        return popup != null;
    }

    @Override
    public void render(int int_1, int int_2, float float_1) {
        super.render(int_1, int_2, float_1);

        if (hasPopup()) {
            getPopup().render(int_1, int_2, float_1);
        }
    }

    @Override
    public boolean mouseClicked(double double_1, double double_2, int int_1) {
        if (hasPopup()) {
            getPopup().mouseClicked(double_1, double_2, int_1);
            return true;
        }

        return super.mouseClicked(double_1, double_2, int_1);
    }

    @Override
    public boolean mouseReleased(double double_1, double double_2, int int_1) {
        if (hasPopup()) {
            getPopup().mouseReleased(double_1, double_2, int_1);
            return true;
        }

        return super.mouseReleased(double_1, double_2, int_1);
    }

    @Override
    public boolean mouseDragged(double double_1, double double_2, int int_1, double double_3, double double_4) {
        if (hasPopup()) {
            getPopup().mouseDragged(double_1, double_2, int_1, double_3, double_4);
            return true;
        }

        return super.mouseDragged(double_1, double_2, int_1, double_3, double_4);
    }

    @Override
    public boolean mouseScrolled(double double_1, double double_2, double double_3) {
        if (hasPopup()) {
            getPopup().mouseScrolled(double_1, double_2, double_3);
            return true;
        }

        return super.mouseScrolled(double_1, double_2, double_3);
    }

    @Override
    public boolean isMouseOver(double double_1, double double_2) {
        if (hasPopup()) {
            getPopup().isMouseOver(double_1, double_2);
            return true;
        }

        return super.isMouseOver(double_1, double_2);
    }

    @Override
    public boolean keyPressed(int int_1, int int_2, int int_3) {
        if (hasPopup()) {
            getPopup().keyPressed(int_1, int_2, int_3);
            return true;
        }

        return super.keyPressed(int_1, int_2, int_3);
    }

    @Override
    public boolean keyReleased(int int_1, int int_2, int int_3) {
        if (hasPopup()) {
            getPopup().keyReleased(int_1, int_2, int_3);
            return true;
        }

        return super.keyReleased(int_1, int_2, int_3);
    }

    @Override
    public boolean charTyped(char char_1, int int_1) {
        if (hasPopup()) {
            getPopup().charTyped(char_1, int_1);
            return true;
        }

        return super.charTyped(char_1, int_1);
    }

    @Override
    public boolean changeFocus(boolean boolean_1) {
        if (hasPopup()) {
            getPopup().changeFocus(boolean_1);
            return true;
        }

        return super.changeFocus(boolean_1);
    }

    @Override
    public void resize(MinecraftClient minecraftClient_1, int int_1, int int_2) {
        if (hasPopup()) {
            getPopup().resize(minecraftClient_1, int_1, int_2);
        }

        super.resize(minecraftClient_1, int_1, int_2);
    }

    @Override
    public void focusOn(Element element_1) {
        if (hasPopup()) {
            getPopup().focusOn(element_1);
        }

        super.focusOn(element_1);
    }

    @Override
    public void setFocused(Element element_1) {
        if (hasPopup()) {
            getPopup().setFocused(element_1);
        }

        super.setFocused(element_1);
    }

    @Override
    public Element getFocused() {
        if (hasPopup()) {
            return getPopup().getFocused();
        }

        return super.getFocused();
    }
}
