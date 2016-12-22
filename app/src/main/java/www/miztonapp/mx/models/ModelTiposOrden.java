package www.miztonapp.mx.models;

/**
 * Created by EwanS on 21/12/2016.
 */

public class ModelTiposOrden {
    public String Id;
    public String TipoInstalacion;
    public String TipoOrden;
    public ModelTiposOrden(String Id, String TipoInstalacion, String TipoOrden){
        this.Id = Id;
        this.TipoInstalacion = TipoInstalacion;
        this.TipoOrden  = TipoOrden;
    }
    @Override
    public String toString() {
        return this.TipoOrden;
    }
}
