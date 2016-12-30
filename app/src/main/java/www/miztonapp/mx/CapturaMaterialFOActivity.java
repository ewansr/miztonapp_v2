package www.miztonapp.mx;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import www.miztonapp.mx.adapters.MaterialAdapterListView;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.models.ModelMateriales;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.requests.RequestMateriales;
import www.miztonapp.mx.requests.RequestOrdenesTrabajo;
import www.miztonapp.mx.utilerias.Utils;

import static www.miztonapp.mx.utilerias.Utils.crear_toast;
import static www.miztonapp.mx.utilerias.Utils.isEquals;

public class CapturaMaterialFOActivity extends AppCompatActivity {
    private String IdModem;
    private String IdFibra;
    private String IdCinturon;
    private String _id = "-1";


    Toolbar toolbar;
    MaterialBetterSpinner spModem, spFibra, spFibraExtra, spCinturones;
    FancyButton btnCantidadCinturon;
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
            "125M + 25M",
            "125M + 50M"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_material_fo);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CapturaMaterialFOActivity.this);
        alertDialog.setTitle("Captura");

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
        spModem.setEnabled(false);
        spFibra = (MaterialBetterSpinner) findViewById(R.id.sp_fibra);
        spFibra.setEnabled(false);

        ArrayAdapter<String> adapter_fibra_extra = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,  FIBRA_EXTRA);
        spFibraExtra = (MaterialBetterSpinner) findViewById(R.id.sp_fibra_extra);
        spFibraExtra.setEnabled(false);
        spFibraExtra.setAdapter(adapter_fibra_extra);

        spCinturones = (MaterialBetterSpinner) findViewById(R.id.sp_cinturones);
        spCinturones.setEnabled(false);
        btnCantidadCinturon = (FancyButton) findViewById(R.id.btnCantidadCinturon);
        llenarsp();

        final TabHost host = (TabHost)findViewById(R.id.tabhost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Principal");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Princ.");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Complementos");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Comp.");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Otros");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Otros");
        host.addTab(spec);

        final ExpandableLayout expLayPrincipal = (ExpandableLayout) findViewById(R.id.expandable_principal);
        final ExpandableLayout expLay = (ExpandableLayout) findViewById(R.id.expandable_layout);
        final ExpandableLayout expLayOtros = (ExpandableLayout) findViewById(R.id.expandable_layout_otros);
