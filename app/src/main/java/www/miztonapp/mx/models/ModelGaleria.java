package www.miztonapp.mx.models;

/**
 * Created by Saulo on 05/10/2016.
 */

public class ModelGaleria {
    public String numero;
    public String ruta_imagen;
    public String nombre_imagen;

    public ModelGaleria(String numero, String ruta_imagen, String nombre_imagen){
        this.numero        = numero;
        this.ruta_imagen   = ruta_imagen;
        this.nombre_imagen = nombre_imagen;
    }


    @Override
    public String toString(){
        return this.nombre_imagen;
    }
}
