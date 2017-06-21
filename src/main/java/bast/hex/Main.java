package bast.hex;

import bast.hex.Controller.AISecondController;
import bast.hex.Model.AI3;
import bast.hex.Model.HexModel;
import bast.hex.View.HexView;

//import bast.hex.Model.AI3;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class Main {

    private Main(){
        //Creation du model
        HexModel model = new HexModel();
        //Creation de la vue
        HexView view = new HexView(model,"HexGame - Abomnes - Bretheau - S3C");
        //Creation du controller
        AISecondController controller = new AISecondController(model,view, new AI3());
//        HexController controller = new HexController(model, view);
//        AIRController controller = new AIRController(model, view, new AI(model.grid.getNbLines(), 300), new AI3());
    }

    public static void main(String[] args) {
        new Main();
    }
}
