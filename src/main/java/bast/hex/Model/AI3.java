package bast.hex.Model;

import java.awt.*;

public class AI3 implements IAI {

    private Color cWinner = Color.WHITE;

    @Override
    public Cell play(HexModel model) {
        int val, maxVal = -Integer.MAX_VALUE;
        Cell coupActuel = null, meilleurCoup = null;
        for(Cell c : model.grid) {
            if (c.getColor() == Color.WHITE && !c.getBorder()) {
                coupActuel = c;
                model.grid.play(c, model.getPlayer());
                val = min(model, 4);
                if (val > maxVal) {
                    maxVal = val;
                    meilleurCoup = coupActuel;
                }
                model.grid.cancel(c);
            }
        }
        return meilleurCoup != null ? meilleurCoup : coupActuel;
    }

    public int min(HexModel model, int profondeur) {
        if (profondeur <= 2 || winner(model) != Color.WHITE) {
            return eval(model);
        }
        int val, minVal = Integer.MAX_VALUE;
        for(Cell c : model.grid) {
            if (c.getColor() == Color.WHITE && !c.getBorder()){
                model.grid.play(c, model.getPlayer());
                val = max(model, profondeur-1);
                if (val < minVal) {
                    minVal = val;
                }
                model.grid.cancel(c);
            }
        }
        return minVal;
    }

    public int max(HexModel model, int profondeur) {
        if (profondeur <= 2 || winner(model) != Color.WHITE) {
            return eval(model);
        }
        int val, maxVal = -Integer.MAX_VALUE;
        for(Cell c : model.grid) {
            if (c.getColor() == Color.WHITE && !c.getBorder()){
                model.grid.play(c, model.getPlayer());
                val = min(model, profondeur-1);
                if (val > maxVal) {
                    maxVal = val;
                }
                model.grid.cancel(c);
            }
        }
        return maxVal;
    }

    public int nbSeries(HexModel model, Color color) {
        if (color == Color.BLUE) {
            return model.grid.getBlueState().nbSeries(model);
        }
        return model.grid.getRedState().nbSeries(model);
    }

    public int eval(HexModel model) {
        Color winner = winner(model);
        if (winner != Color.WHITE) {
            cWinner = Color.WHITE;
            int nbCoups = model.grid.getBlueState().size()+model.grid.getRedState().size();
            if (winner == model.getPlayer()) {
                return 1000-nbCoups;
            }
            return -1000+nbCoups;
        }
        return nbSeries(model, model.getPlayer())-nbSeries(model, model.getPlayer() == Color.BLUE ? Color.RED : Color.BLUE);
    }

    public Color winner(HexModel model) {
        if (cWinner != Color.WHITE) {
            return cWinner;
        }
        if (model.getPlayer() == Color.BLUE) {
            if (model.grid.getBlueState().win(model, Color.BLUE)) {
                return cWinner = Color.BLUE;
            }
        }
        else if (model.grid.getRedState().win(model, Color.RED)) {
            return cWinner = Color.RED;
        }
        return Color.WHITE;
    }
}
