package org.vaadin.example;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.Clases.DatePick;
import org.vaadin.example.Clases.DatosTwitter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@PageTitle("Twitter")

@Route()
public class DatosGrid extends VerticalLayout {
    com.vaadin.flow.component.grid.Grid<DatosTwitter> grid = new com.vaadin.flow.component.grid.Grid<>(DatosTwitter.class, false);
    com.vaadin.flow.component.grid.Grid.Column<DatosTwitter> Id = grid.addColumn(DatosTwitter::getId).setHeader("Id twitter"); //id twitter
    com.vaadin.flow.component.grid.Grid.Column<DatosTwitter> usuario = grid.addColumn(DatosTwitter::getUsuario).setHeader("usuario"); //usuario
    com.vaadin.flow.component.grid.Grid.Column<DatosTwitter> tweet = grid.addColumn(DatosTwitter::getTweet).setHeader("tweet"); //tweet
    com.vaadin.flow.component.grid.Grid.Column<DatosTwitter> fecha = grid.addColumn(DatosTwitter::getFecha).setHeader("fecha"); //fecha



    public DatosGrid() {
        ArrayList<DatosTwitter> datosTwitter = new ArrayList<>(); // Creamos un ArrayList de tipo DatosZonasBasicasSalud

        Dialog formulario = new Dialog(); // Creamos un dialogo para el formulario
        formulario.getElement().setAttribute("aria-label", "Crear nueva Zona de Salud Básica"); // Le ponemos un nombre al dialogo

        RouterLink vistainicio = new RouterLink("<-- Volver", Index.class); // Creamos un enlace a la vista de inicio
        vistainicio.setHighlightCondition(HighlightConditions.sameLocation()); // Para que se resalte el enlace cuando estemos en la vista de DatosGrid

        //Botón para volver a la página principal
        VerticalLayout dialogLayout = crearTw(formulario); // Creamos un layout vertical para el dialogo
        formulario.add(dialogLayout); // Añadimos el layout al dialogo

        Button addUser = new Button("Crear un nuevo tweet", e -> formulario.open()); // Creamos un boton para abrir el dialogo

        HorizontalLayout header = new HorizontalLayout(addUser, formulario); // Creamos un layout horizontal para el boton y el dialogo
        header.setAlignItems(Alignment.CENTER); // Alineamos el boton y el dialogo al centro
        header.getThemeList().clear(); // Limpiamos el tema del layout

        add (vistainicio, header); // Añadimos el layout al grid
        // Fin de boton para añadir una nueva zona de salud

        Button editarZonaBasica = new Button("Editar el tweet"); // Creamos un boton para editar una zona de salud
        editarZonaBasica.setEnabled(true); // Desactivamos el boton

        Button eliminar = new Button("Eliminar"); // Creamos un boton para eliminar una zona de salud
        eliminar.setEnabled(true); // Desactivamos el boton

        eliminar.addThemeVariants(ButtonVariant.LUMO_ERROR); // Le ponemos un tema al boton
        eliminar.getStyle().set("margin-inline-start", "auto"); // Le ponemos un estilo al boton

        grid.setSelectionMode(com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI); // Ponemos el modo de seleccion de una sola fila
        grid.addSelectionListener(selection -> { // Añadimos un listener al grid
            int size = selection.getAllSelectedItems().size(); // Creamos una variable para el tamaño de la seleccion
            boolean isSingleSelection = size == 1; // Creamos una variable para la seleccion de un solo elemento
            editarZonaBasica.setEnabled(isSingleSelection); // Activamos el boton de editar
            editarZonaBasica.addClickListener(listener -> { // Añadimos un listener al boton de editar
                DatosTwitter datos = selection.getFirstSelectedItem().get(); // Creamos una variable para los datos de la zona de salud
                Dialog formularioEditar = new Dialog(); // Creamos un dialogo para el formulario
                formularioEditar.getElement().setAttribute("aria-label", "Editar usuario"); // Le ponemos un nombre al dialogo
                VerticalLayout dialogLayoutEditar = editarTw(formularioEditar, datos); // Creamos un layout vertical para el dialogo
                formularioEditar.add(dialogLayoutEditar); // Añadimos el layout al dialogo
                formularioEditar.open(); // Abrimos el dialogo
            });
            eliminar.setEnabled(size != 0); // Activamos el boton de eliminar
            eliminar.addClickListener(listener -> { // Añadimos un listener al boton de eliminar
                DatosTwitter datos = selection.getFirstSelectedItem().get(); // Creamos una variable para los datos de la zona de salud
                eliminarTw(datos); // Llamamos al metodo para eliminar la zona de salud
            });
        });

        HorizontalLayout footer = new HorizontalLayout(editarZonaBasica, eliminar); // Creamos un layout horizontal para los botones de editar y eliminar
        footer.getStyle().set("flex-wrap", "wrap"); // Le ponemos un estilo al layout

        add(grid, footer); // Añadimos el grid y el layout al grid
        grid.setItems(datosTwitter); // Añadimos los datos al grid
        add(grid); // Añadimos el grid a la vista

        updategrid(); // Actualizamos el grid
    }

