package com.ddoerr.scriptit.libraries.types;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import net.minecraft.scoreboard.ScoreboardPlayerScore;

public class ScoreModel extends AnnotationBasedModel {
    public static ScoreModel From(ScoreboardPlayerScore score) {
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.score = score;
        return scoreModel;
    }

    private ScoreboardPlayerScore score;

    @Getter
    public String getPlayer() {
        return score.getPlayerName();
    }

    @Getter
    public int getScore() {
        return score.getScore();
    }
}
