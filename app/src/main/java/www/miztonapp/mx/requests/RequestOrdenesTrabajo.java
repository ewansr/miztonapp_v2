package www.miztonapp.mx.requests;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import www.miztonapp.mx.api.Request;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.api.mExceptionCode;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.models.ModelOrdenesUpdate;


/**
 * Created by Saulo on 05/10/2016.
 */

public abstract class RequestOrdenesTrabajo extends AppCompatActivity  {
    public void consultar_ordenes(int idpersonal, String inicio, String termino){
        try{
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

                        ordenCargaExitosa(lista);


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

    public abstract void ordenBeforeLoad();
    public abstract void ordenCargaExitosa(ArrayList<ModelOrdenesTrabajo> items);
    public abstract void ordenCargaExitosa(String mensaje);
    public abstract void ordenCargaErronea(mException error);


    public void guardar_orden(String Id, String Folio, String FolioTelmex, String IdPersonal,
                                   String Telefono, String Principal, String Secundario, String TipoOs,
                                   String Distrito,String Central, String Comentarios, String Estutus, String IdTipo,
                                   String Terminal, String Puerto, String IdContratista){
        final String[] mensaje = {""};
        try{
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
                        mensaje[0] = response.getString("message").toString();

                        if (valid != 1){
                            throw new mException(mExceptionCode.INVALID_VALUES, response.getString("message"));
                        }else{
                            ordenCargaExitosa(mensaje[0]);
                        }

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
            String route = "ordenes_trabajo/guardar_orden";
            orden_trabajo.put("folio", Folio);
            orden_trabajo.put("foliotelmex", FolioTelmex);
            orden_trabajo.put("idpersonal", IdPersonal);
            orden_trabajo.put("telefono", Telefono);
            orden_trabajo.put("principal", Principal);
            orden_trabajo.put("secundario", Secundario);
            orden_trabajo.put("tipoos", TipoOs);
            orden_trabajo.put("distrito", Distrito);
            orden_trabajo.put("central", Central);
            orden_trabajo.put("comentarios", Comentarios);
            orden_trabajo.put("estatus", Estutus);
            orden_trabajo.put("idtipo", IdTipo);
            orden_trabajo.put("terminal", Terminal);
            orden_trabajo.put("puerto", Puerto);
            orden_trabajo.put("idcontratista", IdContratista);
            request.post(route, orden_trabajo);
        }catch (Exception error){
            ordenCargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
        }
    }

    //** Edición de orden de trabajo

    public void editar_orden(String Id, String Folio, String FolioTelmex, String IdPersonal,
                              String Telefono, String Principal, String Secundario, String TipoOs,
                              String Distrito,String Central, String Comentarios, String Estutus, String IdTipo,
                              String Terminal, String Puerto, String IdContratista){
        final String[] mensaje = {""};
        try{
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
                        mensaje[0] = response.getString("message").toString();

                        if (valid != 1){
                            throw new mException(mExceptionCode.INVALID_VALUES, response.getString("message"));
                        }else{
                            ordenCargaExitosa(mensaje[0]);
                        }

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
            String route = "ordenes_trabajo/editar_orden";
            orden_trabajo.put("idfolio", Id);
            orden_trabajo.put("folio", Folio);
            orden_trabajo.put("foliotelmex", FolioTelmex);
            orden_trabajo.put("idpersonal", IdPersonal);
            orden_trabajo.put("telefono", Telefono);
            orden_trabajo.put("principal", Principal);
            orden_trabajo.put("secundario", Secundario);
            orden_trabajo.put("tipoos", TipoOs);
            orden_trabajo.put("distrito", Distrito);
            orden_trabajo.put("central", Central);
            orden_trabajo.put("comentarios", Comentarios);
            orden_trabajo.put("estatus", Estutus);
            orden_trabajo.put("idtipo", IdTipo);
            orden_trabajo.put("terminal", Terminal);
            orden_trabajo.put("puerto", Puerto);
            orden_trabajo.put("idcontratista", IdContratista);
            request.post(route, orden_trabajo);
        }catch (Exception error){
            ordenCargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
        }
    }

    //** Buscar una orden de trabajo por ID

    public void buscarOrden(String id){
        try{
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
                        int found = response.getInt("found");


                        if (valid != 1 || found != 1 ){
                            throw new mException(mExceptionCode.INVALID_VALUES, response.getString("message"));
                        }

                        JSONObject item = response.getJSONObject( "data" );
                        ArrayList<ModelOrdenesTrabajo> lista = new  ArrayList<ModelOrdenesTrabajo>();


                            lista.add(new ModelOrdenesTrabajo (
                                    item.getString("idFolio"),
                                    "1",
                                    item.getString("Folio"),
                                    item.getString("Telefono"),
                                    item.getString("TipoOS"),
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
                        ordenCargaExitosa(lista);
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
            String route = "ordenes_trabajo/buscar_orden";
            orden_trabajo.put("id", id);
            request.post(route, orden_trabajo);

        }catch (Exception error){
            ordenCargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
        }
    }
}
