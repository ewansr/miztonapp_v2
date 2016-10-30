package www.miztonapp.mx;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.Placeholders;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.ftp.FTPUploadRequest;
import net.gotev.uploadservice.ftp.UnixPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import www.miztonapp.mx.utilerias.FTPServerConfig;
import www.miztonapp.mx.utilerias.SaveWallpaperAsync;

import static www.miztonapp.mx.utilerias.Utils.clonar_imagen;

public class ImagenActivity extends AppCompatActivity {
    String url_foto_local = null;
    String telefono = null;
    String fecha = null;
    String usuario = null;
    String nombre_original_archivo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);

        Bundle bundle = getIntent().getExtras();
        String url_imagen = bundle.getString("url_imagen");
        url_foto_local = bundle.getString("url_foto");

        telefono = bundle.getString("telefono");
        fecha = bundle.getString("fecha");
        usuario = bundle.getString("usuario");
        nombre_original_archivo = bundle.getString("nombre_original_archivo");

        ImageView imagen_evidencia = (ImageView) findViewById(R.id.imagen_evidencia);
        Button btn_sustituir = (Button) findViewById(R.id.btn_sustituir);

        btn_sustituir.setVisibility(View.GONE);
        if ((url_imagen == null) && (url_foto_local != null)) {
            Picasso.with(this)
                    .load(url_foto_local)
                    .error(R.drawable.briefcase_material)
                    .noFade()
                    .placeholder(R.drawable.progress_animation)
                    .into(imagen_evidencia);
            btn_sustituir.setVisibility(View.VISIBLE);
        }

        if (url_imagen != null) {
            SaveWallpaperAsync saveWallpaperAsync = new SaveWallpaperAsync(this) {
                @Override
                public void onPostExec(String uri) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri imgUri = Uri.parse("file://" + uri);
                    intent.setDataAndType(imgUri, "image/*");
                    startActivity(intent);
                    finish();
                }
            };
            saveWallpaperAsync.execute(url_imagen);
        }

        btn_sustituir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Boolean fallo;
                File archivo_origen = new File(url_foto_local);
                if(clonar_imagen(archivo_origen, nombre_original_archivo, ImagenActivity.this)){
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
                    uploadNotificationConfig.setInProgressMessage("Transfiriendo (1/1) a" + Placeholders.UPLOAD_RATE + " (" + Placeholders.PROGRESS + ")");
                    uploadNotificationConfig.setIcon(R.drawable.colection_material);
                    uploadNotificationConfig.setErrorMessage("No se pudo cargar el archivo al servidor");
                    uploadNotificationConfig.setCompletedMessage("Archivo cargado.");
                    uploadNotificationConfig.setRingToneEnabled(true);

                    UnixPermissions unixPermissions = new UnixPermissions("755");

                    try {
                        String uploadId = new FTPUploadRequest(ImagenActivity.this, FTPServerConfig.direccion_ip, 21)
                                .setUsernameAndPassword(FTPServerConfig.usuario, FTPServerConfig.contrasena)
                                .addFileToUpload(url_foto_local, "/html/images/" + fecha + "/" + usuario  + "/" + telefono + "/", unixPermissions)
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
        });
    }
}
