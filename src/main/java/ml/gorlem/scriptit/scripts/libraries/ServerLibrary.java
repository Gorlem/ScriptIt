package ml.gorlem.scriptit.scripts.libraries;

import ml.gorlem.scriptit.api.libraries.LibraryInitializer;
import ml.gorlem.scriptit.api.libraries.LibraryRegistry;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import net.minecraft.client.MinecraftClient;

import java.util.stream.Collectors;

public class ServerLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("server");

//        namespace.registerVariable("name", (name, minecraft) -> minecraft.getCurrentServerEntry().name);
//        namespace.registerVariable("address", (name, minecraft) -> minecraft.getCurrentServerEntry().address);
//        namespace.registerVariable("label", (name, minecraft) -> minecraft.getCurrentServerEntry().label);

        namespace.registerFunction("players", this::players);
    }

    private Object players(String name, MinecraftClient minecraft, Object... arguments) {
        return minecraft.getNetworkHandler().getPlayerList()
                .stream().map(entry -> entry.getProfile().getName())
                .collect(Collectors.toList());
    }
}
