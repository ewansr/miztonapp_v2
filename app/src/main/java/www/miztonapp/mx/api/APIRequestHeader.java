package www.miztonapp.mx.api;

/**
 * Representa un item en los headers de una petición HTTP.
 *
 * @author edmsamuel 22/06/16.
 */
public class APIRequestHeader {

    public String name;
    public String value;

    public APIRequestHeader(String name, String value ) {
        this.name = name.replaceAll( "\\s+", "-" );
        this.value = value;
    }
}
