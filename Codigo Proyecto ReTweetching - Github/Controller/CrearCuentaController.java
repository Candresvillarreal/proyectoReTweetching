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

public class CrearCuentaController implements Initializable {
	
	//---------------------------------------------------> Variables <----------------------------------------------------------------------------------------------------------
	
	
	@FXML
	TextField campoNombre, campoApellido, campoEmail, campoUsuario, TFpassword, TFconfirmaPassword;
	
	@FXML
	PasswordField campoPassword, campoConfirmaPassword;
	
	@FXML
	Button botonCrearCuenta, botonCancelar, mostrarOcultarPassword;
	
	@FXML
	CheckBox verPassword;
	
	private Usuario crearCuenta = new Usuario();
	
	private ResultSet resultado;
	
	Comodin cmd = new Comodin();
	
	//--------------------------------------------------->Tooltips de ayuda <---------------------------------------------------------------------------------------------------
	
	public void ayudaCampos(MouseEvent event) {
		
		campoNombre.setTooltip(new Tooltip("Debes introducir tu nombre"));
		
		campoApellido.setTooltip(new Tooltip("Debes introducir tu apellido"));
		
		campoPassword.setTooltip(new Tooltip("Debes introducir un password válido: entre 8 y 20 caracteres, mínimo una minúscula, "
				+ "una mayúscula y un carácter especial (!@#$%&+()-+=^) y no debe contener ningún espacio en blanco"));
		
		campoConfirmaPassword.setTooltip(new Tooltip("Introduce de nuevo la contraseña"));
		
		campoEmail.setTooltip(new Tooltip("Debes introducir una cuenta válida de correo electrónico"));
		
		campoUsuario.setTooltip(new Tooltip("Debes introducir un nombre de usuario"));
		
	}
	
	//--------------------------------------------------->Método que crea una nueva cuenta <------------------------------------------------------------------------------------
	
