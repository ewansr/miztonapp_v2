package www.miztonapp.mx.api;

import android.app.AlertDialog;
import android.content.Context;

import www.miztonapp.mx.utilerias.Utils;

/**
 * Created by Sam on 09/07/16.
 */

public class mException extends Exception {

    public mExceptionCode code;
    public String message;
    public String title;

    public mException(mExceptionCode code, String message) {
        super(message);
        this.message = message;
        this.code = code;
        this.title = "Hay problemas";
    }

    public mException(mExceptionCode code, String title, String message) {
        super(message);
        this.code = code;
        this.title = title;
        this.message = message;
    }


    public AlertDialog get_dialog(Context context) {
        return Utils.crear_alerta( context, title, message );
    }


}

