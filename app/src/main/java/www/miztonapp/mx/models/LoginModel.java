package www.miztonapp.mx.models;

/**
 * Created by EwanS on 02/10/2016.
 */

public class LoginModel {
    public static int id;
    public static int idpersonal;
    public static String nombre;

    public LoginModel( int id, String nombre, int idpersonal) {
        this.id = id;
        this.nombre = nombre;
        this.idpersonal = idpersonal;
    }
}