package www.miztonapp.mx.utilerias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.models.Image;

import net.gotev.uploadservice.Placeholders;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.ftp.FTPUploadRequest;
import net.gotev.uploadservice.ftp.UnixPermissions;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import www.miztonapp.mx.R;

/**
 * Created by EwanS on 17/08/2016.
 */
public class Utils  {

    /*
     *@Context Parametro contexto de activity
     */
    static String ruta_absoluta_archivo;
    private static ProgressDialog progressDialog;

    public static void subir_imagenes_ftp(final Context context, final ArrayList<Image> lista_rutas, String[] directorio_crear) {
        progressDialog = new ProgressDialog( context );
        progressDialog.setMessage( "Procesando imagenes... Espere" );
        progressDialog.show();
        // Solicitar la creción del directorio en el servidor FTP

        final FTPUtils ftpUtils = new FTPUtils(context, directorio_crear) {
            @Override
            public void procesoExitoso(FTPFile[] archivos_imagen) {
                autorizarCarga(context, lista_rutas, directorio_crear, archivos_imagen.length);
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void procesoErroneo() {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        };
        ftpUtils.execute();
    }

    private static void autorizarCarga(Context context, ArrayList<Image> lista_rutas, String[] directorio_crear, int iniciar_en){
        try {
            int i = 0;


            Utils.crear_toast(context, "Inicializando carga de fotos...").show();
            for (i = 0; i<lista_rutas.size(); i++){
                String ruta_archivo = lista_rutas.get(i).path.toString();
                Boolean fallo;
                File archivo_origen = new File(ruta_archivo);
                int sufijo_contador = i + iniciar_en+1;

                // se hace la siguiente operacion para no reemplazar los archivos ya existentes
                //i+iniciar_en
                if(clonar_imagen(archivo_origen, directorio_crear[2] + "_"+Integer.toString(sufijo_contador)+".JPG", context)){
                    fallo = false;
                    Log.i("renombrando archivo", "Success");
                } else {
                    fallo = true;
                    Log.i("renombrando archivo", "Fail");
                    //Si no se pudo hacemos el ultimo intento
                    //Tratando de copiar el archivo
                }


                //Si no hay falla
                if (!fallo) {

                    UploadService.UPLOAD_POOL_SIZE = 1;
                    UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();
                    uploadNotificationConfig.setTitle("Subiendo a Mizton Server");
                    uploadNotificationConfig.setInProgressMessage("Transfiriendo ("+Integer.toString(i+1)+"/"+Integer.toString(lista_rutas.size())+") a "+ Placeholders.UPLOAD_RATE + " (" + Placeholders.PROGRESS + ")");
                    uploadNotificationConfig.setIcon(R.drawable.colection_material);
                    uploadNotificationConfig.setErrorMessage("No se pudo cargar el archivo al servidor");
                    uploadNotificationConfig.setCompletedMessage("Archivo cargado.");
                    uploadNotificationConfig.setRingToneEnabled(true);

                    UnixPermissions unixPermissions = new UnixPermissions("755");

                    String uploadId = new FTPUploadRequest(context, "104.236.201.168", 21)
                            .setUsernameAndPassword("ewansr", "saul2007#")
                            .addFileToUpload(ruta_absoluta_archivo, "/html/images/" + directorio_crear[0] + "/" + directorio_crear[1]  + "/" + directorio_crear[2] + "/", unixPermissions)
                            .setNotificationConfig(uploadNotificationConfig)
                            .setAutoDeleteFilesAfterSuccessfulUpload(true)
                            .setMaxRetries(10)
                            .startUpload();
                }
            }
        } catch (Exception exc) {
            Log.e("AndroidUploadService", exc.getMessage(), exc);
        }
    }


    public static boolean clonar_imagen(File origen, String nombre_archivo, Context context) {
        Boolean exitoso = false;
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = context.checkCallingOrSelfPermission(permission);

        if (res == PackageManager.PERMISSION_GRANTED) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(origen.getAbsolutePath(), options);
            exitoso = storeImage(bitmap, context, nombre_archivo);
        }
        return exitoso;
    }

    private static Boolean storeImage(Bitmap image, Context context, String nombre_archivo ) {
        File pictureFile = getOutputMediaFile(context, nombre_archivo);
        Boolean estatus = false;
        if (pictureFile == null) {
            estatus = false;
            Log.d("Error imagen",
                    "Error al crear el archivo, revisa los permisos de almacenamiento: ");// e.getMessage());
        }else {
            try {
                ruta_absoluta_archivo = null;
                FileOutputStream fos = new FileOutputStream(pictureFile);
                image.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                fos.close();
                ruta_absoluta_archivo = pictureFile.getAbsolutePath();
                estatus = true;
            } catch (FileNotFoundException e) {
                estatus = false;
                Log.d("Error imagen", "Archivo no encontrado: " + e.getMessage());
            } catch (IOException e) {
                estatus = false;
                Log.d("Error imagen", "Error al acceder al archivo " + e.getMessage());
            }
        }
        return estatus;
    }

    private static File getOutputMediaFile(Context context, String nombre_archivo){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
//        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName=nombre_archivo;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }




    /**
     * Para el manejo de strings
     */
    public static Boolean isEquals(String string, String stringCompare){
        return new String(string).equals(stringCompare);
    }

    public static Toast crear_toast(Context context, String mensaje){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate( R.layout.custom_toast, null );
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.custom_toast_container);

        TextView text = (TextView) linearLayout.findViewById(R.id.text);
        text.setText(mensaje);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(linearLayout);
        return toast;
    }


    /**
     * para el manejo de ventanas ( Layouts )
     */



    /** Esta Función colorea la barra de estado en caso de ser versión compatible*/
    public static void setStatusColor(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static void showMessage( String title, String message, Context context, AlertDialog.Builder builder ){
            builder = new AlertDialog.Builder( context );
            builder
                    .setTitle( title )
                    .setMessage( message )
                    .create()
                    .show();
    }

    public static void showMessage( String title, String message, Context context ){
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder
                .setTitle( title )
                .setMessage( message )
                .create()
                .show();
    }

    public static android.app.AlertDialog crear_alerta(Context context, String titulo, String mensaje ) {
        final android.app.AlertDialog.Builder builder;
        builder = new android.app.AlertDialog.Builder( context );
        builder.setTitle( titulo );
        builder.setMessage( mensaje );

        builder.setPositiveButton( "Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        return builder.create();
    }

    public static void showLoadMessage( String message, ProgressDialog progressDialog, Context context ){
        progressDialog = new ProgressDialog( context );
        progressDialog.setMessage( message );
        progressDialog.show();
    }

    public static void showLoadMessage( String message, ProgressDialog progressDialog ){
        progressDialog.setMessage( message );
        progressDialog.show();
    }


    public static class LoadRemoteImg extends AsyncTask<String, Void, Bitmap> {
        private Exception exception;
        private ImageView img;

        public LoadRemoteImg(ImageView img){
            this.img = img;
        }

        protected Bitmap doInBackground(String... params) {
            try {

                URL url = null;
                try {
                    url = new URL(params[0].toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return bmp;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(Bitmap bmp) {
            if (bmp != null && img != null) {
                img.setImageBitmap(bmp);
            }
        }
    }
}

