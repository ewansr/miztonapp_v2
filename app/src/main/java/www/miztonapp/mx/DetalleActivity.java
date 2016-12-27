package www.miztonapp.mx;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.commons.net.ftp.FTPFile;

import java.util.ArrayList;

import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.models.LoginModel;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.requests.RequestOrdenesTrabajo;
import www.miztonapp.mx.utilerias.FTPServerConfig;
import www.miztonapp.mx.utilerias.FTPUtils;
import www.miztonapp.mx.utilerias.Utils;

public class DetalleActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static Context context;
    private String _id;
    private String _telefono;
    private String _fecha;
    private String _tipoinstalacion;
    private String _tipoorden;
    private String _editable;
    private LoginModel data_usuario;
    private ArrayList<ModelOrdenesTrabajo> orden;
    public static MaterialEditText edtEstatus    ;
    public static MaterialEditText edtTipoOrden  ;
    public static MaterialEditText edtContratista;
    public static MaterialEditText edtfolio      ;
    public static MaterialEditText edtTelefono   ;
    public static MaterialEditText edtDistrito   ;
    public static MaterialEditText edtTerminal   ;
    public static MaterialEditText edtPuerto     ;
    public static MaterialEditText edtComentarios;
    private static FTPFile[] lista_archivos;
    private RequestOrdenesTrabajo rBuscaOrden;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });


        Bundle extras = getIntent().getExtras();
        _id = extras.getString("id");
        data_usuario = Utils.obtener_usuario(this);
        context = DetalleActivity.this;

        rBuscaOrden = new RequestOrdenesTrabajo() {
            @Override
            public void ordenBeforeLoad() {
                progressDialog = new ProgressDialog( context );
                progressDialog.setMessage( "Actualizando información de órden... Espere" );
                progressDialog.show();
            }

            @Override
            public void ordenCargaExitosa(ArrayList<ModelOrdenesTrabajo> items) {

                orden = items;
                _telefono = items.get(0).telefono_orden;
                _fecha = items.get(0).fecha;
                _tipoinstalacion = items.get(0).tipo_instalacion;
                _tipoorden = items.get(0).tipo_orden;
                _editable  = items.get(0).editable;

                edtEstatus.setText(items.get(0).estatus_orden);
                edtTipoOrden.setText(items.get(0).tipo_orden);
                edtContratista.setText(items.get(0).contratista);
                edtfolio.setText(items.get(0).folio_orden);
                edtTelefono.setText(items.get(0).telefono_orden);
                edtDistrito.setText(items.get(0).distrito);
                edtTerminal.setText(items.get(0).terminal);
                edtPuerto.setText(items.get(0).puerto);
                edtComentarios.setText(items.get(0).comentarios);
                desabilitaControles();

                DetalleActivity.this.setTitle(_telefono);
                toolbar.setSubtitle(items.get(0).garantia);

                String usuario = data_usuario.nombre_completo;
                FTPServerConfig.ruta_crear_ftp[0] = _fecha.substring(0,10);
                FTPServerConfig.ruta_crear_ftp[2] = _telefono;
                FTPServerConfig.ruta_crear_ftp[1] = usuario;
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

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 2){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.red));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.red));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.red));
                        mViewPager.setBackgroundColor(getResources().getColor(R.color.darkred));
                    }
                }
                if (tab.getPosition() == 1){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.green));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.green));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.green));
                    }
                }

                if (tab.getPosition() == 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        mViewPager.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        desabilitaControles();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionMenu fam_opciones = (FloatingActionMenu) findViewById(R.id.menu);
        fam_opciones.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener(){
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    FTPUtils ftpclient = new FTPUtils(context, FTPServerConfig.ruta_crear_ftp) {
                        @Override
                        public void procesoExitoso(FTPFile[] archivos_imagen) {
                            lista_archivos = archivos_imagen.clone();
                        }

                        @Override
                        public void procesoErroneo() {

                        }
                    };
                    ftpclient.execute();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.menu_subir);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrir_galeria();
            }
        });

        FloatingActionButton fab_galeria = (FloatingActionButton) findViewById(R.id.menu_detalle);
        fab_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrir_galeria_ftp();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_editar) {
            if (Utils.isEquals(_editable, "NO")) {
                Utils.crear_alerta(context, "Aviso", "El tiempo de edición ha finalizado").show();
            }else{
                if (orden.size() == 1){
                    Bundle bundle = new Bundle();
                    bundle.putString( "id", orden.get(0).Id);
                    bundle.putString( "folio", orden.get(0).folio_orden);
                    bundle.putString( "telefono", orden.get(0).telefono_orden);
                    bundle.putString( "principal", orden.get(0).principal);
                    bundle.putString( "secundario", orden.get(0).secundario);
                    bundle.putString( "tipo", orden.get(0).tipo_orden);
                    bundle.putString( "contratista", orden.get(0).contratista);
                    bundle.putString( "idcontratista", orden.get(0).idcontratista);
                    bundle.putString( "estatus", orden.get(0).estatus_orden);
                    bundle.putString( "distrito", orden.get(0).distrito);
                    bundle.putString( "terminal", orden.get(0).terminal);
                    bundle.putString( "puerto", orden.get(0).puerto);
                    bundle.putString( "comentarios", orden.get(0).comentarios);
                    bundle.putString( "idtipo", orden.get(0).idtipo);


                    Intent i = new Intent(DetalleActivity.this, RegistroFibraOpticaActivity.class);
                    i.putExtra("modo", "edicion");
                    i.putExtra("datos", bundle);
                    startActivity(i);
                }
            }
            return true;
        }
        if (id == R.id.action_refresh) {
            rBuscaOrden.buscarOrden(_id);
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
//            final DetalleActivity da = new DetalleActivity();
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(R.layout.fragment_detalle, container, false);
                edtEstatus     = (MaterialEditText) rootView.findViewById(R.id.edtEstatus);
                edtTipoOrden   = (MaterialEditText) rootView.findViewById(R.id.edtTipoOrden);
                edtContratista = (MaterialEditText) rootView.findViewById(R.id.edtContratista);
                edtfolio       = (MaterialEditText) rootView.findViewById(R.id.edtFolio);
                edtTelefono    = (MaterialEditText) rootView.findViewById(R.id.edtTelefono);
                edtDistrito    = (MaterialEditText) rootView.findViewById(R.id.edtDistrito);
                edtTerminal    = (MaterialEditText) rootView.findViewById(R.id.edtTerminal);
                edtPuerto      = (MaterialEditText) rootView.findViewById(R.id.edtPuerto);
                edtComentarios = (MaterialEditText) rootView.findViewById(R.id.edtComentarios);
            }else
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.activity_ordenes_detalle, container, false);
            }else
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                rootView = inflater.inflate(R.layout.fragment_detalle, container, false);
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "INFORMACIÓN DE ORDEN";
                case 1:
                    return "UBICAR";
                case 2:
                    return "MATERIALES";

            }
            return null;
        }
    }

    public void desabilitaControles(){
        edtEstatus.setEnabled(false);
        edtTipoOrden.setEnabled(false);
        edtContratista.setEnabled(false);
        edtfolio.setEnabled(false);
        edtTelefono.setEnabled(false);
        edtDistrito.setEnabled(false);
        edtTerminal.setEnabled(false);
        edtPuerto.setEnabled(false);
        edtComentarios.setEnabled(false);
    }


    public void abrir_galeria_ftp(){
        final Intent intent = new Intent(context, GaleriaActivity.class);
//        String usuario = data_usuario.nombre_completo;
//        FTPServerConfig.ruta_crear_ftp[0] = _fecha.substring(0,10);
//        FTPServerConfig.ruta_crear_ftp[2] = _telefono;
//        FTPServerConfig.ruta_crear_ftp[1] = usuario;
        intent.putExtra("FTPFile", lista_archivos);
        intent.putExtra("telefono", FTPServerConfig.ruta_crear_ftp[2]);
        intent.putExtra("fecha", FTPServerConfig.ruta_crear_ftp[0]);
        intent.putExtra("usuario", FTPServerConfig.ruta_crear_ftp[1]);
        startActivity(intent);
    }

    public void abrir_galeria(){
        final Intent intent = new Intent(context, AlbumSelectActivity.class);
//        String usuario = data_usuario.nombre_completo;
//        FTPServerConfig.ruta_crear_ftp[0] = _fecha.substring(0,10);
//        FTPServerConfig.ruta_crear_ftp[2] = _telefono;
//        FTPServerConfig.ruta_crear_ftp[1] = usuario;

        if (lista_archivos.length < 15) {
            intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 15 - lista_archivos.length);
            startActivityForResult(intent, Constants.REQUEST_CODE);
        }else {
            Utils.crear_toast(context, "Has subido el máximo no. de imágenes permitido (" + Integer.toString(lista_archivos.length) +")").show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Adaptador Ordenes", "onActivityResult");

        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            final ArrayList<Image> lista_imagen  = images;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("¿Estas seguro que deseas subir esas imagenes?")
                    .setCancelable(false)
                    .setPositiveButton("Subir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Utils.subir_imagenes_ftp(context, lista_imagen, FTPServerConfig.ruta_crear_ftp);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
