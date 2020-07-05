package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementContainer;
import com.ddoerr.scriptit.api.hud.HudHorizontalAnchor;
import com.ddoerr.scriptit.api.hud.HudVerticalAnchor;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.scripts.ScriptContainerImpl;
import com.ddoerr.scriptit.triggers.ContinuousTrigger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;

import java.util.HashMap;
import java.util.Map;

public class HudElementContainerImpl extends DrawableHelper implements Element, HudElementContainer {

    private Map<String, Object> options = new HashMap<>();
    private HudElement hudElement;
    private ScriptContainer scriptContainer;

    private double xDifference = 0;
    private double yDifference = 0;

    private int width = 0;
    private int height = 0;

    private HudHorizontalAnchor horizontalAnchor = HudHorizontalAnchor.LEFT;
    private HudVerticalAnchor verticalAnchor = HudVerticalAnchor.TOP;


    public HudElementContainerImpl(HudElement hudElement, double xPosition, double yPosition) {
        scriptContainer = new ScriptContainerImpl(new ContinuousTrigger(), new ScriptBuilder());

        this.hudElement = hudElement;

        hudElement.setDefaults(this);
        setRealPosition(new Point(xPosition, yPosition));
    }

    @Override
    public void setAnchor(HudHorizontalAnchor horizontalAnchor, HudVerticalAnchor verticalAnchor) {
        this.horizontalAnchor = horizontalAnchor;
        this.verticalAnchor = verticalAnchor;
    }

    @Override
    public HudVerticalAnchor getVerticalAnchor() {
        return verticalAnchor;
    }

    @Override
    public HudHorizontalAnchor getHorizontalAnchor() {
        return horizontalAnchor;
    }

    @Override
    public void setRealPosition(Point position) {
        xDifference = position.getX() - horizontalAnchor.getBaseValue();
        yDifference = position.getY() - verticalAnchor.getBaseValue();

        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    @Override
    public Point getRealPosition() {
        return new Point(
                horizontalAnchor.getBaseValue() + xDifference,
                verticalAnchor.getBaseValue() + yDifference
        );
    }

    @Override
    public void setRelativePosition(Point position) {
        xDifference = position.getX();
        yDifference = position.getY();

        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    @Override
    public Point getRelativePosition() {
        return new Point(xDifference, yDifference);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setOption(String key, Object value) {
        options.put(key, value);
        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    @Override
    public Object getOption(String key) {
        return options.getOrDefault(key, null);
    }

    @Override
    public Map<String, Object> getOptions() {
        return options;
    }

    @Override
    public HudElement getHudElement() {
        return hudElement;
    }

    @Override
    public void render(int var1, int var2, float var3) {
        Rectangle rectangle = hudElement.render(getRealPosition(), this);
        width = rectangle.getWidth();
        height = rectangle.getHeight();
    }

    @Override
    public void tick() {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        if (minecraft.player == null || minecraft.world == null)
            return;

        scriptContainer.getTrigger().check();
    }

    @Override
    public ScriptContainer getScriptContainer() {
        return scriptContainer;
    }
}
