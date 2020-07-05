package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.bus.KeyBindingBusExtension;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.scripts.ScriptContainerManager;
import com.ddoerr.scriptit.api.util.DurationHelper;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.screens.widgets.KeyBindingButtonWidget;
import com.ddoerr.scriptit.screens.widgets.ValuesDropdownWidget;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.scripts.ScriptContainerImpl;
import com.ddoerr.scriptit.triggers.BusTrigger;
import com.ddoerr.scriptit.triggers.ContinuousTrigger;
import com.ddoerr.scriptit.api.triggers.Trigger;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScriptEditorScreen extends AbstractHistoryScreen {
    private InputUtil.KeyCode keyCode;
    private Identifier event;
    private int time;
    private TemporalUnit unit;
    private String script;

    private ScriptContainer scriptContainer;

    private WTabHolder.WTab keyBindingsTab;
    private WTabHolder.WTab eventsTab;
    private WTabHolder.WTab durationTab;

    private ScriptItRegistry registry;
    private ScriptContainerManager scriptContainerManager;

    public ScriptEditorScreen(ScreenHistory history, ScriptItRegistry registry, ScriptContainerManager scriptContainerManager) {
        super(history);

        this.registry = registry;
        this.scriptContainerManager = scriptContainerManager;

        setupWidgets();
    }

    public ScriptEditorScreen(ScreenHistory history, ScriptItRegistry registry, ScriptContainerManager scriptContainerManager, ScriptContainer scriptContainer) {
        super(history);

        this.registry = registry;
        this.scriptContainerManager = scriptContainerManager;
        this.scriptContainer = scriptContainer;

        script = scriptContainer.getScript().getScriptSource().getContent();

        Trigger trigger = scriptContainer.getTrigger();

        if (trigger instanceof BusTrigger) {
            BusTrigger busTrigger = (BusTrigger) trigger;
            String id = busTrigger.getId();

            if (KeyBindingBusExtension.isKeyEvent(id)) {
                keyCode = InputUtil.fromName(id);
            }
            else {
                event = new Identifier(id);
            }
        }

        if (trigger instanceof ContinuousTrigger) {
            ContinuousTrigger continuousTrigger = (ContinuousTrigger) trigger;
            Duration duration = continuousTrigger.getDuration();

            Pair<ChronoUnit, Long> unitAndAmount = DurationHelper.getUnitAndAmount(duration);
            unit = unitAndAmount.getLeft();
            time = unitAndAmount.getRight().intValue();
        }

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

        addKeyTriggerTab(tabHolder);
        addEventTriggerTab(tabHolder);
        addDurationTriggerTab(tabHolder);

        int tabNumber = unit != null ? 3 : event != null ? 2 : 1;
        WTabHolder.WTab tab = tabNumber == 1 ? keyBindingsTab : tabNumber == 2 ? eventsTab : durationTab;

        tabHolder.selectTab(tabNumber);
        tab.getToggle().setToggleState(true);
    }

    private void addKeyTriggerTab(WTabHolder tabHolder) {
        keyBindingsTab = tabHolder.addTab(Items.TRIPWIRE_HOOK, new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.keybinding").toString()));

        KeyBindingButtonWidget keyBindingButtonWidget = keyBindingsTab.createChild(KeyBindingButtonWidget.class, Position.of(tabHolder, 10, 30), Size.of(100, 20));
        keyBindingButtonWidget.setOnChange(keyCode -> this.keyCode = keyCode);

        if (keyCode != null) {
            keyBindingButtonWidget.setKeyCode(keyCode);
        }
    }

    private void addEventTriggerTab(WTabHolder tabHolder) {
        eventsTab = tabHolder.addTab(Items.FIREWORK_ROCKET, new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.event").toString()));

        ValuesDropdownWidget<Identifier> eventDropdown = eventsTab.createChild(ValuesDropdownWidget.class, Position.of(tabHolder, 10, 30), Size.of(100, 20));
        if (event == null) {
            eventDropdown.setLabel(new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.event.select").toString()));
        } else {
            eventDropdown.selectValue(event);
        }
        eventDropdown.addValues(new ArrayList<>(registry.events.getIds()));
        eventDropdown.setOnChange(event -> this.event = event);
    }

    private void addDurationTriggerTab(WTabHolder tabHolder) {
        List<ChronoUnit> units = Arrays.asList(ChronoUnit.MILLIS, ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS);

        durationTab = tabHolder.addTab(Items.CLOCK, new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.duration").toString()));

        WTextField timeText = durationTab.createChild(WTextField.class, Position.of(tabHolder, 10, 30), Size.of(135, 20));

        Runnable parseTime = () -> {
            try {
                time = Integer.parseInt(timeText.getText());
            } catch (NumberFormatException ignored) { }
        };

        timeText.setOnKeyPressed((widget, keyPressed, character, keyModifier) -> parseTime.run());
        timeText.setOnCharTyped((widget, character, keyCode) -> parseTime.run());

        if (unit != null) {
            timeText.setText(Integer.toString(time));
        }

        ValuesDropdownWidget<ChronoUnit> durationDropdown = durationTab.createChild(ValuesDropdownWidget.class, Position.of(tabHolder, 155, 30), Size.of(135, 20))
                .setTranslationPrefix("scripts.triggers.duration.values");

        durationDropdown.addValues(units);

        if (unit == null) {
            durationDropdown.setLabel(new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.duration.select").toString()));
        } else {
            durationDropdown.selectValue((ChronoUnit)unit);
        }

        durationDropdown.setOnChange(unit -> this.unit = unit);

        durationTab.add(durationDropdown, timeText);
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

        scriptContainer.setScript(new ScriptBuilder().fromString(script));

        if (keyBindingsTab.getToggle().getToggleState() && keyCode != null && keyCode != InputUtil.UNKNOWN_KEYCODE) {
            scriptContainer.setTrigger(new BusTrigger(keyCode.getName()));
        } else if (eventsTab.getToggle().getToggleState() && event != null) {
            scriptContainer.setTrigger(new BusTrigger(event.toString()));
        } else if (durationTab.getToggle().getToggleState() && unit != null) {
            scriptContainer.setTrigger(new ContinuousTrigger(Duration.of(time, unit)));
        }

        scriptContainer.enable();

        ConfigCallback.EVENT.invoker().saveConfig(ScriptEditorScreen.class);

        onClose();
    }
}
