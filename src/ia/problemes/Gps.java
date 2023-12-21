package ia.problemes;

import ia.framework.common.Action;
import ia.framework.common.ArgParse;
import ia.framework.common.State;
import ia.framework.recherche.Problem;

import java.io.IOException;
import java.util.List;

public class Gps extends Problem {

    private final List<Ville> graphe = Ville.des("src/ia/problemes/villes.txt");
    public static final int SIZE = 100;

    public Gps() throws IOException, ClassNotFoundException {
        init();
    }


    public void init() throws IOException, ClassNotFoundException {

        //on ajout les etats
        STATES = new GpsState[SIZE + 2];
        for (int i = 0; i < SIZE; i++) {
            //avec le fichier villes1000.ser
            STATES[i] = new GpsState(graphe.get(i).getNom(), graphe.get(i).getLatitude(), graphe.get(i).getLongitude());
        }
        //on ajoute les etats de depart et d'arrivee

        //etat de depart
        STATES[STATES.length - 2] = ArgParse.makeInitialState("gps");

        //etat d'arrivee
        STATES[STATES.length - 1] = new GpsState("Gaillard");


        //on ajoute les actions
        Action AUTOROUTE = new Action("autoroute");
        Action VOIE_RAPIDE = new Action("voie rapide");
        Action DEPARTEMENTALE = new Action("departementale");
        ACTIONS = new Action[]{AUTOROUTE, VOIE_RAPIDE, DEPARTEMENTALE};


        //on ajoute les transitions pour les etats de depart et d'arrivee
        for (int i = 0; i < SIZE; i++) {
            TRANSITIONS.addTransition(STATES[SIZE],DEPARTEMENTALE, STATES[i],distance(STATES[i], STATES[SIZE]));
            TRANSITIONS.addTransition(STATES[i],DEPARTEMENTALE, STATES[SIZE+1],distance(STATES[i], STATES[SIZE+1]));
        }

        //on ajoute les transitions pour les 100 premieres villes
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(i != j) {
                    if(i < 50 && j < 50){
                        TRANSITIONS.addTransition(STATES[i],AUTOROUTE, STATES[j],distance(STATES[i], STATES[j]));
                    } else if(j < SIZE && i < SIZE){
                        TRANSITIONS.addTransition(STATES[i],VOIE_RAPIDE, STATES[j],distance(STATES[i], STATES[j]));
                    }
                }
            }
        }

    }

    public static double distance(State state1,State state2) {
        double lat1 = ((GpsState)state1).getLat();
        double lon1 = ((GpsState)state1).getLon();
        double lat2 = ((GpsState)state2).getLat();
        double lon2 = ((GpsState)state2).getLon();
        double a = Math.sin(Math.toRadians(lat2 - lat1) / 2) * Math.sin(Math.toRadians(lat2 - lat1) / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(Math.toRadians(lon2 - lon1) / 2) * Math.sin(Math.toRadians(lon2 - lon1) / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371.0 * c;
    }

    @Override
    public boolean isGoalState(State s) {
        return s.equals(STATES[SIZE + 1]);
    }
}
