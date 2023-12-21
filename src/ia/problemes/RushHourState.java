package ia.problemes;

import ia.framework.common.Action;
import ia.framework.common.Misc;
import ia.framework.common.State;
import ia.framework.jeux.GameState;
import ia.framework.recherche.HasHeuristic;

import java.util.*;

public class RushHourState extends State implements HasHeuristic {

    /**
     * attribut static représentant tous les etats possibles pour 1 case
     */
    public static final char EMPTY = ' ';
    public static final char ROUGE = 'R';
    public static final char VERT = 'V';
    public static final char JAUNE = 'J';
    public static final char MARRON = 'M';
    public static final char ORANGE = 'O';
    public static final char GRIS = 'G';
    public static final char BEIGE = 'I';
    public static final char BLEUCLAIR = 'L';
    public static final char VERTFONCE = 'T';
    public static final char VIOLET = 'E';
    public static final char BLEU = 'B';
    public static final char ROSE = 'S';
    public static final char BLEUFONCE = 'F';
    public static final char BLANC = 'C';

    private char[] board;

    public RushHourState(int difficulty) {
        initializeBoard(difficulty);

    }

    public RushHourState(char[] board) {
        this.board = board.clone();
    }

    /**
     * remplie le board en fonction de la difficlté sélectionné par l'utilisateur
     * @param difficulty
     */
    private void initializeBoard(int difficulty) {
        switch (difficulty) {
            case 1:
                board = new char[] { EMPTY, EMPTY, JAUNE, EMPTY, VERT, VERT,
                                    EMPTY, EMPTY, JAUNE, EMPTY, EMPTY, EMPTY,
                                    ROUGE, ROUGE, JAUNE, EMPTY, EMPTY, EMPTY,
                                    VIOLET, VIOLET, VIOLET, EMPTY, EMPTY, BLEU,
                                    EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLEU,
                                    EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLEU
                };
                break;

            case 2:
                board = new char[] { JAUNE, JAUNE, JAUNE, VERT, EMPTY, ORANGE,
                        EMPTY, EMPTY, VIOLET, VERT, EMPTY, ORANGE,
                        ROUGE, ROUGE, VIOLET, EMPTY, EMPTY, BLEUCLAIR,
                        EMPTY, EMPTY, VIOLET, ROSE, ROSE, BLEUCLAIR,
                        BLEU, BLEU, BLEU, EMPTY, BLEUFONCE, EMPTY,
                        EMPTY, VERTFONCE, VERTFONCE, EMPTY, BLEUFONCE, EMPTY
                };
                break;

            case 3:
                board = new char[] { VERT, VERT, ORANGE, JAUNE, EMPTY, EMPTY,
                        VIOLET, EMPTY, ORANGE, JAUNE, EMPTY, EMPTY,
                        VIOLET, ROUGE, ROUGE, JAUNE, EMPTY, EMPTY,
                        VIOLET, EMPTY, EMPTY, BLEU, BLEU, BLEU,
                        VERTFONCE, VERTFONCE, VERTFONCE, EMPTY, BLEUCLAIR, ROSE,
                        BLEUFONCE, BLEUFONCE, EMPTY, EMPTY, BLEUCLAIR, ROSE
                };
                break;


            case 4:
                board = new char[] { VERT, ORANGE, BLEUCLAIR, BLEUCLAIR, ROSE, BLEUFONCE,
                        VERT, ORANGE, EMPTY, EMPTY, ROSE, BLEUFONCE,
                        JAUNE, ROUGE, ROUGE, VERTFONCE, EMPTY, BLANC,
                        JAUNE, GRIS, GRIS, VERTFONCE, EMPTY, BLANC,
                        JAUNE, EMPTY, BEIGE, VIOLET, VIOLET, VIOLET,
                        MARRON, MARRON, BEIGE, BLEU, BLEU, BLEU
                };
                break;
            default:
                System.out.println("Erreur du choix de difficulté : Choisir une difficulté entre 1 et 3");
        }

    }

    private int getLigne(int indice) {
        return indice/6;
    }

    private int getColonne(int indice) {
        return indice%6;
    }

    private int getPosition(int x, int y) {
        return x + 3 * y;
    }

    private int getValueAt(int x, int y) {
        return board[getPosition(x, y)];
    }


