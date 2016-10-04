package www.miztonapp.mx.requests;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import www.miztonapp.mx.api.Request;
import www.miztonapp.mx.models.LoginModel;
import www.miztonapp.mx.api.*;

/**
 * Created by EwanS on 02/10/2016.
 */

public abstract class RequestLogin extends AppCompatActivity {

    public void iniciar_sesion(String usuario, String contrasena) throws JSONException {
        try{
            Request request = new Request(){
                @Override public void RequestBeforeExecute() {
                    loginAutenticacionBeforeLoad();
                }

                @Override public void RequestCompleted(JSONObject response) {
                    try {

                        JSONObject usuario_response = null;
                        int valid = response.getInt( "logueo_valido" );

                        if ( valid != 1 ) {
                            throw new mException( mExceptionCode.INVALID_VALUES, response.getString( "message" ) );
                        }else{
                            usuario_response = response.getJSONObject( "master_usuarios" );
                        }

                        loginAutenticacionExitosa( new LoginModel(
                               usuario_response.getInt( "Idusuario" ),
                                usuario_response.getString( "Usuario")
                        ) );

                    } catch ( mException error ) {
                        loginAutenticacionErronea( error );
                    } catch ( Exception error ) {
                        loginAutenticacionErronea( new mException( mExceptionCode.UNKNOWN, error.getMessage() ) );
                    }
                }

                @Override public void RequestError(Exception error ) {
                    loginAutenticacionErronea( new mException( mExceptionCode.UNKNOWN, error.getMessage() ) );
                }
            };

            JSONObject login = new JSONObject();
            String route = "usuarios/login";

            login.put( "correo", usuario );
            login.put( "contrasena", contrasena );
            request.post( route, login );
        }catch (Exception error){
            loginAutenticacionErronea( new mException( mExceptionCode.UNKNOWN, error.getMessage() ) );
        }

    }

    public abstract void loginAutenticacionBeforeLoad();

    /**
     * Invocado cuando la autenticación es exitosa
     * @param usuario APILoginModel
     */
    public abstract void loginAutenticacionExitosa( LoginModel usuario );

    /**
     * Invocado cuando hay errores en la request ó en la autenticación
     * @param error mException
     */
    public abstract void loginAutenticacionErronea(mException error);

}
