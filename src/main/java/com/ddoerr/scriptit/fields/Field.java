package com.ddoerr.scriptit.fields;

import com.ddoerr.scriptit.screens.widgets.PanelWidget;
import net.minecraft.text.Text;

public interface Field<T> {
    T getValue();
    void setValue(T value);

    void deserialize(String value);
    String serialize();

    void setTitle(Text title);
    void setDescription(Text description);

    void createWidget(PanelWidget panel);
    void applyTemporaryValue();
}
