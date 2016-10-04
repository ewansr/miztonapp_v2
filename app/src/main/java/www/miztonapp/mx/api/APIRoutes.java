package www.miztonapp.mx.api;

/**
 * Clase de rutas
 *
 * @author edmsamuel
 */
public class APIRoutes {
//    public static String misMenus = "/mis-menus";
//    public static String miMenuItem = "/mi-menu/";
//
//    public static String miMenu(int id ) {
//        return ( miMenuItem + id );
//    }

    public static String resource_url( String resource ) {
      return "http://104.236.201.168/API_mizton/" + resource;
    }
}
