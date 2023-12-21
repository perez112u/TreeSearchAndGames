package ia.algo.recherche;

import ia.framework.common.Action;
import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.*;

public class DFS extends TreeSearch
{
    /**
     * Crée un algorithme de recherche
     *
     * @param p Le problème à résoudre
     * @param s L'état initial
     */
    public DFS(SearchProblem p, State intial_state) {
        super(p, intial_state);
    }

    @Override
    public boolean solve() {
        //On commence à l'état initial :
        SearchNode node = SearchNode.makeRootSearchNode(intial_state);
        State state = node.getState();

        // On initialise la frontière ( FIFO ) avec ce noeud
        Deque<SearchNode> deque = new ArrayDeque<>();
        frontier = Collections.asLifoQueue(deque);
        frontier.add(node);
        explored.add(state);

        //Tant que la frontière n'est pas vide
        while(!frontier.isEmpty())
        {
            //Retirer le premier noeud de la frontière
            node = frontier.remove();
            state = node.getState();

            //Si le noeud contient un état but on retourne vrai
            if(problem.isGoalState(state))
            {
                end_node = node;
                return true;
            }

            //Sinon
            //On l'ajoute a l'état dèjà visité
            explored.add(state);
            // On récupere les enfants du noeud
            // Les actions possibles depuis cette état
            ArrayList<Action> actions = problem.getActions(state);
            ArrayList<SearchNode> noeudsEnfants = new ArrayList<>();

            for(Action action : actions)
            {
                SearchNode nodeChild = SearchNode.makeChildSearchNode(problem,node,action);
                noeudsEnfants.add(nodeChild);
            }

            //Pour chaque enfant du noeud
            //S'il n'est pas dans la frontiers et dans les déja visité ajouté à la frontière
            for (SearchNode enfant : noeudsEnfants)
            {
                State etatEnfant = enfant.getState();
                if(!frontier.contains(enfant) && !explored.contains(etatEnfant))
                {
                    frontier.add(enfant);
                }
            }
        }

        return false;
    }
}