    private void eliminarTw(DatosTwitter datos) { // Metodo para eliminar una zona de salud
        String codigo = datos.getId();
        URL url; // Creamos una variable para la url
        URLConnection con; // Creamos una variable para la conexion
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create(); // Creamos una variable para el gson
        int tamanio = codigo.length(); // Creamos una variable para el tamaño del json
        // Crear nuevo archivo json y sobreescribir el anterior

        try {
            url = new URL("http://localhost:8090/ptbtwitter"); // Creamos la url
            con = url.openConnection(); // Abrimos la conexion
            HttpURLConnection http = (HttpURLConnection) con; // Creamos una variable para la conexion http
            http.setDoOutput(true); // Activamos la salida
            http.setRequestMethod("DELETE"); // Le ponemos el metodo post
            http.setFixedLengthStreamingMode(tamanio); // Le ponemos el tamaño del json
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // Le ponemos el tipo de contenido
            http.connect(); // Conectamos
            byte[] out = codigo.getBytes(StandardCharsets.UTF_8); // Creamos un array de bytes para el json

            try (OutputStream os = http.getOutputStream()) {
                os.write(out); // Escribimos el json
            }
        } catch (IOException e1) {
            e1.printStackTrace(); // Imprimimos la pila de errores
        }
        try {
            TimeUnit.SECONDS.sleep(1); // Esperamos un segundo
        } catch (InterruptedException e1) {
            e1.printStackTrace(); // Imprimimos la pila de errores
        }
        updategrid(); // Actualizamos el grid
        Notification notification = Notification.show("El usuario ha sido eliminado"); // Creamos una notificacion
        notification.setPosition(Notification.Position.MIDDLE); // Le ponemos la posicion al centro
        // Actualizar la pagina
    }

