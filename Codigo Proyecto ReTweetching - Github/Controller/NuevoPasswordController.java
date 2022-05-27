package Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.apache.commons.codec.digest.DigestUtils;
import Model.Usuario;
import Recursos.Comodin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;


public class NuevoPasswordController implements Initializable {
	
	@FXML
	TextField campoCodigoRecuperacion, campoEmail, TFNuevaContrasena, TFRepiteNuevaContrasena;
	
	@FXML
	PasswordField campoNuevaContrasena, campoRepiteNuevaContrasena;
	
	@FXML
	Button botonActualizar, botonCancelar;
	
	@FXML
	CheckBox verPassword;
	
	Usuario nuevoPassword = new Usuario(); 
	
	Comodin cmd = new Comodin();
	
	private ResultSet resultado;
	
	//---------------------------------------------------> Tooltips de ayuda <-------------------------------------------------------------------------------------------------
	
	public void ayudaCampos(MouseEvent event) {
		
		campoCodigoRecuperacion.setTooltip(new Tooltip("Debes introducir tu nombre"));
		
		campoEmail.setTooltip(new Tooltip("Debes introducir tu apellido"));
		
		campoNuevaContrasena.setTooltip(new Tooltip("Debes introducir un password válido: entre 8 y 20 caracteres, mínimo una minúscula, "
				+ "una mayúscula y un carácter especial (!@#$%&+()-+=^) y no debe contener ningún espacio en blanco"));
		
		campoRepiteNuevaContrasena.setTooltip(new Tooltip("Introduce de nuevo la contraseña"));
		
	}
	
	//---------------------------------------------------> Método que crea la nueva contraseña <--------------------------------------------------------------------------------
	
