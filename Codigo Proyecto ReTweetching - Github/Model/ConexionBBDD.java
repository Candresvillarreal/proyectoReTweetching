package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBBDD {
	
	//---------------------------------------------------> Defino las variables para la conexi�n con la base de datos Retwetching <--------------------------------------------
	
	private final static String controlador = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/ProyectoRetweetching";
	private String usuario= "tu_usuario";
	private String password = "tu_password";
	Connection conexion = null;
	
	//---------------------------------------------------> Cargo el controlador y el objeto conexi�n <-------------------------------------------------------------------------
	
	static {
		
		try {
			
			Class.forName(controlador);
			
		}catch(ClassNotFoundException e) {
			
			System.out.println("Error al cargar el controlador");
			
			e.printStackTrace();
			
		}
	}
	
	//---------------------------------------------------> Metodo que establece la conexi�n <----------------------------------------------------------------------------------
	
	public Connection conectar() {
		
		try {
			
			conexion = DriverManager.getConnection(url, usuario, password);
			
			//System.out.println("Conexi�n establecida");
			
		}catch(SQLException e) {
			
			System.out.println("No se ha podido establecer la conexi�n solicitada");
			
			e.printStackTrace();		
			
		}
		
		return conexion;
		
	}
	
	//---------------------------------------------------> Metodo que establece la conexi�n <----------------------------------------------------------------------------------
	
	public void cerrarConexion() {
		
		try {
			
			conexion.close();
			
			//System.out.println("Conexi�n cerrada");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
	}

}
