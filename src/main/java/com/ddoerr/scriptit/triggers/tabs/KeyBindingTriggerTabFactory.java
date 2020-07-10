package com.ddoerr.scriptit.triggers.tabs;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.triggers.tabs.TriggerTabFactory;
import com.ddoerr.scriptit.screens.widgets.KeyBindingButtonWidget;
import com.ddoerr.scriptit.triggers.KeyBindingTrigger;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import spinnery.widget.WTabHolder;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Map;

public class KeyBindingTriggerTabFactory implements TriggerTabFactory {
    @Override
    public WTabHolder.WTab createTriggerTab(WTabHolder tabHolder, Map<String, String> triggerData) {
        TranslatableText tabTitle = new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "scripts.triggers.keybinding").toString());
        WTabHolder.WTab tab = tabHolder.addTab(Items.TRIPWIRE_HOOK, tabTitle);

        KeyBindingButtonWidget keyBindingButtonWidget = tab.createChild(KeyBindingButtonWidget.class, Position.of(tabHolder, 10, 30), Size.of(100, 20));
        keyBindingButtonWidget.setOnChange(keyCode -> triggerData.put(KeyBindingTrigger.KEYNAME, keyCode.getName()));

        if (triggerData.containsKey(KeyBindingTrigger.KEYNAME)) {
            keyBindingButtonWidget.setKeyCode(InputUtil.fromName(triggerData.get(KeyBindingTrigger.KEYNAME)));
        }

        return tab;
    }
}
