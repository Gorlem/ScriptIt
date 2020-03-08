package com.ddoerr.scriptit.util.settings;

import com.ddoerr.scriptit.mixin.OptionKeyAccessor;
import com.ddoerr.scriptit.util.ObjectConverter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.BooleanOption;

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

    @Override
    public void set(Object object) {
        setter.accept(ObjectConverter.toBoolean(object));
    }

    @Override
    public String getPossibleValues() {
        return "true, false";
    }

    @Override
    public Object toggle() {
        boolean value = getter.get();
        setter.accept(!value);
        return !value;
    }
}
