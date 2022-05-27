package Model;

import com.mongodb.MongoClient;


public class ConexionMongoDB {
	
	public ConexionMongoDB() {
		
	}
	
	public static MongoClient crearConexion() {
		
		MongoClient mongo = null;
		
		try {
			
		mongo = new MongoClient();
		
		//mongo = new MongoClient("localhost", 27017);
		
		System.out.println("Conexión establecida con MongoDB");
		
		}catch(Exception e) {
			
			e.printStackTrace();
		}
		
		return mongo;
		
	}

}
