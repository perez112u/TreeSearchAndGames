package ia.problemes;

import ia.framework.common.Action;
import ia.framework.common.State;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.recherche.SearchProblem;

import java.util.ArrayList;
import java.util.Objects;

public class RushHour extends SearchProblem {

    /**
     * attributs String static des actions possibles pour une voitures
     */
    public static final String UP    = "Up";
    public static final String LEFT  = "Left";
    public static final String DOWN  = "Down";
    public static final String RIGHT = "Right";
    private RushHourState rushHourState;


    public RushHour( int difficulty) {

        this.rushHourState = new RushHourState(difficulty);
        setActions(this.rushHourState);

    }

    /**
     * methode implementant le tableau des actions, 1 action par colonne
     * @param rushHourState état initial utilisé pour déterminer l'orientation de chaque voiture (ses 2 actions possibles)
     */
    private void setActions(RushHourState rushHourState) {
        char[] vehicules = rushHourState.getVehicules();
        ACTIONS = new Action[vehicules.length*2];
        int i = 0;
        for (char vehicule : vehicules) {
            char orientation = rushHourState.getOrientation(vehicule);

            if (orientation == 'H') {
                ACTIONS[i] = new Action(vehicule + " Left");
                ACTIONS[i+1] = new Action(vehicule + " Right");
            }
            if (orientation == 'V') {
                ACTIONS[i] = new Action(vehicule + " Up");
                ACTIONS[i+1] = new Action(vehicule + " Down");
            }
            if (orientation != 'H' && orientation != 'V') {
                System.out.println("orientation non reconnu");
            }
            i += 2;
        }
    }

    @Override
    public ArrayList<Action> getActions(State s) {
       ArrayList<Action> actions = new ArrayList<Action>();
        for (Action a : ACTIONS) {
            if( ((RushHourState) s).isLegal(a))
                actions.add(a);
        }
        return actions;
    }

    @Override
    public State doAction(State s, Action a) {
        RushHourState b = (RushHourState) s.clone();

        String[] st = a.getName().split(" ");
        // on recupere le vehicule sur lequel on souhaite effectué l'action
        char vehicule = st[0].charAt(0);
        // on recupere une des 4 actions possibles
        String direction = st[1];

        if (Objects.equals(direction, LEFT))
            b.moveVehiculeLeft(vehicule);
        else if (Objects.equals(direction, RIGHT))
            b.moveVehiculeRight(vehicule);
        else if (Objects.equals(direction, UP))
            b.moveVehiculeUp(vehicule);
        else if (Objects.equals(direction, DOWN))
            b.moveVehiculeDown(vehicule);
        else {
            throw new IllegalArgumentException("Invalid" + direction);
        }

        System.out.println(b);
        return b;

    }

    @Override
    public boolean isGoalState(State s) {
        return ((RushHourState)s).estArrivee();
    }

    @Override
    public double getActionCost(State s, Action a) {
        return 1;
    }

}
