package www.miztonapp.mx.adapters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import www.miztonapp.mx.GaleriaActivity;
import www.miztonapp.mx.ImagenActivity;
import www.miztonapp.mx.R;
import www.miztonapp.mx.models.ModelGaleria;

/**
 * Created by Saulo on 31/08/2016.
 */
public class GaleriaRecyclerAdapter extends RecyclerView.Adapter<GaleriaRecyclerAdapter.ordenesViewHolder> {
    private ArrayList<ModelGaleria> items_galeria;
    private static ArrayList<Image> lista_imagen = null;
    private final Context context;
    private static String numero_telefono;
    private static String fecha_orden;
    private static String nombre_archivo;
    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;

    public GaleriaRecyclerAdapter(ArrayList<ModelGaleria> items_galeria, Context context ){
        this.items_galeria = items_galeria;
        this.context = context;
    }

    @Override
    public String toString() {
        return nombre_archivo;
    }


    @Override
    public ordenesViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.cardview_galeria, parent, false );
        ordenesViewHolder rentasHolder = new ordenesViewHolder(v);
        return rentasHolder;
    }

    @Override
    public void onBindViewHolder(ordenesViewHolder holder, int position ) {
        //Se usa una dependencia para el cacheo de imagenes dentro de ImageView
        Picasso.with( context )
                .load(items_galeria.get( position ).ruta_imagen.replace(" ", "%20"))
                .error( R.drawable.briefcase_material )
                .resize(50, 50)
                .noFade()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder( R.drawable.progress_animation )
                .into( holder.cv_image );

        holder.url_imagen = items_galeria.get(position).ruta_imagen.replace(" ", "%20");
        holder.telefono   = items_galeria.get(position).numero;
        nombre_archivo = items_galeria.get(position).nombre_imagen;


        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        Random rnd = new Random();
        int randomColor = rnd.nextInt(8);
        holder.cv.setCardBackgroundColor(androidColors[randomColor]);
    }

    @Override
    public int getItemCount() {
        return items_galeria.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView ){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ordenesViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        ImageView cv_image;
        Button btn_cambiar;
        public String url_imagen;
        public String telefono;
        public String fecha;

        ordenesViewHolder(View itemView) {
            super(itemView);
            cv = ( CardView )  itemView.findViewById( R.id.cv );
            cv_image = (ImageView) itemView.findViewById( R.id.cv_image_galeria );
            btn_cambiar = (Button) itemView.findViewById( R.id.btn_cambiar );
            itemView.setOnClickListener( clickListener );
            btn_cambiar.setOnClickListener( clickListener );
            url_imagen = null;
        }

        public View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
            if (v.getId() == R.id.rl_cv){
                Intent intent = new Intent(context, ImagenActivity.class);
                intent.putExtra("url_imagen", url_imagen);
                context.startActivity(intent);
            }

            if (v.getId() == R.id.btn_cambiar) {
                abrir_galeria();
            }
            }
        };

        public void abrir_galeria(){
            GaleriaActivity.nombre_original_archivo = nombre_archivo;
            Intent intent = new Intent(context, AlbumSelectActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 1);
            ((Activity)context).startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    }




    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));


        GaleriaActivity.imageUri = Uri.fromFile(photo);
        GaleriaActivity.nombre_original_archivo = nombre_archivo;
        ((Activity)context).startActivityForResult(intent, TAKE_PICTURE);
    }
}