    private VerticalLayout crearTw(Dialog dialog) {
        H2 titulo = new H2("Crear nueva Zona básica de Salud"); // Creamos un titulo para el formulario

        DatePick fechapick = new DatePick(); // Creamos un objeto de la clase DatePick
        //TextField codigo = new TextField("Código geometría: "); // Creamos un campo de texto para el codigo de la geometria
        TextField usuario = new TextField("usuario: "); // Creamos un campo de texto para el nombre de la zona de salud
        TextField tweet = new TextField("tweet: "); // Creamos un campo de texto para la incidencia de los ultimos 14 dias
        VerticalLayout campos = new VerticalLayout(usuario, tweet, fechapick); // Creamos un layout vertical para los campos de texto

        campos.setSpacing(false); // Quitamos el espacio entre los campos de texto
        campos.setPadding(false); // Quitamos el padding entre los campos de texto
        campos.setAlignItems(Alignment.STRETCH); // Alineamos los campos de texto

        //Button cancelar = new Button("Cancelar", e -> dialog.close()); // Creamos un boton para cancelar
        Button cancelar= new Button("Cancelar", e -> dialog.close()); // Creamos un boton para cancelar
        Button guardar = new Button("Guardar", e -> dialog.close()); // Creamos un boton para guardar
        cancelar.addClickListener(event -> {
            dialog.close(); // Cerramos el dialogo
            grid.deselectAll(); // Desseleccionamos todos los elementos del grid
            UI.getCurrent().getPage().reload(); // Recargamos la pagina
        }); // Añadimos un listener al boton de cancelar
        guardar.addClickListener(event -> {
            dialog.close(); // Cerramos el dialogo
            grid.deselectAll(); // Desseleccionamos todos los elementos del grid
            UI.getCurrent().getPage().reload(); // Recargamos la pagina
        }); // Añadimos un listener al boton de guardar
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // Le ponemos un estilo al boton de guardar
        HorizontalLayout botones = new HorizontalLayout(cancelar, guardar); // Creamos un layout horizontal para los botones de cancelar y guardar
        botones.setJustifyContentMode(JustifyContentMode.END); // Alineamos los botones a la derecha

        VerticalLayout dialogLayout = new VerticalLayout(titulo, campos, botones); // Creamos un layout vertical para el dialogo
        dialogLayout.setPadding(false); // Quitamos el padding del layout
        dialogLayout.setAlignItems(Alignment.STRETCH); // Alineamos los elementos del layout
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%"); // Le ponemos un estilo al layout

        guardar.addClickListener(e -> {
            DatosTwitter tw = new DatosTwitter(); // Creamos un tweet
            tw.setUsuario(usuario.getValue()); // Le ponemos el valor del campo de texto al objeto
            //Asigna el valor de la incideia de los ultimos 14 dias convirtiendolo a float
            tw.setTweet(tweet.getValue());

            String fecha = fechapick.getFechaEnvio(); // Creamos una variable para la fecha
            System.out.println("This " + fecha);
            Date fechaHora = new Date(fecha);
            tw.setFecha(fechaHora); // Le ponemos el valor del campo de texto al objeto


            URL url; // Creamos una variable para la url
            URLConnection con; // Creamos una variable para la conexion
            Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();; // Creamos una variable para el gson
            String json = gson.toJson(tw); // Creamos una variable para el json
            System.out.println(json); // Imprimimos el json por consola
            int tamanio = json.length(); // Creamos una variable para el tamaño del json

            try {
                url = new URL("http://localhost:8090/ptbtwitter"); // Creamos la url
                con = url.openConnection(); // Abrimos la conexion
                HttpURLConnection http = (HttpURLConnection) con; // Creamos una variable para la conexion http
                http.setDoOutput(true); // Activamos la salida
                http.setRequestMethod("POST"); // Le ponemos el metodo post
                http.setFixedLengthStreamingMode(tamanio); // Le ponemos el tamaño del json
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // Le ponemos el tipo de contenido
                http.connect(); // Conectamos
                byte[] out = json.getBytes(StandardCharsets.UTF_8); // Creamos un array de bytes para el json

                try (OutputStream os = http.getOutputStream()) {
                    os.write(out); // Escribimos el json
                }
            } catch (IOException e1) {
                e1.printStackTrace(); // Imprimimos la pila de errores
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e1) {
                e1.printStackTrace(); // Imprimimos la pila de errores
            }
            updategrid(); // Actualizamos el grid
        });
        return dialogLayout; // Devolvemos el layout
    }

