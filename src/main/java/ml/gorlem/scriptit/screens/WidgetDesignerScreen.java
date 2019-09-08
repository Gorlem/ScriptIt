package ml.gorlem.scriptit.screens;

import ml.gorlem.scriptit.ScriptItMod;
import ml.gorlem.scriptit.api.hud.HudElement;
import ml.gorlem.scriptit.api.hud.HudElementFactory;
import ml.gorlem.scriptit.api.util.geometry.Rectangle;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.elements.AbstractHudElement;
import ml.gorlem.scriptit.elements.HudElementManager;
import ml.gorlem.scriptit.loader.HudElementLoader;
import ml.gorlem.scriptit.widgets.DropDownButtonWidget;
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

    private HudElementFactory hudElementFactory = null;
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
        children.addAll(hudElementManager.getAll());

        Window window = MinecraftClient.getInstance().window;
        hotbar = Rectangle.centerHorizontal(
                window.getScaledWidth(), window.getScaledHeight() - AbstractHudElement.HOTBAR_HEIGHT,
                AbstractHudElement.HOTBAR_WIDTH, AbstractHudElement.HOTBAR_HEIGHT);

        this.addButton(addButton = new DropDownButtonWidget(
                hotbar.getMinX(), hotbar.getMinY() + 1,
                hotbar.getWidth(), 20,
                I18n.translate("scriptit:hud.add")));

        Map<String, HudElementFactory> factories = hudElementLoader.getFactories();

        List<DropDownButtonWidget.Option> options = factories.entrySet().stream()
                .map(factory -> new DropDownButtonWidget.Option(I18n.translate("scriptit:hud.add." + factory.getKey()), option -> hudElementFactory = factory.getValue()))
                .collect(Collectors.toList());
        addButton.setOptions(options);
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

        if (hudElementFactory == null)
            return false;

        HudElement hudElement = hudElementFactory.create(x, y);

        hudElementManager.add(hudElement);
        children.add(hudElement);

        if (hudElement instanceof AbstractHudElement) {
            ((AbstractHudElement) hudElement).openPopup();
        }

        hudElementFactory = null;
        return true;
    }
}
