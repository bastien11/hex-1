package bast.hex.Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class State extends HashSet<Cell> {

    public int nbSeries(HexModel model) {
        return nbSeries(new HashSet<>(), model);
    }

    private int nbSeries(HashSet<Cell> invalids, HexModel model) {
        int nbSeries = 0;
        for (Cell c : this) {
            if (!invalids.contains(c)) {
                List<Cell> neighbors = neighbors(invalids, model, c);
                if (neighbors.size() > 0) {
                    invalids.add(c);
                    ++nbSeries;
                    nbSeries += nbSeries(invalids, model);
                }
            }
        }
        return nbSeries;
    }

    public boolean win(HexModel model, Color color) {
        return win(new HashSet<>(), null, model, color);
    }

    private boolean win(HashSet<Cell> invalids, Cell start, HexModel model, Color color) {
        if (start == null) {
            if (color == Color.BLUE) {
                for (int i = 1; i <= model.grid.getNbColumns(); ++i) {
                    Cell c = model.grid.getCell(i, 1);
                    if (contains(c) && win(invalids, c, model, color)) {
                        return true;
                    }
                }
            } else {
                for (int j = 1; j <= model.grid.getNbLines(); ++j) {
                    Cell c = model.grid.getCell(1, j);
                    if (contains(c) && win(invalids, c, model, color)) {
                        return true;
                    }
                }
            }
            return false;
        }
        List<Cell> neighbors = neighbors(invalids, model, start);
        for (Cell c : neighbors) {
            if (color == Color.BLUE) {
                if (c.getY() >= model.grid.getNbLines()-2) {
                    return true;
                }
            } else {
                if (c.getX() >= model.grid.getNbColumns()-2) {
                    return true;
                }
            }
            invalids.add(c);
            if (win(invalids, c, model, color)) {
                return true;
            }
        }
        return false;
    }

    public List<Cell> neighbors(HexModel model, Cell c) {
        return neighbors(new HashSet<>(), model, c);
    }

    private List<Cell> neighbors(HashSet<Cell> invalids, HexModel model, Cell c) {
        int i = c.getX();
        int j = c.getY();
        Cell c1 = model.grid.getCell(i-1,j);
        Cell c2 = model.grid.getCell(i-1,j+1);
        Cell c3 = model.grid.getCell(i,j+1);
        Cell c4 = model.grid.getCell(i+1,j);
        Cell c5 = model.grid.getCell(i+1,j-1);
        Cell c6 = model.grid.getCell(i,j-1);
        List<Cell> state = new ArrayList<>();
        if (c1 != null && !c1.getBorder() && contains(c1) && !invalids.contains(c1)) {
            state.add(c1);
            invalids.add(c1);
        }
        if (c2 != null && !c2.getBorder() && contains(c2) && !invalids.contains(c2)) {
            state.add(c2);
            invalids.add(c2);
        }
        if (c3 != null && !c3.getBorder() && contains(c3) && !invalids.contains(c3)) {
            state.add(c3);
            invalids.add(c3);
        }
        if (c4 != null && !c4.getBorder() && contains(c4) && !invalids.contains(c4)) {
            state.add(c4);
            invalids.add(c4);
        }
        if (c5 != null && !c5.getBorder() && contains(c5) && !invalids.contains(c5)) {
            state.add(c5);
            invalids.add(c5);
        }
        if (c6 != null && !c6.getBorder() && contains(c6) && !invalids.contains(c6)) {
            state.add(c6);
            invalids.add(c6);
        }
        return state;
    }
}
