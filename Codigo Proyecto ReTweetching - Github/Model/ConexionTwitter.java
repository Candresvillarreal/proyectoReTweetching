package Model;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


public class ConexionTwitter {
	
	String accessToken = "tokenTwitter";
	
	String accessTokenSecret = "accessTokenSecret";
	
	String consumerKey = "consumerKey";
	
	String consumerSecret = "consumerSecret";
	
	public ConexionTwitter() {	
		
	}
	
	public Twitter conexionTwitter() throws TwitterException{
	
		//Creo objeto ConfigurationBuilder para poder configurar la conexi�n con las claves obtenidas
		
		ConfigurationBuilder cbuilder = new ConfigurationBuilder()
				.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessTokenSecret);
		
		//Accedo a mi cuenta twitter
		
		Twitter tf = null;
		
		try {
		
		tf = new TwitterFactory(cbuilder.build()).getInstance();
		
		System.out.println("Conexi�n establecida");
		
		}catch(Exception e) {
			
			e.printStackTrace();
			
			System.out.println("Se ha producido un error");
		}
		
		return tf;
	}

}
