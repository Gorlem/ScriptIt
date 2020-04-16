package com.ddoerr.scriptit.libraries.settings;

public interface ToggleableSettingModel<T> extends SettingModel<T> {
    T toggle();
}
