/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import it.polito.tdp.food.model.Condiment;
import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="boxIngrediente"
    private ComboBox<Condiment> boxIngrediente; // Value injected by FXMLLoader

    @FXML // fx:id="btnDietaEquilibrata"
    private Button btnDietaEquilibrata; // Value injected by FXMLLoader
    
    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaDieta(ActionEvent event) {
    	this.txtResult.clear();
    	Condiment c;
		if((c = this.boxIngrediente.getValue())==null) {
			this.txtResult.appendText("Devi selezionare un ingrediente!\n");
    		return;
		}
		this.txtResult.appendText(" *** DIETA EQUILIBRATA che comprende "+c+" ***\n");
		for(Condiment cond : model.dietaEquilibrata(c)) {
			this.txtResult.appendText(cond.toString()+"\n");
		}
		
		this.txtResult.appendText("Calorie dieta: "+model.calorieDietaBest());

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Integer calorie;
    	
    	if(this.txtCalorie.getText().isEmpty()) {
    		this.txtResult.appendText("Devi inserire una quantità di calorie!\n");
    		return;
    	}
    	
    	calorie  = Integer.parseInt(this.txtCalorie.getText());
    	
    	if(calorie<0) {
    		this.txtResult.appendText("Devi inserire una quantità di calorie >0!\n");
    		return;
    	}

    	model.creaGrafo(calorie);
    	this.txtResult.appendText("GRAFO CREATO!\nVERTCI: "+model.numVertici()+
				"\nARCHI: "+model.numArchi()+"\n");
    	for(Condiment c : model.listIngredientiOrdinatiPerCalorieDecr()) {
    		this.txtResult.appendText("\nINGREDIENTE: "+c.getDisplay_name()+
					"\nCalorie: "+model.getCalorieIngrediente(c.getFood_code())+
					"\n# cibi che lo contengono: "+model.getNumeroCibiIngrediente(c.getFood_code())+
					"\n");
		}
    	


    	this.boxIngrediente.getItems().clear();
    	this.boxIngrediente.getItems().addAll(model.getCondimentTraSelezionati());

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxIngrediente != null : "fx:id=\"boxIngrediente\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnDietaEquilibrata != null : "fx:id=\"btnDietaEquilibrata\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
