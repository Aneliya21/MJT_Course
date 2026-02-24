package Labs.lab_02.src;

import Labs.lab_02.src.date.DateEvent;
import Labs.lab_02.src.elimination.EliminationRule;
import Labs.lab_02.src.elimination.LowestRatingEliminationRule;
import Labs.lab_02.src.ergenka.Ergenka;

public class ShowAPIImpl implements ShowAPI {

    private Ergenka[] ergenkas;
    private EliminationRule[] defaultEliminatonRules;

    public ShowAPIImpl(Ergenka[] ergenkas, EliminationRule[] defaultEliminationRules) {
        this.ergenkas = ergenkas;
        this.defaultEliminatonRules = defaultEliminationRules;
    }

    @Override
    public Ergenka[] getErgenkas() {
        return ergenkas;
    }

    @Override
    public void playRound(DateEvent dateEvent) {
        if (ergenkas == null) {
            return;
        }

        for (Ergenka e : ergenkas) {
            organizeDate(e, dateEvent);
        }
    }

    @Override
    public void eliminateErgenkas(EliminationRule[] eliminationRules) {
        if (ergenkas == null) {
            return;
        }

        EliminationRule[] rulesToApply = eliminationRules;
        if (eliminationRules == null || eliminationRules.length == 0) {
            if (this.defaultEliminatonRules == null || this.defaultEliminatonRules.length == 0) {
                rulesToApply = new EliminationRule[]{ new LowestRatingEliminationRule() };
            } else {
                rulesToApply = this.defaultEliminatonRules;
            }
        }

        for (EliminationRule rule : rulesToApply) {
            if (rule == null) {
                continue;
            }
            Ergenka[] result = rule.eliminateErgenkas(this.ergenkas);
            if (result != null) {
                this.ergenkas = result;
            }
        }
    }

    @Override
    public void organizeDate(Ergenka ergenka, DateEvent dateEvent) {
        if (ergenka == null) {
            return;
        }
        ergenka.reactToDate(dateEvent);
    }
}