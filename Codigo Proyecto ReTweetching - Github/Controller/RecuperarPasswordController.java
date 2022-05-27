package Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;
import Model.Usuario;
import Recursos.Comodin;
import Recursos.EnviaMail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class RecuperarPasswordController implements Initializable {
	
	//---------------------------------------------------> Variables <--------------------------------------------------------------------------------------------------------
	
	@FXML
	TextField campoEmail;
	
	@FXML
	Button botonRecuperarPassword, botonCancelar;
	
	private Usuario recuperaPassword = new Usuario();
	
	private ResultSet resultado;
	
	Comodin cmd = new Comodin();
	
	//---------------------------------------------------> Método para recuperar la contraseña <-----------------------------------------------------------------------------
	
	public void recuperarPassword(ActionEvent event) {
		
		//Compruebo que el campo Email no está vacío
		
		if(campoEmail.getText().isEmpty()) {
			
			campoEmail.setStyle("-fx-control-inner-background: #ef9a9a");

			cmd.ventanaAviso("Aviso", "Introducir Email", "Para recuperar la contraseña debe introducir una dirección de Email válida");
			
		}else {
		
			//Si se ha introducido un mail, compruebo si existe en la base de datos
			
			resultado = recuperaPassword.comprobarEmail(campoEmail.getText());
		
			//Si el resultset está vacío, los datos introducidos son incorrectos o no existen.  En caso contrario el login es correcto
			
			try {
				
				if(!resultado.next()) {
					
					cmd.ventanaAviso("Aviso", "Email Incorrecto", "Debe introducir un Email válido");
					
					campoEmail.setStyle("-fx-control-inner-background:  #f6fc38");
					
					System.out.println("Los datos introducidos son incorrectos");
					
				}else {
					
					//El mail es correcto
						
					//Genero un código de recuperación
						
					char[] caracteres = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();	//secuencia de caracteres
					
					int longitud = caracteres.length;											//longitud del array
					
					Random codigo = new Random();												//instancio la clase Random
					
					StringBuffer buffer = new StringBuffer();									//creo un buffer
					
					for(int i=0; i<10; i++) {													//se escogen 10 caracteres
						
						buffer.append(caracteres[codigo.nextInt(longitud)]);					//añado los caracteres al array
					}
					
					String codigoRecuperacion = buffer.toString();								//guardo el codigo en una variable
					
					System.out.println(codigoRecuperacion);
						
					//Guardo el codigo de recuperacion en la tabla recupera_password
						
					recuperaPassword.guardarCodigoRecuperacion(codigoRecuperacion, campoEmail.getText());
						
					//Envío un mail al usuario con el codigo de recuperacion
					
					EnviaMail em = new EnviaMail();
					
					em.EnviaMailTLS(campoEmail.getText(), 
							"Código recuperación cuenta TweetChing", 
							"Este es su códido de recuperación" + "\n\n" + codigoRecuperacion + "\n El equipo de Retweetching");
					
					
					//cmd.ventanaConfirmación("Confirmacion", "Código enviado", "Se ha enviado un código de recuperación a su email");
					
					//Si el mail se ha enviado, abro la ventana NuevoPassword
					
					cmd.crearNuevaVista("NuevoPassword");
					
					//Cierro la ventana actual
					
					cmd.cerrarVentanaActual(event);
						
				}
								
			}catch(Exception e) {
					
				//Si no se envía el mail, lanzo ventana error y vuelvo a la pantalla recuperar password
				
				//cmd.ventanaError("Error", "Código no enviado", "No se ha podido enviar el email.  Vuelva intentarlo");
				
				cmd.crearNuevaVista("RecuperarPassword");
				
				//cierro la ventana actual
				
				cmd.cerrarVentanaActual(event);
					
				e.printStackTrace();
			}
			
		}
			
	}
	
	//---------------------------------------------------> Método para volver a la pantalla de Login <------------------------------------------------------------------------
	
	public void volverMenuLogin(ActionEvent event) {
		
		cmd.crearNuevaVista("Login");
		
		//Cierro la ventana actual
		
		cmd.cerrarVentanaActual(event);	
		
	}
	
	//---------------------------------------------------> Initialize <------------------------------------------------------------------------------------------------------
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		botonRecuperarPassword.setOnAction(this::recuperarPassword);
		
		botonCancelar.setOnAction(this::volverMenuLogin);
				
	}
	
}
