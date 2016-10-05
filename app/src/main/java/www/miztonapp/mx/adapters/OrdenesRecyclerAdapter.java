package www.miztonapp.mx.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import www.miztonapp.mx.R;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;

/**
 * Created by Saulo on 31/08/2016.
 */
public class OrdenesRecyclerAdapter extends RecyclerView.Adapter<OrdenesRecyclerAdapter.ordenesViewHolder>{
    private ArrayList<ModelOrdenesTrabajo> items_orden_trabajo;
    private Context context;

    public OrdenesRecyclerAdapter(ArrayList< ModelOrdenesTrabajo > items_orden_trabajo, Context context ){
        this.items_orden_trabajo = items_orden_trabajo;
        this.context = context;
    }

    @Override
    public ordenesViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.cardview_ordenes, parent, false );
        ordenesViewHolder rentasHolder = new ordenesViewHolder(v);
        return rentasHolder;
    }

    @Override
    public void onBindViewHolder(ordenesViewHolder holder, int position ) {
        holder.cv_titulo.setText(items_orden_trabajo.get(position).folio_orden);
        holder.cv_detalle.setText(items_orden_trabajo.get(position).telefono_orden);
        holder.cv_direccion.setText(items_orden_trabajo.get(position).tipo_orden);


        //Se usa una dependencia para el cacheo de imagenes dentro de ImageView

//        Picasso.with( context )
//                .load(items_orden_trabajo.get( position ).equipo_img)
//                .error( R.drawable.crane_express )
//                .placeholder( R.drawable.crane_express )
//                .into( holder.cv_image );


//        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
//        Random rnd = new Random();
//        int randomColor = rnd.nextInt(8);
//        holder.cv.setCardBackgroundColor(androidColors[randomColor]);
    }

    @Override
    public int getItemCount() {
        return items_orden_trabajo.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView ){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ordenesViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView cv_titulo;
        TextView cv_detalle;
        TextView cv_direccion;

        ordenesViewHolder(View itemView) {
            super(itemView);

            cv          = (CardView)  itemView.findViewById( R.id.cv );
            cv_titulo   = ( TextView )  itemView.findViewById( R.id.cv_titulo );
            cv_detalle  = ( TextView )  itemView.findViewById( R.id.cv_detalle );
            cv_titulo   = ( TextView )  itemView.findViewById( R.id.cv_direccion );

            itemView.setOnClickListener( clickListener );
        }

        public View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

            }
        };

    }

}
