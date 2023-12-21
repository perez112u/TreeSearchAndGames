package ia.algo.recherche;

import ia.framework.common.Action;
import ia.framework.common.State;
import ia.framework.recherche.HasHeuristic;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class GFS extends TreeSearch
{

    /**
     * Crée un algorithme de recherche
     *
     * @param p Le problème à résoudre
     * @param initial_state L'état initial
     */
    public GFS(SearchProblem p, State initial_state) {
        super(p, initial_state);
    }

    @Override
    public boolean solve() {
        //On commence à l'état initial :
        SearchNode node = SearchNode.makeRootSearchNode(intial_state);
        State state = node.getState();

        // On initialise la frontière ( FIFO ) avec ce noeud
        frontier = new PriorityQueue<>(Comparator.comparingDouble(SearchNode::getHeuristic));
        frontier.add(node);


        //Tant que la frontière n'est pas vide
        while(!frontier.isEmpty())
        {
            //Retirer le premier nœud de la frontière
            node = frontier.poll();
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
            // On récupère les enfants du nœud
            // Les actions possibles depuis cet état
            ArrayList<Action> actions = problem.getActions(state);
            ArrayList<SearchNode> noeudsEnfants = new ArrayList<>();

            for(Action action : actions)
            {
                SearchNode nodeChild = SearchNode.makeChildSearchNode(problem,node,action);
                noeudsEnfants.add(nodeChild);
            }

            //Pour chaque enfant du noeud
            //S'il n'est pas dans la frontiers et dans les déja visité ajouté à la frontière
            for (SearchNode enfant : noeudsEnfants) {
                State stateEnfant = enfant.getState();

                // Si l'état enfant n'a pas été exploré et n'est pas déjà dans la frontière
                if (!explored.contains(stateEnfant) && !isInFrontier(stateEnfant)) {
                    frontier.add(enfant);
                } else if (isInFrontier(stateEnfant)) {
                    SearchNode existingNode = getNodeFromFrontier(stateEnfant);

                    // Comparer le coût entre le nœud existant et le nœud enfant
                    if (existingNode.getHeuristic() > enfant.getHeuristic()) {
                        frontier.remove(existingNode); // Supprimer l'ancien nœud de la frontière
                        frontier.add(enfant); // Ajouter le nouveau nœud avec un coût plus faible
                    }
                }
            }

        }

        return false;
    }

    /**
     * Permets de savoir si un noeud est dans la frontiere
     * @param state
     * @return
     */
    private boolean isInFrontier(State state) {
        for (SearchNode frontierNode : frontier) {
            if (frontierNode.getState().equals(state)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Renvoie un noeud qui a le bon state
     * @param state
     * @return retourne le noeud
     */
    private SearchNode getNodeFromFrontier(State state) {
        for (SearchNode frontierNode : frontier) {
            if (frontierNode.getState().equals(state)) {
                return frontierNode;
            }
        }
        return null;
    }
}
