package com.ddoerr.scriptit.libraries.types;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class TeamModel extends AnnotationBasedModel {
    public static TeamModel From(Team team) {
        TeamModel teamModel = new TeamModel();
        teamModel.team = team;
        return teamModel;
    }

    private Team team;

    @Getter
    public String getId() {
        return team.getName();
    }

    @Getter
    public String getName() {
        return team.getDisplayName().asFormattedString();
    }

    @Getter
    public String getPrefix() {
        return team.getPrefix().asFormattedString();
    }

    @Getter
    public String getSuffix() {
        return team.getSuffix().asFormattedString();
    }

    @Getter
    public Formatting getColor() {
        return team.getColor();
    }

    @Getter
    public List<String> getPlayers() {
        return new ArrayList<>(team.getPlayerList());
    }
}
