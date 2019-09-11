package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.api.hud.HudHorizontalAnchor;
import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudVerticalAnchor;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import com.ddoerr.scriptit.widgets.ColorDisplayWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.Window;
import net.minecraft.text.LiteralText;

import java.util.Arrays;
import java.util.List;

public class WidgetOptionsPopup extends Popup {
    Rectangle background;
    HudElement hudElement;

    TextFieldWidget binding;
    TextFieldWidget forecolorSelection;
    ColorDisplayWidget forecolorDisplay;
    TextFieldWidget backcolorSelection;
    ColorDisplayWidget backcolorDisplay;
    ButtonWidget horizontalAnchorButton;
    ButtonWidget  verticalAnchorButton;

    HudHorizontalAnchor horizontalAnchor;
    HudVerticalAnchor verticalAnchor;

    int width = 200;
    int height = 200;

    public WidgetOptionsPopup(HudElement hudElement) {
        super(new LiteralText("Text Hud Element Settings"));
        this.hudElement = hudElement;
    }

    void save() {
        hudElement.setOption(HudElement.BINDING, binding.getText());
        hudElement.setOption(HudElement.FORE_COLOR, forecolorSelection.getText());
        hudElement.setOption(HudElement.BACK_COLOR, backcolorSelection.getText());

        Point point = hudElement.getRealPosition();
        hudElement.setOption(HudElement.HORIZONTAL_ANCHOR, horizontalAnchor);
        hudElement.setOption(HudElement.VERTICAL_ANCHOR, verticalAnchor);
        hudElement.setRealPosition(point);

        onClose();
    }

    @Override
    protected void init() {
        super.init();

        boolean firstLoad = background == null;

        if (firstLoad) {
            horizontalAnchor = hudElement.getOption(HudElement.HORIZONTAL_ANCHOR);
            verticalAnchor = hudElement.getOption(HudElement.VERTICAL_ANCHOR);
        }

        Window window = MinecraftClient.getInstance().window;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        background = Rectangle.center(window.getScaledWidth(), window.getScaledHeight(), width, height);

        binding = new TextFieldWidget(
            textRenderer,
            background.getMinX() + 10, background.getMinY() + 20,
            180, 20,
            binding,
            "Script Binding"
        );
        binding.setMaxLength(1000);
        children.add(binding);

        forecolorSelection = new TextFieldWidget(
            textRenderer,
            background.getMinX() + 10, background.getMinY() + 60,
            155, 20,
                forecolorSelection,
            "Forecolor Selection"
        );
        forecolorSelection.setMaxLength(1000);
        forecolorSelection.setChangedListener((text) -> {
            forecolorDisplay.setColor(Color.parse(text));
        });
        children.add(forecolorSelection);

        forecolorDisplay = new ColorDisplayWidget(
                background.getMinX() + 170, background.getMinY() + 60,
                20, 20,
                Color.BLACK);
        forecolorDisplay.setColor(Color.parse(forecolorSelection.getText()));
        children.add(forecolorDisplay);

        backcolorSelection = new TextFieldWidget(
                textRenderer,
                background.getMinX() + 10, background.getMinY() + 100,
                155, 20,
                backcolorSelection,
                "Backcolor Selection"
        );
        backcolorSelection.setMaxLength(1000);
        backcolorSelection.setChangedListener((text) -> {
            backcolorDisplay.setColor(Color.parse(text));
        });
        children.add(backcolorSelection);

        backcolorDisplay = new ColorDisplayWidget(
                background.getMinX() + 170, background.getMinY() + 100,
                20, 20,
                Color.BLACK);
        backcolorDisplay.setColor(Color.parse(backcolorSelection.getText()));
        children.add(backcolorDisplay);

        verticalAnchorButton = new ButtonWidget(
                background.getMinX() + 10, background.getMinY() + 130,
                85, 20,
                verticalAnchor.toString(), (button) -> {
            List<HudVerticalAnchor> hudAnchors = Arrays.asList(HudVerticalAnchor.values());
            int index = hudAnchors.indexOf(verticalAnchor);
            int newIndex = index + 1 == hudAnchors.size() ? 0 : index + 1;
            verticalAnchor = hudAnchors.get(newIndex);
            button.setMessage(verticalAnchor.toString());
        });
        children.add(verticalAnchorButton);

        horizontalAnchorButton = new ButtonWidget(
                background.getMinX() + 105, background.getMinY() + 130,
                85, 20,
                horizontalAnchor.toString(), (button) -> {
            List<HudHorizontalAnchor> hudAnchors = Arrays.asList(HudHorizontalAnchor.values());
            int index = hudAnchors.indexOf(horizontalAnchor);
            int newIndex = index + 1 == hudAnchors.size() ? 0 : index + 1;
            horizontalAnchor = hudAnchors.get(newIndex);
            button.setMessage(horizontalAnchor.toString());
        });
        children.add(horizontalAnchorButton);

        children.add(new ButtonWidget(background.getMinX() + 10, background.getMaxY() - 30, width - 20, 20, "Save", (button) -> {
            save();
        }));

        if (firstLoad) {
            binding.setText(hudElement.getOption(HudElement.BINDING));
            forecolorSelection.setText(hudElement.getOption(HudElement.FORE_COLOR));
            backcolorSelection.setText(hudElement.getOption(HudElement.BACK_COLOR));
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float v) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        fill(background.getMinX(), background.getMinY(), background.getMaxX(), background.getMaxY(), Color.BLACK.getValue());

        fill(background.getMinX(), background.getMinY() - 20, background.getMaxX(), background.getMinY() - 1, Color.RED.getValue());
        drawString(textRenderer, title.getString(), background.getMinX() + 10, background.getMinY() - 14, Color.WHITE.getValue());

        for (Element element : children) {
            if (element instanceof Drawable) {
                ((Drawable) element).render(mouseX, mouseY, v);
            }

            if (element instanceof TextFieldWidget) {
                AbstractButtonWidget buttonWidget = ((AbstractButtonWidget) element);

                String message = buttonWidget.getMessage();
                drawString(textRenderer, message, buttonWidget.x, buttonWidget.y - 10, Color.WHITE.getValue());
            }
        }
    }

    @Override
    public boolean mouseClicked(double double_1, double double_2, int int_1) {
        if (!background.contains(double_1, double_2)) {
            this.onClose();
            return true;
        }

        return super.mouseClicked(double_1, double_2, int_1);
    }
}
