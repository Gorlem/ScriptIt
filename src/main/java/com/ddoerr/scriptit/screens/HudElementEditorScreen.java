package com.ddoerr.scriptit.screens;

import spinnery.client.BaseScreen;

public class HudElementEditorScreen extends BaseScreen {
//    private void showSetup(HudElement hudElement) {
//        WInterface mainInterface = getInterfaceHolder().getInterfaces().get(0);
//
//        Window window = MinecraftClient.getInstance().getWindow();
//
//        WVerticalList list = new WVerticalList(
//                WPosition.of(WType.FREE, window.getScaledWidth() / 2 - 100, window.getScaledHeight() / 2 - 100, 10),
//                WSize.of(200, 200),
//                mainInterface
//        );
//
//        PanelWidget plane = new PanelWidget(
//                WPosition.of(WType.ANCHORED, 0, 0, 0, list),
//                WSize.of(190, 190),
//                mainInterface
//        );
//
//        list.add(plane);
//
//        WButton editScript = new WButton(
//                WPosition.of(WType.ANCHORED, 5, 5, 15, plane),
//                WSize.of(180, 20),
//                mainInterface
//        );
//        editScript.setLabel(new LiteralText("Edit Script"));
//        editScript.setOnMouseClicked(() -> {
//            history.open(() -> new ScriptEditorScreen(hudElement.getScriptContainer()));
//            editScript.setOnMouseClicked(null);
//        });
//        plane.add(editScript);
//
//        int y = 30;
//
//        for (Map.Entry<String, Object> entry : hudElement.getOptions().entrySet()) {
//            WStaticText title = new WStaticText(
//                    WPosition.of(WType.ANCHORED, 5, y, 15, plane),
//                    mainInterface,
//                    new LiteralText(entry.getKey())
//            );
//            WDynamicText text = new WDynamicText(
//                    WPosition.of(WType.ANCHORED, 5, y + 10, 15, plane),
//                    WSize.of(180, 16),
//                    mainInterface
//            );
//
//            text.setText(entry.getValue().toString());
//
//            plane.add(title, text);
//            y += 35;
//        }
//
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
//    }
}
