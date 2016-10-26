package www.miztonapp.mx.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.util.ArrayList;

import www.miztonapp.mx.R;
import www.miztonapp.mx.activity_ordenes_detalle;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.utilerias.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Saulo on 31/08/2016.
 */
public class OrdenesRecyclerAdapter extends RecyclerView.Adapter<OrdenesRecyclerAdapter.ordenesViewHolder>{
    private ArrayList<ModelOrdenesTrabajo> items_orden_trabajo;
    private static ArrayList<Image> lista_imagen = null;
    private final Context context;
    private static String numero_telefono;
    private static String fecha_orden;

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
        holder.cv_titulo.setText(items_orden_trabajo.get(position).telefono_orden);
        holder.cv_detalle.setText(items_orden_trabajo.get(position).folio_orden);
        holder.cv_tipo.setText(items_orden_trabajo.get(position).tipo_instalacion);
        holder.cv_estatus.setText(items_orden_trabajo.get(position).estatus_orden);
        holder.cv_fecha.setText(items_orden_trabajo.get(position).fecha.substring(0,10));
        holder.cv_tipo_orden.setText(items_orden_trabajo.get(position).tipo_orden);
        holder.telefono = items_orden_trabajo.get(position).telefono_orden;
        holder.fecha    = items_orden_trabajo.get(position).fecha;
        holder.tipo_instalacion    = items_orden_trabajo.get(position).tipo_instalacion;

        if ( items_orden_trabajo.get(position).tipo_instalacion.equals("FO") ){
            holder.cv_image.setImageResource(R.drawable.fiber_icon_material);
        }else{
            holder.cv_image.setImageResource(R.drawable.ethernet_icon_material);
        }

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
        TextView cv_tipo_orden;
        TextView cv_tipo;
        TextView cv_fecha;
        TextView cv_estatus;
        ImageView cv_image;
        Button btn_detalle;
        String telefono;
        String fecha;
        String tipo_instalacion;

        ordenesViewHolder(View itemView) {
            super(itemView);

            cv              = ( CardView )  itemView.findViewById( R.id.cv );
            cv_titulo       = ( TextView )  itemView.findViewById( R.id.cv_titulo );
            cv_detalle      = ( TextView )  itemView.findViewById( R.id.cv_detalle );
            cv_tipo_orden   = ( TextView )  itemView.findViewById( R.id.cv_tipo_orden );
            cv_tipo         = ( TextView )  itemView.findViewById( R.id.cv_tipo );
            cv_fecha        = ( TextView )  itemView.findViewById( R.id.cv_fecha );
            cv_estatus      = ( TextView )  itemView.findViewById( R.id.cv_estatus );
            cv_image        = ( ImageView ) itemView.findViewById( R.id.cv_image );
            btn_detalle     = ( Button )    itemView.findViewById( R.id.btn_cambiar);

            ImageButton subir_imagen = (ImageButton) itemView.findViewById(R.id.btn_subir);
            btn_detalle.setOnClickListener(clickListener);
            subir_imagen.setOnClickListener(clickListener);
            itemView.setOnClickListener( clickListener );
        }


        public View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
            if (v.getId() == R.id.btn_subir){
                abrir_galeria();
            }
            if (v.getId() == R.id.btn_cambiar){
                abrir_detalle(context);
            }
            }
        };

        public void abrir_detalle(Context context){
            Intent i = new Intent(context, activity_ordenes_detalle.class);
            i.putExtra("telefono", telefono);
            i.putExtra("fecha", fecha);
            i.putExtra("tipo_instalacion", tipo_instalacion);

            context.startActivity(i);
        }

        public void abrir_galeria(){
            numero_telefono = telefono;
            fecha_orden     = fecha;

//            int archivos_permitidos = FTPUtils.numArchivos("/html/images/"+fecha_orden+"/"+numero_telefono);


            Intent intent = new Intent(context, AlbumSelectActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
            ((Activity)context).startActivityForResult(intent, Constants.REQUEST_CODE);
        }

    }

//    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d("Adaptador Ordenes", "onActivityResult");
//
//        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
//            lista_imagen = images;
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setMessage("¿Estas seguro que deseas subir esas imagenes?")
//                    .setCancelable(false)
//                    .setPositiveButton("Subir", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            Utils.subir_imagenes_ftp(context,lista_imagen, numero_telefono, fecha_orden.substring(0,10));
//                        }
//                    })
//                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alert = builder.create();
//            alert.show();
//        }
//    }


}
