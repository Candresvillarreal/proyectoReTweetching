package Recursos;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Comodin {
	
	public Comodin() {
		
	}

	//-------------------------------------------------------------------> Métodos que crean Ventanas Emergentes <----------------------------------------------------------------------
	
	//Ventana Error
	
	public void ventanaError(String titulo, String cabecera, String contenido) {
		
		Alert nuevaAlerta = new Alert(AlertType.ERROR);
		
		nuevaAlerta.getDialogPane().setGraphic(new ImageView("/Recursos/LogoError48.jpg"));
		
		nuevaAlerta.setTitle(titulo);
		
		nuevaAlerta.setHeaderText(cabecera);

		nuevaAlerta.setContentText(contenido);
		
		nuevaAlerta.showAndWait();
		
	}
	
	//Ventana Confirmación
	
	public void ventanaConfirmación(String titulo, String cabecera, String contenido) {
		
		Alert nuevaAlerta = new Alert(AlertType.CONFIRMATION);
		
		nuevaAlerta.getDialogPane().setGraphic(new ImageView("/Recursos/LogoConfirmacion48.jpg"));

		nuevaAlerta.setTitle(titulo);
		
		nuevaAlerta.setHeaderText(cabecera);
		
		nuevaAlerta.setContentText(contenido);
		
		nuevaAlerta.showAndWait();
				
	}
	
	//Ventana Información
	
	public void ventanaInformacion(String titulo, String cabecera, String contenido) {
		
		Alert nuevaAlerta = new Alert(AlertType.INFORMATION);
		
		nuevaAlerta.getDialogPane().setGraphic(new ImageView("/Recursos/LogoInfo48.jpg"));
		
		nuevaAlerta.setTitle(titulo);
		
		nuevaAlerta.setHeaderText(cabecera);
		
		nuevaAlerta.setContentText(contenido);
		
		nuevaAlerta.showAndWait();
			
	}
	
	//Ventana Aviso
	
	public void ventanaAviso(String titulo, String cabecera, String contenido) {
		
		Alert nuevaAlerta = new Alert(AlertType.WARNING);
		
		nuevaAlerta.getDialogPane().setGraphic(new ImageView("/Recursos/LogoWarning48.jpg"));
		
		nuevaAlerta.setTitle(titulo);
		
		nuevaAlerta.setHeaderText(cabecera);
		
		nuevaAlerta.setContentText(contenido);
		
		nuevaAlerta.showAndWait();
			
	}	
	
	//-------------------------------------------------------------------> Método que crea nuevos Stages <------------------------------------------------------------------------------
	
	public void crearNuevaVista(String rutaVista) {
		
		try {
			
			//Creo un nuevo Stage para que el usuario rellene los datos necesarios para crear su cuenta
			
			Parent parent = FXMLLoader.load(getClass().getResource("/View/"+rutaVista+".fxml"));
			
			Stage stage = new Stage();
			
			stage.getIcons().add(new Image("/Recursos/LogoPrueba500x500FondoGris.jpg"));
			
			stage.setScene(new Scene(parent, 950, 600));
			
			stage.show();
			
		} catch (Exception e) {
			
			//Si se produce un error lanzo un aviso de error
			
			ventanaError("Error", "Se ha producido un error", "Vuelva a intentarlo en unos minutos...");
	
			e.printStackTrace();
			
		}
		
	}
	
	//-------------------------------------------------------------------> Método que cierra Stages <----------------------------------------------------------------------------------
	
	public void cerrarVentanaActual(ActionEvent event) {
		
		((Node)(event.getSource())).getScene().getWindow().hide();
		
	}

}
