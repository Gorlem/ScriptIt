package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.mixin.FpsAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.util.List;
import java.util.stream.Collectors;

public class GameLibrary extends AnnotationBasedModel {
    private MinecraftClient minecraft;

    public GameLibrary(MinecraftClient minecraft) {
        this.minecraft = minecraft;
    }

    @Getter
    public int getFps() {
        return FpsAccessor.getCurrentFps();
    }

    @Getter
    public String getVersion() {
        return minecraft.getGame().getVersion().getName();
    }

    @Getter
    public List<String> getMods() {
        return FabricLoader.getInstance()
                .getAllMods()
                .stream()
                .map(m -> m.getMetadata().getId())
                .collect(Collectors.toList());
    }
}
