package com.ddoerr.scriptit.triggers.tabs;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.triggers.tabs.TriggerTabFactory;
import com.ddoerr.scriptit.screens.widgets.ValuesDropdownWidget;
import com.ddoerr.scriptit.triggers.DurationTrigger;
import net.minecraft.item.Items;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import spinnery.widget.WTabHolder;
import spinnery.widget.WTextField;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DurationTriggerTabFactory implements TriggerTabFactory {
    @Override
    public WTabHolder.WTab createTriggerTab(WTabHolder tabHolder, Map<String, String> triggerData) {
        List<ChronoUnit> units = Arrays.asList(ChronoUnit.MILLIS, ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS);

        WTabHolder.WTab tab = tabHolder.addTab(Items.CLOCK, new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.duration").toString()));

        WTextField timeText = tab.createChild(WTextField.class, Position.of(tabHolder, 10, 30), Size.of(135, 20));

        timeText.setOnKeyPressed((widget, keyPressed, character, keyModifier) -> triggerData.put(DurationTrigger.TIMENAME, timeText.getText()));
        timeText.setOnCharTyped((widget, character, keyCode) ->  triggerData.put(DurationTrigger.TIMENAME, timeText.getText()));

        if (triggerData.containsKey(DurationTrigger.TIMENAME)) {
            timeText.setText(DurationTrigger.TIMENAME);
        }

        ValuesDropdownWidget<ChronoUnit> durationDropdown = tab.createChild(ValuesDropdownWidget.class, Position.of(tabHolder, 155, 30), Size.of(135, 20))
                .setTranslationPrefix("scripts.triggers.duration.values");

        durationDropdown.addValues(units);

        if (triggerData.containsKey(DurationTrigger.UNITNAME)) {
            durationDropdown.selectValue(ChronoUnit.valueOf(triggerData.get(DurationTrigger.UNITNAME)));
        } else {
            TranslatableText defaultText = new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.duration.select").toString());
            durationDropdown.setLabel(defaultText);
        }

        durationDropdown.setOnChange(unit -> triggerData.put(DurationTrigger.UNITNAME, unit.name()));

        tab.add(durationDropdown, timeText);

        return tab;
    }
}