	public void nuevoPassword(ActionEvent event) {
		
		String codigoRec = campoCodigoRecuperacion.getText();
		
		String email = campoEmail.getText();
		
		String password1 = campoNuevaContrasena.getText();
		
		String password2 = campoRepiteNuevaContrasena.getText();
		
		String passwordEnc = DigestUtils.md5Hex(password1);  //Encripto el password
		
		boolean camposVacios = true;
		
		boolean camposValidados = true;

		// Validaciones -> revisar que los campos se han rellenado 
		
		if(codigoRec.isEmpty() || password1.isEmpty() || password2.isEmpty()){
			
			if(codigoRec.isEmpty()) {
				
				campoCodigoRecuperacion.setStyle("-fx-control-inner-background: #ef9a9a");
				
				camposVacios = false;
				
			}
			
			if(email.isEmpty()) {
				
				campoEmail.setStyle("-fx-control-inner-background: #ef9a9a");
				
				camposVacios = false;
				
			}
			
			if(password1.isEmpty()) {
				
				campoNuevaContrasena.setStyle("-fx-control-inner-background: #ef9a9a");
				
				TFNuevaContrasena.setStyle("-fx-control-inner-background: #ef9a9a");
				
				camposVacios = false;
				
			}
			
			if(password2.isEmpty()) {
				
				campoRepiteNuevaContrasena.setStyle("-fx-control-inner-background: #ef9a9a");
				
				TFRepiteNuevaContrasena.setStyle("-fx-control-inner-background: #ef9a9a");
				
				camposVacios = false;
				
			}
			
			cmd.ventanaAviso("Aviso", "Deben rellenarse todos los campos", "Revisa los campos marcados.");
			
		}
		
		// Validaciones sobre contraseñas y email
		
		if(!codigoRec.isEmpty() && !password1.isEmpty() && password2.isEmpty() || !camposValidados) {
			
			//Compruebo que el código introducido existe en la BBDD
			
			if(!codigoRec.isEmpty()) {
				
				resultado = nuevoPassword.comprobarCodigoRecuperacion(codigoRec);
				
				try {
					
					if(!resultado.next()) {
						
						cmd.ventanaError("Error", "Codigo de recuperación incorrecto", "Debe introducir el código recibido por correo electrónico");
						
						camposValidados = false;
						
					}
				}catch(SQLException e) {
					
					e.printStackTrace();
					
				}
			}
			
			if(!password1.equals(password2)) {
				
				//Creo una alerta que avisa que las contraseñas no coinciden
				
				cmd.ventanaAviso("Aviso", "Las contraseñas introducidas deben ser iguales", "Compruebe las contraseñas introducidas");
				
				camposValidados = false;
				
				}
		}
			
		if(!password1.isEmpty() && !password2.isEmpty() || !camposValidados){
			
			if(nuevoPassword.validarPassword(password1)==false) {
				
				 //Coloreo los textfield de password y confirma password y lanzo una ventana de aviso
				
				 campoNuevaContrasena.setStyle("-fx-control-inner-background: #f6fc38");
				 
				 TFNuevaContrasena.setStyle("-fx-control-inner-background: #f6fc38");
				 
				 campoRepiteNuevaContrasena.setStyle("-fx-control-inner-background: #f6fc38");
				 
				 TFRepiteNuevaContrasena.setStyle("-fx-control-inner-background: #f6fc38");
				 
				 cmd.ventanaAviso("Aviso", "Password incorrecto","El password debe tener entre 8 y 20 caracteres y al menos un dígito, "
						+ "una minúscula,una mayúscula y un carácter especial (!@#$%&+()-+=^) y no debe contener ningún espacio en blanco");
				 
				 camposValidados = false;
				 
			}
			
		}
		
		if(!email.isEmpty() || !camposValidados) {
			
			resultado = nuevoPassword.comprobarEmail(email);
			
			try {
				
				if(!resultado.next()) {
					
					cmd.ventanaError("Error", "Email Incorrecto", "El email introducido no pertenece a ningún usuario registrado");
					
					//System.out.println("Los datos introducidos son incorrectos");
					
					camposValidados = false;
					
					resultado.close();
				
				}
					
			}catch(SQLException e) {
					
				e.printStackTrace();
			
			}	
				
		}

		// Actualizar tabla usuarios 
		
		if(camposVacios == true && camposValidados == true ) {
			
			/*Primero compruebo que el Email y el código introducido pertenecen al mismo usuario y que el usuario y el mail tienen su correspondencia 
			 * en la tabla Usuarios y guardo el Id en la variable Id
			 */
			
			resultado = nuevoPassword.comprobacionAntesActualizacion(campoEmail.getText(), campoCodigoRecuperacion.getText());
			
			try {
				
				if(!resultado.next()) {
					
					cmd.ventanaError("Error", "Los datos introducidos no coinciden", "Debe introducir su email y el código de recuperación recibido");
					
				}else {
					
					//Guardo el Id obtenido en la consulta
					
					String Id = resultado.getString(1);
					
					//Actulizado la contraseña en la tabla Usuarios, utilizando el password encriptados
					
					//Faltaría añadir un condicional?? y falta encriptar el password en el metodo en clase usuario
					
					if(nuevoPassword.actualizarPassword(passwordEnc, email, Id) == true) {
							
					//Si el login es correcto, abro la ventana del menú principal
							
					cmd.crearNuevaVista("Login");
					
					//Cierro la ventana actual
					
					cmd.cerrarVentanaActual(event);
					
					//Abro una ventana de confirmación
					
					cmd.ventanaConfirmación("Confirmación", "Contraseña actualizada", "La contraseña ha sido actualizada.  Puedes iniciar el login");	
					
					}else {
						
						cmd.ventanaError("Error", "Error al actulizar la contraseña", "Contraseña no actualizada.  Inténtelo en unos minutos");
						
						cmd.crearNuevaVista("RecuperarPassword");
					}
					
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				//Si se produce un error vuelvo a abrir la ventana NuevoPassword y lanzo un aviso de error
				
				cmd.ventanaError("Error", "Se producido un error", "Intente de nuevo modificar su contraseña en unos minutos");
				
				e.printStackTrace();
			}				
		
		}
		
	}
	
	//---------------------------------------------------> Método que se ejecuta al pulsar el botón Cancelar -> abre ventana de login <-----------------------------------------
	
	public void cancelar(ActionEvent event) {
			
		cmd.crearNuevaVista("Login");
		
		//Cierro la ventana actual
		
		cmd.cerrarVentanaActual(event);
			
	}

	//---------------------------------------------------> Método que muestra / oculta las contraseñas <-------------------------------------------------------------------------
	
	public void muestraPassword(ActionEvent event) {
		
		//Escribo las contraseñas introducidas en los TextField que hay debajo de PasswordField
		
		TFNuevaContrasena.setText(campoNuevaContrasena.getText());
		
		TFRepiteNuevaContrasena.setText(campoRepiteNuevaContrasena.getText());
		
		if(verPassword.isSelected()==false) {
			
			//No se ve la contraseña
			
			TFNuevaContrasena.setVisible(false);
			
			campoNuevaContrasena.setVisible(true);
			
			campoRepiteNuevaContrasena.setVisible(true);
			
			TFRepiteNuevaContrasena.setVisible(false);
			
			verPassword.setText("Ver contraseñas");
				
		}else {
			
			//Se ve la contraseña
			
			TFNuevaContrasena.setVisible(true);
			
			TFRepiteNuevaContrasena.setVisible(true);
			
			campoNuevaContrasena.setVisible(false);
			
			campoRepiteNuevaContrasena.setVisible(false);
			
			verPassword.setText("Ocultar contraseñas");
			
		}
		
	}
	
	//---------------------------------------------------> Método Initialize <--------------------------------------------------------------------------------------------------

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		TFNuevaContrasena.setVisible(false);
		
		TFRepiteNuevaContrasena.setVisible(false);
		
		botonActualizar.setOnAction(this::nuevoPassword);
		
		botonCancelar.setOnAction(this::cancelar);
		
		verPassword.setOnAction(this::muestraPassword);
		
	}

}
