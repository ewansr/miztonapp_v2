package www.miztonapp.mx.SQL;

/**
 * Created by Saulo on 18/07/2016.
 */
public class SQLiteDBConfig {
    public static String DATABASE_NAME = "dbmizton.db";
    public static int DATABASE_VERSION = 2;
    public static final String TABLA_USUARIO = "perfil_usuario";
    public static final String STRING_TYPE    = "text";
    public static final String INT_TYPE       = "integer";

    public static class cols_user {
        public static final String ID_QUOTES        = "id";
        public static final String NOMBRE           = "nombre";
        public static final String IDPERSONAL       = "idpersonal";
        public static final String CODIGO_PERSONAL  = "codigo_personal";
        public static final String FOLIO_TELMEX     = "folio_telmex";
        public static final String USUARIO          = "usuario";
    }

    public static final String CREATE_USER =
            "create table "+ TABLA_USUARIO + "(" +
                    cols_user.ID_QUOTES        + " " + INT_TYPE + "," +
                    cols_user.NOMBRE + " " + STRING_TYPE + " null," +
                    cols_user.USUARIO + " " + STRING_TYPE + " null, " +
                    cols_user.IDPERSONAL + " " + INT_TYPE + " null, " +
                    cols_user.CODIGO_PERSONAL     + " " + STRING_TYPE + " null, " +
                    cols_user.FOLIO_TELMEX + " " + STRING_TYPE +  " null )";


    public static String user_seeder = "INSERT INTO " + TABLA_USUARIO + " VALUES( -1, '', -1, '', '', '' )";

}
