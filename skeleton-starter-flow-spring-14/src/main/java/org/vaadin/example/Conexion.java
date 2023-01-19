package org.vaadin.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Conexion {

    public Conexion() {
        try{

            //URL url = new URL("http://BackendContenedor:8090/dzbs");
            URL url = new URL("http://localhost:8090/ptbtwitter"); //URL de la API (RUTA DEL BACKEND)
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrimos la conexión
            connection.setRequestMethod("GET"); //Establecemos el método de la petición
            connection.setConnectTimeout(5000); //Establecemos el tiempo de espera de la conexión
            connection.setReadTimeout(5000); //Establecemos el tiempo de espera de la lectura de la conexión

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //Creamos un buffer para leer la respuesta
            String inputLine; //Creamos una variable para guardar la respuesta
            StringBuilder builder = new StringBuilder(); //Creamos un StringBuilder para guardar la respuesta
            while ((inputLine = reader.readLine()) != null){ //Mientras haya líneas que leer, las va a ir guardando en la variable inputLine
                builder.append(inputLine); //Añadimos la linea leida al StringBuilder
            }
            reader.close(); //Cerramos el buffer

            System.out.println(builder.toString()); //Imprimimos la respuesta

        }catch(IOException e){ //Capturamos las excepciones
            e.printStackTrace(); //Imprimimos la pila de errores
        }
    }

}
