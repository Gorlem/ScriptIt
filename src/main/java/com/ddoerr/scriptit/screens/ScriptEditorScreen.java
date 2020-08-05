package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.hud.HudHorizontalAnchor;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.api.scripts.ScriptContainerManager;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerFactory;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.fields.Field;
import com.ddoerr.scriptit.fields.FieldAssembler;
import com.ddoerr.scriptit.screens.widgets.PanelWidget;
import com.ddoerr.scriptit.screens.widgets.ValuesDropdownWidget;
import com.ddoerr.scriptit.scripts.ScriptContainerImpl;
import com.ddoerr.scriptit.extension.triggers.KeyBindingTrigger;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.*;

public class ScriptEditorScreen extends AbstractHistoryScreen {
    private ScriptItRegistry registry;
    private ScriptContainerManager scriptContainerManager;
    private FieldAssembler fieldAssembler;

    private Map<Identifier, Trigger> triggers = new HashMap<>();
    private Identifier selectedTrigger;
    private String script;
    private ScriptContainer scriptContainer;
    private Identifier language;

    public ScriptEditorScreen(ScreenHistory history, ScriptItRegistry registry, ScriptContainerManager scriptContainerManager, FieldAssembler fieldAssembler) {
        super(history);

        this.registry = registry;
        this.scriptContainerManager = scriptContainerManager;
        this.fieldAssembler = fieldAssembler;

        language = registry.languages.getDefaultId();

        setupWidgets();
    }

    public ScriptEditorScreen(ScreenHistory history, ScriptItRegistry registry, ScriptContainerManager scriptContainerManager, FieldAssembler fieldAssembler, ScriptContainer scriptContainer) {
        super(history);

        this.registry = registry;
        this.scriptContainerManager = scriptContainerManager;
        this.scriptContainer = scriptContainer;
        this.fieldAssembler = fieldAssembler;

        script = scriptContainer.getScript().getScriptSource().getContent();

        selectedTrigger = scriptContainer.getTrigger().getIdentifier();
        language = scriptContainer.getScript().getLanguage();

        if (language == null) {
            language = registry.languages.getDefaultId();
        }

        setupWidgets();
    }

    private void setupWidgets() {
        WInterface mainInterface = getInterface();

        setupTriggerWidget(mainInterface);
        setupLanguageSelection(mainInterface);
        setupScriptWidget(mainInterface);
        setupButtonBar(mainInterface);

        mainInterface.onAlign();
    }

    private void setupTriggerWidget(WInterface mainInterface) {
        WTabHolder tabHolder = mainInterface.createChild(WTabHolder.class, Position.of(20, 20, 0), Size.of(300, 120));

        if (selectedTrigger == null) {
            selectedTrigger = KeyBindingTrigger.IDENTIFIER;
        }

        List<Identifier> ids = new ArrayList<>(registry.triggers.getIds());
        for (Identifier identifier : ids) {
            String tabTitle = "scriptit:trigger." + identifier.toString();
            Item item = Registry.ITEM.get(new Identifier(I18n.translate(tabTitle + ".icon")));
            WTabHolder.WTab tab = tabHolder.addTab(item, new TranslatableText(tabTitle));

            Trigger trigger;

            if (scriptContainer != null && scriptContainer.getTrigger().getIdentifier().equals(identifier)) {
                trigger = scriptContainer.getTrigger();
            } else {
                TriggerFactory triggerFactory = registry.triggers.get(identifier);
                trigger = triggerFactory.createTrigger();
            }

            triggers.put(identifier, trigger);

            PanelWidget panelWidget = tab.createChild(PanelWidget.class, Position.of(tabHolder, 10, 30), Size.of(180, 0));

            fieldAssembler.assembleFields(panelWidget, trigger.getFields());

            if (selectedTrigger.equals(identifier)) {
                tab.getToggle().setToggleState(true);
            }

            tab.getToggle().setOnMouseClicked((w, mx, my, mb) -> selectedTrigger = identifier);
        }

        int tabNumber = ids.indexOf(selectedTrigger) + 1;
        tabHolder.selectTab(tabNumber);
    }

    private void setupLanguageSelection(WInterface mainInterface) {
        Set<Identifier> languageIds = registry.languages.getIds();

        ValuesDropdownWidget<Identifier> languages = mainInterface.createChild(ValuesDropdownWidget.class)
                .setTranslationPrefix("language")
                .setSize(Size.of(100, 20))
                .setOnAlign(w -> w.setPosition(Position.ofTopRight(mainInterface).add(-150, 20, 0)));
        languages.addValues(languageIds);
        languages.selectValue(language);
        languages.setOnChange(id -> language = id);
    }

    private void setupScriptWidget(WInterface mainInterface) {
        WTextArea scriptContent = mainInterface.createChild(WTextArea.class, Position.of(20, 160, 0))
                .setOnAlign(w -> w.setSize(Size.of(mainInterface).add(-40, -210)));

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

        Trigger trigger = triggers.get(selectedTrigger);

        for (Map.Entry<String, Field<?>> entry : trigger.getFields().entrySet()) {
            entry.getValue().applyTemporaryValue();
        }

        scriptContainer.setTrigger(trigger);

        ScriptBuilder scriptBuilder = new ScriptBuilder()
                .fromString(script)
                .name(trigger.toString())
                .language(language.toString());
        scriptContainer.setScript(scriptBuilder);

        scriptContainer.enable();
        trigger.start();

        ConfigCallback.EVENT.invoker().saveConfig(ScriptEditorScreen.class);

        onClose();
    }
}
