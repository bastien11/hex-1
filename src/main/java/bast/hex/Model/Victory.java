package bast.hex.Model;

import java.awt.*;
import java.util.ArrayList;

class Victory {

    private ArrayList<Cell> listeOuverteJ1 = new ArrayList<>();
    private ArrayList<Cell> listeOuverteJ2 = new ArrayList<>();
    private Grid grid;

    public Color getWinner() {
        return winner;
    }

    private Color winner; // Blanche pour auncun gagnant, Bleu ou Rouge selon le gagnant

    public void setWinner(Color winner) {
        this.winner = winner;
    }

    Victory(ArrayList<Cell> coupsJoues, int dimension) {
        this.grid = new Grid(dimension);
        winner = Color.WHITE;
        grid.buildGrid();
        for (Cell cell : coupsJoues) {
            grid.getCell(cell.getX(), cell.getY()).setColor(cell.getColor());
        }
    }

    Victory(Grid grid) {
        winner = Color.WHITE;
        this.grid = grid;
    }

    ArrayList<Cell> getArrayListeOuverteJ1() {
        return listeOuverteJ1;
    }

    ArrayList<Cell> getArrayListeOuverteJ2() {
        return listeOuverteJ2;
    }

    void analyseVictory(Cell position) {
        researchVictory(position.getX(), position.getY());
    }

    public boolean researchVictory(int i, int j) {
        if (getWinner() != Color.WHITE) {
            return true;
        }
        boolean victory;
        Cell startingCell; // Cellule de départ de l'algorithme de victoire
        ArrayList<Cell> neighborCells; // ArrayList qui va stocker les celluls voisines

        startingCell = grid.getCell(i,j);
        grid.setPast(startingCell); // On dit qu'on est deja passe par cette cellule pour ne pas y retourner
        neighborCells = researchNeighborCells(startingCell); // On récupère les cellules voisines de la cellule de départ
        victory = researchWinner(neighborCells); // On lance l'algo récursif

        // Si il y a victory on affecte le gagnant au bon player
        if (victory) {
            if (startingCell.getColor() == Color.BLUE)
                setWinner(Color.BLUE);

            else
                setWinner(Color.RED);
        }
        grid.resetPast(); // On remet l'attribut passe de toutes les cellules a false
        return victory;
    }

    public boolean researchWinner(ArrayList<Cell> neighborCells) {

        boolean victory = false;
        ArrayList<Cell> nextNeighborCells; // Arraylist qui va stocker les cellules voisines des cellules de l'arrayList passée en paramètre

        // On tets si il y a des cellules voisines
        if (!neighborCells.isEmpty()) {
            // Pour toutes chaque cellule de l'arrayList passés en paramètre on va récuperer les cellules voisines de ces  cellules
            for (Cell cell : neighborCells) {
                nextNeighborCells = researchNeighborCells(cell);

                // Test de victory
                for (Cell cells : nextNeighborCells) {
                    if (cells.getColor() == Color.BLUE) {
                        if (cells.getX() >= grid.getNbLines()-2) {
                            victory = true;
                        }
                        listeOuverteJ1.add(cells);
                    }
                    if (cells.getColor() == Color.RED) {
                        if (cells.getX() >= grid.getNbLines()-2) {
                            victory = true;
                        }
                        listeOuverteJ2.add(cells);
                    }
                }

                // Si le player ne gagne pas on relance l'algorithme de recherche de gagnant
                if (!victory)
                    victory = researchWinner(nextNeighborCells);
            }
        }

        return victory;
    }

    public int nbSeries(int i, int j) {
        Cell c = grid.getCell(i, j);
        ArrayList<Cell> n = researchNeighborCells(c);
        int nb_series = 0;
        for (Cell nc : n) {

        }
        return nb_series;
    }

    public ArrayList<Cell> researchNeighborCells(Cell cellForResearch) {

        ArrayList<Cell> neighborCells = new ArrayList<>(); // Arraylist qui va stocker les cellules voisines si elles existes
        int i = cellForResearch.getX();
        int j = cellForResearch.getY();
        Color color = cellForResearch.getColor();

        // On créer 6 celules qui correspondent aux 6 cellules voisines d'une cellule
        Cell c1 = grid.getCell(i-1,j);
        Cell c2 = grid.getCell(i-1,j+1);
        Cell c3 = grid.getCell(i,j+1);
        Cell c4 = grid.getCell(i+1,j);
        Cell c5 = grid.getCell(i+1,j-1);
        Cell c6 = grid.getCell(i,j-1);

        // En revanche pour les cellules des coins et du bord il n'y a pas 6 cellules voisines mais moins, c'est pourquoi on test si elles sont null.
        // Si elles le sont c'est qu'il n'existe pas de cellule voisines avec les coordonées reseignéé.

        if (c1 != null && !grid.getPast(c1) && c1.getColor() == color) {
            neighborCells.add(c1);
            grid.setPast(c1);
        }

        if (c2 != null && !grid.getPast(c2) && c2.getColor() == color) {
            neighborCells.add(c2);
            grid.setPast(c2);
        }

        if (c3 != null && !grid.getPast(c3) && c3.getColor() == color) {
            neighborCells.add(c3);
            grid.setPast(c3);
        }

        if (c4 != null && !grid.getPast(c4) && c4.getColor() == color) {
            neighborCells.add(c4);
            grid.setPast(c4);
        }

        if (c5 != null && !grid.getPast(c5) && c5.getColor() == color) {
            neighborCells.add(c5);
            grid.setPast(c5);
        }

        if (c6 != null && !grid.getPast(c6) && c6.getColor() == color) {
            neighborCells.add(c6);
            grid.setPast(c6);
        }

        return neighborCells;
    }
}
