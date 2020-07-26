package com.ddoerr.scriptit.extension.libraries;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.models.PlayerEntryModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ServerLibrary extends AnnotationBasedModel {
    MinecraftClient minecraft;

    public ServerLibrary(MinecraftClient minecraft) {
        this.minecraft = minecraft;
    }
    @Getter
    public String getName() {
        ServerInfo currentServerEntry = minecraft.getCurrentServerEntry();
        if (currentServerEntry != null) {
            return currentServerEntry.name;
        }

        if (minecraft.isInSingleplayer() && minecraft.isIntegratedServerRunning() && minecraft.getServer() != null) {
            return minecraft.getServer().getLevelName();
        }

        return StringUtils.EMPTY;
    }

    @Getter
    public String getAddress() {
        ServerInfo currentServerEntry = minecraft.getCurrentServerEntry();
        if (currentServerEntry != null) {
            return currentServerEntry.address;
        }

        if (minecraft.isInSingleplayer() && minecraft.isIntegratedServerRunning()) {
            return "local";
        }

        return StringUtils.EMPTY;
    }

    @Getter
    public String getLabel() {
        ServerInfo currentServerEntry = minecraft.getCurrentServerEntry();
        if (currentServerEntry != null) {
            return currentServerEntry.label;
        }

        if (minecraft.isInSingleplayer() && minecraft.isIntegratedServerRunning() && minecraft.getServer() != null) {
            return minecraft.getServer().getServerMotd();
        }

        return StringUtils.EMPTY;
    }

    @Getter
    public List<PlayerEntryModel> getPlayers() {
        if (minecraft.getNetworkHandler() == null) {
            return Collections.emptyList();
        }

        return minecraft.getNetworkHandler().getPlayerList().stream()
                .map(PlayerEntryModel::From)
                .collect(Collectors.toList());
    }
}