    /**
     * methode permettant de déterminer l'orientation d'une voiture sur le board
     * @param vehicule
     * @return char orientation de la voiture ('H' pour horizontal 'V' pour vertical)
     */
    public char getOrientation(char vehicule) {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == vehicule) {

                int l = getLigne(i);
                int c = getColonne(i);

                if ((c < 5 && board[i+1] == vehicule)) {
                    return 'H';
                }
                if ((l < 5 && board[i+6] == vehicule) ) {
                    return 'V';
                }
            }
        }
        return ' ';
    }



    private char[] getBoard() {
        return board;
    }

    @Override
    protected State cloneState() {
        char[] originalBoard = this.getBoard();

        char[] clonedBoard = new char[36];
        for (int i = 0; i < 36; i++) {
            clonedBoard[i] = originalBoard[i];
        }

        return new RushHourState(clonedBoard);
    }

    @Override
    protected boolean equalsState(State o) {
        char[] otherBoard = ((RushHourState)o).getBoard();

        for (int i = 0; i < board.length; i++) {
            if (board[i] != otherBoard[i]) {
                return false;
            }
        }

        return true;
    }


    @Override
    protected int hashState() {
        return Arrays.hashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("\n+---+---+---+---+---+---+\n");
        for (int i = 0; i < 6; i++) {
            res.append("|");
            for (int j = 0; j < 6; j++) {
                res.append(" ");
                res.append(board[6 * i + j]);
                res.append(" |");
            }
            res.append("\n");
            res.append("+---+---+---+---+---+---+\n");
        }
        return res.toString();
    }


    /**
     * coup legal si la voiture qu'on souhaite déplacer n'atteri pas dans une case déja occupée ou ne sort pas du board
     * @param a
     * @return
     */
    public boolean isLegal(Action a) {
        String[] s = a.getName().split(" ");
        char vehicule = s[0].charAt(0);
        String direction = s[1];

        int[] caseVehicule = getIndicesCases(vehicule);

        if (Objects.equals(direction, RushHour.LEFT)) {
            int[] cases = getIndicesCases(vehicule);
            int igauche = cases[0];
            int colonne = getColonne(igauche);
            return (colonne > 0) && (board[igauche - 1] == EMPTY);
        }
        if (Objects.equals(direction, RushHour.RIGHT)) {
            int[] cases = getIndicesCases(vehicule);
            int idroit = cases[cases.length-1];
            int colonne = getColonne(idroit);
            return (colonne < 5) && (board[idroit + 1] == EMPTY);
        }
        if (Objects.equals(direction, RushHour.UP)) {
            int[] cases = getIndicesCases(vehicule);
            int ihaut = cases[0];
            int ligne = getLigne(ihaut);
            return (ligne > 0) && (board[ihaut - 6] == EMPTY);
        }
        if (Objects.equals(direction, RushHour.DOWN)) {
            int[] cases = getIndicesCases(vehicule);
            int ibas = cases[cases.length-1];
            int ligne = getLigne(ibas);
            return (ligne < 5) && (board[ibas + 6] == EMPTY);
        }
        else
            throw new IllegalArgumentException("Invalid " + direction);

    }

    /**
     * methode retournant tous les vehicules disponible sur cet état
     */
    public char[] getVehicules() {
        List<Character> li = new ArrayList<Character>();
        for (char c : board) {
            if (!li.contains(c) && c != EMPTY) {
                li.add(c);
            }
        }

        //conversion de la liste en tableau de char
        char[] res = new char[li.size()];
        for (int i = 0; i < li.size(); i++) {
            res[i] = li.get(i);
        }
        return res;
    }

    /**
     * methode retournant les indices du board sur lesquels le vehicule donné est présent
     * @param vehicule
     */
    private int[] getIndicesCases(char vehicule) {
        List<Integer> li = new ArrayList<Integer>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == vehicule) {
                li.add(i);
            }
        }
        int[] res = new int[li.size()];
        for (int i = 0; i < li.size(); i++) {
            res[i] = li.get(i);
        }
        return res;
    }

    public void moveVehiculeLeft(char vehicule) {
        int[] caseVehicule = getIndicesCases(vehicule);
        for (int i : caseVehicule) {
            board[i-1] = board[i];
            board[i] = EMPTY;
        }


    }

    public void moveVehiculeRight(char vehicule) {
        int[] caseVehicule = getIndicesCases(vehicule);
        for (int i = caseVehicule.length-1; i >= 0; i--) {
            int indice = caseVehicule[i];
            board[indice+1] = board[indice];
            board[indice] = EMPTY;
        }
    }

    public void moveVehiculeUp(char vehicule) {
        int[] caseVehicule = getIndicesCases(vehicule);
        for (int i : caseVehicule) {
            board[i-6] = board[i];
            board[i] = EMPTY;
        }
    }

    public void moveVehiculeDown(char vehicule) {
        int[] caseVehicule = getIndicesCases(vehicule);
        for (int i = caseVehicule.length-1; i >= 0; i--) {
            int indice = caseVehicule[i];
            board[indice+6] = board[indice];
            board[indice] = EMPTY;
        }
    }

    public boolean estArrivee() {
        return getValueAt(2, 5) == ROUGE;
    }

    // retourne le nombre de case non vide restante entre la voiture rouge est l'arrivée
    @Override
    public double getHeuristic() {
        int indice = getIndicesCases(ROUGE)[1] + 1;
        int res = 0;
        while (indice <= 18) {  //case d arrivée
            if (board[indice] != EMPTY) {
                res++;
            }
            indice++;
        }
        return res;
    }
}
