package com.ddoerr.scriptit.models;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class PlayerEntryModel extends AnnotationBasedModel {
    public static PlayerEntryModel From(PlayerListEntry playerListEntry) {
        PlayerEntryModel playerEntryModel = new PlayerEntryModel();
        playerEntryModel.playerListEntry = playerListEntry;
        return playerEntryModel;
    }

    private PlayerListEntry playerListEntry;

    @Getter
    public String getUuid() {
        return playerListEntry.getProfile().getId().toString();
    }

    @Getter
    public String getName() {
        return playerListEntry.getProfile().getName();
    }

    @Getter
    public String getFormatted() {
        return Optional.ofNullable(playerListEntry.getDisplayName())
                .map(Text::asFormattedString)
                .orElse(StringUtils.EMPTY);
    }

    @Getter
    public String getTeam() {
        return Optional.ofNullable(playerListEntry.getScoreboardTeam())
                .map(Team::getName)
                .orElse(StringUtils.EMPTY);
    }

    @Getter
    public GameMode getGamemode() {
        return Optional.ofNullable(playerListEntry.getGameMode())
                .orElse(GameMode.NOT_SET);
    }
}