//        final FancyButton btn_Guardar = (FancyButton) findViewById(R.id.btn_guardar);
//        btn_Guardar.setText("Guardar " + host.getCurrentTabTag());


        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                Log.d("TabHost", "onTabChanged: tab number=" + host.getCurrentTab());
                switch (host.getCurrentTab()) {
                    case 0:
                        if (expLayPrincipal.isExpanded())
                            expLayPrincipal.collapse();
                        else
                            expLayPrincipal.expand();

                        expLay.collapse();
                        expLayOtros.collapse();
                        break;
                    case 1:
                        if (expLay.isExpanded())
                            expLay.collapse();
                        else
                            expLay.expand();

                        expLayPrincipal.collapse();
                        expLayOtros.collapse();

                        break;
                    case 2:
                        if (expLayOtros.isExpanded())
                            expLayOtros.collapse();
                        else
                            expLayOtros.expand();

                        expLay.collapse();
                        expLayPrincipal.collapse();

                        break;
                    default:

                        break;
                }
            }
        });


        btnCantidadCinturon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(CapturaMaterialFOActivity.this);
                input.setSingleLine(true);
                input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setMessage(spCinturones.getText().toString());
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.briefcase_material);

                alertDialog.setPositiveButton("Añadir",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isEquals(input.getText().toString(),"")){
                                    btnCantidadCinturon.setText(input.getText().toString());
                                    RequestMateriales rCantidades = new RequestMateriales() {
                                        @Override
                                        public void BeforeLoad() {

                                        }

                                        @Override
                                        public void CargaExitosa(ArrayList<ModelMateriales> items) {

                                        }

                                        @Override
                                        public void CargaErronea(mException error) {
                                            crear_toast(CapturaMaterialFOActivity.this,error.getMessage()).show();
                                        }

                                        @Override
                                        public void materialCargaExitosa(String mensaje) {
                                            crear_toast(CapturaMaterialFOActivity.this, mensaje).show();
                                        }
                                    };rCantidades.guardar_cantidad_material(_id,IdCinturon,btnCantidadCinturon.getText().toString(),"normal");
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


        spFibraExtra.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                RequestMateriales rCantidad = new RequestMateriales() {
                    @Override
                    public void BeforeLoad() {

                    }

                    @Override
                    public void CargaExitosa(ArrayList<ModelMateriales> items) {

                    }

                    @Override
                    public void CargaErronea(mException error) {
                        crear_toast(CapturaMaterialFOActivity.this, error.getMessage()).show();
                    }

                    @Override
                    public void materialCargaExitosa(String mensaje) {

                    }
                };

                // poner todos en 0
                if (position == 0){
                    rCantidad.guardar_cantidad_material(_id,"21","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"22","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"23","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"24","0","extra");
                }

                if (position == 1){
                    rCantidad.guardar_cantidad_material(_id,"21","1","extra");
                    rCantidad.guardar_cantidad_material(_id,"22","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"23","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"24","0","extra");
                }

                if (position == 2){
                    rCantidad.guardar_cantidad_material(_id,"21","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"22","1","extra");
                    rCantidad.guardar_cantidad_material(_id,"23","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"24","0","extra");
                }

                if (position == 3){
                    rCantidad.guardar_cantidad_material(_id,"21","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"22","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"23","1","extra");
                    rCantidad.guardar_cantidad_material(_id,"24","0","extra");
                }

                if (position == 4){
                    rCantidad.guardar_cantidad_material(_id,"21","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"22","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"23","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"24","1","extra");
                }

                if (position == 5){
                    rCantidad.guardar_cantidad_material(_id,"21","1","extra");
                    rCantidad.guardar_cantidad_material(_id,"22","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"23","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"24","1","extra");
                }

                if (position == 6){
                    rCantidad.guardar_cantidad_material(_id,"21","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"22","1","extra");
                    rCantidad.guardar_cantidad_material(_id,"23","0","extra");
                    rCantidad.guardar_cantidad_material(_id,"24","1","extra");
                }
            }
        });


    }

    public void llenarsp(){
        final String _ìd = this._id;
        RequestMateriales rModem = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialFOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(final ArrayList<ModelMateriales> items) {
                ArrayAdapter<ModelMateriales> adapter = new ArrayAdapter<ModelMateriales>(CapturaMaterialFOActivity.this,
                        android.R.layout.simple_dropdown_item_1line, items );
                spModem.setAdapter(adapter);
                spModem.setEnabled(true);

                // Ver si hay algo guardado en la base de datos
                String id_modem=null;
                String nombre_modem=null;
                for (int i=0;i<items.size();i++){
                    if (!isEquals(items.get(i).CantidadReal,"0")){
                        id_modem = items.get(i).Id;
                        nombre_modem = items.get(i).Nombre;
                        break;
                    }
                }

                // Si hay algo guardado usarlo sino dejarlo en blanco
                if (id_modem ==null) {
                    IdModem = ((ModelMateriales) spModem.getAdapter().getItem(0)).Id;
                }else{
                    IdModem = id_modem;
                    spModem.setText(nombre_modem);
                }


                //Guardar cuando se seleccione un item
                spModem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        IdModem = ((ModelMateriales)spModem.getAdapter().getItem(position)).Id;
                        RequestMateriales rCantidad = new RequestMateriales() {
                            @Override
                            public void BeforeLoad() {

                            }

                            @Override
                            public void CargaExitosa(ArrayList<ModelMateriales> items) {

                            }

                            @Override
                            public void CargaErronea(mException error) {
                                crear_toast(CapturaMaterialFOActivity.this, error.getMessage()).show();
                            }

                            @Override
                            public void materialCargaExitosa(String mensaje) {

                            }
                        };

                        // poner todos en 0
                        for (int i=0;i<items.size();i++){
                            rCantidad.guardar_cantidad_material(_id,items.get(i).Id,"0","normal");
                        }

                        //al final solo insertar el mero mero
                        rCantidad.guardar_cantidad_material(_id, IdModem,"1","normal");

                    }
                });

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialFOActivity.this,"Aviso",error.getMessage()).show();
            }

            @Override
            public void materialCargaExitosa(String mensaje) {

            }
        };rModem.getMaterialxLinea("2",_id);//hace referencia a los modem

        RequestMateriales rFibra = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialFOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(final ArrayList<ModelMateriales> items) {
                ArrayAdapter<ModelMateriales> adapter = new ArrayAdapter<ModelMateriales>(CapturaMaterialFOActivity.this,
                        android.R.layout.simple_dropdown_item_1line, items );
                spFibra.setAdapter(adapter);
                spFibra.setEnabled(true);

                // Ver si hay algo guardado en la base de datos
                String id_fibra=null;
                String nombre_fibra=null;
                for (int i=0;i<items.size();i++){
                    if (!isEquals(items.get(i).CantidadReal,"0")){
                        id_fibra = items.get(i).Id;
                        nombre_fibra = items.get(i).Nombre;
                        break;
                    }
                }

                // Si hay algo guardado usarlo sino dejarlo en blanco
                if (id_fibra ==null) {
                    IdFibra = ((ModelMateriales) spFibra.getAdapter().getItem(0)).Id;
                }else{
                    IdFibra = id_fibra;
                    spFibra.setText(nombre_fibra);
                }


                //Guardar cuando se seleccione un item
                spFibra.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        IdFibra = ((ModelMateriales)spFibra.getAdapter().getItem(position)).Id;
                        RequestMateriales rCantidad = new RequestMateriales() {
                            @Override
                            public void BeforeLoad() {

                            }

                            @Override
                            public void CargaExitosa(ArrayList<ModelMateriales> items) {

                            }

                            @Override
                            public void CargaErronea(mException error) {
                                crear_toast(CapturaMaterialFOActivity.this, error.getMessage()).show();
                            }

                            @Override
                            public void materialCargaExitosa(String mensaje) {

                            }
                        };

                        // poner todos en 0
                        for (int i=0;i<items.size();i++){
                            rCantidad.guardar_cantidad_material(_id,items.get(i).Id,"0","normal");
                        }

                        //al final solo insertar el mero mero
                        rCantidad.guardar_cantidad_material(_id,IdFibra,"1","normal");
                    }
                });

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialFOActivity.this,"Aviso",error.getMessage()).show();
            }

            @Override
            public void materialCargaExitosa(String mensaje) {

            }
        };rFibra.getMaterialxLinea("3",_id);//hace referencia a la fibra

        RequestMateriales rCinturones = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialFOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(final ArrayList<ModelMateriales> items) {
                ArrayAdapter<ModelMateriales> adapter = new ArrayAdapter<ModelMateriales>(CapturaMaterialFOActivity.this,
                        android.R.layout.simple_dropdown_item_1line, items );
                spCinturones.setAdapter(adapter);
                spCinturones.setEnabled(true);

                // Ver si hay algo guardado en la base de datos
                String id_cinturon=null;
                String nombre_cinturon=null;
                for (int i=0;i<items.size();i++){
                    if (!isEquals(items.get(i).CantidadReal,"0")){
                        id_cinturon = items.get(i).Id;
                        nombre_cinturon = items.get(i).Nombre;
                        btnCantidadCinturon.setText(items.get(i).CantidadDefault);
                        break;
                    }
                }

                // Si hay algo guardado usarlo sino dejarlo en blanco
                if (id_cinturon ==null) {
                    IdCinturon = ((ModelMateriales) spCinturones.getAdapter().getItem(0)).Id;
                }else{
                    IdCinturon = id_cinturon;
                    spCinturones.setText(nombre_cinturon);
                }

                //Guardar cuando se seleccione un item
                spCinturones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        IdCinturon = ((ModelMateriales)spCinturones.getAdapter().getItem(position)).Id;
                        RequestMateriales rCantidad = new RequestMateriales() {
                            @Override
                            public void BeforeLoad() {

                            }

                            @Override
                            public void CargaExitosa(ArrayList<ModelMateriales> items) {

                            }

                            @Override
                            public void CargaErronea(mException error) {
                                crear_toast(CapturaMaterialFOActivity.this, error.getMessage()).show();
                            }

                            @Override
                            public void materialCargaExitosa(String mensaje) {

                            }
                        };

                        // poner todos en 0
                        for (int i=0;i<items.size();i++){
                            rCantidad.guardar_cantidad_material(_id,items.get(i).Id,"0","normal");
                        }

                        //al final solo insertar el mero mero
                        rCantidad.guardar_cantidad_material(_id,IdCinturon,(isEquals(btnCantidadCinturon.getText().toString(),""))?"0":btnCantidadCinturon.getText().toString(),"normal");
                    }
                });
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialFOActivity.this,"Aviso",error.getMessage()).show();
            }

            @Override
            public void materialCargaExitosa(String mensaje) {

            }
        };rCinturones.getMaterialxLinea("5",_id);//hace referencia a los cinturones

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

                MaterialAdapterListView material_adapter = new MaterialAdapterListView(CapturaMaterialFOActivity.this, items,_ìd);
                listView = (ListView) findViewById(R.id.lv_materiales);
                listView.setAdapter(material_adapter);

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialFOActivity.this,"Aviso",error.getMessage()).show();
            }

            @Override
            public void materialCargaExitosa(String mensaje) {

            }
        };rComplementos.getMaterialxLinea("4",_id);

        RequestMateriales rOtros = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialFOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(ArrayList<ModelMateriales> items) {
                MaterialAdapterListView material_adapter = new MaterialAdapterListView(CapturaMaterialFOActivity.this, items,_id);
                listView = (ListView) findViewById(R.id.lv_materiales_otros);
                listView.setAdapter(material_adapter);

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialFOActivity.this,"Aviso",error.getMessage()).show();
            }

            @Override
            public void materialCargaExitosa(String mensaje) {

            }
        };rOtros.getMaterialxLinea("6",_id);
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