    private VerticalLayout editarTw(Dialog formularioEditar, DatosTwitter tw) {
        H2 titulo = new H2("Editar tweets"); // Creamos un titulo para el formulario

        TextField id = new TextField("id: "); // Creamos un campo de texto para el codigo de la geometria
        TextField usuario = new TextField("usuario: "); // Creamos un campo de texto para el nombre de la zona de salud
        TextField tweet = new TextField("tweet: "); // Creamos un campo de texto para la incidencia de los ultimos 14 dias
        //DatePick fecha = new DatePick(); // Creamos un campo de texto para la fecha del informe
        VerticalLayout campos = new VerticalLayout(id, usuario, tweet); // Creamos un layout vertical para los campos de texto

        campos.setSpacing(false); // Quitamos el espacio entre los campos de texto
        campos.setPadding(false); // Quitamos el padding entre los campos de texto
        campos.setAlignItems(Alignment.STRETCH); // Alineamos los campos de texto

        Button cancelar = new Button("Cancelar"); // Creamos un boton para cancelar
        cancelar.addClickListener(event -> {
            formularioEditar.close(); // Cerramos el dialogo
            grid.deselectAll(); // Desseleccionamos todos los elementos del grid
            UI.getCurrent().getPage().reload(); // Recargamos la pagina
        }); // Añadimos un listener al boton de cancelar
        Button guardar = new Button("Guardar", e -> formularioEditar.close()); // Creamos un boton para guardar
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // Le ponemos un estilo al boton de guardar
        guardar.addClickListener(event -> {
            formularioEditar.close(); // Cerramos el dialogo
            grid.deselectAll(); // Desseleccionamos todos los elementos del grid
            UI.getCurrent().getPage().reload(); // Recargamos la pagina
        }); // Añadimos un listener al boton de cancelar
        HorizontalLayout botones = new HorizontalLayout(cancelar, guardar); // Creamos un layout horizontal para los botones de cancelar y guardar
        botones.setJustifyContentMode(JustifyContentMode.END); // alineos los botones a la derecha
        
        id.setValue(tw.getId()); // Asignamos el valor del codigo de la geometria
        id.setEnabled(false); // Desactivamos el campo de texto
        usuario.setValue(tw.getUsuario()); // Asignamos el valor del nombre de la zona de salud

        tweet.setValue(tw.getTweet().toString()); // Asignamos el valor de la incidencia total


        VerticalLayout dialogLayout = new VerticalLayout(titulo, campos, botones); // Creamos un layout vertical para el dialogo
        dialogLayout.setPadding(false); // Quitamos el padding del layout
        dialogLayout.setAlignItems(Alignment.STRETCH); // Alineamos los elementos del layout
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%"); // Le ponemos un estilo al layout

        guardar.addClickListener(e -> {

            DatosTwitter dtwitter = new DatosTwitter(); // Creamos un objeto de tipo zona basica de salud
            dtwitter.setId(id.getValue()); // Le ponemos el valor del campo de texto al objeto
            dtwitter.setUsuario(usuario.getValue()); // Le ponemos el valor del campo de texto al objeto
            //Asigna el valor de la incideia de los ultimos 14 dias convirtiendolo a float
            dtwitter.setTweet(tweet.getValue()); //(Float.parseFloat(incidencia14.getValue()));

            //Asigna el valor de los casos totales convirtiendolo a int
          //  dtwitter.setCasos_confirmados_totales(Integer.parseInt(casosT.getValue()));


            URL url; // Creamos un objeto de tipo URL
            URLConnection con; // Creamos un objeto de tipo URLConnection
            Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create(); // Creamos un objeto de tipo Gson
            String json = gson.toJson(dtwitter); // Creamos un objeto de tipo String y le asignamos el objeto de tipo Gson convertido a json
            int tamanio = json.length(); // Creamos un objeto de tipo int y le asignamos el tamaño del objeto de tipo String

            try {
                url = new URL("http://localhost:8090/ptbtwitter"); // Le asignamos la url a la variable url
                con = url.openConnection(); // Le asignamos la conexion a la variable con
                HttpURLConnection http = (HttpURLConnection) con; // Creamos un objeto de tipo HttpURLConnection y le asignamos la conexion
                http.setDoOutput(true); // Le decimos que la conexion es de salida
                http.setRequestMethod("PUT"); // Le decimos que el metodo de la conexion es PUT
                http.setFixedLengthStreamingMode(tamanio); // Le decimos que el tamaño de la conexion es el tamaño del objeto de tipo String
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // Le decimos que el tipo de contenido es json
                http.connect(); // Conectamos la conexion
                byte[] out = json.getBytes(StandardCharsets.UTF_8); // Creamos un objeto de tipo byte y le asignamos el objeto de tipo String convertido a bytes
                try (OutputStream os = http.getOutputStream()) { // Creamos un objeto de tipo OutputStream y le asignamos la conexion
                    os.write(out); // Escribimos en la conexion
                }
            } catch (IOException e1) {
                e1.printStackTrace(); // Imprimimos la pila de errores
            }
            try {
                TimeUnit.SECONDS.sleep(1); // Hacemos que el hilo pare 1 segundo
            } catch (InterruptedException e1) {
                e1.printStackTrace(); // Imprimimos la pila de errores
            }
            updategrid(); // Actualizamos el grid
        });
        return dialogLayout; // Devolvemos el layout
    }

    public void updategrid(){
        URL url; // Creamos una variable para la URL
        URLConnection con; // Creamos una variable para la conexion
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create(); // Creamos una variable para la libreria Gson
        String respuesta = ""; // Creamos una variable para la respuesta del servidor
        try{
            url = new URL("http://localhost:8090/ptbtwitter"); //URL del servidor con el puerto
            con = url.openConnection(); //Abrimos la conexion con el servidor
            HttpURLConnection http = (HttpURLConnection) con; //Casteamos la conexion a http
            http.setRequestProperty("accept", "application/json"); //Le decimos que queremos que nos devuelva un json
            InputStream response = http.getInputStream(); //Obtenemos la respuesta
            try (Scanner scanner = new Scanner(response,StandardCharsets.UTF_8.name())) { //Creamos un scanner para leer la respuesta
                respuesta = scanner.useDelimiter("\\A").next(); //Leemos la respuesta y la guardamos en el string
            }
            Type userListType = new TypeToken<List<DatosTwitter>>(){}.getType(); //Creamos un tipo de dato para la lista de usuarios
            List<DatosTwitter> refresh = gson.fromJson(respuesta, userListType); //Creamos una lista de usuarios a partir del json
            grid.setItems(refresh); //Añadimos la lista de equipos al grid
        }catch(IOException e){
            System.out.println("Error al actualizar el grid"); //Si hay algun error lo mostramos por consola
        }
    }

}

