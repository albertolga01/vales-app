package com.petromar.valespetro;

public class MyAppModel {

    private String nombre, precio, stock, img;
    private String name, imageurl, description, IdTarjeta, Placas, folio;


    // Campo nombre
    public String getName() {
        return name;
    }
    // Seteo el nombre
    public void setName(String name) {
        this.name = name;
    }

    // Campo folio
    public String getFolio() {
        return folio;
    }
    // Seteo el folio
    public void setFolio(String folio) {
        this.folio = folio;
    }


    // Campo descripcion
    public String getdescription() {
        return description;
    }
    // Seteo el decripcion
    public void setdescription(String description) {
        this.description = description;
    }

    // Campo Tarjeta
    public String getIdTarjeta() {
        return IdTarjeta;
    }
    // Seteo el Tarjeta
    public void setIdTarjeta(String IdTarjeta) {
        this.IdTarjeta = IdTarjeta;
    }
    // Campo Placas
    public String getPlacas() {
        return Placas;
    }
    // Seteo las Placas
    public void setPlacas(String Placas) {
        this.Placas = Placas;
    }

    // Campo imagen
    public String getimageurl(){
        return imageurl;
    }
    // Seteo la imagen
    public void setImageurl(String imageurl){
        this.imageurl = imageurl;
    }

}
