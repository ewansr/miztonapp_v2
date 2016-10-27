package www.miztonapp.mx;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import org.apache.commons.net.ftp.FTPFile;

import java.util.ArrayList;
import java.util.List;

import www.miztonapp.mx.adapters.GaleriaRecyclerAdapter;
import www.miztonapp.mx.models.ModelGaleria;
import www.miztonapp.mx.utilerias.FTPServerConfig;

public class GaleriaActivity extends AppCompatActivity {
    private static RecyclerView recyclerView;
    private static StaggeredGridLayoutManager staggeredGridLayoutManager;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static Bundle bundle_info_orden;
    private static String telefono;
    private static String usuario;
    private static String fecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimaryDark,
                R.color.colorAccent
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        staggeredGridLayoutManager= new StaggeredGridLayoutManager(2,1);
        recyclerView       = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        FTPFile[] ftpFile = (FTPFile[]) getIntent().getSerializableExtra("FTPFile");
        telefono = getIntent().getStringExtra("telefono");
        usuario = getIntent().getStringExtra("usuario");
        fecha = getIntent().getStringExtra("fecha");

//        usuario.replace(" ", "_");

        GaleriaRecyclerAdapter adaptador_galeria = new GaleriaRecyclerAdapter(construirAdaptador(ftpFile),GaleriaActivity.this);
        recyclerView.setAdapter(adaptador_galeria);
    }

    private ArrayList<ModelGaleria> construirAdaptador(FTPFile[] archivos){
        ArrayList<ModelGaleria> items = new ArrayList<ModelGaleria>();
        for(int i=0; i<archivos.length; i++){
            String ruta = "http://"+FTPServerConfig.ruta_base_imagenes + fecha + "/" +
                    usuario + "/" + telefono + "/" + archivos[i].getName();
            items.add(new ModelGaleria(telefono, ruta));
        }
        return items;
    }

    private void cargarImagenesServidor(){

    }
}
