package www.miztonapp.mx.adapters;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import www.miztonapp.mx.R;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.models.LoginModel;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.requests.RequestOrdenesTrabajo;
import www.miztonapp.mx.utilerias.DateU;
import www.miztonapp.mx.utilerias.Utils;


/**
 * Created by Saulo on 31/08/2016.
 */
public class PlaceHolderFragmentOrdenes extends Fragment {
    private static final String ARG_SECTION_NUMBER = "Sección numero: ";
    private static RecyclerView recyclerView;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static StaggeredGridLayoutManager sgLayoutManager;
    private static Context context;
    private ProgressDialog progressDialog;
    private RequestOrdenesTrabajo ordenes_trabajo;
    private static Boolean yaCargado = false;


    public PlaceHolderFragmentOrdenes(){

    }

    public void _contructor(Context context) {
        this.context = context;
    }

    /**
     * Retorna una instancia del fragmento deseado
     */
    public static PlaceHolderFragmentOrdenes newInstance(Context instanceContext) {
        PlaceHolderFragmentOrdenes fragment = new PlaceHolderFragmentOrdenes();
        fragment._contructor(instanceContext);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onSaveInstanceState(final Bundle outState) {
//        // super.onSaveInstanceState(outState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
//            ((Activity)context).finish();

        }


        View rootView = inflater.inflate( R.layout.reciclerview, container, false );

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimaryDark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                yaCargado = false;
                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                ordenes_trabajo.consultar_ordenes(LoginModel.idpersonal, DateU.StartOfWeek(year, month, day), DateU.EndOfWeek(year, month, day));
            }
        });

        recyclerView      = (RecyclerView) rootView.findViewById( R.id.rv );
        recyclerView.setHasFixedSize( true );
        sgLayoutManager = new StaggeredGridLayoutManager( 1,1 );
        recyclerView.setLayoutManager( sgLayoutManager );

        recyclerView.addOnScrollListener( new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy ){
                super.onScrolled( recyclerView, dx, dy );
            }
        });
        yaCargado = false;
        cargar_datos();
        return rootView;





//        if (savedInstanceState == null) {
//            // During initial setup, plug in the details fragment.
//            DetailsFragment details = new DetailsFragment();
//            details.setArguments(getIntent().getExtras());
//            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
//        }
    }

    public void cargar_datos(){
        if (!yaCargado) {
            ordenes_trabajo = new RequestOrdenesTrabajo() {


                @Override
                public void ordenBeforeLoad() {

                }

                @Override
                public void ordenCargaExitosa(ArrayList<ModelOrdenesTrabajo> items) {
                    OrdenesRecyclerAdapter solicitudesAdapter = new OrdenesRecyclerAdapter(items, context);

                    int size = items.size();

                    if (recyclerView.getAdapter() == null){
                        recyclerView.setAdapter(solicitudesAdapter);
                    }else {
                        recyclerView.getAdapter().notifyItemInserted(items.size() - 1);
                        recyclerView.getAdapter().notifyItemRangeChanged(items.size() - 1, size);
                    }

                    swipeRefreshLayout.setRefreshing(false);
                    yaCargado = true;
                }

                @Override
                public void ordenCargaErronea(mException error) {
                    Utils.showMessage("Aviso", error.getMessage(), context);
                }

            };
            if(!yaCargado) {
                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                ordenes_trabajo.consultar_ordenes(LoginModel.idpersonal, DateU.StartOfWeek(year, month, day), DateU.EndOfWeek(year, month, day));
            }
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


    }


}