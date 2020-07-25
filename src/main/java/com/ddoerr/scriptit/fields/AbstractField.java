package com.ddoerr.scriptit.fields;

import com.ddoerr.scriptit.screens.widgets.PanelWidget;
import net.minecraft.text.Text;
import spinnery.widget.WStaticText;
import spinnery.widget.api.Position;

public abstract class AbstractField<T> implements Field<T> {
    protected T value;
    protected T temporaryValue;

    protected Text title;
    protected Text description;

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public void setTitle(Text title) {
        this.title = title;
    }

    @Override
    public void setDescription(Text description) {
        this.description = description;
    }

    @Override
    public void applyTemporaryValue() {
        value = temporaryValue;
    }

    @Override
    public void createWidget(PanelWidget panel) {
        temporaryValue = value;
        panel.createChild(WStaticText.class, Position.of(panel))
                .setText(title);
    }
}
