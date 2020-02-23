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

public class ScriptsOverviewScreen extends AbstractHistoryScreen {
    ScreenHistory history;

    public ScriptsOverviewScreen() {
        super();

        history = Resolver.getInstance().resolve(ScreenHistory.class);
        minecraft = MinecraftClient.getInstance();
        setupWidgets();
    }

    private void setupWidgets() {
        WInterface mainInterface = getInterface();

        setupList(mainInterface);
        setupAddScriptButton(mainInterface);
        setupOpenDesignerButton(mainInterface);

        mainInterface.onAlign();
    }

    private void setupList(WInterface mainInterface) {
        Scripts scripts = Resolver.getInstance().resolve(Scripts.class);

        WPanel panel = mainInterface.createChild(WPanel.class, Position.of(20, 20, 0));
        panel.setOnAlign(w -> w.setSize(Size.of(mainInterface).add(-40, -70)));

        WVerticalScrollableContainer list = panel.createChild(WVerticalScrollableContainer.class, panel.getPosition().add(4, 4, 0));
        list.setOnAlign(w -> {
            w.setSize(Size.of(panel).add(-8, -8));
        });

        WAbstractWidget lastRow = null;

        for (ScriptContainer scriptContainer : scripts.getAll()) {
            lastRow = setupListRow(list, scriptContainer, lastRow);
        }
    }

    private WAbstractWidget setupListRow(WVerticalScrollableContainer list, ScriptContainer scriptContainer, WAbstractWidget lastRow) {
        PanelWidget row = list.createChild(PanelWidget.class);
        row.setOnAlign(w -> {
            w.setSize(Size.of(list.getWidth(), 20));
            w.setPosition(lastRow == null ? Position.of(list) : Position.ofBottomLeft(lastRow));
        });

        row.createChild(WStaticText.class)
                .setText(scriptContainer.toString())
                .setOnAlign(w -> w.setPosition(Position.of(row, 5, 5)));

        row.createChild(WButton.class)
                .setSize(Size.of(45, 20))
                .setLabel("Edit")
                .setOnMouseClicked((widget, mouseX, mouseY, delta) -> history.open(() -> new ScriptEditorScreen(scriptContainer)))
                .setOnAlign(w -> w.setPosition(Position.ofTopRight(row).add(-110, 0, 0)));

        row.createChild(WButton.class)
                .setSize(Size.of(45, 20))
                .setLabel("Remove")
                .setOnMouseClicked((widget, mouseX, mouseY, delta) -> {
                    Resolver.getInstance().resolve(Scripts.class).remove(scriptContainer);
                    Trigger trigger = scriptContainer.getTrigger();
                    if (trigger != null) {
                        trigger.close();
                    }
                    list.remove(row);
                    ConfigCallback.EVENT.invoker().saveConfig(ScriptsOverviewScreen.class);
                })
                .setOnAlign(w -> w.setPosition(Position.ofTopRight(row).add(- 55, 0, 0)));
        return row;
    }

    private void setupAddScriptButton(WInterface mainInterface) {
        WPanel panel = mainInterface.createChild(WPanel.class)
                .setOnAlign(w -> w.setPosition(Position.ofBottomRight(mainInterface).add(-134, -40, 0)))
                .setSize(Size.of(114, 30));

        panel.createChild(WButton.class)
                .setSize(Size.of(100, 20))
                .setOnAlign(w -> w.setPosition(Position.of(panel, 4, 4)))
                .setLabel("Add new Script")
                .setOnMouseClicked((widget, mouseX, mouseY, delta) -> history.open(ScriptEditorScreen::new));
    }

    private void setupOpenDesignerButton(WInterface mainInterface) {
        WPanel panel = mainInterface.createChild(WPanel.class)
                .setOnAlign(w -> w.setPosition(Position.ofBottomLeft(mainInterface).add(20, -40, 0)))
                .setSize(Size.of(114, 30));

        panel.createChild(WButton.class)
                .setSize(Size.of(100, 20))
                .setOnAlign(w -> w.setPosition(Position.of(panel, 4, 4)))
                .setLabel("Open Designer")
                .setOnMouseClicked((widget, mouseX, mouseY, delta) -> history.open(HudElementScreen::new));
    }
}
