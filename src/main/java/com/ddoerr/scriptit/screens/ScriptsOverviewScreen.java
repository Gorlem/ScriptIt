package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.scripts.Scripts;
import com.ddoerr.scriptit.triggers.Trigger;
import com.ddoerr.scriptit.widgets.PlaneWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.text.LiteralText;
import spinnery.client.BaseScreen;
import spinnery.widget.*;

public class ScriptsOverviewScreen extends BaseScreen {
    WVerticalList scriptsList;

    public ScriptsOverviewScreen() {
        super();

        minecraft = MinecraftClient.getInstance();
        setupWidgets();
    }

    private void setupWidgets() {
        WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 0, 0, 0));
        getInterfaceHolder().add(mainInterface);

        scriptsList = setupList(mainInterface);
        WWidget buttonBar = setupButtonBar(mainInterface);

        mainInterface.add(scriptsList, buttonBar);
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
        PlaneWidget entry = new PlaneWidget(
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
        editButton.setOnMouseClicked(() -> minecraft.openScreen(new ScriptEditorScreen(scriptContainer)));
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

    private WWidget setupButtonBar(WInterface mainInterface) {
        WHorizontalList buttonBar = new WHorizontalList(
                WPosition.of(WType.FREE, minecraft.getWindow().getScaledWidth() - 134, minecraft.getWindow().getScaledHeight() - 40, 5),
                WSize.of(114, 30),
                mainInterface
        );

        WButton saveButton = new WButton(
                WPosition.of(WType.ANCHORED, 0, 0, 10, buttonBar),
                WSize.of(100, 20),
                mainInterface
        );
        saveButton.setLabel(new LiteralText("Add new Script"));
        saveButton.setOnMouseClicked(() -> {
            minecraft.openScreen(new ScriptEditorScreen());
        });

        buttonBar.add(saveButton);

        return buttonBar;
    }
}
