package org.vaadin.example.Clases;

import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class DatePick extends VerticalLayout {

    private String fechaEnvio;

    public DatePick(){

        DateTimePicker dateTimePicker = new DateTimePicker(); // creo un objeto de la clase DateTimePicker
        dateTimePicker.setLocale(new Locale("es", "ES")); // le asigno el idioma español
        add(dateTimePicker); // añado el objeto a la vista
        dateTimePicker.setLabel("Selecciona una fecha y hora: "); // establezco el texto del label
        dateTimePicker.getStyle().set("width", "600px").set("max-width", "100%");   // establezco el ancho del objeto

        dateTimePicker.addValueChangeListener(event -> { // cuando se cambie el valor del objeto
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); // creo un objeto de la clase DateTimeFormatter
            LocalDateTime fechaHoraPick = dateTimePicker.getValue(); // creo un objeto de la clase LocalDateTime y le asigno el valor del objeto
            //System.out.println(fechaHoraPick); // imprimo por consola el valor del objeto
            try {
                String fechaFormateada = fechaHoraPick.format(dateFormatter); // creo un objeto de la clase String y le asigno el valor del objeto formateado
                //System.out.println(fechaFormateada);
                fechaEnvio = fechaFormateada; // le asigno el valor del objeto a la variable fechaEnvio
                //System.out.println(fechaEnvio);
            } catch (Exception e) {
                e.printStackTrace(); // si hay algun error, imprime el error
            }
        });
    }
    public String getfechaHora() {
        return fechaEnvio;
    } // get de la variable fechaEnvio
}


