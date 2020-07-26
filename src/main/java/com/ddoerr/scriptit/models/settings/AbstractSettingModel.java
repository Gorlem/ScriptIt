package com.ddoerr.scriptit.models.settings;

import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractSettingModel<T> extends AnnotationBasedModel implements SettingModel<T> {
    private final String name;
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    protected final MinecraftClient minecraft;

    public AbstractSettingModel(String name, Supplier<T> getter, Consumer<T> setter) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;

        minecraft = MinecraftClient.getInstance();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setValue(T value) {
        setter.accept(value);
        minecraft.options.write();
    }

    @Override
    public T getValue() {
        return getter.get();
    }
}
