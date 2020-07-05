package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.hud.HudHorizontalAnchor;
import com.ddoerr.scriptit.api.hud.HudVerticalAnchor;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.hud.HudElementContainer;
import com.ddoerr.scriptit.screens.widgets.ValuesDropdownWidget;
import com.google.common.collect.Maps;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Map;

public class HudElementEditorScreen extends AbstractHistoryScreen {
    HudElementContainer hudElement;

    Map<String, Object> options;
    HudHorizontalAnchor horizontalAnchor;
    HudVerticalAnchor verticalAnchor;

    public HudElementEditorScreen(ScreenHistory history, HudElementContainer hudElement) {
        super(history);

        this.hudElement = hudElement;

        options = Maps.newHashMap(hudElement.getOptions());
        horizontalAnchor = hudElement.getHorizontalAnchor();
        verticalAnchor = hudElement.getVerticalAnchor();

        showSetup(hudElement);
    }

    private void showSetup(HudElementContainer hudElement) {
        WInterface mainInterface = getInterface();
        WPanel panel = mainInterface.createChild(WPanel.class, Position.of(0, 0, 0), Size.of(200, 200))
                .setOnAlign(WAbstractWidget::center);

        panel.createChild(WButton.class, Position.of(panel, 10, 10), Size.of(180, 20))
                .setOnMouseClicked((widget, mouseX, mouseY, mouseButton) -> {
                    openScreen(ScriptEditorScreen.class, hudElement.getScriptContainer());
                })
                .setLabel(new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "elements.edit.script").toString()));

        int y = 35;

        for (Map.Entry<String, Object> entry : options.entrySet()) {
            panel.createChild(WStaticText.class, Position.of(panel, 10, y))
                    .setText(entry.getKey());

            panel.createChild(WTextField.class, Position.of(panel, 10, y + 10), Size.of(180, 16))
                    .setText(entry.getValue().toString())
                    .setOnKeyPressed((widget, keyCode, character, keyModifier) -> {
                        options.put(entry.getKey(), ((WTextField)widget).getText());
                    })
                    .setOnCharTyped((widget, character, keyCode) -> {
                        options.put(entry.getKey(), ((WTextField)widget).getText());
                    });
            y += 35;
        }

        ValuesDropdownWidget<HudVerticalAnchor> vertical = panel.createChild(ValuesDropdownWidget.class, Position.of(panel, 10, y), Size.of(88, 20));
        vertical.addValues(HudVerticalAnchor.values());
        vertical.selectValue(verticalAnchor);
        vertical.setOnChange(anchor -> verticalAnchor = anchor);

        ValuesDropdownWidget<HudHorizontalAnchor> horizontal = panel.createChild(ValuesDropdownWidget.class, Position.of(panel, 102, y), Size.of(88, 20));
        horizontal.addValues(HudHorizontalAnchor.values());
        horizontal.selectValue(horizontalAnchor);
        horizontal.setOnChange(anchor -> horizontalAnchor = anchor);

        panel.createChild(WButton.class, Position.ofBottomRight(panel).add(-98, -30, 0), Size.of(88, 20))
                .setOnMouseClicked((widget, mouseX, mouseY, mouseButton) -> updateHudElement())
                .setLabel(new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "generic.save").toString()));

        panel.createChild(WButton.class, Position.ofBottomLeft(panel).add(10, -30, 0), Size.of(88, 20))
                .setOnMouseClicked((widget, mouseX, mouseY, mouseButton) -> onClose())
                .setLabel(new TranslatableText(new Identifier(ScriptItMod.MOD_NAME, "generic.cancel").toString()));

        mainInterface.onAlign();
    }

    private void updateHudElement() {
        Point point = hudElement.getRealPosition();
        hudElement.setAnchor(horizontalAnchor, verticalAnchor);
        hudElement.setRealPosition(point);

        for (Map.Entry<String, Object> entry : options.entrySet()) {
            hudElement.setOption(entry.getKey(), entry.getValue());
        }

        onClose();
    }
}
