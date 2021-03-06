package www.miztonapp.mx;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.models.LoginModel;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.requests.RequestOrdenesTrabajo;
import www.miztonapp.mx.utilerias.FTPServerConfig;
import www.miztonapp.mx.utilerias.FTPUtils;
import www.miztonapp.mx.utilerias.Utils;

public class activity_ordenes_detalle extends AppCompatActivity  implements OnMapReadyCallback{
    private GoogleMap mMap;
    private TextView map_direccion;
    private static String _id;
    private static String _fecha;
    private static String _telefono;
    private static String _tipoinstalacion;
    private static String _tipoorden;
    private static String _comentarios;
    private static String _principal;
    private static String _secundario;
    private static String _central;
    private static String _distrito;
    private static String _terminal;
    private static String _puerto;
    private static LoginModel data_usuario;


    private static Context context;
    private static FTPFile[] lista_archivos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = activity_ordenes_detalle.this;

        setContentView(R.layout.activity_ordenes_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        _id = extras.getString("id");
        data_usuario = Utils.obtener_usuario(this);

        final TextView telefono         =(TextView) findViewById( R.id.map_direccion );
        final TextView tipo_instalacion =(TextView) findViewById( R.id.map_direccion );
        final TextView fecha            =(TextView) findViewById( R.id.map_direccion );
        final TextView tipo_orden       =(TextView) findViewById( R.id.map_direccion );
        final TextView no_cargas        =(TextView) findViewById( R.id.map_direccion );

        RequestOrdenesTrabajo rBuscaOrden = new RequestOrdenesTrabajo() {
            @Override
            public void ordenBeforeLoad() {

            }

            @Override
            public void ordenCargaExitosa(ArrayList<ModelOrdenesTrabajo> items) {

                _telefono = items.get(0).telefono_orden;
                _fecha = items.get(0).fecha;
                _tipoinstalacion = items.get(0).tipo_instalacion;
                _tipoorden = items.get(0).tipo_orden;

                telefono.setText(_telefono);
                tipo_instalacion.setText(_tipoinstalacion);
                fecha.setText(_fecha);
                tipo_orden.setText(_tipoorden);

                no_cargas.setText("0 Fotos cargadas");

                String usuario = data_usuario.nombre_completo;
                FTPServerConfig.ruta_crear_ftp[0] = _fecha.substring(0,10);
                FTPServerConfig.ruta_crear_ftp[2] = _telefono;
                FTPServerConfig.ruta_crear_ftp[1] = usuario;
            }

            @Override
            public void ordenCargaExitosa(String mensaje) {

            }

            @Override
            public void ordenCargaErronea(mException error) {

            }
        };rBuscaOrden.buscarOrden(_id);



//        FloatingActionMenu fam_opciones = (FloatingActionMenu) findViewById(R.id.menu);
//        fam_opciones.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener(){
//            @Override
//            public void onMenuToggle(boolean opened) {
//                if (opened) {
//                    FTPUtils ftpclient = new FTPUtils(context, FTPServerConfig.ruta_crear_ftp) {
//                        @Override
//                        public void procesoExitoso(FTPFile[] archivos_imagen) {
//                            lista_archivos = archivos_imagen.clone();
//                        }
//
//                        @Override
//                        public void procesoErroneo() {
//
//                        }
//                    };
//                    ftpclient.execute();
//                }
//            }
//        });
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.menu_subir);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                abrir_galeria();
//            }
//        });
//
//        FloatingActionButton fab_galeria = (FloatingActionButton) findViewById(R.id.menu_detalle);
//        fab_galeria.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                abrir_galeria_ftp();
//            }
//        });

        map_direccion = (TextView) findViewById(R.id.map_direccion);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_ordenes_detalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_refresh:
                // search action
                return true;
            case R.id.action_settings:
                // search action
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ubicacion = new LatLng(19.142901,  -96.135693);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Tel: " + _telefono));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ubicacion, 14);
        mMap.moveCamera(cameraUpdate);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(ubicacion.latitude, ubicacion.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            String complete_address = address + ", " + postalCode + ", " + city + ", " + state + ", " + country;
            map_direccion.setText(complete_address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void abrir_galeria_ftp(){
        final Intent intent = new Intent(this, GaleriaActivity.class);
        String usuario = data_usuario.nombre_completo;
        FTPServerConfig.ruta_crear_ftp[0] = _fecha.substring(0,10);
        FTPServerConfig.ruta_crear_ftp[2] = _telefono;
        FTPServerConfig.ruta_crear_ftp[1] = usuario;
        intent.putExtra("FTPFile", lista_archivos);
        intent.putExtra("telefono", FTPServerConfig.ruta_crear_ftp[2]);
        intent.putExtra("fecha", FTPServerConfig.ruta_crear_ftp[0]);
        intent.putExtra("usuario", FTPServerConfig.ruta_crear_ftp[1]);
        startActivity(intent);
    }

    public void abrir_galeria(){
        final Intent intent = new Intent(this, AlbumSelectActivity.class);
        String usuario = data_usuario.nombre_completo;
        FTPServerConfig.ruta_crear_ftp[0] = _fecha.substring(0,10);
        FTPServerConfig.ruta_crear_ftp[2] = _telefono;
        FTPServerConfig.ruta_crear_ftp[1] = usuario;

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
