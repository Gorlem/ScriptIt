package com.ddoerr.scriptit.libraries.settings;

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

public class IntegerSettingModel extends AbstractNumberSettingModel<Integer> {
    public IntegerSettingModel(String name, Supplier<Integer> getter, Consumer<Integer> setter, int minimum, int maximum) {
        super(name, getter, setter, minimum, maximum, 1);
    }

    public IntegerSettingModel(String name, Supplier<Integer> getter, Consumer<Integer> setter, int minimum, int maximum, int step) {
        super(name, getter, setter, minimum, maximum, step);
    }

    public static IntegerSettingModel fromOption(DoubleOption option) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        return new IntegerSettingModel(
                ((OptionKeyAccessor)option).getKey().substring(8),
                () -> (int)option.get(minecraft.options),
                (value) -> option.set(minecraft.options, (double)value),
                (int)option.getMin(),
                (int)option.getMax(),
                (int)((StepAccessor)option).getStep());
    }

    @Setter
    @Override
    public void setValue(Integer value) {
        super.setValue(MathHelper.clamp(value, minimum, maximum));
    }

    @Getter
    @Override
    public Integer getValue() {
        return super.getValue();
    }

    @Callable
    @Override
    public Integer toggle() {
        int value = getValue();
        int next = value + step;

        if (next > maximum) {
            next = minimum;
        }

        setValue(next);
        return next;
    }
}
