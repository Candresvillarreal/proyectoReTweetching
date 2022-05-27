package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Recursos.Comodin;


public class Usuario {
	
	Comodin cmd = new Comodin();
	
	//---------------------------------------------------> Variables BBDD <----------------------------------------------------------------------------------------------------
	
	private ConexionBBDD conexion;
	
	private Connection cn;
	
	private ResultSet rs;
	
	private PreparedStatement ps;
	
	//---------------------------------------------------> Sentencias SQL <----------------------------------------------------------------------------------------------------
	
	private final String consultaDatosUsuario = "SELECT Usuario, Contrasena FROM Usuarios WHERE Usuario=? AND Contrasena=?";
	
	private final String consultaMail = "SELECT Email FROM Usuarios WHERE Email=?";
	
	private final String consultaUsuario = "SELECT Usuario FROM Usuarios WHERE Usuario=?";
	
	private final String consultaCodigoRecuperacion = "SELECT CodRecuperacion FROM Secundaria WHERE CodRecuperacion=?";
	
	private final String consultaBeforeUpdate = "SELECT Usuarios.Id FROM Usuarios JOIN Secundaria WHERE (Secundaria.Email=? AND Secundaria.CodRecuperacion=?) "
			+ "AND (Usuarios.Id=Secundaria.ID AND Usuarios.Email=Secundaria.Email)";
	
	private final String consultaNombreApellido = "SELECT Nombre, Apellido FROM Usuarios WHERE Usuario=?";
	
	private final String consultaMailusuario = "SELECT Email FROM Usuarios WHERE Usuario=?";
	
	private final String insertaUsuario = "INSERT INTO Usuarios(Nombre, Apellido,Email,Contrasena,Usuario) VALUES(?,?,?,?,?)";
	
	private final String insertaCodigoRecuperacion = "UPDATE Secundaria SET CodRecuperacion=? WHERE Email=?";
	
	private final String actualizaPassword = "UPDATE Usuarios SET contrasena=? WHERE Email=? AND Id=?";
	
	public Usuario() {
		
		conexion = new ConexionBBDD();
		
	}
	
	//---------------------------------------------------> Validaciones Campos Formularios <-----------------------------------------------------------------------------------
	
	// -------------------------------------------------- Validar que la dirección de Email introducida es válida
	
	public boolean validaDireccionMail(String email) {
		
		String patronValido = "^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[esES]|[comCOM]{2})$";
		
		Pattern patron = Pattern.compile(patronValido);
		
		Matcher comparador = patron.matcher(email);
		
		return comparador.find();
		
	}
	
	/*
	 * -------------------------------------------------- Método que valida si un una contraseña tiene entre 8 y 20 caracteres, 
	 * -------------------------------------------------- al menos un dígito, al menos una minúscula y una mayúscula, al menos un 
	 * -------------------------------------------------- carácter especial (!@#$%&+()-+=^) y no contiene ningún espacio en blanco
	 */
	
	public boolean validarPassword(String password) {
		
		String patronValidado = "^(?=.*[0-9])(?=.*[az])(?=.*[AZ])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$";
		
		Pattern patron = Pattern.compile(patronValidado);
		
		Matcher comparador = patron.matcher(password);
		
		return comparador.find();
		
	}
	
	//---------------------------------------------------> Operaciones contra la BBDD <----------------------------------------------------------------------------------------
	
	// -------------------------------------------------- Método que comprueba el usuario y la contraseña
	
