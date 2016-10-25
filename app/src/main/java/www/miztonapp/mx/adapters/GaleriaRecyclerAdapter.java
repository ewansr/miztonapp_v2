package www.miztonapp.mx.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.darsh.multipleimageselect.models.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

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
                .load(items_galeria.get( position ).ruta_imagen)
                .error( R.drawable.picture_ftp )
                .placeholder( R.drawable.picture_ftp )
                .into( holder.cv_image );


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
        ordenesViewHolder(View itemView) {
            super(itemView);
            cv = ( CardView )  itemView.findViewById( R.id.cv );
            cv_image = (ImageView) itemView.findViewById( R.id.cv_image );
            itemView.setOnClickListener( clickListener );
        }


        public View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
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
