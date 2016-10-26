package www.miztonapp.mx.utilerias;

/**
 * Created by Saulo on 06/10/2016.
 */

import android.app.ProgressDialog;
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
    static String[] directorio_crear;
//    String nombre_directorio_crear;
//    String directorio_fecha;
    private static FTPFile[] lista_archivos;
    private static ProgressDialog progressDialog;


//    public FTPUtils(Context context, String nombre_directorio_crear, String directorio_fecha){
//        this.context = context;
//        this.nombre_directorio_crear = nombre_directorio_crear;
//        this.directorio_fecha = directorio_fecha;
//    }

    public FTPUtils(Context context, String[] directorio_crear){
        this.context = context;
        this.directorio_crear = directorio_crear;
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

    private static void aplicarPermisos(FTPClient ftpClient, String permisos, String directorio_archivo){
        try {
            ftpClient.sendSiteCommand("chmod "+ permisos + " " + directorio_archivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            try {
                conectarFTP(ftpclient,
                        servidor_ftp,
                        puerto,
                        usuario,
                        contrasena);

                Boolean cambio_dir;
                cambio_dir = ftpclient.changeWorkingDirectory("html");
                if (cambio_dir) {
                    cambio_dir = ftpclient.changeWorkingDirectory("images");
                    if (cambio_dir){

                        for (int i= 0; i < directorio_crear.length; i++) {
                            cambio_dir = ftpclient.changeWorkingDirectory(directorio_crear[i]);
                            //Si no existe hay que añadirlo al directorio del server ftp
                            if (!cambio_dir){
                                //Creamos el directorio
                                ftpclient.makeDirectory(directorio_crear[i]);
                                //Aplicamos permiso de lectura y ejecución pública
                                aplicarPermisos(ftpclient, "775", directorio_crear[i]);
                                //Entramos a ese directorio creado
                                cambio_dir = ftpclient.changeWorkingDirectory(directorio_crear[i]);
                                //Comprobamos que no hubo pedos
                                int returnCode = ftpclient.getReplyCode();
                                if (returnCode == 250) {
                                    proceso_exitoso = true;
                                }
                            }else{proceso_exitoso = true;}

                        }

                    }
                }
//                Boolean directorio_cambiado;
//                directorio_cambiado = ftpclient.changeWorkingDirectory("html");
//                directorio_cambiado = ftpclient.changeWorkingDirectory("images");
//
//                //SI es correcto el directorio al que quiero acceder
//                if (!directorio_cambiado) {
//                    throw new mException(mExceptionCode.UNKNOWN, "El recurso solicitado no existe en el servidor");
//                }
//
//                //Si no existe el directorio fecha hay que crearlo
//                directorio_cambiado = ftpclient.changeWorkingDirectory(directorio_fecha);
//                if (!directorio_cambiado) {
//                    ftpclient.makeDirectory(directorio_fecha);
//                    aplicarPermisos(ftpclient, "775", directorio_fecha);
//                    directorio_cambiado = ftpclient.changeWorkingDirectory(directorio_fecha);
//                    int returnCode = ftpclient.getReplyCode();
//                }
//
//                //Crear la carpeta y verificar que existe o no.
//                ftpclient.changeWorkingDirectory(nombre_directorio_crear);
//                int returnCode = ftpclient.getReplyCode();
//
//                //550 archivo/Directorio Inválido
//                if (returnCode == 250) {
//                    proceso_exitoso = true;
//                }
//
//                if (returnCode == 550) {
//                    proceso_exitoso = ftpclient.makeDirectory(nombre_directorio_crear);
//                    aplicarPermisos(ftpclient, "775", nombre_directorio_crear);
//                }
//
//                obtenerRespuestaServidor(ftpclient);
//
//                int codigo_respuesta = ftpclient.getReplyCode();
//
//                if (!FTPReply.isPositiveCompletion(codigo_respuesta)) {
//                    Log.v("Operación fallida-> ", Integer.toString(codigo_respuesta));
//                }

                FTPFile[] subFiles = ftpclient.listFiles();
                lista_archivos  = subFiles;

                ftpclient.logout();
                ftpclient.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return proceso_exitoso;
    }

    @Override
    protected void onPostExecute(Boolean resultado) {
        if (resultado){
            procesoExitoso(lista_archivos);
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, "","Calculando espacio disponible para esta órden... por favor espere",true);
    }

    public abstract void procesoExitoso(FTPFile[] archivos_imagen);
    public abstract void procesoErroneo();

}
