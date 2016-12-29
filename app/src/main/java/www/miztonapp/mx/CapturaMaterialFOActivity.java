package www.miztonapp.mx;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import www.miztonapp.mx.adapters.MaterialAdapterListView;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.models.ModelMateriales;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.requests.RequestMateriales;
import www.miztonapp.mx.requests.RequestOrdenesTrabajo;
import www.miztonapp.mx.utilerias.Utils;

public class CapturaMaterialFOActivity extends AppCompatActivity {
    private String IdModem;
    private String IdFibra;
    private String IdCinturon;
    private String _id;
    Toolbar toolbar;
    MaterialBetterSpinner spModem, spFibra, spFibraExtra, spCinturones;
    ListView listView;
    private static final String[] ESTATUS = new String[] {
            "Liquidada", "Objetada", "Queja", "Retornada"
    };

    private static final String[] FIBRA_EXTRA = new String[] {
            "NINGUNA",
            "25M",
            "50M",
            "75M",
            "125M",
            "125M + 25m",
            "125M + 50M"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_material_fo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        _id = extras.getString("id");
        cargar_orden();

        spModem = (MaterialBetterSpinner) findViewById(R.id.sp_modem);
        spFibra = (MaterialBetterSpinner) findViewById(R.id.sp_fibra);

        ArrayAdapter<String> adapter_fibra_extra = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,  FIBRA_EXTRA);
        spFibraExtra = (MaterialBetterSpinner) findViewById(R.id.sp_fibra_extra);
        spFibraExtra.setAdapter(adapter_fibra_extra);

        spCinturones = (MaterialBetterSpinner) findViewById(R.id.sp_cinturones);
        llenarsp();


        TextView tvExpand = (TextView) findViewById(R.id.tvExpand);
        final ExpandableLayout expLay = (ExpandableLayout) findViewById(R.id.expandable_layout);
        tvExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expLay.isExpanded()){
                        expLay.collapse(true);
                }else
                    expLay.expand(true);

            }
        });

    }

    public void llenarsp(){
        RequestMateriales rModem = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialFOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(ArrayList<ModelMateriales> items) {
                ArrayAdapter<ModelMateriales> adapter = new ArrayAdapter<ModelMateriales>(CapturaMaterialFOActivity.this,
                        android.R.layout.simple_dropdown_item_1line, items );
                spModem.setAdapter(adapter);
//                spModem.setText(Utils.setItemIndex(0, spModem));
                IdModem = ((ModelMateriales)spModem.getAdapter().getItem(0)).Id;

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialFOActivity.this,"Aviso",error.getMessage()).show();
            }
        };rModem.getMaterialxLinea("2");//hace referencia a los modem

        RequestMateriales rFibra = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialFOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(ArrayList<ModelMateriales> items) {
                ArrayAdapter<ModelMateriales> adapter = new ArrayAdapter<ModelMateriales>(CapturaMaterialFOActivity.this,
                        android.R.layout.simple_dropdown_item_1line, items );
                spFibra.setAdapter(adapter);
//                spModem.setText(Utils.setItemIndex(0, spModem));
                IdFibra = ((ModelMateriales)spFibra.getAdapter().getItem(0)).Id;
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialFOActivity.this,"Aviso",error.getMessage()).show();
            }
        };rFibra.getMaterialxLinea("3");//hace referencia a la fibra

        RequestMateriales rCinturones = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialFOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(ArrayList<ModelMateriales> items) {
                ArrayAdapter<ModelMateriales> adapter = new ArrayAdapter<ModelMateriales>(CapturaMaterialFOActivity.this,
                        android.R.layout.simple_dropdown_item_1line, items );
                spCinturones.setAdapter(adapter);
//                spModem.setText(Utils.setItemIndex(0, spModem));
                IdCinturon = ((ModelMateriales)spCinturones.getAdapter().getItem(0)).Id;
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialFOActivity.this,"Aviso",error.getMessage()).show();
            }
        };rCinturones.getMaterialxLinea("5");//hace referencia a los cinturones


        RequestMateriales rComplementos = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialFOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(ArrayList<ModelMateriales> items) {
                MaterialAdapterListView material_adapter = new MaterialAdapterListView(CapturaMaterialFOActivity.this, items);
                listView = (ListView) findViewById(R.id.lv_materiales);
                listView.setAdapter(material_adapter);

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialFOActivity.this,"Aviso",error.getMessage()).show();
            }
        };rComplementos.getMaterialxLinea("4");
    }

    public void cargar_orden(){
        RequestOrdenesTrabajo rBuscaOrden = new RequestOrdenesTrabajo() {
            ProgressDialog progressDialog;
            @Override
            public void ordenBeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialFOActivity.this );
                progressDialog.setMessage( "Actualizando información de órden... Espere" );
                progressDialog.show();
            }

            @Override
            public void ordenCargaExitosa(ArrayList<ModelOrdenesTrabajo> items) {
                CapturaMaterialFOActivity.this.setTitle(items.get(0).telefono_orden);
                toolbar.setSubtitle(items.get(0).garantia);

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void ordenCargaExitosa(String mensaje) {

            }

            @Override
            public void ordenCargaErronea(mException error) {

            }
        };rBuscaOrden.buscarOrden(_id);
    }
}
