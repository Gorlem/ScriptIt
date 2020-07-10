package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.api.scripts.ScriptContainerManager;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerFactory;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.scripts.ScriptContainerImpl;
import com.ddoerr.scriptit.triggers.KeyBindingTrigger;
import com.ddoerr.scriptit.api.triggers.tabs.TriggerTabFactory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptEditorScreen extends AbstractHistoryScreen {
    private ScriptItRegistry registry;
    private ScriptContainerManager scriptContainerManager;
    private TriggerFactory triggerFactory;

    private Map<Identifier, Map<String, String>> triggersData = new HashMap<>();
    private Identifier selectedTrigger;
    private String script;
    private ScriptContainer scriptContainer;

    public ScriptEditorScreen(ScreenHistory history, ScriptItRegistry registry, ScriptContainerManager scriptContainerManager, TriggerFactory triggerFactory) {
        super(history);

        this.registry = registry;
        this.scriptContainerManager = scriptContainerManager;
        this.triggerFactory = triggerFactory;

        setupWidgets();
    }

    public ScriptEditorScreen(ScreenHistory history, ScriptItRegistry registry, ScriptContainerManager scriptContainerManager,TriggerFactory triggerFactory, ScriptContainer scriptContainer) {
        super(history);

        this.registry = registry;
        this.scriptContainerManager = scriptContainerManager;
        this.triggerFactory = triggerFactory;
        this.scriptContainer = scriptContainer;

        script = scriptContainer.getScript().getScriptSource().getContent();

        Trigger trigger = scriptContainer.getTrigger();
        triggersData.put(trigger.getIdentifier(), trigger.getData());
        selectedTrigger = trigger.getIdentifier();

        setupWidgets();
    }

    private void setupWidgets() {
        WInterface mainInterface = getInterface();

        setupTriggerWidget(mainInterface);
        setupScriptWidget(mainInterface);
        setupButtonBar(mainInterface);

        mainInterface.onAlign();
    }

    private void setupTriggerWidget(WInterface mainInterface) {
        WTabHolder tabHolder = mainInterface.createChild(WTabHolder.class, Position.of(20, 20, 0), Size.of(300, 60));

        Registry<TriggerTabFactory> triggerTabs = (Registry<TriggerTabFactory>)registry.get(new Identifier(ScriptItMod.MOD_NAME, "trigger_tabs"));

        if (selectedTrigger == null) {
            selectedTrigger = KeyBindingTrigger.IDENTIFIER;
        }

        List<Identifier> ids = new ArrayList<>(triggerTabs.getIds());
        for (Identifier identifier : ids) {
            TriggerTabFactory triggerTabFactory = triggerTabs.get(identifier);
            WTabHolder.WTab tab = triggerTabFactory.createTriggerTab(tabHolder, triggersData.computeIfAbsent(identifier, key -> new HashMap<>()));

            if (selectedTrigger.equals(identifier)) {
                tab.getToggle().setToggleState(true);
            }

            tab.getToggle().setOnMouseClicked((w, mx, my, mb) -> selectedTrigger = identifier);
        }

        int tabNumber = ids.indexOf(selectedTrigger) + 1;
        tabHolder.selectTab(tabNumber);
    }

    private void setupScriptWidget(WInterface mainInterface) {
        WTextArea scriptContent = mainInterface.createChild(WTextArea.class, Position.of(20, 100, 0))
                .setOnAlign(w -> w.setSize(Size.of(mainInterface).add(-40, -150)));

        if (script != null) {
            scriptContent.setText(script);
        }

        scriptContent.setOnKeyPressed((widget, keyPressed, character, keyModifier) -> script = scriptContent.getText());
        scriptContent.setOnCharTyped((widget, character, keyCode) -> script = scriptContent.getText());
    }

    private void setupButtonBar(WInterface mainInterface) {
        WPanel panel = mainInterface.createChild(WPanel.class)
                .setSize(Size.of(200, 30))
                .setOnAlign(w -> w.setPosition(Position.ofBottomRight(mainInterface).add(-220, -40, 0)));

        int size = (200 - 3 * 5) / 2;

        mainInterface.createChild(WButton.class)
                .setSize(Size.of(size, 20))
                .setOnAlign(w -> w.setPosition(Position.of(panel).add(5, 5, 1)))
                .setLabel(new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "generic.cancel").toString()))
                .setOnMouseClicked((widget, mouseX, mouseY, delta) -> onClose());

        mainInterface.createChild(WButton.class)
                .setSize(Size.of(size, 20))
                .setOnAlign(w -> w.setPosition(Position.of(panel).add(size + 10, 5, 1)))
                .setLabel(new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "generic.save").toString()))
                .setOnMouseClicked((widget, mouseX, mouseY, delta) -> updateScriptContainer());
    }

    private void updateScriptContainer() {
        if (scriptContainer == null) {
            scriptContainer = new ScriptContainerImpl();
            scriptContainerManager.add(scriptContainer);
        }

        Trigger trigger = triggerFactory.createTrigger(selectedTrigger, triggersData.get(selectedTrigger));

        scriptContainer.setTrigger(trigger);

        ScriptBuilder scriptBuilder = new ScriptBuilder()
                .fromString(script)
                .name(trigger.toString());
        scriptContainer.setScript(scriptBuilder);

        scriptContainer.enable();

        ConfigCallback.EVENT.invoker().saveConfig(ScriptEditorScreen.class);

        onClose();
    }
}
