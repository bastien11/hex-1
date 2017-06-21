package bast.hex.Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

public class AI implements IAI {
    private int origProfondeur;
    private int dimension;
    private int profondeur;
    private boolean gagnantJ1;
    private boolean gagnantJ2;

    public AI(int dimension, int profondeur) {
        this.gagnantJ1 = true;
        this.gagnantJ2 = true;
        this.dimension = dimension;
        this.profondeur = profondeur;
        this.origProfondeur = profondeur;
    }

    public Cell play(HexModel model) {
        ArrayList<Cell> plateau = new ArrayList<>();
        for (Cell c : model.grid) {
            if (!c.getBorder() && c.getColor() != Color.WHITE) {
                plateau.add(c);
            }
        }
        boolean movePossible = true;
        int tmp, vMax = -10000, maxI = 0, maxJ = 0;
        for (int i = 1; i < dimension-1; i++) {
            for (int j = 1; j < dimension-1; j++) {
                for (Cell board : plateau) {
                    if (board.getX() == i && board.getY() == j) {
                        movePossible = false;
                    }
                }
                if (movePossible) {
                    Cell position = new Cell(i, j, model.getPlayer());
                    plateau.add(position);
                    tmp = min(plateau);
                    profondeur = origProfondeur;
                    Long nbr = Math.abs(UUID.randomUUID().getMostSignificantBits());
                    if (tmp > vMax || ((tmp == vMax) && (nbr % 2 == 0))) {
                        vMax = tmp;
                        maxI = i;
                        maxJ = j;
                    }
                    plateau.remove(position);
                }
                movePossible = true;
            }
        }
        return model.grid.getCell(maxI, maxJ);
    }
    
    private int min(ArrayList<Cell> coupsJoues){
        if (profondeur == 0 || !gagnantJ1 || !gagnantJ2) {
            return eval(coupsJoues);
        }
        int valeurMinimale = 10000;
        int tmp;
        ArrayList<Cell> plateau = new ArrayList<>(coupsJoues);
        boolean coupPossible = true;
        for (int i = 1; i < dimension-1; i++) {
            for (int j = 1; j < dimension-1; j++) {
                for (Cell plateau1 : plateau) {
                    if (plateau1.getX() == i && plateau1.getY() == j) {
                        coupPossible = false; break;
                    }
                }
                if (coupPossible && profondeur > 0) {
                    Cell position = new Cell(i, j, Color.BLUE);
                    plateau.add(position);
                    profondeur -= 1;
                    tmp = max(plateau);
                    Long nbr = Math.abs(UUID.randomUUID().getMostSignificantBits());
                    if (tmp < valeurMinimale || ((tmp == valeurMinimale) && (nbr%2 == 0))) {
                        valeurMinimale = tmp;
                    }
                    plateau.remove(position);
                }
                coupPossible = true;
            }
        }
        return valeurMinimale;
    }
    
    private int max(ArrayList<Cell> coupsJoues){
        if (profondeur == 0 || !gagnantJ1 || !gagnantJ2) {
            return eval(coupsJoues);
        }
        int valeurMaximale = -10000;
        int tmp;
        ArrayList<Cell> plateau = new ArrayList<>(coupsJoues);
        boolean coupPossible = true;
        for (int i = 1; i < dimension-1; i++) {
            for (int j = 1; j < dimension-1; j++) {
                for (Cell plateau1 : plateau) {
                    if (plateau1.getX() == i && plateau1.getY() == j) {
                        coupPossible = false;
                        break;
                    }
                }
                if (coupPossible && profondeur > 0) {
                    Cell position = new Cell(i, j, Color.RED);
                    plateau.add(position);
                    profondeur -= 1;
                    tmp = min(plateau);
                    Long nbr = Math.abs(UUID.randomUUID().getMostSignificantBits());
                    if (tmp > valeurMaximale || ((tmp == valeurMaximale) && (nbr%2 == 0))) {
                        valeurMaximale = tmp;
                    }
                }
                coupPossible = true;
            }
        }
        return valeurMaximale;
    }
    
    private int eval(ArrayList<Cell> coupsJoues){
        int nbPions;
        ArrayList<Cell> plateau = new ArrayList<>(coupsJoues);
        //Nombre de pions presents sur le plateau
        nbPions = coupsJoues.size();
        if (!gagnantJ1 || !gagnantJ2) {
            if (!gagnantJ2) {
                return 1000 - nbPions;
            } else if (!gagnantJ1) {
                return -1000 + nbPions;
            } else {
                return 0;
            }
        }
        //On compte le plus grand nombre de pions alignés de chacun des joueurs
        int resultat;
        resultat = nb_series(plateau, dimension-2);
        return resultat;
    }
    
    private int nb_series(ArrayList<Cell> coupsJoues, int n){
        int compteur1 = 0;
        int compteur2 = 0;
        int tmp1;
        int tmp2;
        ArrayList<Cell> plateau = new ArrayList<>(coupsJoues);
        for (Cell plateau1 : plateau) {
            Victory victoire = new Victory(plateau, dimension);
            victoire.analyseVictory(plateau1);
            gagnantJ1 = victoire.getWinner() == Color.BLUE;
            gagnantJ2 = !gagnantJ1;
            tmp1 = victoire.getArrayListeOuverteJ1().size();
            if (n <= tmp1) {
                compteur1 += tmp1;
            }
            tmp2 = victoire.getArrayListeOuverteJ2().size();
            if (n <= tmp2) {
                compteur2 += tmp2;
            }
        }
        return compteur1 - compteur2;
    }
}
