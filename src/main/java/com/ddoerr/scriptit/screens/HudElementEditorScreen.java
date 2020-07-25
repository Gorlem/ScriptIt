package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.hud.HudElementContainer;
import com.ddoerr.scriptit.api.hud.HudHorizontalAnchor;
import com.ddoerr.scriptit.api.hud.HudVerticalAnchor;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.fields.Field;
import com.ddoerr.scriptit.fields.FieldAssembler;
import com.ddoerr.scriptit.screens.widgets.PanelWidget;
import com.ddoerr.scriptit.screens.widgets.ValuesDropdownWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WButton;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Map;

public class HudElementEditorScreen extends AbstractHistoryScreen {
    private final FieldAssembler fieldAssembler;
    private HudElementContainer container;

    private HudHorizontalAnchor horizontalAnchor;
    private HudVerticalAnchor verticalAnchor;

    private Map<String, Field<?>> fields;

    public HudElementEditorScreen(ScreenHistory history, FieldAssembler fieldAssembler, HudElementContainer container) {
        super(history);
        this.fieldAssembler = fieldAssembler;

        this.container = container;
        fields = container.getHudElement().getFields();

        horizontalAnchor = container.getHorizontalAnchor();
        verticalAnchor = container.getVerticalAnchor();

        showSetup(container);
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

        PanelWidget child = panel.createChild(PanelWidget.class, Position.of(panel, 10, 35), Size.of(180, 0));
        fieldAssembler.assembleFields(child, fields);

        int y = 35 + child.getHeight();

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
        Point point = container.getRealPosition();
        container.setAnchor(horizontalAnchor, verticalAnchor);
        container.setRealPosition(point);

        for (Map.Entry<String, Field<?>> entry : fields.entrySet()) {
            entry.getValue().applyTemporaryValue();
        }

        onClose();
    }
}
