package Controller;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.apache.commons.codec.digest.DigestUtils;
import Model.ConexionBBDD;
import Model.Usuario;
import Recursos.Comodin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class LoginController implements Initializable {
	
	//-------------------------------------------------------------------> Variables <------------------------------------------------------------------------------------------
	
	@FXML
	TextField campoUsuario, passwordV;
	
	@FXML
	PasswordField passwordNV;
	
	@FXML
	Button botonEntrar, botonCrearCuenta;
	
	@FXML
	Hyperlink recuperar;
	
	@FXML
	CheckBox mostrarPassword;
	
	ConexionBBDD conexion = new ConexionBBDD();
	
	Usuario usuarioLogin = new Usuario();
	
	private ResultSet resultado;
	
	boolean campoValidado = true;
	
	boolean datosValidados = true;
	
	Comodin cmd = new Comodin();
	
	//---------------------------------------> variables estáticas para poder utilizarlas desde otras clases (MenuConsulta, Resultados...) <------------------------------------
	
	
	static String usuario;  
	
	static String nombre;
	
	static String apellido;
	
	public LoginController() {
		
	}
	
	//---------------------------------------> Tooltips de ayuda <--------------------------------------------------------------------------------------------------------------
	
	public void ayudaCampos(MouseEvent event) {
		
		campoUsuario.setTooltip(new Tooltip("Debes introducir tu Usuario"));
		
		passwordNV.setTooltip(new Tooltip("Debes introducir tu contraseña"));
		
	}
	
	//---------------------------------------> Método que realizan el Loging <--------------------------------------------------------------------------------------------------
	
	public void comprobarLogin(ActionEvent event) {
		
		usuario = campoUsuario.getText();
		
		String password = passwordNV.getText();
		
		String passwordEnc = DigestUtils.md5Hex(password); 	 	//Encripto la contraseña para poder compararla con la guardada en la BBDD
		
		/*
		 * ************************************************** Compruebo que se han introducido valores en las campos de usuario y password
		 * ************************************************** en caso contrario, coloreo los campos vacíos y lanzo un aviso
		 */
		
		if(usuario.isEmpty() || password.isEmpty() || !campoValidado) {
			
			if(usuario.isEmpty()) {
				
				campoUsuario.setStyle("-fx-control-inner-background: #ef9a9a");
				
				campoValidado = false;
				
			}else {
				
				campoUsuario.setStyle("-fx-control-inner-background: #ffffff");
				
				campoValidado = true;
				
			}
			
			if(password.isEmpty()) {
				
				passwordNV.setStyle("-fx-control-inner-background: #ef9a9a");
				
				passwordV.setStyle("-fx-control-inner-background: #ef9a9a");
				
				campoValidado = false;
				
			}else {
				
				passwordNV.setStyle("-fx-control-inner-background: #ffffff");
				
				passwordV.setStyle("-fx-control-inner-background: #ffffff");
				
				campoValidado = true;

			}
			
			if(!campoValidado) cmd.ventanaAviso("Aviso", "Imposible realizar Login", "Debe completar todos los campos. Revise los campos marcados");
			
		}
		
		/*
		 * ****************************************** Si se ha introducido una contraseña, valido si la contraseña tiene entre 8 y 20 caracteres, 
		 * ****************************************** al menos un dígito, al menos una minúscula y una mayúscula, al menos un
         * ****************************************** carácter especial (!@#$%&+()-+=^) y no contiene ningún espacio en blanco
		 */
		
		if(!password.isEmpty() || !datosValidados ) {
			
			password = passwordNV.getText();
			
			passwordEnc = DigestUtils.md5Hex(password);
			
			if(usuarioLogin.validarPassword(password)==false) {
			
				passwordNV.setStyle("-fx-control-inner-background: #f6fc38");
				
				passwordV.setStyle("-fx-control-inner-background: #f6fc38");
				
				datosValidados = false;
				
				cmd.ventanaAviso("Aviso", "Contraseña Incorrecta","Debe introducir una contraseña valida");
				
			}else {
				
				passwordNV.setStyle("-fx-control-inner-background: #ffffff");
				
				passwordV.setStyle("-fx-control-inner-background: #ffffff");
				
				datosValidados = true;

			}
			
		}
		
		if(campoValidado==true && datosValidados==true) {
			
			nombre = null;
			
			apellido = null;
		
			//Si se han rellenado los campos, obtengo el valor introducido en los campos Usuario y Password y ejecuto la validación utilizando el método de LoginControler

			resultado = usuarioLogin.validaDatosLogin(usuario, passwordEnc);		
			
			//Si el resultset está vacío, los datos introducidos son incorrectos o no existen.  En caso contrario el login es correcto
			
			try {
				
				if(!resultado.next()) {
					
					cmd.ventanaError("Error", "Datos Incorrectos", "Los datos introducidos no son correctos");
					
					campoUsuario.setStyle("-fx-control-inner-background: #b3ecff");
					
					passwordNV.setStyle("-fx-control-inner-background:  #b3ecff");
					
					passwordV.setStyle("-fx-control-inner-background:  #b3ecff");
					
				}else {
					
					//Obtengo el nombre y el apellido del usuario a partir del usuario introducido para darle la bienvenida
					
					resultado = usuarioLogin.obtenerNombreApellido(usuario);
					
					if(resultado.next()) {
						
						nombre = resultado.getString(1);
						
						apellido = resultado.getString(2);
						
					}
					
					//Abro la ventana del Menu de consultas

					cmd.crearNuevaVista("MenuConsulta");
					
					cmd.ventanaConfirmación("Bienvenido", "Bienvenido a Tweetching " + nombre + " " + apellido + "!!", "¿Qué quieres hacer?");
					
					//Oculto la ventana incial de Login
					
					cmd.cerrarVentanaActual(event);	
					
				}

			}catch(SQLException e) {
				
				e.printStackTrace();
				
			}
					
		}
			
	}
	
	//---------------------------------------> Método que se ejecuta al pulsar el botón Crear Cuenta <--------------------------------------------------------------------------
	
	public void crearCuenta(ActionEvent event) {
		
		//Creo un nuevo Stage para que el usuario rellene los datos necesarios para crear su cuenta
		
		cmd.crearNuevaVista("CrearCuenta");
		
		//Oculto la ventana incial de Login
		
		cmd.cerrarVentanaActual(event);
		
	}
	
	//---------------------------------------> Método que se ejecuta al pulsar sobre "He olvidado la contraseña" <--------------------------------------------------------------
	
	public void passwordForgotten(ActionEvent event) {
		
		//Creo un nuevo Stage para que el usuario rellene los datos necesarios para crear su cuenta
		
		cmd.crearNuevaVista("RecuperarPassword");
		
		//Oculto la ventana incial de Login
		
		cmd.cerrarVentanaActual(event);
			
	}
	
	//---------------------------------------> Método que muestra y oculta la contraseña introducida <--------------------------------------------------------------------------
	
	public void muestroPassword(ActionEvent evento) {
		
		//Escribo las contraseñas introducidas en los TextField que hay debajo de PasswordField
		
		passwordV.setText(passwordNV.getText());
	
		if(mostrarPassword.isSelected()==true) {
			
			//No se ve la contraseña

			passwordNV.setVisible(false); 
			
			passwordV.setVisible(true); 
			
			mostrarPassword.setText("Ver contraseñas");
				
		}else {
			
			//Se ve la contraseña
			
			passwordV.setVisible(false);
			
			passwordNV.setVisible(true);
			
			mostrarPassword.setText("Ocultar contraseñas");
			
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		//Hago que los TextField que utilizo para mostrar las contraseñas, no sean visibles inicialmente
		
		passwordV.setVisible(false);
		
		botonEntrar.setOnAction(this::comprobarLogin);
		
		botonCrearCuenta.setOnAction(this::crearCuenta);
		
		recuperar.setOnAction(this::passwordForgotten);
		
		mostrarPassword.setOnAction(this::muestroPassword);
			
	}
	
}
