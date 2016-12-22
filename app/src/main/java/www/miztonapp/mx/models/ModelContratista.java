package www.miztonapp.mx.models;

/**
 * Created by EwanS on 21/12/2016.
 */

public class ModelContratista {
    public String Id;
    public String Contratista;
    public String Activo;

    public ModelContratista(String Id, String Contratista, String Activo){
        this.Id = Id;
        this.Contratista = Contratista;
        this.Activo = Activo;
    }

    @Override
    public String toString(){
        return this.Contratista;
    }
}
