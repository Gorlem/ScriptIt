package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.elements.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementProvider;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.elements.HudElementManager;
import com.ddoerr.scriptit.loader.HudElementLoader;
import com.ddoerr.scriptit.widgets.DropDownButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Window;
import net.minecraft.text.LiteralText;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WidgetDesignerScreen extends ScreenWithPopup {
    private Rectangle hotbar;
    private DropDownButtonWidget addButton;

    private HudElementProvider hudElementProvider = null;
    private HudElementManager hudElementManager;
    private HudElementLoader hudElementLoader;

    public WidgetDesignerScreen() {
        super(new LiteralText("WidgetDesignerScreen"));

        hudElementManager = Resolver.getInstance().resolve(HudElementManager.class);
        hudElementLoader = Resolver.getInstance().resolve(HudElementLoader.class);
    }

    @Override
    protected void init() {
        super.init();
        minecraft.keyboard.enableRepeatEvents(true);

        children.addAll(hudElementManager.getAll());

        Window window = MinecraftClient.getInstance().window;
        hotbar = Rectangle.centerHorizontal(
                window.getScaledWidth(), window.getScaledHeight() - HudElement.HOTBAR_HEIGHT,
                HudElement.HOTBAR_WIDTH, HudElement.HOTBAR_HEIGHT);

        this.addButton(addButton = new DropDownButtonWidget(
                hotbar.getMinX(), hotbar.getMinY() + 1,
                hotbar.getWidth(), 20,
                I18n.translate("scriptit:hud.add")));

        Map<String, HudElementProvider> providers = hudElementLoader.getProviders();

        List<DropDownButtonWidget.Option> options = providers.entrySet().stream()
                .map(provider -> new DropDownButtonWidget.Option(I18n.translate("scriptit:hud.add." + provider.getKey()), option -> hudElementProvider = provider.getValue()))
                .collect(Collectors.toList());
        addButton.setOptions(options);
    }

    @Override
    public void removed() {
        super.removed();
        minecraft.keyboard.enableRepeatEvents(false);
    }

    @Override
    public void render(int mouseX, int mouseY, float float_1) {
        super.render(mouseX, mouseY, float_1);
    }

    @Override
    public boolean mouseClicked(double x, double y, int int_1) {
        boolean result = super.mouseClicked(x, y, int_1);

        if (result)
            return true;

        if (hudElementProvider == null)
            return false;

        HudElement hudElement = new HudElement(hudElementProvider, x, y);

        hudElementManager.add(hudElement);
        children.add(hudElement);

        if (hudElement instanceof HudElement) {
            ((HudElement) hudElement).openPopup();
        }

        hudElementProvider = null;
        return true;
    }
}
