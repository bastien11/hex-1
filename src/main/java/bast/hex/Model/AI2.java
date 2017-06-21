package bast.hex.Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AI2 implements IAI {
    private final int dimension;
    private ArrayList<Cell> coupsJoues, chaineDeNoeuds = new ArrayList<>();

    public AI2(int dimension) {
        this.dimension = dimension;
    }

    public Cell premierCoup() {
        Random rand = new Random();
        int nbr = rand.nextInt(2);
        int nbrAleatoire = rand.nextInt(dimension);
        Cell position = new Cell(1, 1, Color.BLUE); //Permet de determiner si l'ia va jouer sur la premiere ligne ou sur la derniere ligne
        if (nbr == 0) {
            position.setX(1);
            position.setY(nbrAleatoire);
        } else {
            position.setX(dimension-2);
            position.setY(nbrAleatoire);
        }
        Victory victoire = new Victory(new ArrayList<>(), dimension);
        victoire.analyseVictory(position);
        chaineDeNoeuds = victoire.getArrayListeOuverteJ1();
        return position;
    }

    public Cell play(HexModel model) {
        coupsJoues = new ArrayList<>();
        for (Cell c : model.grid) {
            if (!c.getBorder() && c.getColor() != Color.WHITE) {
                coupsJoues.add(c);
            }
        }

        Cell c;
        if (coupsJoues.size() <= 0) {
            c = premierCoup();
        } else {
            c = poserPion(coupsJoues);
        }
        return model.grid.getCell(c.getX(), c.getY());
    }

    public Cell poserPion(List<Cell> plateau) {
        coupsJoues = new ArrayList<>(plateau);
        Cell positionAnalysee = new Cell(0, 0, Color.BLUE);
        Cell positionAJouer = new Cell(0, 0, Color.BLUE);
        boolean coupPossible;
        for (int i = chaineDeNoeuds.size() - 1; i > -1; i--) {
            Cell noeud = chaineDeNoeuds.get(i);
            positionAnalysee.setX(noeud.getX());
            positionAnalysee.setY(noeud.getY());
            //Si la position recuperee appartient a la partie superieure du plateau, on essaye de rejoindre le bas
            if (positionAnalysee.getX() < ((dimension - 1) / 2)) {
                if (positionAnalysee.getX() > 0) {
                    positionAJouer = initialiserCell(positionAnalysee.getX() + 1, positionAnalysee.getY());
                    coupPossible = coupJouable(positionAJouer);
                    if (coupPossible == true) {
                        chaineDeNoeuds.add(positionAJouer);
                        return positionAJouer;
                    } else if (positionAnalysee.getY() > 0) { //Si l'on ne se trouve pas sur la premiere colonne (pour eviter de sortir des limites du plateau
                        positionAJouer = initialiserCell(positionAnalysee.getX() + 1, positionAnalysee.getY() - 1);
                        coupPossible = coupJouable(positionAJouer);
                        if (coupPossible == true) {
                            chaineDeNoeuds.add(positionAJouer);
                            return positionAJouer;
                        }
                    }
                } else { //si l'on est sur la premiere ligne
                    positionAJouer = initialiserCell(positionAnalysee.getX() + 1, positionAnalysee.getY());
                    coupPossible = coupJouable(positionAJouer);
                    if (coupPossible) {
                        chaineDeNoeuds.add(positionAJouer);
                        return positionAJouer;
                    } else if (positionAnalysee.getY() > 0) { //Sinon, on verifie que l'on ne se trouve pas sur la premiere colonne
                        positionAJouer = initialiserCell(positionAnalysee.getX() + 1, positionAnalysee.getY() - 1);
                        coupPossible = coupJouable(positionAJouer);
                        if (coupPossible) {
                            chaineDeNoeuds.add(positionAJouer);
                            return positionAJouer;
                        }
                    }
                }
            } else { //Si la position recuperee appartient a la partie inferieure du plateau, on essaye de rejoindre le haut
                if (positionAnalysee.getX() < dimension - 1) {
                    positionAJouer = initialiserCell(positionAnalysee.getX() - 1, positionAnalysee.getY());
                    coupPossible = coupJouable(positionAJouer);
                    if (coupPossible == true) {
                        chaineDeNoeuds.add(positionAJouer);
                        return positionAJouer;
                    } else if (positionAnalysee.getY() < dimension - 1) {
                        positionAJouer = initialiserCell(positionAnalysee.getX() - 1, positionAnalysee.getY() + 1);
                        coupPossible = coupJouable(positionAJouer);
                        if (coupPossible == true) {
                            chaineDeNoeuds.add(positionAJouer);
                            return positionAJouer;
                        }
                    }
                } else { //si l'on est sur la derniere ligne
                    positionAJouer = initialiserCell(positionAnalysee.getX() - 1, positionAnalysee.getY());
                    coupPossible = coupJouable(positionAJouer);
                    if (coupPossible) {
                        chaineDeNoeuds.add(positionAJouer);
                        return positionAJouer;
                    } else if (positionAnalysee.getY() < dimension - 1) { //Sinon, on verifie que l'on ne se trouve pas sur la derniere colonne
                        positionAJouer = initialiserCell(positionAnalysee.getX() - 1, positionAnalysee.getY() + 1);
                        coupPossible = coupJouable(positionAJouer);
                        if (coupPossible) {
                            chaineDeNoeuds.add(positionAJouer);
                            return positionAJouer;
                        }
                    }
                }
            }
            //Si on ne peut jouer ni en haut ni en bas, on joue sur les cotes
            if (positionAnalysee.getY() == dimension - 1) {
                //si on est sur la derniere colonne, on ne joue qu'a gauche
                positionAJouer = initialiserCell(positionAnalysee.getX(), positionAnalysee.getY() - 1);
                coupPossible = coupJouable(positionAJouer);
                if (coupPossible == true) {
                    chaineDeNoeuds.add(positionAJouer);
                    return positionAJouer;
                }
            } else if (positionAnalysee.getY() == 0) { //si on est sur la premiere colonne, on ne joue qu'a droite
                positionAJouer = initialiserCell(positionAnalysee.getX(), positionAnalysee.getY() + 1);
                coupPossible = coupJouable(positionAJouer);
                if (coupPossible == true) {
                    chaineDeNoeuds.add(positionAJouer);
                    return positionAJouer;
                }
            } else { //sinon on peut essayer de jouer a gauche ou a droite
                positionAJouer = initialiserCell(positionAnalysee.getX(), positionAnalysee.getY() + 1);
                coupPossible = coupJouable(positionAJouer);
                if (coupPossible == true) {
                    chaineDeNoeuds.add(positionAJouer);
                    return positionAJouer;
                } else {
                    positionAJouer = initialiserCell(positionAnalysee.getX(), positionAnalysee.getY() - 1);
                    coupPossible = coupJouable(positionAJouer);
                    if (coupPossible == true) {
                        chaineDeNoeuds.add(positionAJouer);
                        return positionAJouer;
                    }
                }
            }
        }
        //si aucune des positions precedentes n'a pu etre joue, alors on pose un pion sur n'importe quelle case libre
        do {
            Random rand = new Random();
            int nbrAleatoire = rand.nextInt(dimension);
            positionAJouer.setX(nbrAleatoire);
            nbrAleatoire = rand.nextInt(dimension);
            positionAJouer.setY(nbrAleatoire);
            coupPossible = coupJouable(positionAJouer);
        } while (coupPossible == false);
        return positionAJouer;
    }

    public Cell initialiserCell(int abscisse, int ordonnee) {
        return new Cell(abscisse, ordonnee, Color.BLUE);
    }

    //permet de voir si la position est deja occupee ou non
    public boolean coupJouable(Cell positionAJouer) {
        boolean coupPossible = true;
        for (Cell coupJoue : coupsJoues) {
            if (coupJoue.getX() == positionAJouer.getX() && coupJoue.getY() == positionAJouer.getY()) {
                coupPossible = false;
                break;
            }
        }
        return coupPossible;
    }
}
