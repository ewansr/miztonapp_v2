package www.miztonapp.mx.requests;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import www.miztonapp.mx.api.Request;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.api.mExceptionCode;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;


/**
 * Created by Saulo on 05/10/2016.
 */

public abstract class RequestOrdenesTrabajo extends AppCompatActivity {
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
                        int valid = orden_response.getInt("valid");

                        if (valid != 1){
                            throw new mException(mExceptionCode.INVALID_VALUES, response.getString("message"));
                        }

                        JSONArray items = response.getJSONArray("mt_foliosxtecnicos");
                        ArrayList<ModelOrdenesTrabajo> lista = new  ArrayList<ModelOrdenesTrabajo>();

                        for ( int i = 0; i < items.length() ; i++) {
                            JSONObject item = items.getJSONObject(i);
                            lista.add(new ModelOrdenesTrabajo (
                                    item.getString("Folio"),
                                    item.getString("Telefono"),
                                    item.getString("TipoOS")
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
    public abstract void ordenCargaErronea(mException error);

}
