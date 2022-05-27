package Controller;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// TODO Auto-generated method stub
		
		//Parent root =FXMLLoader.load(getClass().getResource("../View/MenuConsulta.fxml"));  //----------------------> Probando Pantalla MenuConsulta
		
		//Parent root =FXMLLoader.load(getClass().getResource("../View/Login.fxml"));  //-------------------------------> Probando Pantalla Login
		
		//Parent root =FXMLLoader.load(getClass().getResource("../View/RecuperarPassword.fxml"));  //---------------------> Probando Pantalla RecuperarPassword
		
		//Parent root =FXMLLoader.load(getClass().getResource("../View/NuevoPassword.fxml"));  //---------------------> Probando Pantalla NuevoPassword
		
		//Parent root =FXMLLoader.load(getClass().getResource("../View/Resultados.fxml"));  //---------------------> Probando Pantalla Resultados
		
		//Parent root =FXMLLoader.load(getClass().getResource("../View/ResultadosGuardados.fxml"));  //---------------------> Probando Pantalla ResultadosGuardados
		
		//Parent root =FXMLLoader.load(getClass().getResource("../View/Mapa.fxml"));  //---------------------> Probando Pantalla Mapa
		
		//Parent root =FXMLLoader.load(getClass().getResource("../View/Login.fxml"));      //-------------------------> Es la buena para lanzar App
		
		Parent root =FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
		
		primaryStage.getIcons().add(new Image("/Recursos/LogoPrueba500x500FondoTransparente.jpg"));
		
		primaryStage.setScene(new Scene(root, 950,600));
		
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		launch(args);

	}

}
