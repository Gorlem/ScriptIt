package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.loader.EventLoader;
import com.ddoerr.scriptit.scripts.LifeCycle;
import com.ddoerr.scriptit.widgets.KeyBindingButtonWidget;
import javafx.scene.input.KeyCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import org.apache.commons.lang3.StringUtils;
import spinnery.client.BaseScreen;
import spinnery.widget.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptEditorScreen extends BaseScreen {
    private LifeCycle lifeCycle = LifeCycle.Instant;
    private String event;
    private ChronoUnit unit;

    private EventLoader eventLoader;

    public ScriptEditorScreen() {
        super();
        eventLoader = Resolver.getInstance().resolve(EventLoader.class);

        WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 0, 0, 0));
        getInterfaceHolder().add(mainInterface);

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

        WTabHolder tabHolder = new WTabHolder(WPosition.of(WType.FREE, 50, 100, 0), WSize.of(300, 60), mainInterface);

        WTabHolder.WTab keyBindings = tabHolder.addTab(Items.TRIPWIRE_HOOK, new LiteralText("Key Bindings"));

        KeyBindingButtonWidget keyBindingButtonWidget = new KeyBindingButtonWidget(
            WPosition.of(WType.ANCHORED, 10, 30, 0, tabHolder),
            WSize.of(100, 20),
            mainInterface
        );

        keyBindings.add(keyBindingButtonWidget);

        List<String> eventsList = eventLoader.getEvents();

        WTabHolder.WTab events = tabHolder.addTab(Items.FIREWORK_ROCKET, new LiteralText("Events"));
        WDropdown eventDropdown = new WDropdown(
                WPosition.of(WType.ANCHORED, 10, 30, 0, tabHolder),
                WSize.of(100, 20, 100, 20 + eventsList.size() * 11),
                mainInterface);
        eventDropdown.setLabel(new LiteralText("Select an event:"));

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

        List<ChronoUnit> units = Arrays.stream(ChronoUnit.values()).filter(ChronoUnit::isTimeBased).collect(Collectors.toList());

        WTabHolder.WTab duration = tabHolder.addTab(Items.CLOCK, new LiteralText("Duration"));

        WDynamicText timeText = new WDynamicText(
                WPosition.of(WType.ANCHORED, 10, 30, 0, tabHolder),
                WSize.of(100, 20),
                mainInterface
        );

        WDropdown durationDropdown = new WDropdown(
                WPosition.of(WType.ANCHORED, 140, 30, 0, tabHolder),
                WSize.of(100, 20, 100, 20 + units.size() * 11),
                mainInterface);
        durationDropdown.setLabel(new LiteralText("Select a time unit:"));

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

        tabHolder.selectTab(1);

        WDynamicText scriptContent = new WDynamicText(
                WPosition.of(WType.FREE, 50, 160, 0),
                WSize.of(300, 200),
                mainInterface
        );


        mainInterface.add(dropdown, tabHolder, scriptContent);
    }
}
