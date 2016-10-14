package www.miztonapp.mx.utilerias;

/**
 * Created by Saulo on 06/10/2016.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.api.mExceptionCode;

public abstract class FTPUtils extends AsyncTask<String, Void, Boolean> {
    static Context context;
    String nombre_directorio_crear;
    String directorio_fecha;
    int no_archivos;


    public FTPUtils(Context context, String nombre_directorio_crear, String directorio_fecha){
        this.context = context;
        this.nombre_directorio_crear = nombre_directorio_crear;
        this.directorio_fecha = directorio_fecha;
    }

    public static Boolean conectarFTP(FTPClient ftpclient, String servidor, int puerto, String usuario, String contrasena){
        Boolean respuesta = false;
        try {
            ftpclient.connect(servidor, puerto);
            obtenerRespuestaServidor(ftpclient);
            int codigo_respuesta = ftpclient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(codigo_respuesta)) {
                Log.v("Operación fallida-> ", Integer.toString(codigo_respuesta));
                throw new mException(mExceptionCode.UNKNOWN, Integer.toString(codigo_respuesta));
            }

            respuesta = ftpclient.login(usuario, contrasena);
            obtenerRespuestaServidor(ftpclient);
            if (!respuesta) {
                Log.v("Error", "No se obtuvo acceso al servidor");
                throw new mException(mExceptionCode.UNKNOWN, "No se obtuvo acceso al servidor");
            }

            respuesta = true;

        }catch (IOException e) {
            respuesta = false;
//            Utils.crear_alerta(context, "Error", e.getMessage()).show();
        } catch (mException e) {
            respuesta = false;
//            Utils.crear_alerta(context, "Error", e.getMessage()).show();
        }
        return respuesta;
    }


    public static int numArchivos(String directorio_acceder){
        FTPClient ftpClient = new FTPClient();
        int no_archivos = 0;
        conectarFTP(ftpClient,
                    FTPServerConfig.direccion_ip,
                    FTPServerConfig.puerto,
                    FTPServerConfig.usuario,
                    FTPServerConfig.contrasena);

        try {
            Boolean directorio_cambiado = ftpClient.changeWorkingDirectory(directorio_acceder);
            if (directorio_cambiado) {
                FTPFile[] subFiles = ftpClient.listFiles();
                no_archivos = subFiles.length;
            }
        } catch (IOException e) {
            no_archivos = 0;
            Utils.crear_alerta(context, "Error", e.getMessage()).show();
        }
        return no_archivos;
    }

    /**
     * Obtener la respuesta del servidor ftp
     * @param ftpClient
     */
    private static void obtenerRespuestaServidor(FTPClient ftpClient){
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0){
            for (String aReply: replies){
                Log.v("Reply Server -> ", aReply);
            }
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {

        String servidor_ftp = FTPServerConfig.direccion_ip;
        int puerto = FTPServerConfig.puerto;
        String usuario = FTPServerConfig.usuario;
        String contrasena = FTPServerConfig.contrasena;

        Boolean proceso_exitoso = false;

        FTPClient ftpclient = new FTPClient();

        try{
            conectarFTP(ftpclient,
                    servidor_ftp,
                    puerto,
                    usuario,
                    contrasena);

            Boolean directorio_cambiado ;
            directorio_cambiado = ftpclient.changeWorkingDirectory("html");
            directorio_cambiado = ftpclient.changeWorkingDirectory("images");

            //SI es correcto el directorio al que quiero acceder
            if (!directorio_cambiado){
                throw new mException(mExceptionCode.UNKNOWN, "El recurso solicitado no existe en el servidor");
            }

            //Si no existe el directorio fecha hay que crearlo
            directorio_cambiado = ftpclient.changeWorkingDirectory(directorio_fecha);
            if (!directorio_cambiado){
                ftpclient.makeDirectory(directorio_fecha);
                directorio_cambiado =ftpclient.changeWorkingDirectory(directorio_fecha);
            }

            //Crear la carpeta y verificar que existe o no.
            ftpclient.changeWorkingDirectory(nombre_directorio_crear);
            int returnCode = ftpclient.getReplyCode();

            //550 archivo/Directorio Inválido
            if (returnCode == 250) {
                proceso_exitoso = true;
            }
            if (returnCode == 550) {
                proceso_exitoso = ftpclient.makeDirectory(nombre_directorio_crear);
            }

            FTPFile[] subFiles = ftpclient.listFiles();
            no_archivos = subFiles.length;

            ftpclient.logout();
            ftpclient.disconnect();
        } catch (mException e) {
            Utils.crear_alerta(context, "Error", e.getMessage()).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return proceso_exitoso;
    }

    @Override
    protected void onPostExecute(Boolean resultado) {
        if (resultado){
            procesoExitoso(no_archivos);
        }
    }

    public abstract void procesoExitoso(int items_count);
    public abstract void procesoErroneo();

}
