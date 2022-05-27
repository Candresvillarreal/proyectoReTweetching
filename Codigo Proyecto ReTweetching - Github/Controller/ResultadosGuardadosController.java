package Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import com.mongodb.client.MongoIterable;
import Model.Resultados;
import Model.Usuario;
import Recursos.Comodin;
import Recursos.EnviaMail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ResultadosGuardadosController implements Initializable{
	
	@FXML
	Button botonBuscar, botonMenu, botonSalir, botonExportar;
	
	@FXML
	TextArea campoBusquedas,  campoResultados;
	
	@FXML
	TextField campoBusqueda;
	
	Comodin cmd = new Comodin();
	
	Resultados resultado = new Resultados();
	
	Usuario usuarioResultados = new Usuario();
	
	MongoIterable<String> coleccionesUsuario;
	
	private ResultSet rs;
	
	String mail;
	
	String usuario = LoginController.usuario;
	
	static List<Document> muestraResultadosUsuario;  //static porque esa misma List se utiliza para obtener los resultados mostrados y exportarlos a csv
	
	static String busqueda;   //static porque esa misma List se utiliza para obtener los resultados mostrados y exportarlos a csv
	
	static JsonWriterSettings estiloImpresion = JsonWriterSettings.builder().indent(true).build();  //Muestro los resultados con la sangria propia de MongoDB
	
	//---------------------------------------------------> M�todo recupera las b�squedas realizadas por el usuario <---------------------------------
	
	public void mostrarBusquedas() {
		
		String usuario = LoginController.usuario + "_";
		
		String busquedas = "";
		
		coleccionesUsuario = resultado.muestraBusquedas(usuario);
		
		boolean sinResultados = true;
		
		for(String coleccion : coleccionesUsuario) {
			
			//Selecciono s�lo los resultados que empiezan por el nombre del usuario (las colecciones se crean usuario_busqueda)
			
			if(coleccion.startsWith(usuario)) {
				
				campoBusquedas.appendText("");
				
				//Elimino del resultado la parte usuario_ para guardar �nicamente las busquedas realizadas por el usuario
				
				int longitud = usuario.length();
				
				busquedas = coleccion.substring(longitud);
				
				//Muestro en el recuadro las b�squedas guardadas por el usuario
				
				campoBusquedas.appendText(busquedas + "\n");
				
				sinResultados = false;
				
			}
			
		}
		
		//Si no hay resultados, lo indico en el mismo recuadro
		
		if(sinResultados==true) {
			
			campoBusquedas.appendText("Sin Resultados");
				
		}
		
	}
	
	// --------------------------------------------------->  M�todo que se ejecuta al pulsar Buscar --------> busca los resultados guardados en mongoDB
	
	public void mostrarResultados(ActionEvent event) {
		
		busqueda = campoBusqueda.getText();
		
		if(busqueda.isEmpty()) {
			
			cmd.ventanaAviso("Aviso", "Campo vac�o", "Debe introducir una palabra para realizar la b�squeda");
			
		}else {
		
			muestraResultadosUsuario = resultado.mostrarResultadosGuardados(usuario, busqueda);
			
			for(Document resultado : muestraResultadosUsuario) {
				
				campoResultados.setWrapText(true);
				
				campoResultados.appendText(resultado.toJson(estiloImpresion) + "\n");
				
			}
			
		}
		
	}
	
	// --------------------------------------------------->   M�todo que se ejecuta al pulsar Menu ------> abro la pantalla MenuConsulta.fxml
	
	public void abrirMenu(ActionEvent event) {
		
		cmd.crearNuevaVista("MenuConsulta");
		
		//cierro la ventana actual
		
		cmd.cerrarVentanaActual(event);
		
	}
	
	// --------------------------------------------------->   M�todo que se ejecuta al pulsar el bot�n Exportar --------> exportar resultados a CSV y enviar por email
	
	public void exportarBusqueda(ActionEvent event) {
		
		//Recupero los datos solicitados por el usuario utilizando la variable est�tica muestraResultadoUsuario
		
		List<String[]> datos = resultado.preparaDatosExportacionMongoDB(muestraResultadosUsuario);
		
		//Creo y escribo el CSV
		
		resultado.crearCSVmongoDB(datos, muestraResultadosUsuario, usuario, busqueda );
		
		//Obtengo el email del usuario
		
		rs = usuarioResultados.obtenerMailUsuario(LoginController.usuario);
		
		try {
		
			if(rs.next()) {
				
				mail = rs.getString(1);
				
			}
				
		}catch(SQLException e) {
			
			e.printStackTrace();
			
		}

		//Env�o el archivo por correo electr�nico recuperando el archivo CSV guardado utilizando el nombre del usuario y la b�squeda realizada
		
		EnviaMail correo = new EnviaMail();
		
		correo.EnviaMailTLSFicheroAdjunto(mail, "Datos solicitados", usuario, busqueda);
		
	}

	// --------------------------------------------------->   M�todo que se ejecuta al pulsar el bot�n Salir --------> cierro la aplicaci�n <---------------------------------
	
	public void salir(ActionEvent event) {
		
		cmd.cerrarVentanaActual(event);
		
	}
	
	// --------------------------------------------------->   M�todo Initialize <--------------------------------------------------------------------------------------------- 
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		//Muestro las busquedas que el usuario ha guardado
		
		mostrarBusquedas();
		
		//M�todos que ejecuta cada bot�n
		
		botonBuscar.setOnAction(this::mostrarResultados);
		
		botonExportar.setOnAction(this::exportarBusqueda);
		
		botonMenu.setOnAction(this::abrirMenu);
		
		botonSalir.setOnAction(this::salir);
		
	}

}
