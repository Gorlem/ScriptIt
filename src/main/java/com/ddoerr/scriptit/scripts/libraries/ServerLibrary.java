package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.util.ObjectConverter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import java.util.stream.Collectors;

public class ServerLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("server");

        namespace.registerVariable("name", this::name);
        namespace.registerVariable("address", this::address);
        namespace.registerVariable("label", this::label);

        namespace.registerFunction("players", this::players);
    }

    private Object name(String name, MinecraftClient minecraft) {
        ServerInfo currentServerEntry = minecraft.getCurrentServerEntry();
        if (currentServerEntry != null) {
            return currentServerEntry.name;
        }

        if (minecraft.isInSingleplayer() && minecraft.isIntegratedServerRunning()) {
            return minecraft.getServer().getLevelName();
        }

        return null;
    }

    private Object address(String name, MinecraftClient minecraft) {
        ServerInfo currentServerEntry = minecraft.getCurrentServerEntry();
        if (currentServerEntry != null) {
            return currentServerEntry.address;
        }

        if (minecraft.isInSingleplayer() && minecraft.isIntegratedServerRunning()) {
            return "local";
        }

        return null;
    }

    private Object label(String name, MinecraftClient minecraft) {
        ServerInfo currentServerEntry = minecraft.getCurrentServerEntry();
        if (currentServerEntry != null) {
            return currentServerEntry.label;
        }

        if (minecraft.isInSingleplayer() && minecraft.isIntegratedServerRunning()) {
            return minecraft.getServer().getServerMotd();
        }

        return null;
    }

    private Object players(String name, MinecraftClient minecraft, Object... arguments) {
        return minecraft.getNetworkHandler().getPlayerList()
                .stream().map(ObjectConverter::convert)
                .collect(Collectors.toList());
    }
}
