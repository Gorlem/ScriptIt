package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.elements.HudElement;
import spinnery.client.BaseScreen;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Map;

public class HudElementEditorScreen extends AbstractHistoryScreen {
    public HudElementEditorScreen(HudElement hudElement) {
        super();

        showSetup(hudElement);
    }

    private void showSetup(HudElement hudElement) {
        WInterface mainInterface = getInterface();
        WPanel panel = mainInterface.createChild(WPanel.class, Position.of(0, 0, 0), Size.of(200, 200));
        panel.center();

        panel.createChild(WButton.class, Position.of(panel, 10, 10), Size.of(180, 20))
                .setOnMouseClicked((widget, mouseX, mouseY, mouseButton) -> {
                    openScreen(() -> new ScriptEditorScreen(hudElement.getScriptContainer()));
                })
                .setLabel("Edit Script");


        int y = 35;

        for (Map.Entry<String, Object> entry : hudElement.getOptions().entrySet()) {
            panel.createChild(WStaticText.class, Position.of(panel, 10, y))
                    .setText(entry.getKey());

            panel.createChild(WTextField.class, Position.of(panel, 10, y + 10), Size.of(180, 16))
                    .setText(entry.getValue().toString());
            y += 35;
        }

//        ValuesDropdownWidget<HudVerticalAnchor> vertical = new ValuesDropdownWidget<>(
//                WPosition.of(WType.ANCHORED, 5, y, 15, plane),
//                WSize.of(88, 20),
//                mainInterface
//        );
//        plane.add(vertical);
//        vertical.addValues(HudVerticalAnchor.values());
//        vertical.selectValue(HudVerticalAnchor.TOP);
//
//        ValuesDropdownWidget<HudHorizontalAnchor> horizontal = new ValuesDropdownWidget<>(
//                WPosition.of(WType.ANCHORED, plane.getWidth() - 90, y, 15, plane),
//                WSize.of(88, 20),
//                mainInterface
//        );
//        plane.add(horizontal);
//        horizontal.addValues(HudHorizontalAnchor.values());
//        horizontal.selectValue(HudHorizontalAnchor.LEFT);
//
//        WButton saveButton = new WButton(
//                WPosition.of(WType.ANCHORED, plane.getWidth() - 90, plane.getHeight() - 25, 15, plane),
//                WSize.of(88, 20),
//                mainInterface
//        );
//        saveButton.setLabel(new LiteralText("Save"));
//        plane.add(saveButton);
//
//        WButton cancelButton = new WButton(
//                WPosition.of(WType.ANCHORED, 5, plane.getHeight() - 25, 15, plane),
//                WSize.of(88, 20),
//                mainInterface
//        );
//        cancelButton.setLabel(new LiteralText("Cancel"));
//        cancelButton.setOnMouseClicked(() -> {
//            history.back();
//            cancelButton.setOnMouseClicked(null);
//        });
//        plane.add(cancelButton);
//
//        mainInterface.add(list);
    }
}
