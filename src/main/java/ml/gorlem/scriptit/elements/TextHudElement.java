package ml.gorlem.scriptit.elements;

import ml.gorlem.scriptit.screens.Popup;
import ml.gorlem.scriptit.screens.WidgetOptionsPopup;
import ml.gorlem.scriptit.api.util.Color;
import ml.gorlem.scriptit.api.util.geometry.Point;
import net.minecraft.client.MinecraftClient;

public class TextHudElement extends AbstractHudElement {
    public TextHudElement(double xPosition, double yPosition) {
        super(xPosition, yPosition);
    }

    @Override
    void initOptions() {
        super.initOptions();

        setOption(BINDING, "return \"Text Hud Element\"");
    }

    @Override
    Popup getOptionsPopup() {
        return new WidgetOptionsPopup(this);
    }

    @Override
    public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getStringWidth(lastResult) + 2 * DEFAULT_PADDING;
    }

    @Override
    public int getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight + 2 * DEFAULT_PADDING;
    }

    @Override
    public void render(int mouseX, int mouseY, float float1) {
        Point point = getRealPosition();
        double xPosition = point.getX();
        double yPosition = point.getY();

        Color backColor = parseColorFromOption(BACK_COLOR);
        if (backColor != null) {
            fill((int)xPosition, (int)yPosition, (int)xPosition + getWidth(), (int)yPosition + getHeight(), backColor.getValue());
        }

        Color foreColor = parseColorFromOption(FORE_COLOR);
        if (foreColor != null) {
            MinecraftClient.getInstance().textRenderer
                .drawWithShadow(lastResult, (int)xPosition + DEFAULT_PADDING, (int)yPosition + DEFAULT_PADDING, foreColor.getValue());
        }

        super.render(mouseX, mouseY, float1);
    }
}
