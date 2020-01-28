package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.*;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.screens.Popup;
import com.ddoerr.scriptit.screens.ScreenWithPopup;
import com.ddoerr.scriptit.screens.WidgetDesignerScreen;
import com.ddoerr.scriptit.screens.WidgetOptionsPopup;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.triggers.ContinuousTrigger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Tickable;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HudElement extends DrawableHelper implements Tickable, Element, Drawable {

    public static final int DEFAULT_PADDING = 2;
    public static final int HOTBAR_WIDTH = 182;
    public static final int HOTBAR_HEIGHT = 24;

    public static final String BINDING = "binding";
    public static final String FORE_COLOR = "fore-color";
    public static final String BACK_COLOR = "back-color";
    public static final String HORIZONTAL_ANCHOR = "horizontal-anchor";
    public static final String VERTICAL_ANCHOR = "vertical-anchor";

    static Map<InputUtil.KeyCode, Point> movementKeys = new HashMap<>();
    static List<InputUtil.KeyCode> removalKeys = new ArrayList<>();

    static {
        movementKeys.put(InputUtil.fromName("key.keyboard.up"), new Point(0, -1));
        movementKeys.put(InputUtil.fromName("key.keyboard.right"), new Point(1, 0));
        movementKeys.put(InputUtil.fromName("key.keyboard.down"), new Point(0, 1));
        movementKeys.put(InputUtil.fromName("key.keyboard.left"), new Point(-1, 0));

        removalKeys.add(InputUtil.fromName("key.keyboard.backspace"));
        removalKeys.add(InputUtil.fromName("key.keyboard.delete"));
    }

    double xDifference = 0;
    double yDifference = 0;

    Instant lastTimeClicked = Instant.now();
    Duration durationBetweenClicks = Duration.ofMillis(200);

    Map<String, Object> options = new HashMap<>();
    HudElementProvider provider;
    ScriptContainer scriptContainer;

    int width = 0;
    int height = 0;

    HudHorizontalAnchor horizontalAnchor = HudHorizontalAnchor.LEFT;
    HudVerticalAnchor verticalAnchor = HudVerticalAnchor.TOP;


    public HudElement(HudElementProvider provider, double xPosition, double yPosition) {
        scriptContainer = new ScriptContainer(new ContinuousTrigger());

        this.provider = provider;

        provider.setDefaults(this);
        setRealPosition(new Point(xPosition, yPosition));
    }

    public void setAnchor(HudHorizontalAnchor horizontalAnchor, HudVerticalAnchor verticalAnchor) {
        this.horizontalAnchor = horizontalAnchor;
        this.verticalAnchor = verticalAnchor;
    }

    public HudVerticalAnchor getVerticalAnchor() {
        return verticalAnchor;
    }

    public HudHorizontalAnchor getHorizontalAnchor() {
        return horizontalAnchor;
    }

    public void setRealPosition(Point position) {
        xDifference = position.getX() - horizontalAnchor.getBaseValue();
        yDifference = position.getY() - verticalAnchor.getBaseValue();

        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    public Point getRealPosition() {
        return new Point(
                horizontalAnchor.getBaseValue() + xDifference,
                verticalAnchor.getBaseValue() + yDifference
        );
    }

    public void setRelativePosition(Point position) {
        xDifference = position.getX();
        yDifference = position.getY();

        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    public Point getRelativePosition() {
        return new Point(xDifference, yDifference);
    }

    public void setOption(String key, Object value) {
        options.put(key, value);
        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    public Object getOption(String key) {
        return options.getOrDefault(key, null);
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public HudElementProvider getProvider() {
        return provider;
    }

    private void move(double xDelta, double yDelta) {
        Point point = getRealPosition();
        setRealPosition(new Point(point.getX() + xDelta, point.getY() + yDelta));
    }

    @Override
    public boolean isMouseOver(double x, double y) {
        Point point = getRealPosition();
        return x >= point.getX() && x <= point.getX() + width && y >= point.getY() && y <= point.getY() + height;
    }

    @Override
    public boolean mouseDragged(double x, double y, int int_1, double xDelta, double yDelta) {
        move(xDelta, yDelta);
        return true;
    }

    @Override
    public boolean mouseClicked(double x, double y, int int_1) {
        boolean over = isMouseOver(x, y);
        if (!over)
            return false;

        Instant timeClicked = Instant.now();
        Duration duration = Duration.between(lastTimeClicked, timeClicked);

        if (duration.compareTo(durationBetweenClicks) < 0) {
            openPopup();
        }

        lastTimeClicked = timeClicked;

        return true;
    }

    public void openPopup() {
        Popup popup = new WidgetOptionsPopup(this);
        Screen screen = MinecraftClient.getInstance().currentScreen;

        if (screen instanceof ScreenWithPopup) {
            ((ScreenWithPopup) screen).openPopup(popup);
        }
    }

    @Override
    public boolean keyPressed(int int_1, int int_2, int int_3) {
        InputUtil.KeyCode keyCode = InputUtil.getKeyCode(int_1, int_2);

        for (Map.Entry<InputUtil.KeyCode, Point> entry : movementKeys.entrySet()) {
            if (keyCode == entry.getKey()) {
                Point point = entry.getValue();
                move(point.getX(), point.getY());
                return true;
            }
        }

        for (InputUtil.KeyCode removalKey : removalKeys) {
            if (keyCode == removalKey) {
                Resolver.getInstance().resolve(HudElementManager.class).remove(this);
                MinecraftClient.getInstance().currentScreen.children().remove(this);

                ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean changeFocus(boolean boolean_1) {
        return true;
    }

    @Override
    public void render(int var1, int var2, float var3) {
        Rectangle rectangle = provider.render(getRealPosition(), this);
        width = rectangle.getWidth();
        height = rectangle.getHeight();

        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (!(screen instanceof WidgetDesignerScreen)) {
            return;
        }

        Point point = getRealPosition();
        int x = (int) point.getX();
        int y = (int) point.getY();

        switch (verticalAnchor) {
            case MIDDLE:
                y += height / 2;
                break;
            case BOTTOM:
                y += height - 1;
                break;
        }

        switch (horizontalAnchor) {
            case CENTER:
                x += width / 2;
                break;
            case RIGHT:
                x += width - 1;
                break;
        }

        fill(x - 1, y - 1, x + 2, y + 2, Color.RED.getValue());
    }

    @Override
    public void tick() {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        if (minecraft.player == null || minecraft.world == null)
            return;

        scriptContainer.runIfPossible();
    }

    public static Color parseAndRun(String value) {
        Color color = Color.parse(value);

        if (color != null)
            return color;

        try {
            Script script = new ScriptBuilder().fromString(value).build();
            Object result = script.runInstantly();
            return Color.parse(result.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ScriptContainer getScriptContainer() {
        return scriptContainer;
    }
}