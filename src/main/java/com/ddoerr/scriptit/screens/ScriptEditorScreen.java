package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.api.util.DurationHelper;
import com.ddoerr.scriptit.bus.KeyBindingBusExtension;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.loader.EventLoader;
import com.ddoerr.scriptit.scripts.LifeCycle;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.scripts.Scripts;
import com.ddoerr.scriptit.triggers.BusTrigger;
import com.ddoerr.scriptit.triggers.ContinuousTrigger;
import com.ddoerr.scriptit.triggers.Trigger;
import com.ddoerr.scriptit.widgets.KeyBindingButtonWidget;
import com.ddoerr.scriptit.widgets.ValuesDropdownWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import spinnery.client.BaseScreen;
import spinnery.widget.*;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ScriptEditorScreen extends BaseScreen {
    private LifeCycle lifeCycle = LifeCycle.Instant;
    private InputUtil.KeyCode keyCode;
    private String event;
    private int time;
    private TemporalUnit unit;
    private String script;

    private ScriptContainer scriptContainer;

    private Window window;

    private WTabHolder.WTab keyBindingsTab;
    private WTabHolder.WTab eventsTab;
    private WTabHolder.WTab durationTab;

    private Supplier<Screen> onCloseSupplier = () -> null;
    private ScreenHistory history;

    public ScriptEditorScreen() {
        super();

        history = Resolver.getInstance().resolve(ScreenHistory.class);

        setupWidgets();
    }

    public ScriptEditorScreen(ScriptContainer scriptContainer) {
        super();

        history = Resolver.getInstance().resolve(ScreenHistory.class);

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
        window = MinecraftClient.getInstance().getWindow();

        WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 0, 0, 0));
        getInterfaceHolder().add(mainInterface);

        WWidget lifeCycleDropdown = setupLifeCycleWidget(mainInterface);
        WWidget triggerTabHolder = setupTriggerWidget(mainInterface);
        WWidget scriptContentWidget = setupScriptWidget(mainInterface);
        WWidget buttonBarWidget = setupButtonBar(mainInterface);

        mainInterface.add(lifeCycleDropdown, triggerTabHolder, scriptContentWidget, buttonBarWidget);
    }

    private WWidget setupLifeCycleWidget(WInterface mainInterface) {
        ValuesDropdownWidget<LifeCycle> dropdown = new ValuesDropdownWidget<>(
                WPosition.of(WType.FREE, window.getScaledWidth() - 150, 20, 0),
                WSize.of(100, 20),
                mainInterface);
        dropdown.selectValue(lifeCycle);
        dropdown.addValues(LifeCycle.Instant, LifeCycle.Threaded);
        dropdown.setOnChange(lifeCycle -> this.lifeCycle = lifeCycle);

        return dropdown;
    }

    private WWidget setupTriggerWidget(WInterface mainInterface) {
        WTabHolder tabHolder = new WTabHolder(WPosition.of(WType.FREE, 20, 20, 10), WSize.of(300, 60), mainInterface);

        addKeyTriggerTab(tabHolder, mainInterface);
        addEventTriggerTab(tabHolder, mainInterface);
        addDurationTriggerTab(tabHolder, mainInterface);

        int tabNumber = unit != null ? 3 : event != null ? 2 : 1;
        WTabHolder.WTab tab = tabNumber == 1 ? keyBindingsTab : tabNumber == 2 ? eventsTab : durationTab;

        tabHolder.selectTab(tabNumber);
        tab.getToggle().setToggleState(true);

        return tabHolder;
    }

    private void addKeyTriggerTab(WTabHolder tabHolder, WInterface mainInterface) {
        keyBindingsTab = tabHolder.addTab(Items.TRIPWIRE_HOOK, new LiteralText("Key Bindings"));

        KeyBindingButtonWidget keyBindingButtonWidget = new KeyBindingButtonWidget(
                WPosition.of(WType.ANCHORED, 10, 30, 0, tabHolder),
                WSize.of(100, 20),
                mainInterface
        );
        keyBindingButtonWidget.setOnChange(keyCode -> this.keyCode = keyCode);

        if (keyCode != null) {
            keyBindingButtonWidget.setKeyCode(keyCode);
        }

        keyBindingsTab.add(keyBindingButtonWidget);
    }

    private void addEventTriggerTab(WTabHolder tabHolder, WInterface mainInterface) {
        EventLoader eventLoader = Resolver.getInstance().resolve(EventLoader.class);
        List<String> eventsList = eventLoader.getEvents();

        eventsTab = tabHolder.addTab(Items.FIREWORK_ROCKET, new LiteralText("Events"));

        ValuesDropdownWidget<String> eventDropdown = new ValuesDropdownWidget<>(
                WPosition.of(WType.ANCHORED, 10, 30, 10, tabHolder),
                WSize.of(100, 20),
                mainInterface);
        if (event == null) {
            eventDropdown.setLabel(new LiteralText("Select an event:"));
        } else {
            eventDropdown.selectValue(event);
        }
        eventDropdown.addValues(eventsList);
        eventDropdown.setOnChange(event -> this.event = event);

        eventsTab.add(eventDropdown);
    }

    private void addDurationTriggerTab(WTabHolder tabHolder, WInterface mainInterface) {
        List<ChronoUnit> units = Arrays.asList(ChronoUnit.MILLIS, ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS);

        durationTab = tabHolder.addTab(Items.CLOCK, new LiteralText("Duration"));

        WDynamicText timeText = new WDynamicText(
                WPosition.of(WType.ANCHORED, 10, 30, 0, tabHolder),
                WSize.of(135, 20),
                mainInterface
        );

        Runnable parseTime = () -> {
            try {
                time = Integer.parseInt(timeText.getText());
            } catch (NumberFormatException ignored) { }
        };

        timeText.setOnKeyPressed(parseTime);
        timeText.setOnCharTyped(parseTime);

        if (unit != null) {
            timeText.setText(Integer.toString(time));
        }

        ValuesDropdownWidget<ChronoUnit> durationDropdown = new ValuesDropdownWidget<>(
                WPosition.of(WType.ANCHORED, 155, 30, 10, tabHolder),
                WSize.of(135, 20),
                mainInterface);

        durationDropdown.addValues(units);

        if (unit == null) {
            durationDropdown.setLabel(new LiteralText("Select a time unit:"));
        } else {
            durationDropdown.selectValue((ChronoUnit)unit);
        }

        durationDropdown.setOnChange(unit -> this.unit = unit);

        durationTab.add(durationDropdown, timeText);
    }

    private WWidget setupScriptWidget(WInterface mainInterface) {
        WDynamicText scriptContent = new WDynamicText(
                WPosition.of(WType.FREE, 20, 100, 0),
                WSize.of(window.getScaledWidth() - 40, window.getScaledHeight() - 150),
                mainInterface
        );

        if (script != null) {
            scriptContent.setText(script);
        }

        scriptContent.setOnKeyPressed(() -> script = scriptContent.getText());
        scriptContent.setOnCharTyped(() -> script = scriptContent.getText());

        return scriptContent;
    }

    private WWidget setupButtonBar(WInterface mainInterface) {
        WHorizontalList buttonBar = new WHorizontalList(
                WPosition.of(WType.FREE, window.getScaledWidth() - 220, window.getScaledHeight() - 40, 5),
                WSize.of(200, 30),
                mainInterface
        );

        WButton cancelButton = new WButton(
                WPosition.of(WType.ANCHORED, 0, 0, 10, buttonBar),
                WSize.of(100, 20),
                mainInterface
        );
        cancelButton.setLabel(new LiteralText("Cancel"));
        cancelButton.setOnMouseClicked(() -> {
            onClose();
            cancelButton.setOnMouseClicked(null);
        });

        WButton saveButton = new WButton(
                WPosition.of(WType.ANCHORED, 0, 0, 10, buttonBar),
                WSize.of(100, 20),
                mainInterface
        );
        saveButton.setLabel(new LiteralText("Save"));
        saveButton.setOnMouseClicked(() -> {
            updateScriptContainer();
            saveButton.setOnMouseClicked(null);
        });

        buttonBar.add(cancelButton);
        buttonBar.add(saveButton);

        return buttonBar;
    }

    private void updateScriptContainer() {
        if (scriptContainer == null) {
            scriptContainer = new ScriptContainer();
            Resolver.getInstance().resolve(Scripts.class).add(scriptContainer);
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

        ConfigCallback.EVENT.invoker().saveConfig(ScriptEditorScreen.class);

        onClose();
    }

    @Override
    public void onClose() {
        history.back();
    }

    public void setOnCloseScreen(Supplier<Screen> screenSupplier) {
        onCloseSupplier = screenSupplier;
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
}
