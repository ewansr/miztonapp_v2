package www.miztonapp.mx.models;

/**
 * Created by EwanS on 02/10/2016.
 */

public class LoginModel {
    public static int id;
    public static int idpersonal;
    public static String nombre;
    public static String nombre_completo;
    public static String folio_telmex;

    public LoginModel( int id, String nombre, int idpersonal, String nombre_completo, String folio_telmex) {
        this.id = id;
        this.nombre = nombre;
        this.idpersonal = idpersonal;
        this.nombre_completo = nombre_completo;
        this.folio_telmex = folio_telmex;
    }
}