package www.miztonapp.mx.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import www.miztonapp.mx.R;
import www.miztonapp.mx.models.ModelMateriales;

/**
 * Created by EwanS on 27/12/2016.
 */


public class MaterialAdapterListView extends ArrayAdapter<ModelMateriales> {
    public MaterialAdapterListView(Context context, ArrayList<ModelMateriales> materiales) {
        super(context, 0, materiales);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ModelMateriales material = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_base, parent, false);
        }

        TextView tvTitulo   = (TextView) convertView.findViewById(R.id.tv_titulo);
        TextView tvCantidad = (TextView) convertView.findViewById(R.id.tv_cantidad);
        TextView tvUm       = (TextView) convertView.findViewById(R.id.tv_um);

        tvTitulo.setText(material.Nombre );
        tvCantidad.setText(material.CantidadDefault);
        tvUm.setText(material.UnidadMedida);
        return convertView;
    }
}
