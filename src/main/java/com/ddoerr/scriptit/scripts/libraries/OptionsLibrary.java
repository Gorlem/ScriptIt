package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.mixin.OptionKeyAccessor;
import com.ddoerr.scriptit.util.settings.BooleanSetting;
import com.ddoerr.scriptit.util.settings.EnumSetting;
import com.ddoerr.scriptit.util.settings.Setting;
import com.ddoerr.scriptit.util.settings.StringSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.CloudRenderMode;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.client.options.Option;

import java.util.ArrayList;
import java.util.List;

public class OptionsLibrary implements LibraryInitializer {
    private List<Setting> options = new ArrayList<>();
    private MinecraftClient minecraft = MinecraftClient.getInstance();

    public OptionsLibrary() {
        options.add(BooleanSetting.fromOption(Option.AUTO_JUMP));
        options.add(new StringSetting("lastServer", () -> minecraft.options.lastServer, lastServer -> minecraft.options.lastServer = lastServer));
        options.add(new EnumSetting<>("renderClouds", CloudRenderMode.class, () -> minecraft.options.cloudRenderMode, cloudRenderMode -> minecraft.options.cloudRenderMode = cloudRenderMode));
    }

    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry options = registry.registerLibrary("options");

        options.registerFunction("set", this::setOption);
        options.registerFunction("get", this::getOption);
    }

    private Object setOption(String name, MinecraftClient minecraft, Object... arguments) {
        String key = arguments[0].toString();
        Setting option = findOption(key);
        option.set(arguments[1]);
        return null;
    }

    private Object getOption(String name, MinecraftClient minecraft, Object... arguments) {
        String key = arguments[0].toString();
        Setting option = findOption(key);
        return option.get();
    }

    private Setting findOption(String name) {
        return options.stream()
                .filter(o -> o.getName().equalsIgnoreCase(name))
                .findFirst()
                .get();
    }
}
