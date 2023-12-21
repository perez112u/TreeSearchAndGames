package ia.algo.jeux;


import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.common.State;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

/**
 * Définie un joueur MinMax
 *
 */

public class MinMaxPlayer extends Player {

    /**
     * Crée un joueur minmax
     * @param g l'instance du jeux
     * @param p1 vrai si joueur 1
     */
    public MinMaxPlayer(Game g, boolean p1){
        super(g, p1);
        name = "Minmax";
    }

    public Action getMove(GameState state){
        ActionValuePair valCoup;
        if (this.player == 0) {
            valCoup = maxValeur(state);
        } else {
             valCoup = minValeur(state);
        }

        return valCoup.getAction();
    }

    public ActionValuePair maxValeur(GameState state) {
        if (game.endOfGame(state)) {
            return  new ActionValuePair(null, state.getGameValue());
        }

        ActionValuePair bestAction = new ActionValuePair(null, Double.NEGATIVE_INFINITY);
        for (Action coup : game.getActions(state)) {
            GameState nextState = (GameState) game.doAction(state, coup);
            ActionValuePair actionCourante = minValeur(nextState);

            if (actionCourante!= null && actionCourante.getValue() > bestAction.getValue()) {
                bestAction = new ActionValuePair(coup, actionCourante.getValue());
            }
        }
        return bestAction;
    }

    public ActionValuePair minValeur(GameState state) {
        if (game.endOfGame(state)) {
            return  new ActionValuePair(null, state.getGameValue());
        }

        ActionValuePair bestAction = new ActionValuePair(null, Double.MAX_VALUE);

        for (Action coup : game.getActions(state)) {
            GameState nextState = (GameState) game.doAction(state, coup);
            ActionValuePair actionCourante = maxValeur(nextState);

            if (actionCourante.getValue() < bestAction.getValue()) {
                bestAction = new ActionValuePair(coup, actionCourante.getValue());
            }
        }
        return bestAction;
    }


}
