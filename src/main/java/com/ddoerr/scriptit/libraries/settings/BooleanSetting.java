package com.ddoerr.scriptit.libraries.settings;

import com.ddoerr.scriptit.mixin.OptionKeyAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.BooleanOption;

import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BooleanSetting extends AbstractSetting<Boolean> implements ToggleableSetting {
    public BooleanSetting(String name, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        super(name, getter, setter);
    }

    public static BooleanSetting fromOption(BooleanOption option) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        return new BooleanSetting(
                ((OptionKeyAccessor)option).getKey().substring(8),
                () -> option.get(minecraft.options),
                (value) -> option.set(minecraft.options, value.toString()));
    }

    public static BooleanSetting fromOption(BooleanOption option, String name) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        return new BooleanSetting(
                name,
                () -> option.get(minecraft.options),
                (value) -> option.set(minecraft.options, value.toString()));
    }

    @Override
    public String getPossibleValues() {
        return "true, false";
    }

    @Override
    public Type getType() {
        return boolean.class;
    }

    @Override
    public Object toggle() {
        boolean value = getter.get();
        setter.accept(!value);
        return !value;
    }
}
