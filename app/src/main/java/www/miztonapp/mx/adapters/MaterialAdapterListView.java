package www.miztonapp.mx.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import www.miztonapp.mx.R;
import www.miztonapp.mx.models.ModelMateriales;

import static www.miztonapp.mx.utilerias.Utils.isEquals;

/**
 * Created by EwanS on 27/12/2016.
 */


public class MaterialAdapterListView extends ArrayAdapter<ModelMateriales> {
    Context context;
    public MaterialAdapterListView(Context context, ArrayList<ModelMateriales> materiales) {
        super(context, 0, materiales);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ModelMateriales material = getItem(position);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Captura");



       convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_base, parent, false);


        TextView tvTitulo   = (TextView) convertView.findViewById(R.id.tv_titulo);
        final TextView tvCantidad = (TextView) convertView.findViewById(R.id.tv_cantidad);
        TextView tvUm       = (TextView) convertView.findViewById(R.id.tv_um);
        final RelativeLayout relative = (RelativeLayout) convertView.findViewById(R.id.relative_principal);

        tvTitulo.setText(material.Nombre );
        tvCantidad.setText(material.CantidadDefault);
        tvUm.setText(material.UnidadMedida);

        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(context);
                input.setSingleLine(true);
                input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setMessage(material.Nombre + " " + material.UnidadMedida);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.briefcase_material);

                alertDialog.setPositiveButton("Añadir",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isEquals(input.getText().toString(),"")){
                                    material.CantidadDefault = input.getText().toString();
                                    notifyDataSetChanged();
                                }
                            }
                        });

                alertDialog.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });



                alertDialog.show();
            }
        });



        return convertView;
    }
}