	public void crearCuenta(ActionEvent event) {
		
		String nombre = campoNombre.getText();
		
		String apellido = campoApellido.getText();
		
		String password1 = campoPassword.getText();
		
		String password1Enc = DigestUtils.md5Hex(password1);	//Encripto el password
		
		String password2 = campoConfirmaPassword.getText();
		
		String email = campoEmail.getText();
		
		String usuario = campoUsuario.getText();
		
		boolean camposCumplimentadosValidados = true;	//Variable que confirma que todas los campos están validados
		
		boolean camposValidados = true;					// Variable que confirma que todos los campos que lo requieren han sido validados
		
		//Compruebo si uno o varios campos están vacíos
		
		if(nombre.isEmpty() || apellido.isEmpty() || password1.isEmpty()|| password2.isEmpty() || email.isEmpty() || usuario.isEmpty() || !camposCumplimentadosValidados) {
			
			//Si algún campo está vacío, le cambio el color de fondo
			
			if(nombre.isEmpty()) {
				
				campoNombre.setStyle("-fx-control-inner-background: #ef9a9a");
				
				camposCumplimentadosValidados = false;
				
			}else {
				
				campoNombre.setStyle("-fx-control-inner-background: #ffffff");

			}
			
			if(apellido.isEmpty()) {
				
				campoApellido.setStyle("-fx-control-inner-background: #ef9a9a");
				
				camposCumplimentadosValidados = false;

			}else {
				
				campoApellido.setStyle("-fx-control-inner-background: #ffffff");

			}
			
			if(password1.isEmpty()) {
				
				campoPassword.setStyle("-fx-control-inner-background: #ef9a9a");
				
				TFpassword.setStyle("-fx-control-inner-background: #ef9a9a");
				
				camposCumplimentadosValidados = false;

			}else {
				
				campoPassword.setStyle("-fx-control-inner-background: #ffffff");
				
				TFpassword.setStyle("-fx-control-inner-background: #ffffff");

				
			}
			
			if(password2.isEmpty()) {
				
				campoConfirmaPassword.setStyle("-fx-control-inner-background: #ef9a9a");
				
				TFconfirmaPassword.setStyle("-fx-control-inner-background: #ef9a9a");
				
				camposCumplimentadosValidados = false;

			}else {
				
				campoConfirmaPassword.setStyle("-fx-control-inner-background: #ffffff");
				
				TFconfirmaPassword.setStyle("-fx-control-inner-background: #ffffff");

			}
			
			if(email.isEmpty()) {
				
				campoEmail.setStyle("-fx-control-inner-background: #ef9a9a");
				
				camposCumplimentadosValidados = false;
				
			}else {
				
				campoEmail.setStyle("-fx-control-inner-background: #ffffff");

				
			}
			
			if(usuario.isEmpty()) {
				
				campoUsuario.setStyle("-fx-control-inner-background: #ef9a9a");
				
				camposCumplimentadosValidados = false;
				
			}else {
				
				campoUsuario.setStyle("-fx-control-inner-background: #ffffff");
				
			}
			
			
			//Creo una alerta que avisa que deben rellenarse todos los campos
			
			cmd.ventanaAviso("Aviso", "Hay campos vacíos en el formulario", "Para crear la cuenta, debe rellenar todos los campos del formulario. Revise los campos marcados");	
			
			
		
		}
		
		if(camposCumplimentadosValidados == true) {
			
			campoNombre.setStyle("-fx-control-inner-background: #ffffff");
			
			campoApellido.setStyle("-fx-control-inner-background: #ffffff");
			
			campoPassword.setStyle("-fx-control-inner-background: #ffffff");
			
			TFpassword.setStyle("-fx-control-inner-background: #ffffff");
			
			campoConfirmaPassword.setStyle("-fx-control-inner-background: #ffffff");
			
			TFconfirmaPassword.setStyle("-fx-control-inner-background: #ffffff");
			
			campoEmail.setStyle("-fx-control-inner-background: #ffffff");
			
			campoUsuario.setStyle("-fx-control-inner-background: #ffffff");
			
		}
		
		
		//Validaciones sobre los campos contraseña, email y usuario
		
		if(!nombre.isEmpty() && !apellido.isEmpty() && !password1.isEmpty() && !password2.isEmpty() && !email.isEmpty() && !usuario.isEmpty() || !camposValidados){
			
			if(!password1.equals(password2)) {
				
				campoPassword.setStyle("-fx-control-inner-background: #f6fc38");
					 
				TFpassword.setStyle("-fx-control-inner-background: #f6fc38");
					 
				campoConfirmaPassword.setStyle("-fx-control-inner-background: #f6fc38");
					 
				TFconfirmaPassword.setStyle("-fx-control-inner-background: #f6fc38");
				
				//Creo una alerta que avisa que las contraseñas no coinciden
				
				cmd.ventanaAviso("Aviso", "Las contraseñas introducidas deben ser iguales", "Compruebe las contraseñas introducidas");
				
				camposValidados = false;
			
			}else {
				
				campoPassword.setStyle("-fx-control-inner-background: #ffffff");
				 
				TFpassword.setStyle("-fx-control-inner-background: #ffffff");
					 
				campoConfirmaPassword.setStyle("-fx-control-inner-background: #ffffff");
					 
				TFconfirmaPassword.setStyle("-fx-control-inner-background: #ffffff");
	
			}
			
			if(!password1.isEmpty() && !password2.isEmpty()){
				
				/*Compruebo que las contraseñas introducidas tienen entre 8 y 20 caracteres, al menos un dígito, al menos una minúscula y una
				 *mayúscula, al menos un carácter especial (!@#$%&+()-+=^) y no contiene ningún espacio en blanco 
				 */
				
				if(crearCuenta.validarPassword(password1)==false) {
					
					//Coloreo los textfield de password y confirma password y lanzo una ventana de aviso
					
					 campoPassword.setStyle("-fx-control-inner-background: #f6fc38");
					 
					 TFpassword.setStyle("-fx-control-inner-background: #f6fc38");
					 
					 campoConfirmaPassword.setStyle("-fx-control-inner-background: #f6fc38");
					 
					 TFconfirmaPassword.setStyle("-fx-control-inner-background: #f6fc38");
					 
					 cmd.ventanaAviso("Aviso", "Password incorrecto","El password debe tener entre 8 y 20 caracteres y al menos un dígito, "
							+ "una minúscula,una mayúscula y un carácter especial (!@#$%&+()-+=^) y no debe contener ningún espacio en blanco");
					 
					 camposValidados = false;
					 
				}else {
					
					campoPassword.setStyle("-fx-control-inner-background: #ffffff");
					 
					TFpassword.setStyle("-fx-control-inner-background: #ffffff");
					 
					campoConfirmaPassword.setStyle("-fx-control-inner-background: #ffffff");
					 
					TFconfirmaPassword.setStyle("-fx-control-inner-background: #ffffff");

				}
				
			}
			
			if(!email.isEmpty()) {
				
				//Compruebo que el mail introducido es una direccion válida
				
				if(crearCuenta.validaDireccionMail(email)==false) {
					
					//Coloreo los textfield de password y confirma password y lanzo una ventana de aviso
					
					campoEmail.setStyle("-fx-control-inner-background: #f6fc38");
					 
					cmd.ventanaAviso("Aviso", "Email incorrecto","Debe introducir una dirección válida de correo electrónico");
					 
					camposValidados = false;
					
				}else {
					
					campoEmail.setStyle("-fx-control-inner-background: #ffffff");
	
				}
				
				if(!email.isEmpty()) {
					
					//Compruebo que el mail introducido no existe en la BBDD
					
					resultado = crearCuenta.comprobarEmail(email);
					
					try {
						
						//Si existe un resultado, lanzo una ventana de aviso y coloreo el campo usuario
						
						if(resultado.next()) {
							
							campoEmail.setStyle("-fx-control-inner-background: #f6fc38");
							
							cmd.ventanaAviso("Aviso", "Email incorrecto","El Email introducido ya existe.  Inicie sesión con su usuario");
							
							camposValidados = false;
							
						}else {
							
							campoEmail.setStyle("-fx-control-inner-background: #ffffff");

						}
					
					}catch(SQLException e) {
						
						e.printStackTrace();
						
					}
				}
				
			}
			
			if(!usuario.isEmpty()){
				
				 //Compruebo que el campo de usuario no existe en la BBDD
	
				resultado = crearCuenta.comprobarUsuario(usuario);
				
				try {
					
					//Si existe un resultado, lanzo una ventana de aviso y coloreo el campo usuario
					
					if(resultado.next()) {
						
						campoUsuario.setStyle("-fx-control-inner-background: #f6fc38");
						
						cmd.ventanaAviso("Aviso", "El usuario introducido ya existe", "Introduzca un nombre de usuario distinto");	
						
						camposValidados = false;
						
					}else {
						
						campoUsuario.setStyle("-fx-control-inner-background: #ffffff");
			
					}
					
				}catch(SQLException e) {
					
					e.printStackTrace();
				}
				
			}
			
		}
				
		//Si todo está correcto, ejecuto el insert 
		
		if(camposCumplimentadosValidados==true && camposValidados == true) {
			
			if(crearCuenta.insertaUsuario(nombre, apellido,  email, password1Enc, usuario) == true) {
			
			cmd.ventanaConfirmación("Confirmación", "Datos guardados", "¡¡Bienvenido " + nombre + " " + apellido + " !! Tu cuenta ha sido creada.  Intenta acceder");
				
			//Abro la ventana de Login
			
			cmd.crearNuevaVista("Login");
			
			//Oculto la ventana incial de Login
			
			cmd.cerrarVentanaActual(event);
			
			}else {
				
				cmd.ventanaError("Error", "Se ha producido un error", "Intente crear la cuenta en unos minutos....");
				
				cmd.crearNuevaVista("CrearCuenta");
				
			}
							
		}
						
	}
	
