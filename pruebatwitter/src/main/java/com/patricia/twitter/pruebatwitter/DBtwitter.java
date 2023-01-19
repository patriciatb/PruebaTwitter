package com.patricia.twitter.pruebatwitter;

import com.patricia.twitter.pruebatwitter.Clases.DatosTwitter;

import java.util.ArrayList;

public class DBtwitter {
    private ArrayList<DatosTwitter> tweets; // Lista de datos

    public DBtwitter(ArrayList<DatosTwitter> datos) {
        this.tweets = datos;
    } // Constructor de la clase DB

    public DBtwitter() { // constructor
    }

    // Getter & Setter
    public ArrayList<DatosTwitter> getDatos() {
        return tweets;
    }
    public void setDatos(ArrayList<DatosTwitter> datos) {
        this.tweets = datos;
    }
}

