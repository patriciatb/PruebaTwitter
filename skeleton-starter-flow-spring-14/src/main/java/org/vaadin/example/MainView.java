package org.vaadin.example;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/datostwitter") //ESTO ES LA RUTA DE 8080/DZBS PARA VER EL FRONT
public class MainView extends VerticalLayout {
    VerticalLayout contenedor = new VerticalLayout(); //Creamos un contenedor vertical
    //  VerticalLayout contenedor60 = new VerticalLayout(); //Creamos un contenedor vertical

    public MainView() {
        Conexion conexion = new Conexion(); //Creamos la conexión
        //Conexion60 conexion60 = new Conexion60(); //Creamos la conexión con la segunda tabla

        add(contenedor, new DatosGrid()); // Añado el componente GridBasic a la pestaña dzbs
        // add(contenedor60, new Grid60()); // Añado el componente GridBasic a la pestaña dzbs
    }
}