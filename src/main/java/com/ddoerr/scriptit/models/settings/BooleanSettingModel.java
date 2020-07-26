package com.ddoerr.scriptit.models.settings;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.annotations.Setter;
import com.ddoerr.scriptit.mixin.OptionKeyAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.BooleanOption;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BooleanSettingModel extends AbstractSettingModel<Boolean> implements ToggleableSettingModel<Boolean> {
    public BooleanSettingModel(String name, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        super(name, getter, setter);
    }

    public static BooleanSettingModel fromOption(BooleanOption option) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        return new BooleanSettingModel(
                ((OptionKeyAccessor)option).getKey().substring(8),
                () -> option.get(minecraft.options),
                (value) -> option.set(minecraft.options, value.toString()));
    }

    public static BooleanSettingModel fromOption(BooleanOption option, String name) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        return new BooleanSettingModel(
                name,
                () -> option.get(minecraft.options),
                (value) -> option.set(minecraft.options, value.toString()));
    }

    @Setter
    @Override
    public void setValue(Boolean value) {
        super.setValue(value);
    }

    @Getter
    @Override
    public Boolean getValue() {
        return super.getValue();
    }

    @Override
    public String getPossibleValues() {
        return "true, false";
    }


    @Callable
    @Override
    public Boolean toggle() {
        boolean value = getValue();
        setValue(!value);
        return !value;
    }
}
