package ml.gorlem.scriptit.api.libraries;

import net.minecraft.client.MinecraftClient;

public interface VariableUpdater {
    Object update(String name, MinecraftClient minecraft);
}