	//---------------------------------------------------> Método que se ejecuta al pulsar el botón Cancelar -> abre ventana de login <-----------------------------------------
	
	public void cancelarCuenta(ActionEvent event) {
		
		//Vuelvo a la vista de Login
		
		cmd.crearNuevaVista("Login");
		
		//Oculto la ventana incial de Login
		
		cmd.cerrarVentanaActual(event);
				
	}
	
	//---------------------------------------------------> Método que muestra las contraseñas introducidas <--------------------------------------------------------------------
	
	public void muestraPassword(ActionEvent event) {
		
		//Escribo las contraseñas introducidas en los TextField que hay debajo de PasswordField
		
		
		TFpassword.setText(campoPassword.getText());
		
		TFconfirmaPassword.setText(campoConfirmaPassword.getText());
		
		if(verPassword.isSelected()==false) {
			
			//No se ve la contraseña
			
			TFpassword.setVisible(false);
			
			campoPassword.setVisible(true);
			
			campoConfirmaPassword.setVisible(true);
			
			TFconfirmaPassword.setVisible(false);
			
			verPassword.setText("Ver contraseñas");
				
		}else {
			
			//Se ve la contraseña
			
			TFpassword.setVisible(true);
			
			TFpassword.setEditable(true);
			
			TFconfirmaPassword.setVisible(true);
			
			campoPassword.setVisible(false);
			
			campoConfirmaPassword.setVisible(false);
			
			verPassword.setText("Ocultar contraseñas");
			
		}
		
	}
	
	//---------------------------------------------------> Initialize <--------------------------------------------------------------------------------------------------------

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		//Hago que los TextField que utilizo para mostrar las contraseñas, no sean visibles inicialmente
		
		TFpassword.setVisible(false);
		
		TFconfirmaPassword.setVisible(false);
		
		botonCrearCuenta.setOnAction(this::crearCuenta);
		
		botonCancelar.setOnAction(this::cancelarCuenta);
		
		verPassword.setOnAction(this::muestraPassword);

	}
	
}





			 
			
			
			
			
	
		
		
