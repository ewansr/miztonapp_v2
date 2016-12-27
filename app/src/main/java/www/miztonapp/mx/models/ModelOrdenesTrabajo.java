package www.miztonapp.mx.models;

/**
 * Created by Saulo on 05/10/2016.
 */

public class ModelOrdenesTrabajo {
    public String Id;
    public String contador_filas;
    public String folio_orden;
    public String telefono_orden;
    public String tipo_orden;
    public String principal;
    public String secundario;
    public String fecha;
    public String tipo_instalacion;
    public String estatus_orden;
    public String comentarios;
    public String garantia;
    public String central;
    public String distrito;
    public String terminal;
    public String puerto;
    public String contratista;


    public ModelOrdenesTrabajo(String Id,String contador_filas, String folio_orden,String telefono_orden,
                               String tipo_instalacion, String tipo_orden, String fecha,
                               String estatus_orden, String comentarios, String garantia, String central,
                               String distrito, String terminal, String puerto, String contratista, String principal, String secundario){
        this.Id = Id;
        this.contador_filas     = contador_filas;
        this.folio_orden        = folio_orden;
        this.telefono_orden     = telefono_orden;
        this.tipo_instalacion   = tipo_instalacion;
        this.tipo_orden         = tipo_orden;
        this.fecha              = fecha;
        this.estatus_orden      = estatus_orden;
        this.comentarios        = comentarios;
        this.garantia           = garantia;
        this.central            = central;
        this.distrito           = distrito;
        this.terminal           = terminal;
        this.puerto             = puerto;
        this.contratista        = contratista;
        this.principal          = principal;
        this.secundario         = secundario;
    }

}
