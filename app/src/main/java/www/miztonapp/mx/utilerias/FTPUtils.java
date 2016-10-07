package www.miztonapp.mx.utilerias;

/**
 * Created by Saulo on 06/10/2016.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.api.mExceptionCode;

public abstract class FTPUtils extends AsyncTask<String, Void, Boolean> {
    Context context;
    String nombre_directorio_crear;

    public FTPUtils(Context context, String nombre_directorio_crear){
        this.context = context;
        this.nombre_directorio_crear = nombre_directorio_crear;
    }

    private static void showServerReply(FTPClient ftpClient){
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0){
            for (String aReply: replies){
                Log.v("Reply Server -> ", aReply);
            }
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String servidor_ftp = "104.236.201.168";
        int puerto = 21;
        String usuario = "ewansr";
        String contrasena = "saul2007#";
        Boolean exitoso = false;
        FTPClient ftpclient = new FTPClient();
        try{
            ftpclient.connect(servidor_ftp, puerto);
            showServerReply(ftpclient);
            int codigo_respuesta = ftpclient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(codigo_respuesta)){
                Log.v("Operación fallida, codigo de respuesta -> ", Integer.toString(codigo_respuesta));
                throw new mException(mExceptionCode.UNKNOWN, Integer.toString(codigo_respuesta));
            }

            exitoso = ftpclient.login(usuario, contrasena);
            showServerReply(ftpclient);
            if (!exitoso){
                Log.v("Error","No se obtuvo acceso al servidor");
                throw new mException(mExceptionCode.UNKNOWN, "No se obtuvo acceso al servidor");
            }

            Boolean success = ftpclient.changeWorkingDirectory("html");
            success = ftpclient.changeWorkingDirectory("images");
            //SI es correcto el directorio al que quiero acceder
            if (!success){

                throw new mException(mExceptionCode.UNKNOWN, "El recurso solicitado no existe en el servidor");
            }

            //Antes de hacer el desmadre y crear la carpeta
            //verificar que existe o no.
            ftpclient.changeWorkingDirectory(nombre_directorio_crear);
            int returnCode = ftpclient.getReplyCode();

            //550 archivo/Directorio Inválido
            if (returnCode == 550) {
                exitoso = ftpclient.makeDirectory(nombre_directorio_crear);
            }
            ftpclient.logout();
            ftpclient.disconnect();

        } catch (mException e) {
            Utils.crear_alerta(context, "Error", e.getMessage()).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exitoso;
    }

    @Override
    protected void onPostExecute(Boolean resultado) {
        if (resultado){
            procesoExitoso();
        }
    }

    public abstract void procesoExitoso();
}
