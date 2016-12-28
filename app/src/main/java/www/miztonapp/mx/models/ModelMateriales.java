package www.miztonapp.mx.models;

import com.google.android.gms.vision.text.Line;

/**
 * Created by EwanS on 21/12/2016.
 */

public class ModelMateriales {
    public String Id;
    public String Nombre;
    public String IdMedida;
    public String UnidadMedida;
    public String IdLinea;
    public String Linea;
    public String Codigo;
    public String CantidadDefault;

    public ModelMateriales(String Id, String Nombre, String IdMedida, String UnidadMedida, String IdLinea, String Linea, String Codigo, String CantidadDefault){
        this.Id = Id;
        this.Nombre = Nombre;
        this.IdMedida = IdMedida;
        this.UnidadMedida = UnidadMedida;
        this.IdLinea = IdLinea;
        this.Linea = Linea;
        this.Codigo = Codigo;
        this.CantidadDefault = CantidadDefault;
    }

    @Override
    public String toString(){
        return this.Nombre;
    }
}
