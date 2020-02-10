package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.scripts.Scripts;
import com.ddoerr.scriptit.triggers.Trigger;
import com.ddoerr.scriptit.widgets.PanelWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import spinnery.client.BaseScreen;
import spinnery.widget.*;

public class ScriptsOverviewScreen extends BaseScreen {
    WVerticalList scriptsList;
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
        this.getInterfaceHolder().keyPressed(character, keyCode, keyModifier);
        if (character == 256) {
            onClose();
            return true;
        } else {
            return false;
        }
    }

    private void setupWidgets() {
        WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 0, 0, 0));
        getInterfaceHolder().add(mainInterface);

        mainInterface.add(new WStaticText(
                WPosition.of(WType.FREE, 25, 25, 0),
                mainInterface,
                new LiteralText("HACK")
        ));

        scriptsList = setupList(mainInterface);
        WWidget addScript = setupAddScriptButton(mainInterface);
        WWidget openDesigner = setupOpenDesignerButton(mainInterface);

        mainInterface.add(scriptsList, addScript, openDesigner);
    }

    private WVerticalList setupList(WInterface mainInterface) {
        Scripts scripts = Resolver.getInstance().resolve(Scripts.class);

        WVerticalList scriptsList = new WVerticalList(
                WPosition.of(WType.FREE, 20, 20, 5),
                WSize.of(minecraft.getWindow().getScaledWidth() - 40, minecraft.getWindow().getScaledHeight() - 70),
                mainInterface
        );

        for (ScriptContainer scriptContainer : scripts.getAll()) {
            WWidget row = setupListRow(scriptsList, mainInterface, scriptContainer);
            scriptsList.add(row);
        }

        return scriptsList;
    }

    private WWidget setupListRow(WVerticalList parent, WInterface mainInterface, ScriptContainer scriptContainer) {
        PanelWidget entry = new PanelWidget(
                WPosition.of(WType.ANCHORED, 0, 0, 10, parent),
                WSize.of(minecraft.getWindow().getScaledWidth() - 40, 20),
                mainInterface
        );

        WStaticText description = new WStaticText(
                WPosition.of(WType.ANCHORED, 5, 5, 15, entry),
                mainInterface,
                new LiteralText(scriptContainer.toString())
        );
        entry.add(description);

        WButton editButton = new WButton(
                WPosition.of(WType.ANCHORED, entry.getWidth() - 110, 0, 15, entry),
                WSize.of(45, 20),
                mainInterface
        );
        editButton.setLabel(new LiteralText("Edit"));
        editButton.setOnMouseClicked(() -> {
            history.open(() -> new ScriptEditorScreen(scriptContainer));
            editButton.setOnMouseClicked(null);
        });
        entry.add(editButton);

        WButton removeButton = new WButton(
                WPosition.of(WType.ANCHORED, entry.getWidth() - 55, 0, 15, entry),
                WSize.of(45, 20),
                mainInterface
        );
        removeButton.setLabel(new LiteralText("Remove"));
        removeButton.setOnMouseClicked(() -> {
            Resolver.getInstance().resolve(Scripts.class).remove(scriptContainer);
            Trigger trigger = scriptContainer.getTrigger();
            if (trigger != null) {
                trigger.close();
            }
            parent.remove(entry);
            ConfigCallback.EVENT.invoker().saveConfig(ScriptsOverviewScreen.class);
        });
        entry.add(removeButton);

        return entry;
    }

    private WWidget setupAddScriptButton(WInterface mainInterface) {
        WHorizontalList buttonBar = new WHorizontalList(
                WPosition.of(WType.FREE, minecraft.getWindow().getScaledWidth() - 134, minecraft.getWindow().getScaledHeight() - 40, 5),
                WSize.of(114, 30),
                mainInterface
        );

        WButton addNewScript = new WButton(
                WPosition.of(WType.ANCHORED, 0, 0, 10, buttonBar),
                WSize.of(100, 20),
                mainInterface
        );
        addNewScript.setLabel(new LiteralText("Add new Script"));
        addNewScript.setOnMouseClicked(() -> {
            history.open(ScriptEditorScreen::new);
            addNewScript.setOnMouseClicked(null);
        });

        buttonBar.add(addNewScript);

        return buttonBar;
    }

    private WWidget setupOpenDesignerButton(WInterface mainInterface) {
        WHorizontalList buttonBar = new WHorizontalList(
                WPosition.of(WType.FREE, 20, minecraft.getWindow().getScaledHeight() - 40, 5),
                WSize.of(114, 30),
                mainInterface
        );

        WButton saveButton = new WButton(
                WPosition.of(WType.ANCHORED, 0, 0, 10, buttonBar),
                WSize.of(100, 20),
                mainInterface
        );
        saveButton.setLabel(new LiteralText("Open Designer"));
        saveButton.setOnMouseClicked(() -> {
            history.open(HudElementScreen::new);
            saveButton.setOnMouseClicked(null);
        });

        buttonBar.add(saveButton);

        return buttonBar;
    }

    @Override
    public void render(int mouseX, int mouseY, float tick) {
        super.render(mouseX, mouseY, tick);
    }
}
