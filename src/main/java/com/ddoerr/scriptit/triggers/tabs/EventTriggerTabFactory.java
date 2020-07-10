package com.ddoerr.scriptit.triggers.tabs;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.triggers.tabs.TriggerTabFactory;
import com.ddoerr.scriptit.screens.widgets.ValuesDropdownWidget;
import com.ddoerr.scriptit.triggers.EventTrigger;
import net.minecraft.item.Items;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import spinnery.widget.WTabHolder;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.ArrayList;
import java.util.Map;

public class EventTriggerTabFactory implements TriggerTabFactory {
    private ScriptItRegistry registry;

    public EventTriggerTabFactory(ScriptItRegistry registry) {
        this.registry = registry;
    }

    @Override
    public WTabHolder.WTab createTriggerTab(WTabHolder tabHolder, Map<String, String> triggerData) {
        TranslatableText tabTitle = new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.event").toString());
        WTabHolder.WTab tab = tabHolder.addTab(Items.FIREWORK_ROCKET, tabTitle);

        ValuesDropdownWidget<Identifier> eventDropdown = tab.createChild(ValuesDropdownWidget.class, Position.of(tabHolder, 10, 30), Size.of(100, 20));
        if (triggerData.containsKey(EventTrigger.EVENTNAME)) {
            eventDropdown.selectValue(new Identifier(triggerData.get(EventTrigger.EVENTNAME)));
        } else {
            TranslatableText defaultText = new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.event.select").toString());
            eventDropdown.setLabel(defaultText);
        }
        eventDropdown.addValues(new ArrayList<>(registry.events.getIds()));
        eventDropdown.setOnChange(event -> triggerData.put(EventTrigger.EVENTNAME, event.toString()));

        return tab;
    }
}
