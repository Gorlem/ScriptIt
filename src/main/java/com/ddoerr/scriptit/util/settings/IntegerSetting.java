package com.ddoerr.scriptit.util.settings;

import com.ddoerr.scriptit.mixin.OptionKeyAccessor;
import com.ddoerr.scriptit.mixin.StepAccessor;
import com.ddoerr.scriptit.util.ObjectConverter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntegerSetting extends AbstractNumberSetting<Integer> {
    public IntegerSetting(String name, Supplier<Integer> getter, Consumer<Integer> setter, int minimum, int maximum) {
        super(name, getter, setter, minimum, maximum, 1);
    }

    public IntegerSetting(String name, Supplier<Integer> getter, Consumer<Integer> setter, int minimum, int maximum, int step) {
        super(name, getter, setter, minimum, maximum, step);
    }

    public static IntegerSetting fromOption(DoubleOption option) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        return new IntegerSetting(
                ((OptionKeyAccessor)option).getKey().substring(8),
                () -> (int)option.get(minecraft.options),
                (value) -> option.set(minecraft.options, (double)value),
                (int)option.getMin(),
                (int)option.getMax(),
                (int)((StepAccessor)option).getStep());
    }

    @Override
    public void set(Object object) {
        setter.accept(MathHelper.clamp(ObjectConverter.toInteger(object), minimum, maximum));
    }

    @Override
    public Object toggle() {
        int value = getter.get();
        int next = value + step;

        if (next > maximum) {
            next = minimum;
        }

        setter.accept(next);
        return next;
    }
}
