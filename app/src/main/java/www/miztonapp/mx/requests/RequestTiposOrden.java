package www.miztonapp.mx.requests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import www.miztonapp.mx.api.Request;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.api.mExceptionCode;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.models.ModelTiposOrden;


/**
 * Created by Saulo on 05/10/2016.
 */

public abstract class RequestTiposOrden {
    public void getTiposOrden(String Tipo){
        try{
            Request request = new Request(){
                @Override
                public void RequestBeforeExecute(){
                    tOrdenBeforeLoad();
                }

                @Override
                public void RequestCompleted(JSONObject response){
                    try {
                        JSONObject TOrden_Reponse = null;
                        int valid = response.getInt("valid");

                        if (valid != 1){
                            throw new mException(mExceptionCode.INVALID_VALUES, response.getString("message"));
                        }

                        JSONArray items = response.getJSONArray("mt_tipoorden");
                        ArrayList<ModelTiposOrden> lista = new  ArrayList<ModelTiposOrden>();

                        for ( int i = 0; i < items.length() ; i++) {
                            JSONObject item = items.getJSONObject(i);
                            lista.add(new ModelTiposOrden (
                                    item.getString("IdTipo"),
                                    item.getString("TipoInstalacion"),
                                    item.getString("TipoOrden")
                            ) );
                        }

                        tOrdenCargaExitosa(lista);


                    }catch (Exception error) {
                        tOrdenCargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
                    }
                }

                @Override
                public void RequestError(Exception error){
                    tOrdenCargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
                }
            };

            JSONObject orden_trabajo = new JSONObject();
            String route = "controller_tipo_orden/catalogo";
            orden_trabajo.put("tipo", Tipo);

            request.post(route, orden_trabajo);

        }catch (Exception error){
            tOrdenCargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
        }
    }

    public abstract void tOrdenBeforeLoad();
    public abstract void tOrdenCargaExitosa(ArrayList<ModelTiposOrden> items);
    public abstract void tOrdenCargaErronea(mException error);

}
