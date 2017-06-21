package bast.hex.Model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class Grid extends ArrayList<Cell>{

    private int nbLines;
    private int nbColumns;
    private State states[] = new State[2];

    public State getRedState() {
        return states[1];
    }

    public State getBlueState() {
        return states[0];
    }

    public Grid(int nbLines) {
        this.nbLines = nbLines;
        this.nbColumns = nbLines;
        states[0] = new State();
        states[1] = new State();
    }
    public void buildGrid() {
        Cell c;
        for (int i = 0; i < nbLines; i++) {
            for (int j = 0; j < nbColumns; j++) {
                if((i != 0 && j != nbColumns -1) || (i != nbLines -1 && j != 0 )) {
                    if (i == 0)
                        c = new Cell(i,j, Color.BLUE,true);
                    else if (i == nbLines - 1)
                        c = new Cell(i,j,Color.BLUE,true);
                    else if (j == 0)
                        c = new Cell(i,j,Color.RED,true);
                    else if (j == nbColumns - 1)
                        c = new Cell(i,j, Color.RED,true);
                    else
                        c = new Cell(i,j, Color.WHITE,false);

                    add(c);
                }
            }
        }
    }

    public int getNbLines(){
        return nbLines;
    }

    public int getNbColumns(){
        return nbColumns;
    }

    public Cell getCell(int i, int j) {
        Cell cell = null;
        for (Cell c : this){
            if (c.getX() == i && c.getY() == j) {
                cell = c;
            }
        }
        return cell;
    }

    public void setPast(Cell cell) {
        cell.setPast(true);
    }

    public boolean getPast(Cell cell) {
        return cell.getPast();
    }

    public void resetPast() {
        for (Cell c : this)
            c.setPast(false);
    }

    public void play(Cell c, Color color){
        c.setColor(color);
        if(color == Color.blue){
            getBlueState().add(c);
        } else {
            getRedState().add(c);
        }
    }

    public void cancel(Cell c){
        if(c.getColor() == Color.blue){
            getBlueState().remove(c);
        } else {
            getRedState().remove(c);
        }
        c.setColor(Color.WHITE);
    }
}
