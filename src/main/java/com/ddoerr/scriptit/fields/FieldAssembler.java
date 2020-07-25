package com.ddoerr.scriptit.fields;

import com.ddoerr.scriptit.screens.widgets.PanelWidget;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Map;

public class FieldAssembler {
    public void assembleFields(PanelWidget panel, Map<String, Field<?>> fields) {
        int y = 0;

        for (Map.Entry<String, Field<?>> entry : fields.entrySet()) {
            PanelWidget fieldPanel = panel.createChild(PanelWidget.class, Position.of(panel, 0, y), Size.of(panel.getWidth(), 0));
            entry.getValue().createWidget(fieldPanel);
            y += fieldPanel.getHeight() + 5;
        }

        panel.setHeight(y);
    }
}
