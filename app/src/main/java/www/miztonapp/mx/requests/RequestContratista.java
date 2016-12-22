package www.miztonapp.mx.requests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import www.miztonapp.mx.api.Request;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.api.mExceptionCode;
import www.miztonapp.mx.models.ModelContratista;
import www.miztonapp.mx.models.ModelTiposOrden;


/**
 * Created by Saulo on 05/10/2016.
 */

public abstract class RequestContratista {
    public void getContratistas(){
        try{
            Request request = new Request(){
                @Override
                public void RequestBeforeExecute(){
                    BeforeLoad();
                }

                @Override
                public void RequestCompleted(JSONObject response){
                    try {
                        JSONObject TContratistas_Reponse = null;
                        int valid = response.getInt("valid");

                        if (valid != 1){
                            throw new mException(mExceptionCode.INVALID_VALUES, response.getString("message"));
                        }

                        JSONArray items = response.getJSONArray("mt_contratistas");
                        ArrayList<ModelContratista> lista = new  ArrayList<ModelContratista>();

                        for ( int i = 0; i < items.length() ; i++) {
                            JSONObject item = items.getJSONObject(i);
                            lista.add(new ModelContratista(
                                    item.getString("IdContratista"),
                                    item.getString("Contratista"),
                                    item.getString("Activo")
                            ) );
                        }

                        CargaExitosa(lista);


                    }catch (Exception error) {
                        CargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
                    }
                }

                @Override
                public void RequestError(Exception error){
                    CargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
                }
            };

            JSONObject orden_trabajo = new JSONObject();
            String route = "controller_contratista/catalogo";
            request.post(route, orden_trabajo);

        }catch (Exception error){
            CargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
        }
    }

    public abstract void BeforeLoad();
    public abstract void CargaExitosa(ArrayList<ModelContratista> items);
    public abstract void CargaErronea(mException error);

}
