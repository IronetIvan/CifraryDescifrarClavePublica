/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrarydescifrarclavepublica;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author Usuario DAM 2
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
			//SE CREA EL PAR DE CLAVES publica y privada	
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize (1024);
			KeyPair par = keyGen.generateKeyPair();
			PrivateKey clavepriv = par.getPrivate();
			PublicKey clavepub = par.getPublic();			
		
			//SE CREA LA CLAVE SECRETA AES 
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init (128);
			SecretKey clavesecreta = kg.generateKey();
			
			//SE ENVUELVE LA CLAVE SECRETA CON LA RSA PÚBLICA					
			Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			c.init(Cipher.WRAP_MODE, clavepub);
			byte claveenvuelta[] = c.wrap(clavesecreta);				
			
			//CIFRAMOS TEXTO CON LA CLAVE SECRETA
			c = Cipher.getInstance("AES/ECB/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, clavesecreta);			
			byte textoPlano[] = "Esto es un Texto Plano".getBytes();
			byte textoCifrado[] = c.doFinal(textoPlano);
			System.out.println("Encriptado: "+ new String(textoCifrado));						
			
		    //SE DESENVUELVE LA CLAVE SECRETA CON LA CLAVE RSA PRIVADA	
			Cipher c2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			c2.init(Cipher.UNWRAP_MODE, clavepriv);
			Key clavedesenvuelta= c2.unwrap(claveenvuelta, "AES", Cipher.SECRET_KEY);
			
			//DESCIFRAMOS TEXTO CON LA CLAVE DESENVUELTA
			c2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
			c2.init(Cipher.DECRYPT_MODE, clavedesenvuelta);		
			byte desencriptado[] = c2.doFinal(textoCifrado);
			System.out.println("Desencriptado: "+ new String(desencriptado));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}//main
}//..Ejemplo12