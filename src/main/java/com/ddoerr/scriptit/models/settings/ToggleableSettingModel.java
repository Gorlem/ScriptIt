package com.ddoerr.scriptit.models.settings;

public interface ToggleableSettingModel<T> extends SettingModel<T> {
    T toggle();
}
