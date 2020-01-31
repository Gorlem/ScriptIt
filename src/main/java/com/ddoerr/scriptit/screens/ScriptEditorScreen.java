package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.bus.KeyBindingBusExtension;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.loader.EventLoader;
import com.ddoerr.scriptit.scripts.LifeCycle;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.triggers.BusTrigger;
import com.ddoerr.scriptit.triggers.ContinuousTrigger;
import com.ddoerr.scriptit.triggers.Trigger;
import com.ddoerr.scriptit.widgets.KeyBindingButtonWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import spinnery.client.BaseScreen;
import spinnery.widget.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;

public class ScriptEditorScreen extends BaseScreen {
    private LifeCycle lifeCycle = LifeCycle.Instant;
    private InputUtil.KeyCode keyCode;
    private String event;
    private int time;
    private TemporalUnit unit;
    private String script;

    public ScriptEditorScreen(ScriptContainer scriptContainer) {
        super();

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

            List<TemporalUnit> units = duration.getUnits();
            unit = units.get(0);
            time = (int)duration.get(unit);
        }

        setupWidgets();
    }

    public ScriptEditorScreen() {
        super();
        setupWidgets();
    }

    private void setupWidgets() {
        WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 0, 0, 0));
        getInterfaceHolder().add(mainInterface);

        WWidget lifeCycleDropdown = setupLifeCycleWidget(mainInterface);
        WWidget triggerTabHolder = setupTriggerWidget(mainInterface);
        WWidget scriptContentWidget = setupScriptWidget(mainInterface);

        mainInterface.add(lifeCycleDropdown, triggerTabHolder, scriptContentWidget);
    }

    private WWidget setupLifeCycleWidget(WInterface mainInterface) {
        WDropdown dropdown = new WDropdown(WPosition.of(WType.FREE, 200, 50, 0), WSize.of(100, 20, 100, 43), mainInterface);
        dropdown.setLabel(new LiteralText( lifeCycle.toString()));

        WStaticText instantText = new WStaticText(
                WPosition.of(WType.ANCHORED, 0, 0, 1, dropdown),
                mainInterface,
                new LiteralText(LifeCycle.Instant.toString()));
        instantText.setOnMouseClicked(() -> {
            lifeCycle = LifeCycle.Instant;

            dropdown.setLabel(new LiteralText(lifeCycle.toString()));
            dropdown.setState(false);
        });

        WStaticText threadedText = new WStaticText(
                WPosition.of(WType.ANCHORED, 0, 0, 1, dropdown),
                mainInterface,
                new LiteralText(LifeCycle.Threaded.toString()));
        threadedText.setOnMouseClicked(() -> {
            lifeCycle = LifeCycle.Threaded;

            dropdown.setLabel(new LiteralText(lifeCycle.toString()));
            dropdown.setState(false);
        });

        dropdown.add(instantText);
        dropdown.add(threadedText);

        return dropdown;
    }

    private WWidget setupTriggerWidget(WInterface mainInterface) {
        WTabHolder tabHolder = new WTabHolder(WPosition.of(WType.FREE, 50, 100, 10), WSize.of(300, 60), mainInterface);

        addKeyTriggerTab(tabHolder, mainInterface);
        addEventTriggerTab(tabHolder, mainInterface);
        addDurationTriggerTab(tabHolder, mainInterface);

        int tabNumber = unit != null ? 3 : event != null ? 2 : 1;
        tabHolder.selectTab(tabNumber);

        return tabHolder;
    }

    private void addKeyTriggerTab(WTabHolder tabHolder, WInterface mainInterface) {
        WTabHolder.WTab keyBindings = tabHolder.addTab(Items.TRIPWIRE_HOOK, new LiteralText("Key Bindings"));

        KeyBindingButtonWidget keyBindingButtonWidget = new KeyBindingButtonWidget(
                WPosition.of(WType.ANCHORED, 10, 30, 0, tabHolder),
                WSize.of(100, 20),
                mainInterface
        );

        if (keyCode != null) {
            keyBindingButtonWidget.setKeyCode(keyCode);
        }

        keyBindings.add(keyBindingButtonWidget);
    }

    private void addEventTriggerTab(WTabHolder tabHolder, WInterface mainInterface) {
        EventLoader eventLoader = Resolver.getInstance().resolve(EventLoader.class);
        List<String> eventsList = eventLoader.getEvents();

        WTabHolder.WTab events = tabHolder.addTab(Items.FIREWORK_ROCKET, new LiteralText("Events"));
        WDropdown eventDropdown = new WDropdown(
                WPosition.of(WType.ANCHORED, 10, 30, 10, tabHolder),
                WSize.of(100, 20, 100, 20 + eventsList.size() * 11),
                mainInterface);
        if (event == null) {
            eventDropdown.setLabel(new LiteralText("Select an event:"));
        } else {
            eventDropdown.setLabel(new LiteralText(event));
        }

        for (String name : eventsList) {
            WStaticText eventText = new WStaticText(
                    WPosition.of(WType.ANCHORED, 0, 0, 1, eventDropdown),
                    mainInterface,
                    new LiteralText(name));
            eventText.setOnMouseClicked(() -> {
                event = name;

                eventDropdown.setLabel(new LiteralText(name));
                eventDropdown.setState(false);
            });

            eventDropdown.add(eventText);
        }

        events.add(eventDropdown);
    }

    private void addDurationTriggerTab(WTabHolder tabHolder, WInterface mainInterface) {
        List<ChronoUnit> units = Arrays.asList(ChronoUnit.MILLIS, ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS);

        WTabHolder.WTab duration = tabHolder.addTab(Items.CLOCK, new LiteralText("Duration"));

        WDynamicText timeText = new WDynamicText(
                WPosition.of(WType.ANCHORED, 10, 30, 0, tabHolder),
                WSize.of(100, 20),
                mainInterface
        );

        if (unit != null) {
            timeText.setText(Integer.toString(time));
        }

        WDropdown durationDropdown = new WDropdown(
                WPosition.of(WType.ANCHORED, 140, 30, 10, tabHolder),
                WSize.of(100, 20, 100, 20 + units.size() * 11),
                mainInterface);

        if (unit == null) {
            durationDropdown.setLabel(new LiteralText("Select a time unit:"));
        } else {
            durationDropdown.setLabel(new LiteralText(unit.toString()));
        }

        for (ChronoUnit unit : units) {
            WStaticText unitText = new WStaticText(
                    WPosition.of(WType.ANCHORED, 0, 0, 1, durationDropdown),
                    mainInterface,
                    new LiteralText(unit.toString()));
            unitText.setOnMouseClicked(() -> {
                this.unit = unit;

                durationDropdown.setLabel(new LiteralText(unit.toString()));
                durationDropdown.setState(false);
            });

            durationDropdown.add(unitText);
        }
        duration.add(durationDropdown, timeText);
    }

    private WWidget setupScriptWidget(WInterface mainInterface) {
        WDynamicText scriptContent = new WDynamicText(
                WPosition.of(WType.FREE, 50, 160, 0),
                WSize.of(300, 200),
                mainInterface
        );

        if (script != null) {
            scriptContent.setText(script);
        }

        return scriptContent;
    }
}
