package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Inicio")
@Route("")
public class Index extends AppLayout {

    public Index() {
        H1 title = new H1("DIStintos P2"); // Título de la página
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("left", "var(--lumo-space-l)").set("margin", "0").set("position", "absolute"); // Estilo del título
        //Tabs tabs = getTabs(); // Tabs de la página
        H1 title2 = new H1(" "); // Título de la página
        title2.getStyle().set("font-size", "var(--lumo-font-size-l)").set("left", "var(--lumo-space-l)").set("margin", "0").set("position", "absolute"); // Estilo del título


        addToDrawer(title); // Añadir título a la barra lateral
        createDrawer(); //Creamos el menú lateral
    }

    private void createDrawer() {
        RouterLink vistaZonBasica = new RouterLink("Datos Zona Básica Salud", DatosGrid.class); // Creamos un enlace a la vista DatosGrid
        vistaZonBasica.setHighlightCondition(HighlightConditions.sameLocation()); // Para que se resalte el enlace cuando estemos en la vista DatosGrid

       /* RouterLink vistaZonaBasica60 = new RouterLink("Datos Zona Básica Salud mayores de 60 años", Grid60.class); // Creamos un enlace a la vista Grid60
        vistaZonaBasica60.setHighlightCondition(HighlightConditions.sameLocation()); // Para que se resalte el enlace cuando estemos en la vista Grid60 */

        addToDrawer(new VerticalLayout(
                vistaZonBasica // Añadimos el enlace a la vista DatosGrid al menú
                //vistaZonaBasica60 // Añadimos el enlace a la vista Grid60 al menú
        ));
    }
}