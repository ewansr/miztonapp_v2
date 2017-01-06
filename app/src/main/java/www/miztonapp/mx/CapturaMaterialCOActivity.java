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

public class CapturaMaterialCOActivity extends AppCompatActivity {
    private String IdModem, IdParalelo, IdDit, IdJumper;
    private String _id = "-1";


    Toolbar toolbar;
    MaterialBetterSpinner spModem, spParalelos, spDit, spJumpers;
    FancyButton btnCantidadModem, btnParalelos, btnDit, btnJumpers;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_material_co);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CapturaMaterialCOActivity.this);
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

        spModem = (MaterialBetterSpinner) findViewById(R.id.sp_modem_co);
        spModem.setEnabled(false);
        spParalelos = (MaterialBetterSpinner) findViewById(R.id.sp_paralelo_co);
        spParalelos.setEnabled(false);
        spDit = (MaterialBetterSpinner) findViewById(R.id.sp_dit_co);
        spDit.setEnabled(false);
        spJumpers = (MaterialBetterSpinner) findViewById(R.id.sp_jumper_co);
        spJumpers.setEnabled(false);


        llenarsp();

        final TabHost host = (TabHost)findViewById(R.id.tabhost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Principal");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Básicos");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Complementos");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Ver más");
        host.addTab(spec);

        final ExpandableLayout expLayPrincipal = (ExpandableLayout) findViewById(R.id.expandable_principal);
        final ExpandableLayout expLay = (ExpandableLayout) findViewById(R.id.expandable_layout);


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
                        break;
                    case 1:
                        if (expLay.isExpanded())
                            expLay.collapse();
                        else
                            expLay.expand();

                        expLayPrincipal.collapse();
                        break;
                    default:

                        break;
                }
            }
        });

        btnCantidadModem = (FancyButton) findViewById(R.id.btn_cantidad_modem);
        btnCantidadModem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(CapturaMaterialCOActivity.this);
                input.setSingleLine(true);
                input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setMessage(spModem.getText().toString());
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.briefcase_material);

                alertDialog.setPositiveButton("Añadir",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isEquals(input.getText().toString(),"")){
                                    btnCantidadModem.setText(input.getText().toString());
                                    RequestMateriales rCantidades = new RequestMateriales() {
                                        @Override
                                        public void BeforeLoad() {

                                        }

                                        @Override
                                        public void CargaExitosa(ArrayList<ModelMateriales> items) {

                                        }

                                        @Override
                                        public void CargaErronea(mException error) {
                                            crear_toast(CapturaMaterialCOActivity.this,error.getMessage()).show();
                                        }

                                        @Override
                                        public void materialCargaExitosa(String mensaje) {
                                            crear_toast(CapturaMaterialCOActivity.this, mensaje).show();
                                        }
                                    };rCantidades.guardar_cantidad_material(_id,IdModem, btnCantidadModem.getText().toString(),"normal");
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


        btnParalelos = (FancyButton) findViewById(R.id.btn_cantidad_paralelo);
        btnParalelos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(CapturaMaterialCOActivity.this);
                input.setSingleLine(true);
                input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setMessage(spParalelos.getText().toString());
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.briefcase_material);

                alertDialog.setPositiveButton("Añadir",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isEquals(input.getText().toString(),"")){
                                    btnParalelos.setText(input.getText().toString());
                                    RequestMateriales rCantidades = new RequestMateriales() {
                                        @Override
                                        public void BeforeLoad() {

                                        }

                                        @Override
                                        public void CargaExitosa(ArrayList<ModelMateriales> items) {

                                        }

                                        @Override
                                        public void CargaErronea(mException error) {
                                            crear_toast(CapturaMaterialCOActivity.this,error.getMessage()).show();
                                        }

                                        @Override
                                        public void materialCargaExitosa(String mensaje) {
                                            crear_toast(CapturaMaterialCOActivity.this, mensaje).show();
                                        }
                                    };rCantidades.guardar_cantidad_material(_id,IdParalelo, btnParalelos.getText().toString(),"normal");
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


        btnDit = (FancyButton) findViewById(R.id.btn_dit_co);
        btnDit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(CapturaMaterialCOActivity.this);
                input.setSingleLine(true);
                input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setMessage(spDit.getText().toString());
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.briefcase_material);

                alertDialog.setPositiveButton("Añadir",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isEquals(input.getText().toString(),"")){
                                    btnDit.setText(input.getText().toString());
                                    RequestMateriales rCantidades = new RequestMateriales() {
                                        @Override
                                        public void BeforeLoad() {

                                        }

                                        @Override
                                        public void CargaExitosa(ArrayList<ModelMateriales> items) {

                                        }

                                        @Override
                                        public void CargaErronea(mException error) {
                                            crear_toast(CapturaMaterialCOActivity.this,error.getMessage()).show();
                                        }

                                        @Override
                                        public void materialCargaExitosa(String mensaje) {
                                            crear_toast(CapturaMaterialCOActivity.this, mensaje).show();
                                        }
                                    };rCantidades.guardar_cantidad_material(_id,IdDit, btnDit.getText().toString(),"normal");
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

        btnJumpers = (FancyButton) findViewById(R.id.btn_jumper_co);
        btnJumpers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(CapturaMaterialCOActivity.this);
                input.setSingleLine(true);
                input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setMessage(spJumpers.getText().toString());
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.briefcase_material);

                alertDialog.setPositiveButton("Añadir",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isEquals(input.getText().toString(),"")){
                                    btnJumpers.setText(input.getText().toString());
                                    RequestMateriales rCantidades = new RequestMateriales() {
                                        @Override
                                        public void BeforeLoad() {

                                        }

                                        @Override
                                        public void CargaExitosa(ArrayList<ModelMateriales> items) {

                                        }

                                        @Override
                                        public void CargaErronea(mException error) {
                                            crear_toast(CapturaMaterialCOActivity.this,error.getMessage()).show();
                                        }

                                        @Override
                                        public void materialCargaExitosa(String mensaje) {
                                            crear_toast(CapturaMaterialCOActivity.this, mensaje).show();
                                        }
                                    };rCantidades.guardar_cantidad_material(_id,IdJumper, btnJumpers.getText().toString(),"normal");
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

    }

    public void llenarsp(){
        final String _ìd = this._id;
        RequestMateriales rModem = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialCOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(final ArrayList<ModelMateriales> items) {
                ArrayAdapter<ModelMateriales> adapter = new ArrayAdapter<ModelMateriales>(CapturaMaterialCOActivity.this,
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
                        btnCantidadModem.setText(items.get(i).CantidadDefault);
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

               spModem.setFocusableInTouchMode(true);

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
                                crear_toast(CapturaMaterialCOActivity.this, error.getMessage()).show();
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
                Utils.crear_alerta(CapturaMaterialCOActivity.this,"Aviso",error.getMessage()).show();
            }

            @Override
            public void materialCargaExitosa(String mensaje) {

            }
        };rModem.getMaterialxLinea("10",_id);//hace referencia a los modem

        RequestMateriales rParalelos = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialCOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(final ArrayList<ModelMateriales> items) {
                ArrayAdapter<ModelMateriales> adapter = new ArrayAdapter<ModelMateriales>(CapturaMaterialCOActivity.this,
                        android.R.layout.simple_dropdown_item_1line, items );

                spParalelos.setAdapter(adapter);
                spParalelos.setEnabled(true);

                // Ver si hay algo guardado en la base de datos
                String id_modem=null;
                String nombre_modem=null;
                for (int i=0;i<items.size();i++){
                    if (!isEquals(items.get(i).CantidadReal,"0")){
                        id_modem = items.get(i).Id;
                        nombre_modem = items.get(i).Nombre;
                        btnParalelos.setText(items.get(i).CantidadDefault);
                        break;
                    }
                }

                // Si hay algo guardado usarlo sino dejarlo en blanco
                if (id_modem ==null) {
                    IdParalelo = ((ModelMateriales) spParalelos.getAdapter().getItem(0)).Id;
                }else{
                    IdParalelo = id_modem;
                    spParalelos.setText(nombre_modem);
                }

                spParalelos.setFocusableInTouchMode(true);

                //Guardar cuando se seleccione un item
                spParalelos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        IdParalelo = ((ModelMateriales)spParalelos.getAdapter().getItem(position)).Id;
                        RequestMateriales rCantidad = new RequestMateriales() {
                            @Override
                            public void BeforeLoad() {

                            }

                            @Override
                            public void CargaExitosa(ArrayList<ModelMateriales> items) {

                            }

                            @Override
                            public void CargaErronea(mException error) {
                                crear_toast(CapturaMaterialCOActivity.this, error.getMessage()).show();
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
                        rCantidad.guardar_cantidad_material(_id, IdParalelo, btnParalelos.getText().toString(),"normal");

                    }
                });

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialCOActivity.this,"Aviso",error.getMessage()).show();
            }

            @Override
            public void materialCargaExitosa(String mensaje) {

            }
        };rParalelos.getMaterialxLinea("9",_id);

        RequestMateriales rDit = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialCOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(final ArrayList<ModelMateriales> items) {
                ArrayAdapter<ModelMateriales> adapter = new ArrayAdapter<ModelMateriales>(CapturaMaterialCOActivity.this,
                        android.R.layout.simple_dropdown_item_1line, items );

                spDit.setAdapter(adapter);
                spDit.setEnabled(true);

                // Ver si hay algo guardado en la base de datos
                String id_modem=null;
                String nombre_modem=null;
                for (int i=0;i<items.size();i++){
                    if (!isEquals(items.get(i).CantidadReal,"0")){
                        id_modem = items.get(i).Id;
                        nombre_modem = items.get(i).Nombre;
                        btnDit.setText(items.get(i).CantidadDefault);
                        break;
                    }
                }

                // Si hay algo guardado usarlo sino dejarlo en blanco
                if (id_modem ==null) {
                    IdDit = ((ModelMateriales) spParalelos.getAdapter().getItem(0)).Id;
                }else{
                    IdDit = id_modem;
                    spDit.setText(nombre_modem);
                }

                spDit.setFocusableInTouchMode(true);

                //Guardar cuando se seleccione un item
                spDit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        IdDit = ((ModelMateriales)spDit.getAdapter().getItem(position)).Id;
                        RequestMateriales rCantidad = new RequestMateriales() {
                            @Override
                            public void BeforeLoad() {

                            }

                            @Override
                            public void CargaExitosa(ArrayList<ModelMateriales> items) {

                            }

                            @Override
                            public void CargaErronea(mException error) {
                                crear_toast(CapturaMaterialCOActivity.this, error.getMessage()).show();
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
                        rCantidad.guardar_cantidad_material(_id, IdDit, btnDit.getText().toString(),"normal");

                    }
                });

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialCOActivity.this,"Aviso",error.getMessage()).show();
            }

            @Override
            public void materialCargaExitosa(String mensaje) {

            }
        };rDit.getMaterialxLinea("8",_id);

        RequestMateriales rJumper = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialCOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(final ArrayList<ModelMateriales> items) {
                ArrayAdapter<ModelMateriales> adapter = new ArrayAdapter<ModelMateriales>(CapturaMaterialCOActivity.this,
                        android.R.layout.simple_dropdown_item_1line, items );

                spJumpers.setAdapter(adapter);
                spJumpers.setEnabled(true);

                // Ver si hay algo guardado en la base de datos
                String id_modem=null;
                String nombre_modem=null;
                for (int i=0;i<items.size();i++){
                    if (!isEquals(items.get(i).CantidadReal,"0")){
                        id_modem = items.get(i).Id;
                        nombre_modem = items.get(i).Nombre;
                        btnJumpers.setText(items.get(i).CantidadDefault);
                        break;
                    }
                }

                // Si hay algo guardado usarlo sino dejarlo en blanco
                if (id_modem ==null) {
                    IdJumper = ((ModelMateriales) spParalelos.getAdapter().getItem(0)).Id;
                }else{
                    IdJumper = id_modem;
                    spJumpers.setText(nombre_modem);
                }

                spJumpers.setFocusableInTouchMode(true);

                //Guardar cuando se seleccione un item
                spJumpers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        IdJumper = ((ModelMateriales)spJumpers.getAdapter().getItem(position)).Id;
                        RequestMateriales rCantidad = new RequestMateriales() {
                            @Override
                            public void BeforeLoad() {

                            }

                            @Override
                            public void CargaExitosa(ArrayList<ModelMateriales> items) {

                            }

                            @Override
                            public void CargaErronea(mException error) {
                                crear_toast(CapturaMaterialCOActivity.this, error.getMessage()).show();
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
                        rCantidad.guardar_cantidad_material(_id, IdJumper, btnJumpers.getText().toString(),"normal");

                    }
                });

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialCOActivity.this,"Aviso",error.getMessage()).show();
            }

            @Override
            public void materialCargaExitosa(String mensaje) {

            }
        };rJumper.getMaterialxLinea("7",_id);

        RequestMateriales rOtros = new RequestMateriales() {
            ProgressDialog progressDialog;
            @Override
            public void BeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialCOActivity.this );
                progressDialog.setMessage( "Obteniendo materiales..." );
                progressDialog.show();
            }

            @Override
            public void CargaExitosa(ArrayList<ModelMateriales> items) {
                MaterialAdapterListView material_adapter = new MaterialAdapterListView(CapturaMaterialCOActivity.this, items,_id);
                listView = (ListView) findViewById(R.id.lv_materiales);
                listView.setAdapter(material_adapter);

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(CapturaMaterialCOActivity.this,"Aviso",error.getMessage()).show();
            }

            @Override
            public void materialCargaExitosa(String mensaje) {

            }
        };rOtros.getMaterialxLinea("1",_id);
    }

    public void cargar_orden(){
        RequestOrdenesTrabajo rBuscaOrden = new RequestOrdenesTrabajo() {
            ProgressDialog progressDialog;
            @Override
            public void ordenBeforeLoad() {
                progressDialog = new ProgressDialog( CapturaMaterialCOActivity.this );
                progressDialog.setMessage( "Actualizando información de órden... Espere" );
                progressDialog.show();
            }

            @Override
            public void ordenCargaExitosa(ArrayList<ModelOrdenesTrabajo> items) {
                CapturaMaterialCOActivity.this.setTitle(items.get(0).telefono_orden);
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