	public ResultSet validaDatosLogin(String usuario, String password) {
		
		//Establezco conexion con la base de datos
		
		cn = conexion.conectar();
		
		//Inicio el recordset a null
		
		rs = null;
		
		//Compruebo si los datos introducidos por el usuario son correctos
		
		try {
			
			ps = cn.prepareStatement(consultaDatosUsuario);
			
			ps.setString(1, usuario);
			
			ps.setString(2, password);
			
			rs = ps.executeQuery();
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return rs;
		
	}
	
	
	// -------------------------------------------------- Método que comprueba si existe en la base de datos el Email introducido
	
	public ResultSet comprobarEmail(String email) {
		
		Connection cn = conexion.conectar();
		
		rs = null;
		
		try {
			
			ps = cn.prepareStatement(consultaMail);
			
			ps.setString(1, email);
			
			rs = ps.executeQuery();
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return rs;		
		
	}
	
	 // -------------------------------------------------- Método que comprueba que no existe en la base de datos el Usuario introducido
	
	public ResultSet comprobarUsuario(String usuario) {
		
		Connection cn = conexion.conectar();
		
		rs = null;
		
		try {
			
			ps = cn.prepareStatement(consultaUsuario);
			
			ps.setString(1, usuario);
			
			rs = ps.executeQuery();
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
		}

		return rs;
		
	}

	// -------------------------------------------------- Método que obtiene el nombre y el apellido del usuario
	
	public ResultSet obtenerNombreApellido(String usuario) {
		
		Connection cn = conexion.conectar();
		
		rs = null;
		
		try {
			
			ps = cn.prepareStatement(consultaNombreApellido);
			
			ps.setString(1, usuario);
			
			rs = ps.executeQuery();
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
		}

		return rs;
	}
	
	// -------------------------------------------------- Método que obtiene el email de un usuario
	
	public ResultSet obtenerMailUsuario(String usuario) {
		
		Connection cn = conexion.conectar();
		
		rs = null;
		
		try {
			
			ps = cn.prepareStatement(consultaMailusuario);
			
			ps.setString(1, usuario);
			
			rs = ps.executeQuery();
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
		}

		return rs;
		
	}
	
	 // -------------------------------------------------- Método que inserta en la BBDD los datos del nuevo Usuario
	
	public boolean insertaUsuario(String nombre, String apellido, String password, String mail, String usuario) {
		
		Connection cn = conexion.conectar();
		
		try {
			
			ps = cn.prepareStatement(insertaUsuario);
			
			ps.setString(1, nombre);
			
			ps.setString(2, apellido);
			
			ps.setString(3, password);
			
			ps.setString(4, mail);
			
			ps.setString(5, usuario);
			
			ps.executeUpdate();
			
			return true;
			
		} catch (SQLException e) {
			
			//cmd.ventanaError("Error", "Error al crear la cuenta", "Se ha producido un error al crear la cuenta.  Inténtelo en unos minutos...");
			
			e.printStackTrace();
			
			return false;
	
		}
		
	}	

	// -------------------------------------------------- Método que inserta en la BBDD recupera_password el código de recuperacion
	
	public boolean guardarCodigoRecuperacion(String codigoRecuperacion, String Email) {
		
		Connection cn = conexion.conectar();
		
		try {
			
			ps = cn.prepareStatement(insertaCodigoRecuperacion);
			
			ps.setString(1, codigoRecuperacion);
			
			ps.setString(2, Email);
			
			ps.executeUpdate();
			
			return true;
		
		}catch(SQLException e) {
			
			e.printStackTrace();
			
			return false;
			
		}

	}
	
	// -------------------------------------------------- Método que comprueba en la BBDD Secundaria si existe el código de recuperacion

	public ResultSet comprobarCodigoRecuperacion(String codigoRecuperacion) {
		
		Connection cn = conexion.conectar();
		
		rs = null;
		
		try {
			
			ps = cn.prepareStatement(consultaCodigoRecuperacion);
			
			ps.setString(1, codigoRecuperacion);
			
			rs = ps.executeQuery();
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return rs;
				
	}
	
	/*
	* -------------------------------------------------- Método que comprueba en la BBDD Secundaria si el código de recuperacion y el email introducidos
	* -------------------------------------------------- pertenecen al mismo usuario y si el usuario y el mail coinciden también en la tabla Usuarios
	*/
	 
	public ResultSet comprobacionAntesActualizacion(String Email, String CodigoRecuperacion) {
		
		Connection cn = conexion.conectar();
		
		rs = null;
		
		try {
			
			ps = cn.prepareStatement(consultaBeforeUpdate);
			
			ps.setString(1, Email);
			
			ps.setString(2, CodigoRecuperacion);
			
			rs = ps.executeQuery();
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
		}

		return rs;
		
	}
	
	// -------------------------------------------------- Método que actualiza en la tabla Usuarios la nueva contraseña

	public boolean actualizarPassword(String password, String email, String id) {
		
		Connection cn = conexion .conectar();
		
		try {
			
			ps = cn.prepareStatement(actualizaPassword);
			
			ps.setString(1, password);
			
			ps.setString(2, email);
			
			ps.setString(3, id);
			
			ps.executeUpdate();
			
			return true;
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
			return false;
			
		}	
		
	}

}
