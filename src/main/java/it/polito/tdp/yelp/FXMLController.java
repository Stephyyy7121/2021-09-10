/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.BusinessDistance;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<Business> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<Business> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    private boolean creatoGrafo = false;
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	String city = this.cmbCitta.getValue();
    	
    	if (city == null) {
    		txtResult.appendText("Scegliere una citta'.\n");
    	}
    	
    	this.model.creaGrafo(city);
    	this.creatoGrafo = true;
    	txtResult.appendText("Grafo creato!\n#Vertici: " + this.model.getVertici().size()+"\n#Archi: " + this.model.numArchi()+"\n");
    	this.cmbB1.getItems().addAll(this.model.getVertici());
    	this.cmbB2.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {
    	
    	txtResult.clear();
    	if (!creatoGrafo) {
    		this.txtResult.appendText("Non e' stato creato un grafo\n");
    	}
    	
    	Business b1 = this.cmbB1.getValue();
    	if (b1 == null) {
    		txtResult.appendText("Scegliere un locale commerciale\n");
    	}
    	
    	BusinessDistance b = this.model.getLocaleDistante(b1);
    	txtResult.appendText("LOCALE PIU' DISTANTE : " + b.toString());
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {

    	txtResult.clear();
    	if (!creatoGrafo) {
    		this.txtResult.appendText("Non e' stato creato un grafo\n");
    	}
    	
    	Business b1 = this.cmbB1.getValue();
    	if (b1 == null) {
    		txtResult.appendText("Scegliere un locale commerciale b1\n");
    	}
    	
    	Business b2 = this.cmbB2.getValue();
    	if (b2 == null) {
    		txtResult.appendText("Scegliere un locale commerciale b2\n");
    	}
    	
    	String input = this.txtX2.getText();
    	if (input.compareTo("")==0) {
    		txtResult.appendText("Inserire valore di soglia.\n");
    	}
    	
    	double x = 0.0;
    	try {
    		x = Double.parseDouble(input);
    	}catch (NumberFormatException e ) {
    		txtResult.appendText("Inserito valore non valido.\n");
    		return;
    	}
    	
    	List<Business> tour = this.model.getTour(b1, b2, x);
    	double km = this.model.kmPercorsi(tour);
    	
    	for (Business b : tour) {
    		txtResult.appendText(b.toString()+"\n");
    	}
    	txtResult.appendText("Totale KM percorsi e' pari a " + km +"\n");
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbCitta.getItems().addAll(this.model.getCity());
    }
}
