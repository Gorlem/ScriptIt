package com.ddoerr.scriptit.models.settings;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.Model;

public interface SettingModel<T> extends Model {
    String getName();

    T getValue();
    void setValue(T value);

    @Getter
    String getPossibleValues();
}
