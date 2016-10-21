package www.miztonapp.mx.models;

/**
 * Created by EwanS on 02/10/2016.
 */

public class LoginModel {
    public static int id;
    public static int idpersonal;
    public static String nombre;
    public static String nombre_completo;

    public LoginModel( int id, String nombre, int idpersonal, String nombre_completo) {
        this.id = id;
        this.nombre = nombre;
        this.idpersonal = idpersonal;
        this.nombre_completo = nombre_completo;
    }
}