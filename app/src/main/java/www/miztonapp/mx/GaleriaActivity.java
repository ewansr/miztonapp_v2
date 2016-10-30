package www.miztonapp.mx;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import net.gotev.uploadservice.Placeholders;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.ftp.FTPUploadRequest;
import net.gotev.uploadservice.ftp.UnixPermissions;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import www.miztonapp.mx.adapters.GaleriaRecyclerAdapter;
import www.miztonapp.mx.models.ModelGaleria;
import www.miztonapp.mx.utilerias.FTPServerConfig;
import www.miztonapp.mx.utilerias.Utils;

import static www.miztonapp.mx.utilerias.Utils.clonar_imagen;

public class GaleriaActivity extends AppCompatActivity {
    private static RecyclerView recyclerView;
    private static StaggeredGridLayoutManager staggeredGridLayoutManager;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static Bundle bundle_info_orden;
    private static String telefono;
    private static String usuario;
    private static String fecha;
    private static final int TAKE_PICTURE = 1;
    public static Uri imageUri;
    public static String nombre_original_archivo;
    GaleriaRecyclerAdapter adaptador_galeria;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimaryDark,
                R.color.colorAccent
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        staggeredGridLayoutManager= new StaggeredGridLayoutManager(2,1);
        recyclerView       = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        FTPFile[] ftpFile = (FTPFile[]) getIntent().getSerializableExtra("FTPFile");
        telefono = getIntent().getStringExtra("telefono");
        usuario = getIntent().getStringExtra("usuario");
        fecha = getIntent().getStringExtra("fecha");

        adaptador_galeria = new GaleriaRecyclerAdapter(construirAdaptador(ftpFile),GaleriaActivity.this);
        recyclerView.setAdapter(adaptador_galeria);
    }

    private ArrayList<ModelGaleria> construirAdaptador(FTPFile[] archivos){
        ArrayList<ModelGaleria> items = new ArrayList<ModelGaleria>();
        for(int i=0; i<archivos.length; i++){
            String ruta = "http://"+ FTPServerConfig.ruta_base_imagenes + fecha + "/" +
                    usuario + "/" + telefono + "/" + archivos[i].getName();
            items.add(new ModelGaleria(telefono, ruta, archivos[i].getName()));
        }
        return items;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Adaptador Ordenes", "onActivityResult");

        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            final ArrayList<Image> lista_imagen  = images;

            FTPServerConfig.ruta_crear_ftp[0] = fecha;
            FTPServerConfig.ruta_crear_ftp[2] = telefono;
            FTPServerConfig.ruta_crear_ftp[1] = usuario;;

            AlertDialog.Builder builder = new AlertDialog.Builder(GaleriaActivity.this);
            builder.setMessage("¿Estas seguro que deseas sustituir la imagen?")
                    .setCancelable(false)
                    .setPositiveButton("Sustituir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            subirImagen(lista_imagen);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void subirImagen(ArrayList<Image> imagen){
        Boolean fallo;
        File archivo_origen = new File(imagen.get(0).path.toString());
        if(clonar_imagen(archivo_origen, nombre_original_archivo, this)){
            fallo = false;
            Log.i("renombrando archivo", "Success");
        } else {
            fallo = true;
            Log.i("renombrando archivo", "Fail");
            //Si no se pudo hacemos el ultimo intento
            //Tratando de copiar el archivo
        }

        if (!fallo){
            UploadService.UPLOAD_POOL_SIZE = 1;
            UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();
            uploadNotificationConfig.setTitle("Subiendo a Mizton Server");
            uploadNotificationConfig.setInProgressMessage("Transfiriendo (1/1) a " + Placeholders.UPLOAD_RATE + " (" + Placeholders.PROGRESS + ")");
            uploadNotificationConfig.setIcon(R.drawable.colection_material);
            uploadNotificationConfig.setErrorMessage("No se pudo cargar el archivo al servidor");
            uploadNotificationConfig.setCompletedMessage("Archivo cargado.");
            uploadNotificationConfig.setRingToneEnabled(true);

            UnixPermissions unixPermissions = new UnixPermissions("755");

            try {
                String uploadId = new FTPUploadRequest(this, FTPServerConfig.direccion_ip, 21)
                        .setUsernameAndPassword(FTPServerConfig.usuario, FTPServerConfig.contrasena)
                        .addFileToUpload(Utils.ruta_absoluta_archivo, "/html/images/" + fecha + "/" + usuario  + "/" + telefono + "/", unixPermissions)
                        .setNotificationConfig(uploadNotificationConfig)
                        .setAutoDeleteFilesAfterSuccessfulUpload(true)
                        .setMaxRetries(10)
                        .startUpload();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    //esto es para la foto no borrar
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case TAKE_PICTURE:
//                if (resultCode == Activity.RESULT_OK) {
//                    Uri selectedImage = imageUri;
//                    getContentResolver().notifyChange(selectedImage, null);
//                    ContentResolver cr = getContentResolver();
//                    Bitmap bitmap;
//                    try {
//                        bitmap = android.provider.MediaStore.Images.Media
//                                .getBitmap(cr, selectedImage);
//
//                        Intent intent = new Intent(this, ImagenActivity.class);
//                        String dato = nombre_original_archivo;
//                        intent.putExtra("url_foto", imageUri.toString());
//                        intent.putExtra("telefono", telefono);
//                        intent.putExtra("usuario", usuario);
//                        intent.putExtra("fecha", fecha);
//                        intent.putExtra("nombre_original_archivo", nombre_original_archivo);
//                        startActivity(intent);
//
//                        Toast.makeText(this, selectedImage.toString(),
//                                Toast.LENGTH_LONG).show();
//                    } catch (Exception e) {
//                        Toast.makeText(this, "Falla al cargar la imagen", Toast.LENGTH_SHORT)
//                                .show();
//                        Log.e("Camera", e.toString());
//                    }
//                }
//        }
//    }
}
