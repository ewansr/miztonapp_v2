package www.miztonapp.mx.models;

/**
 * Created by Saulo on 05/10/2016.
 */

public class ModelOrdenesTrabajo {
    public String contador_filas;
    public String folio_orden;
    public String telefono_orden;
    public String tipo_orden;
    public String fecha;
    public String tipo_instalacion;
    public String estatus_orden;

    public ModelOrdenesTrabajo(String contador_filas, String folio_orden,String telefono_orden, String tipo_instalacion, String tipo_orden, String fecha, String estatus_orden){
        this.contador_filas     = contador_filas;
        this.folio_orden        = folio_orden;
        this.telefono_orden     = telefono_orden;
        this.tipo_instalacion   = tipo_instalacion;
        this.tipo_orden         = tipo_orden;
        this.fecha              = fecha;
        this.estatus_orden      = estatus_orden;
    }

}
