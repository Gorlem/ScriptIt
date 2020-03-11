package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.bus.KeyBindingBusExtension;
import com.ddoerr.scriptit.api.dependencies.EventLoader;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.scripts.ScriptManager;
import com.ddoerr.scriptit.api.util.DurationHelper;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.screens.widgets.KeyBindingButtonWidget;
import com.ddoerr.scriptit.screens.widgets.ValuesDropdownWidget;
import com.ddoerr.scriptit.api.scripts.LifeCycle;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.triggers.BusTrigger;
import com.ddoerr.scriptit.triggers.ContinuousTrigger;
import com.ddoerr.scriptit.triggers.Trigger;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.apache.commons.lang3.StringUtils;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;

public class ScriptEditorScreen extends AbstractHistoryScreen {
    private LifeCycle lifeCycle = LifeCycle.Instant;
    private InputUtil.KeyCode keyCode;
    private String event;
    private int time;
    private TemporalUnit unit;
    private String script;

    private ScriptContainer scriptContainer;

    private WTabHolder.WTab keyBindingsTab;
    private WTabHolder.WTab eventsTab;
    private WTabHolder.WTab durationTab;

    public ScriptEditorScreen() {
        super();

        setupWidgets();
    }

    public ScriptEditorScreen(ScriptContainer scriptContainer) {
        super();

        this.scriptContainer = scriptContainer;

        lifeCycle = scriptContainer.getLifeCycle();
        script = scriptContainer.getContent();

        Trigger trigger = scriptContainer.getTrigger();

        if (trigger instanceof BusTrigger) {
            BusTrigger busTrigger = (BusTrigger) trigger;
            String id = busTrigger.getId();

            if (KeyBindingBusExtension.isKeyEvent(id)) {
                keyCode = InputUtil.fromName(id);
            }
            else {
                event = id;
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

        setupLifeCycleWidget(mainInterface);
        setupTriggerWidget(mainInterface);
        setupScriptWidget(mainInterface);
        setupButtonBar(mainInterface);

        mainInterface.onAlign();
    }

    private void setupLifeCycleWidget(WInterface mainInterface) {
        ValuesDropdownWidget<LifeCycle> dropdown = mainInterface.createChild(ValuesDropdownWidget.class)
                .setTranslationPrefix("scripts.lifecycle.values")
                .setOnAlign(w -> w.setPosition(Position.ofTopRight(mainInterface).add(-150, 20, 0)))
                .setSize(Size.of(100, 20));
        dropdown.selectValue(lifeCycle);
        dropdown.addValues(LifeCycle.Instant, LifeCycle.Threaded);
        dropdown.setOnChange(lifeCycle -> this.lifeCycle = lifeCycle);
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
        EventLoader eventLoader = Resolver.getInstance().resolve(EventLoader.class);
        List<String> eventsList = eventLoader.getEvents();

        eventsTab = tabHolder.addTab(Items.FIREWORK_ROCKET, new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.event").toString()));

        ValuesDropdownWidget<String> eventDropdown = eventsTab.createChild(ValuesDropdownWidget.class, Position.of(tabHolder, 10, 30), Size.of(100, 20))
                .setTranslationPrefix("scripts.triggers.event.values");
        if (event == null) {
            eventDropdown.setLabel(new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.event.select").toString()));
        } else {
            eventDropdown.selectValue(event);
        }
        eventDropdown.addValues(eventsList);
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
            scriptContainer = new ScriptContainer();
            Resolver.getInstance().resolve(ScriptManager.class).add(scriptContainer);
        }

        scriptContainer.setLifeCycle(lifeCycle);
        scriptContainer.setContent(script);

        if (keyBindingsTab.getToggle().getToggleState() && keyCode != null && keyCode != InputUtil.UNKNOWN_KEYCODE) {
            scriptContainer.setTrigger(new BusTrigger(keyCode.getName()));
        } else if (eventsTab.getToggle().getToggleState() && StringUtils.isNotBlank(event)) {
            scriptContainer.setTrigger(new BusTrigger(event));
        } else if (durationTab.getToggle().getToggleState() && unit != null) {
            scriptContainer.setTrigger(new ContinuousTrigger(Duration.of(time, unit)));
        }

        scriptContainer.enable();

        ConfigCallback.EVENT.invoker().saveConfig(ScriptEditorScreen.class);

        onClose();
    }
}
