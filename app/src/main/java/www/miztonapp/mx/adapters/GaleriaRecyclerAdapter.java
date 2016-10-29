package www.miztonapp.mx.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.model.ImageVideoWrapperEncoder;
import com.darsh.multipleimageselect.models.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import www.miztonapp.mx.ImagenActivity;
import www.miztonapp.mx.R;
import www.miztonapp.mx.models.ModelGaleria;

/**
 * Created by Saulo on 31/08/2016.
 */
public class GaleriaRecyclerAdapter extends RecyclerView.Adapter<GaleriaRecyclerAdapter.ordenesViewHolder>{
    private ArrayList<ModelGaleria> items_galeria;
    private static ArrayList<Image> lista_imagen = null;
    private final Context context;
    private static String numero_telefono;
    private static String fecha_orden;

    public GaleriaRecyclerAdapter(ArrayList<ModelGaleria> items_galeria, Context context ){
        this.items_galeria = items_galeria;
        this.context = context;
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
                .placeholder( R.drawable.progress_animation )
                .into( holder.cv_image );

        holder.url_imagen = items_galeria.get(position).ruta_imagen.replace(" ", "%20");

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
        public String url_imagen;

        ordenesViewHolder(View itemView) {
            super(itemView);
            cv = ( CardView )  itemView.findViewById( R.id.cv );
            cv_image = (ImageView) itemView.findViewById( R.id.cv_image_galeria );
            itemView.setOnClickListener( clickListener );
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

            if (v.getId() == R.id.btn_subir){
//                    abrir_galeria();
            }
            if (v.getId() == R.id.btn_cambiar){
//                    abrir_detalle(context);
            }
            }
        };
    }



}
