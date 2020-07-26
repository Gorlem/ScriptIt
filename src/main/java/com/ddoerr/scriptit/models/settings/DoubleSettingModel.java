package com.ddoerr.scriptit.models.settings;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.annotations.Setter;
import com.ddoerr.scriptit.mixin.OptionKeyAccessor;
import com.ddoerr.scriptit.mixin.StepAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoubleSettingModel extends AbstractNumberSettingModel<Double> {
    public DoubleSettingModel(String name, Supplier<Double> getter, Consumer<Double> setter, double minimum, double maximum, double step) {
        super(name, getter, setter, minimum, maximum, step);
    }

    public static DoubleSettingModel fromOption(DoubleOption option) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        return new DoubleSettingModel(
                ((OptionKeyAccessor)option).getKey().substring(8),
                () -> option.get(minecraft.options),
                (value) -> option.set(minecraft.options, value),
                option.getMin(),
                option.getMax(),
                ((StepAccessor)option).getStep());
    }

    public static DoubleSettingModel fromOption(DoubleOption option, String name) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        return new DoubleSettingModel(
                name,
                () -> option.get(minecraft.options),
                (value) -> option.set(minecraft.options, value),
                option.getMin(),
                option.getMax(),
                ((StepAccessor)option).getStep());
    }

    @Setter
    @Override
    public void setValue(Double value) {
        super.setValue(MathHelper.clamp(value, minimum, maximum));
    }

    @Getter
    @Override
    public Double getValue() {
        return super.getValue();
    }

    @Callable
    @Override
    public Double toggle() {
        double value = getValue();
        double next = value + step;

        if (next > maximum) {
            next = minimum;
        }

        setValue(next);
        return next;
    }
}
