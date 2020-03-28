package com.ddoerr.scriptit.libraries.settings;

import com.ddoerr.scriptit.mixin.OptionKeyAccessor;
import com.ddoerr.scriptit.mixin.StepAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoubleSetting extends AbstractNumberSetting<Double> {
    public DoubleSetting(String name, Supplier<Double> getter, Consumer<Double> setter, double minimum, double maximum, double step) {
        super(name, getter, setter, minimum, maximum, step);
    }

    public static DoubleSetting fromOption(DoubleOption option) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        return new DoubleSetting(
                ((OptionKeyAccessor)option).getKey().substring(8),
                () -> option.get(minecraft.options),
                (value) -> option.set(minecraft.options, value),
                option.getMin(),
                option.getMax(),
                ((StepAccessor)option).getStep());
    }

    public static DoubleSetting fromOption(DoubleOption option, String name) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        return new DoubleSetting(
                name,
                () -> option.get(minecraft.options),
                (value) -> option.set(minecraft.options, value),
                option.getMin(),
                option.getMax(),
                ((StepAccessor)option).getStep());
    }

    @Override
    public void set(Object value) {
        setter.accept(MathHelper.clamp((double)value, minimum, maximum));
    }

    @Override
    public Type getType() {
        return double.class;
    }

    @Override
    public Object toggle() {
        double value = getter.get();
        double next = value + step;

        if (next > maximum) {
            next = minimum;
        }

        setter.accept(next);
        return next;
    }
}
