package com.ddoerr.scriptit.libraries.types;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;

import java.util.List;
import java.util.stream.Collectors;

public class ObjectiveModel extends AnnotationBasedModel {
    public static ObjectiveModel From(ScoreboardObjective objective) {
        ObjectiveModel objectiveModel = new ObjectiveModel();
        objectiveModel.objective = objective;
        return objectiveModel;
    }

    private ScoreboardObjective objective;

    @Getter
    public String getId(){
        return objective.getName();
    }

    @Getter
    public String getName() {
        return objective.getDisplayName().asFormattedString();
    }

    @Getter
    public String getCriterion() {
        return objective.getCriterion().getName();
    }

    @Getter
    public ScoreboardCriterion.RenderType getRenderType() {
        return objective.getRenderType();
    }

    @Getter
    public List<ScoreModel> getScores() {
        return objective.getScoreboard().getAllPlayerScores(objective)
                .stream()
                .map(ScoreModel::From)
                .collect(Collectors.toList());
    }
}
