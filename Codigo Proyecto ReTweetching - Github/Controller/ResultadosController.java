package Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import Model.Resultados;
import Model.Usuario;
import Recursos.Comodin;
import Recursos.EnviaMail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import twitter4j.Status;

public class ResultadosController implements Initializable{
	
	//-----------------------------------------------------> Variables < --------------------------------------------------------------------------------------------------------
	
	@FXML
	Button botonMenu, botonVerMapa, botonGuardarDatos, botonExportaDatos;
	
	@FXML
	TextArea muestraResultados;
	
	Comodin cmd = new Comodin();
	
	Usuario usuarioResultados = new Usuario();
	
	Resultados results = new Resultados();
	
	private ResultSet rs;
	
	String busquedasUsuario;
	
	String mail;
	
	//Variables est�ticas
	
	List<Status> tweets = MenuConsultaController.tweets;
	
	String busqueda = MenuConsultaController.consulta;
	
	String usuario = LoginController.usuario;
	
	//---------------------------------------------------> M�todo que muestra los resultados correspondientes a la b�squeda realizada por el usuario en MenuConsulta < ---------
	
	public void imprimeResultados() {
		
		int total = 0;  //Variable que acumula el n�mero de resultados obtenidos
		
		muestraResultados.setWrapText(true);
		
		//Recupero los resultados utilizando la variable est�tica tweets de la clase MenuConsultaController
		
		for(Status tw : tweets) {
			
			muestraResultados.appendText("Tweet: " + tw.getText() + "\n");
			muestraResultados.appendText("Usuario: " + tw.getUser().getScreenName() + "\n");
			muestraResultados.appendText("Followers: " + tw.getUser().getFollowersCount() + "\n");
			muestraResultados.appendText("Amigos: " + tw.getUser().getFriendsCount() + "\n");
			muestraResultados.appendText("Retweets: " + tw.getRetweetCount() + "\n");
			muestraResultados.appendText("Favoritos: " + tw.getFavoriteCount() + "\n");
			muestraResultados.appendText("Localizaci�n: " + tw.getUser().getLocation() + "\n");
			muestraResultados.appendText("-------------------------------------------------------------------------------------------------------------------------------------------------" + "\n");
			
			total++;
			
		}
		
		//Convierto el n�mero de resultados obtenidos en un String y lo a�ado a los resultados
		
		String totales = String.valueOf(total);
		
		muestraResultados.appendText("N�mero resultados obtenidos: " + totales);
		
	}
	
	//---------------------------------------------------> M�todo que se ejecuta al pulsar sobre el bot�n Men� -----> abro MenuConsulta.fxml < --------------------------------
	
	public void abrirMenu(ActionEvent event) {
		
		cmd.crearNuevaVista("MenuConsulta");
		
		//Cierro la ventana actual
		
		cmd.cerrarVentanaActual(event);
		
	}
	
	//--------------------------------------------------->  M�todo que se ejecuta al pulsar sobre el bot�n Exportar --------> creo .CSV y env�o por mail < ---------------------
	
	public void exportarBusqueda(ActionEvent event) {
		
		//Obtengo los datos de la consulta realizada
		
		List<String[]> resultados = results.preparaDatosExportacion(tweets);
		
		//Creo y escribo el fichero CSV
		
		results.crearCSV(resultados, tweets, usuario, busqueda);
		
		//Obtengo el email del usuario consultando la BBDD
		
		rs = usuarioResultados.obtenerMailUsuario(usuario);
		
		try {
		
			if(rs.next()) {
				
				mail = rs.getString(1);
				
			}
				
		}catch(SQLException e) {
			
			e.printStackTrace();
			
		}

		//Env�o el archivo por correo electr�nico
		
		EnviaMail correo = new EnviaMail();
		
		correo.EnviaMailTLSFicheroAdjunto(mail, "Datos solicitados", usuario, busqueda);
		
	}
	
	//--------------------------------------------------->  M�todo que guarda los resultados en MongoDB < ---------------------------------------------------------------------
	
	public void guardarResultados(ActionEvent event) {
		
		//Guardo los resultados obtenidos en MongoDB
		
		results.guardaResultados(tweets, usuario, busqueda);
		
		cmd.ventanaConfirmaci�n("Confirmaci�n", "Datos Guardados", "Los datos se han guardado correctamente");
		
	}
	
	//--------------------------------------------------->  M�todo Initialize < -----------------------------------------------------------------------------------------------

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		//Muestro los resultados obtenidos por la consulta generada
		
		imprimeResultados();
		
		//M�todos que ejecuta cada uno de los botones
		
		botonExportaDatos.setOnAction(this::exportarBusqueda);
		
		botonGuardarDatos.setOnAction(this::guardarResultados);
		
		botonMenu.setOnAction(this::abrirMenu);
	
	}	

}
