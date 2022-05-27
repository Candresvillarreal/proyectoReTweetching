package Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import Model.Resultados;
import Recursos.Comodin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import twitter4j.Status;


public class MenuConsultaController implements Initializable{
	
	@FXML
	TextField campoBusqueda;
	
	@FXML
	Button botonVerResultados, botonVerMapa, botonSalir, botonAlmacenBusquedas;
	
	Comodin cmd = new Comodin();
	
	Resultados results = new Resultados();
	
	static String consulta;
	
	List<Status> resultados;
	
	static List<Status> tweets;
	
	//---------------------------------------------------> Constructor por defecto <-------------------------------------------------------------------------------------------
	
	public MenuConsultaController() {
		
	}
	
	//---------------------------------------------------> M�todo que se ejecuta al pulsar sobre el bot�n Ver Resultados -> abro Resultados.fxml <------------------------------
	
	public void abrirResultados(ActionEvent event) {
		
		//Guardo la palabra consultada
		
		consulta = campoBusqueda.getText();
		
		//Ejecuto la b�squeda
		
		if(!consulta.isEmpty()) {
		
		resultados = results.busquedaTwitter(consulta);
		
		tweets = resultados;
	
		//Abro Resultados.fxml
	
		cmd.crearNuevaVista("Resultados");
		
		//Cierro la vista actual
		
		cmd.cerrarVentanaActual(event);
		
		}else {
			
			cmd.ventanaAviso("Aviso", "Introduce qu� quieres buscar", "Debes introducir una palabra para realizar la b�squeda");
		}
	
	}
	
	//---------------------------------------------------> M�todo que se ejecuta al pulsar sobre el bot�n Almac�n B�squedas -> abro ResultadosGuardados.fxml <--------------------
	
	public void abrirAlmacenBusquedas(ActionEvent event) {
		
		//Abro ResultadosGuardados.fxml
		
		cmd.crearNuevaVista("ResultadosGuardados");
		
		//Cierro la vista actual
		
		cmd.cerrarVentanaActual(event);
	}
	
	//---------------------------------------------------> M�todo que se ejecuta al pulsar sobre el bot�n Salir <--------------------------------------------------------------
	
	public void cerrarMenu(ActionEvent event) {
		
		//Cierro la aplicaci�n
		
		cmd.ventanaInformacion("Informaci�n", "Saliendo de Retwwetching", "�Muchas gracias " + LoginController.nombre + " " 
		+ LoginController.apellido + " por utilizar Retweetching");
		
		cmd.cerrarVentanaActual(event);
		
	}
	
	//---------------------------------------------------> M�todo Initialize <--------------------------------------------------------------------------------------------------

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		botonVerResultados.setOnAction(this::abrirResultados);
		
		botonSalir.setOnAction(this::cerrarMenu);
		
		botonAlmacenBusquedas.setOnAction(this::abrirAlmacenBusquedas);
		
	}

}
