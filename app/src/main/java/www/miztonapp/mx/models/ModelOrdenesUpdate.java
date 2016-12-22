package www.miztonapp.mx.models;

import android.app.ActivityManager;

/**
 * Created by EwanS on 21/12/2016.
 */

public class ModelOrdenesUpdate {
    public String Id;
    public String Folio;
    public String FolioTelmex;
    public String IdPersonal;
    public String Telefono;
    public String Principal;
    public String Secundario;
    public String TipoOs;
    public String Distrito;
    public String Central;
    public String Comentarios;
    public String Estatus;
    public String IdTipo;
    public String Terminal;
    public String Puerto;
    public String IdContratista;

    public ModelOrdenesUpdate(String Id, String Folio, String FolioTelmex, String IdPersonal,
                              String Telefono, String Principal, String Secundario, String TipoOs,
                              String Distrito,String Central, String Comentarios, String Estutus, String IdTipo,
                              String Terminal, String Puerto, String IdContratista){
        this.Id = Id;
        this.Folio = Folio;
        this.FolioTelmex = FolioTelmex;
        this.IdPersonal = IdPersonal;
        this.Telefono = Telefono;
        this.Principal = Principal;
        this.Secundario = Secundario;
        this.TipoOs = TipoOs;
        this.Distrito = Distrito;
        this.Central = Central;
        this.Comentarios = Comentarios;
        this.Estatus = Estutus;
        this.IdTipo = IdTipo;
        this.Terminal = Terminal;
        this.Puerto = Puerto;
        this.IdContratista = IdContratista;
    }

    @Override
    public String toString(){
        return this.Folio;
    }
}
