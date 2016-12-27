package www.miztonapp.mx;

/**
 * Created by EwanS on 12/10/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import www.miztonapp.mx.adapters.OrdenesRecyclerAdapter;
import www.miztonapp.mx.api.Request;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.api.mExceptionCode;
import www.miztonapp.mx.models.LoginModel;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.utilerias.DateU;
import www.miztonapp.mx.utilerias.Utils;

public class TabFragmentGeneral extends Fragment {
    int layout_inflar;
    private static RecyclerView recyclerView;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static StaggeredGridLayoutManager sgLayoutManager;
    private static Context context;
    private ProgressDialog progressDialog;
    private static Boolean yaCargado = false;
    OrdenesRecyclerAdapter solicitudesAdapter;

    public void initTabFragmentGeneral(Context context){
        this.context = context;
    }

    public TabFragmentGeneral(){

    }

    @Override
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate( R.layout.reciclerview, container, false );

        yaCargado = false;
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimaryDark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                cargar_ordenes(rootView);
            }
        });

        recyclerView      = (RecyclerView) rootView.findViewById( R.id.rv );
        recyclerView.setHasFixedSize( true );
        int columnas_default = 1;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            columnas_default = 1; //ANTES 2 COLUMNAS

        sgLayoutManager = new StaggeredGridLayoutManager( columnas_default, 1 );
        recyclerView.setLayoutManager( sgLayoutManager );

        recyclerView.addOnScrollListener( new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy ){
                super.onScrolled( recyclerView, dx, dy );
            }
        });
//        swipeRefreshLayout.measure(200,200);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        cargar_ordenes(view);
    }




    public void cargar_ordenes(View v){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        LoginModel usuario = Utils.obtener_usuario(context);
        consultar_ordenes(usuario.idpersonal, DateU.StartOfWeek(year, month, day), DateU.EndOfWeek(year, month, day), v);
    }

    public void consultar_ordenes(int idpersonal, String inicio, String termino, View v){
        try{
            final View vista = v;
            Request request = new Request(){
                @Override
                public void RequestBeforeExecute(){
                    ordenBeforeLoad();
                }

                @Override
                public void RequestCompleted(JSONObject response){
                    try {
                        JSONObject orden_response = null;
                        int valid = response.getInt("valid");

                        if (valid != 1){
                            throw new mException(mExceptionCode.INVALID_VALUES, response.getString("message"));
                        }

                        JSONArray items = response.getJSONArray("mt_foliosxtecnicos");
                        ArrayList<ModelOrdenesTrabajo> lista = new  ArrayList<ModelOrdenesTrabajo>();

                        for ( int i = 0; i < items.length() ; i++) {
                            JSONObject item = items.getJSONObject(i);
                            lista.add(new ModelOrdenesTrabajo (
                                    item.getString("idFolio"),
                                    item.getString("row_number"),
                                    item.getString("Folio"),
                                    item.getString("Telefono"),
                                    item.getString("TipoInstalacion"),
                                    item.getString("TipoOS"),
                                    item.getString("FechaCreacion"),
                                    item.getString("Estatus"),
                                    item.getString("Comentarios"),
                                    item.getString("EstatusGarantia"),
                                    item.getString("Central"),
                                    item.getString("Distrito"),
                                    item.getString("Terminal"),
                                    item.getString("Puerto"),
                                    item.getString("sContratista"),
                                    item.getString("Principal"),
                                    item.getString("Secundario"),
                                    item.getString("editable"),
                                    item.getString("IdTipo"),
                                    item.getString("IdContratista")
                            ) );
                        }

                        ordenCargaExitosa(lista, vista);


                    }catch (Exception error) {
                        ordenCargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
                    }
                }

                @Override
                public void RequestError(Exception error){
                    ordenCargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
                }
            };

            JSONObject orden_trabajo = new JSONObject();
            String route = "ordenes_trabajo/todas_ordenes";
            orden_trabajo.put("idpersonal", idpersonal);
            orden_trabajo.put("inicio", inicio);
            orden_trabajo.put("termino", termino);
            request.post(route, orden_trabajo);

        }catch (Exception error){
            ordenCargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
        }
    }



    public void ordenBeforeLoad() {

    }


    public void ordenCargaExitosa(ArrayList<ModelOrdenesTrabajo> items, View v) {


        int size = items.size();

        RecyclerView _recyclerView = (RecyclerView) v.findViewById(R.id.rv);
        if (_recyclerView.getAdapter() == null){
            solicitudesAdapter = new OrdenesRecyclerAdapter(items, context);
            _recyclerView.setAdapter(solicitudesAdapter);
        }else {
            solicitudesAdapter.swap(items);
//            _recyclerView.invalidate();
//            _recyclerView.getAdapter().notifyItemInserted(items.size());
//            _recyclerView.getAdapter().notifyItemRangeChanged(0, size);
//            _recyclerView.getAdapter().notifyDataSetChanged();
        }

        SwipeRefreshLayout _swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);
        if (_swipeRefreshLayout.isRefreshing()) {
            _swipeRefreshLayout.setRefreshing(false);
        }

        yaCargado = true;

    }

    public void ordenCargaErronea(mException error) {
        Utils.showMessage("Aviso", error.getMessage(), context);

    }
}