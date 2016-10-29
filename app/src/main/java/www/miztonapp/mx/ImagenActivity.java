package www.miztonapp.mx;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import www.miztonapp.mx.utilerias.SaveWallpaperAsync;

public class ImagenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);

        Bundle bundle = getIntent().getExtras();
        String url_imagen = bundle.getString("url_imagen");

        ImageView imagen_evidencia = (ImageView) findViewById(R.id.imagen_evidencia);
//        Picasso.with( this )
//                .load(url_imagen)
//                .error( R.drawable.briefcase_material )
//                .resize(1024, 768)
//                .noFade()
//                .placeholder( R.drawable.progress_animation )
//                .into( imagen_evidencia );

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
}
