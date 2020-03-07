package com.ddoerr.scriptit.util.settings;

import com.ddoerr.scriptit.mixin.OptionKeyAccessor;
import com.ddoerr.scriptit.util.ObjectConverter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.DoubleOption;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntegerSetting extends AbstractNumberSetting<Integer> {
    public IntegerSetting(String name, Supplier<Integer> getter, Consumer<Integer> setter, int minimum, int maximum) {
        super(name, getter, setter, minimum, maximum);
    }

    public static IntegerSetting fromOption(DoubleOption option) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        return new IntegerSetting(
                ((OptionKeyAccessor)option).getKey().substring(8),
                () -> (int)option.get(minecraft.options),
                (value) -> option.set(minecraft.options, (double)value),
                (int)option.getMin(),
                (int)option.getMax());
    }

    @Override
    public void set(Object object) {
        setter.accept(ObjectConverter.toInteger(object));
    }
}
