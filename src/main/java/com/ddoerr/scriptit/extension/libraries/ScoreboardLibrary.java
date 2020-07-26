package com.ddoerr.scriptit.extension.libraries;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.models.ObjectiveModel;
import com.ddoerr.scriptit.models.TeamModel;
import net.minecraft.client.MinecraftClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScoreboardLibrary extends AnnotationBasedModel {
    MinecraftClient minecraft;

    public ScoreboardLibrary(MinecraftClient minecraft) {
        this.minecraft = minecraft;
    }

    @Getter
    public List<ObjectiveModel> getObjectives() {
        return Optional.ofNullable(minecraft.world)
                .map(w -> w.getScoreboard()
                        .getObjectives()
                        .stream()
                        .map(ObjectiveModel::From)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Getter
    public List<TeamModel> getTeams() {
        return Optional.ofNullable(minecraft.world)
                .map(w -> w.getScoreboard()
                        .getTeams()
                        .stream()
                        .map(TeamModel::From)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
