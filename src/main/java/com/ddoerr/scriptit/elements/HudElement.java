package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElementProvider;
import com.ddoerr.scriptit.api.hud.HudHorizontalAnchor;
import com.ddoerr.scriptit.api.hud.HudVerticalAnchor;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.triggers.ContinuousTrigger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.util.Tickable;

import java.util.HashMap;
import java.util.Map;

public class HudElement extends DrawableHelper implements Tickable, Element, Drawable {
    public static final int DEFAULT_PADDING = 2;

    public static final String FORE_COLOR = "fore-color";
    public static final String BACK_COLOR = "back-color";

    private Map<String, Object> options = new HashMap<>();
    private HudElementProvider provider;
    private ScriptContainer scriptContainer;

    private double xDifference = 0;
    private double yDifference = 0;

    private int width = 0;
    private int height = 0;

    private HudHorizontalAnchor horizontalAnchor = HudHorizontalAnchor.LEFT;
    private HudVerticalAnchor verticalAnchor = HudVerticalAnchor.TOP;


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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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

    @Override
    public void render(int var1, int var2, float var3) {
        Rectangle rectangle = provider.render(getRealPosition(), this);
        width = rectangle.getWidth();
        height = rectangle.getHeight();
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
            String result = new ScriptBuilder().fromString(value).run();
            return Color.parse(result);
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
