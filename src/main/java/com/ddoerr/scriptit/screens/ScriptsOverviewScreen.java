package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.scripts.Scripts;
import com.ddoerr.scriptit.triggers.Trigger;
import com.ddoerr.scriptit.widgets.PanelWidget;
import net.minecraft.client.MinecraftClient;
import spinnery.client.BaseScreen;
import spinnery.widget.*;
import spinnery.widget.api.Color;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class ScriptsOverviewScreen extends BaseScreen {
    ScreenHistory history;

    public ScriptsOverviewScreen() {
        super();

        history = Resolver.getInstance().resolve(ScreenHistory.class);
        minecraft = MinecraftClient.getInstance();
        setupWidgets();
    }

    @Override
    public void onClose() {
        history.back();
    }

    @Override
    public boolean keyPressed(int character, int keyCode, int keyModifier) {
        this.getScreenInterface().onKeyPressed(character, keyCode, keyModifier);
        if (character == 256) {
            onClose();
            return true;
        } else {
            return false;
        }
    }

    private void setupWidgets() {
        WInterface mainInterface = getScreenInterface();

        setupList(mainInterface);
        setupAddScriptButton(mainInterface);
        setupOpenDesignerButton(mainInterface);
    }

    private void setupList(WInterface mainInterface) {
        Scripts scripts = Resolver.getInstance().resolve(Scripts.class);

        WPanel panel = mainInterface.createChild(WPanel.class, Position.of(20, 20, 5), Size.of(mainInterface).add(-40, -70));
        WVerticalScrollableContainer list = panel.createChild(WVerticalScrollableContainer.class, panel.getPosition().add(4, 4, 0), panel.getSize().add(-8, -8));

        for (ScriptContainer scriptContainer : scripts.getAll()) {
            setupListRow(list, scriptContainer);
        }
    }

    private void setupListRow(WVerticalScrollableContainer list, ScriptContainer scriptContainer) {
        PanelWidget row = list.createChild(PanelWidget.class, Position.of(list), Size.of(list.getWidth(), 20)).setColor(Color.of("000000"));

        row.createChild(WStaticText.class, Position.of(row, 5, 5))
                .setText(scriptContainer.toString());

        row.createChild(WButton.class, Position.ofTopRight(row).add(-110, 0, 0), Size.of(45, 20))
                .setLabel("Edit")
                .setOnMouseClicked((widget, mouseX, mouseY, delta) -> history.open(() -> new ScriptEditorScreen(scriptContainer)));

        row.createChild(WButton.class, Position.ofTopRight(row).add(- 55, 0, 0), Size.of(45, 20))
                .setLabel("Remove")
                .setOnMouseClicked((widget, mouseX, mouseY, delta) -> {
                    Resolver.getInstance().resolve(Scripts.class).remove(scriptContainer);
                    Trigger trigger = scriptContainer.getTrigger();
                    if (trigger != null) {
                        trigger.close();
                    }
                    list.remove(row);
                    ConfigCallback.EVENT.invoker().saveConfig(ScriptsOverviewScreen.class);
                });
    }

    private void setupAddScriptButton(WInterface mainInterface) {
        WPanel panel = mainInterface.createChild(WPanel.class, Position.ofBottomRight(mainInterface).add(-134, -40, 0), Size.of(114, 30));
        panel.createChild(WButton.class, Position.of(panel, 4, 4), Size.of(100, 20))
                .setLabel("Add new Script")
                .setOnMouseClicked((widget, mouseX, mouseY, delta) -> history.open(ScriptEditorScreen::new));
    }

    private void setupOpenDesignerButton(WInterface mainInterface) {
        WPanel panel = mainInterface.createChild(WPanel.class, Position.ofBottomLeft(mainInterface).add(20, -40, 0), Size.of(114, 30));
        panel.createChild(WButton.class, Position.of(panel, 4, 4), Size.of(100, 20))
                .setLabel("Open Designer")
                .setOnMouseClicked((widget, mouseX, mouseY, delta) -> history.open(HudElementScreen::new));
    }

    @Override
    public void render(int mouseX, int mouseY, float tick) {
        super.render(mouseX, mouseY, tick);
    }
}
