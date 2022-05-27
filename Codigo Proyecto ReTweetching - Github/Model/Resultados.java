package Model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import static com.mongodb.client.model.Filters.*;
import com.opencsv.CSVWriter;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Resultados {
	
	public Resultados() {
		
	}
	
	//---------------------------------------------------> Variables conexión MongoDB <-----------------------------------------------------------------------------------------
	
	ConexionMongoDB cmdb; // = new ConexionMongoDB();
	
	static MongoClient mongo; // = cmdb.crearConexion();
	
	private MongoDatabase mdb;
	
	private MongoCollection<Document> coleccion;
	
	private MongoIterable<String> coleccionesUsuario;
	
	private List<Document> lista;
	
	private Document documento;
	
	//private DB db;
	
	//private DBCollection items;
	
	//private String busquedasUsuario;
	
	//---------------------------------------------------> Variables conexión Twitter <----------------------------------------------------------------------------------------
	
	ConexionTwitter ct;
	
	Twitter twitter;
	
	List<Status> tweets;
	
	//---------------------------------------------------> Operaciones sobre Twitter <-----------------------------------------------------------------------------------------
	
	//Método que realiza la búsqueda en Twitter y devuelve la lista de resultados 
	
	public List<Status> busquedaTwitter(String palabraClave) {
		
		ct = new ConexionTwitter();
		
		try {
	
			twitter = ct.conexionTwitter();
		
			Query query = new Query(palabraClave);
			
			query.setCount(20);  //Limito resultados obtenidos
			
			QueryResult resultado = twitter.search(query);
			
			System.out.println("Obteniendo tweets....");
			
			tweets = resultado.getTweets();
			
		}catch(TwitterException te) {
			
			te.printStackTrace();
		}
		
		return tweets;

	}
	
	//Método que recupera la lista de resultados obtenidos
	
	public List<Status> listaResultados(List<Status> tweets) {
		
		return  tweets;
			
	}
	
	// Método que crea una List para exportar a CSV a partir del resultado de una consulta
	
	public List<String[]> preparaDatosExportacion(List<Status> tweets) {
		
		List<String[]> resultados = new ArrayList<>();
		
		String[] cabecera = {"Tweet", "Usuario", "Amigos", "Seguidores", "Retweets", "Favoritos", "Localización", "Fecha Búsqueda"};
		
		resultados.add(cabecera);
		
		for(Status tw : tweets) {
			
			//Convierto los valores númericos en string con Integer.toString
			
			String tweet = tw.getText();
			
			String user = tw.getUser().getScreenName();
			
			String amigos = Integer.toString(tw.getUser().getFriendsCount());
			
			String seguidores = Integer.toString(tw.getUser().getFollowersCount());
			
			String retweets = Integer.toString(tw.getRetweetCount());
			
			String favoritos =  Integer.toString(tw.getFavoriteCount());
			
			String localizacion = tw.getUser().getLocation();
			
			//Capturo la fecha y hora 
			
			String fecha = LocalDateTime.now().toString();
			
			String[] resultado = {tweet, user, amigos, seguidores, retweets, favoritos, localizacion, fecha};
			
			//Añado los resultados
			
			resultados.add(resultado);
			
		}
		
		return resultados;
		
	}
	
	//Método que utiliza la lista anterior para crear un CSV
			
	//Importar el jar OpenCSV
	
	public void crearCSV(List<String[]> resultados, List<Status> tweets, String usuario, String busqueda) {
		
		resultados = preparaDatosExportacion(tweets);
		
		try (CSVWriter writer = new CSVWriter(new FileWriter("C:/Users/Usuario/eclipse-workspace-ProyectoTwitter/Retweetching/Archivos/" 
		+ usuario + "_" + busqueda + ".csv"))){
		
			writer.writeAll(resultados);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	//---------------------------------------------------> Operaciones sobre MongoDB <-----------------------------------------------------------------------------------------
	
	//Método que guarda los resultados obtenidos en MongoDB
	
	public void guardaResultados(List<Status>tweets, String usuario, String busqueda) {
		
		//Inicio la conexión
		
		cmdb = new ConexionMongoDB();
		
		mongo = ConexionMongoDB.crearConexion();
		
		//Accedo a la base de datos
		
		mdb = mongo.getDatabase("ProyectoRetweetching");
		
		//Accedo a la colección del usuario si existe y sino, se crea (la colección identifica al usuario y a la búsqueda)
		
		coleccion = mdb.getCollection(usuario + "_" + busqueda);
		
		for(Status tw : tweets) {
			
			//Creo un nuevo documento con el nombre de la búsqueda
			
			documento = new Document("Busqueda", busqueda)
			
			//Inserto cada resultado obtenido en un nuevo document
			
			//.append("Datos",  new Document("Tweet", tw.getText())
			.append("Tweet", tw.getText())
			.append("Usuario", tw.getUser().getScreenName())
			.append("Amigos", tw.getUser().getFriendsCount())
			.append("Seguidores", tw.getUser().getFollowersCount())
			.append("Retweets", tw.getRetweetCount())
			.append("Favoritos", tw.getFavoriteCount())
			.append("Localizacion", tw.getUser().getLocation())
			.append("Fecha", LocalDateTime.now().toString());
			
			coleccion.insertOne(documento);
			
		}
			
	}
	
	//Método que recupera las búsquedas realizadas por el usuario
	
	public MongoIterable<String> muestraBusquedas(String usuario) {
		
		//Inicio la conexión
		
		cmdb = new ConexionMongoDB();
		
		mongo = ConexionMongoDB.crearConexion();
		
		//Selecciono la base de datos pruebasTwitter
		
		mdb = mongo.getDatabase("ProyectoRetweetching");
		
		//Imprimo sus colecciones
		
		coleccionesUsuario = mdb.listCollectionNames();
		
		return coleccionesUsuario;
			
	}
	
	//Método que recupera las búsquedas guardadas por el usuario filtrando por búsqueda
	
	public List<Document> mostrarResultadosGuardados(String usuario, String busqueda) {
		
		cmdb = new ConexionMongoDB();
		
		mongo = ConexionMongoDB.crearConexion();
		
		//Selecciono la base de datos pruebasTwitter
		
		mdb = mongo.getDatabase("ProyectoRetweetching");
		
		String coleccion = usuario + "_"+ busqueda;
		
		MongoCollection<Document> doc = mdb.getCollection(coleccion);
		
		//Recorrer la lista de resultados con List (utilizo eq para que la coincidencia sea exacta)
		
		List<Document> lista = doc.find(eq("Busqueda", busqueda)).into(new ArrayList<>());
		
		return lista;
		
	}
		
	//Método que recupera de MongoDB filtrando por usuario y búsqueda
	
	public List<Document> muestraResultadosBusqueda(String usuario, String busqueda){
		
		//Accedo a la base de datos
		
		mdb = mongo.getDatabase("ProyectoRetweetching");
		
		//Accedo a la colección del usuario si existe y sino, se crea
		
		coleccion = mdb.getCollection(usuario);
		
		lista = coleccion.find(eq("Busqueda", busqueda)).into(new ArrayList<>());
		
		return lista;
		
	}
	
	//Método que crea una List para exportar a CSV a partir del resultado de una consulta obtenido de MongoDB
	
	public List<String[]> preparaDatosExportacionMongoDB(List<Document> documentos) {
		
		List<String[]> resultados = new ArrayList<>();
		
		String[] cabecera = {"Tweet", "Usuario", "Amigos", "Seguidores", "Retweets", "Favoritos", "Localización", "Fecha Búsqueda"};
		
		resultados.add(cabecera);
		
		for(Document documento : documentos) {
			
			//Convierto los valores númericos en string con Integer.toString
			
			String tweet = documento.getString("Tweet");
			
			String user = documento.getString("Usuario");
			
			int friends = documento.getInteger("Amigos");
			
			int followers = documento.getInteger("Seguidores");
			
			int retw = documento.getInteger("Retweets");
			
			int favourites =  documento.getInteger("Favoritos");
			
			String localizacion = documento.getString("Localización");
			
			String fecha = documento.getString("Fecha");
			
			//Convierto las variables int en String con valueOf
			
			String seguidores = String.valueOf(followers);
			
			String amigos = String.valueOf(friends);
			
			String retweets = String.valueOf(retw);
			
			String favoritos = String.valueOf(favourites);
			
			//Añado cada elemento al string que contendrá todos los documentos
			
			String[] resultado = {tweet, user, amigos, seguidores, retweets, favoritos, localizacion, fecha};
			
			//Añado los resultados
			
			resultados.add(resultado);
			
		}
		
		return resultados;
		
	}
	
	//Método que utiliza la lista anterior para crear un CSV
	
	//Importar el jar OpenCSV
	
	public void crearCSVmongoDB(List<String[]> resultados, List<Document> documentos, String usuario, String busqueda) {
		
		resultados = preparaDatosExportacionMongoDB(documentos);
		
		try (CSVWriter writer = new CSVWriter(new FileWriter("C:/Users/Usuario/eclipse-workspace-ProyectoTwitter/Retweetching/Archivos/" 
		+ usuario + "_" + busqueda + ".csv"))){
		
			writer.writeAll(resultados);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

}
