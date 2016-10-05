package www.miztonapp.mx.models;

/**
 * Created by Saulo on 05/10/2016.
 */

public class ModelOrdenesTrabajo {
    public String folio_orden;
    public String telefono_orden;
    public String tipo_orden;



    public ModelOrdenesTrabajo(String folio_orden,String telefono_orden, String tipo_orden){
        this.folio_orden = folio_orden;
        this.telefono_orden = telefono_orden;
        this.tipo_orden = tipo_orden;
    }

}
