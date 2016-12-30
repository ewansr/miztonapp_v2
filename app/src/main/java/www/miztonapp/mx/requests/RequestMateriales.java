package www.miztonapp.mx.requests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import www.miztonapp.mx.api.Request;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.api.mExceptionCode;
import www.miztonapp.mx.models.ModelContratista;
import www.miztonapp.mx.models.ModelMateriales;


/**
 * Created by Saulo on 05/10/2016.
 */

public abstract class RequestMateriales {
    public void getMaterialxLinea(String idlinea, String idfolio){
        try{
            Request request = new Request(){
                @Override
                public void RequestBeforeExecute(){
                    BeforeLoad();
                }

                @Override
                public void RequestCompleted(JSONObject response){
                    try {
                        JSONObject TMaterial = null;
                        int valid = response.getInt("valid");

                        if (valid != 1){
                            throw new mException(mExceptionCode.INVALID_VALUES, response.getString("message"));
                        }

                        JSONArray items = response.getJSONArray("data");
                        ArrayList<ModelMateriales> lista = new  ArrayList<ModelMateriales>();

                        for ( int i = 0; i < items.length() ; i++) {
                            JSONObject item = items.getJSONObject(i);
                            lista.add(new ModelMateriales(
                                    item.getString("IdArticulo"),
                                    item.getString("Nombre"),
                                    item.getString("IdMedida"),
                                    item.getString("UnidadMedida"),
                                    item.getString("IdLinea"),
                                    item.getString("Linea"),
                                    item.getString("Codigo"),
                                    item.getString("CantidadDefault"),
                                    item.getString("CantidadReal")
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

            JSONObject material = new JSONObject();
            String route = "controller_materiales/material_linea";
            material.put("idlinea",idlinea);
            material.put("idfolio",idfolio);
            request.post(route, material);

        }catch (Exception error){
            CargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
        }
    }


    public void guardar_cantidad_material(String IdFolio, String IdMaterial, String Cantidad ){
        final String[] mensaje = {""};
        try{
            Request request = new Request(){
                @Override
                public void RequestBeforeExecute(){
                    BeforeLoad();
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
                            materialCargaExitosa(mensaje[0]);
                        }

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
            String route = "controller_materiales/inserta_material_folio";
            orden_trabajo.put("idfolio", IdFolio);
            orden_trabajo.put("idmaterial", IdMaterial);
            orden_trabajo.put("cantidad", Cantidad);

            request.post(route, orden_trabajo);
        }catch (Exception error){
            CargaErronea(new mException(mExceptionCode.UNKNOWN, error.getMessage()));
        }
    }

    public abstract void BeforeLoad();
    public abstract void CargaExitosa(ArrayList<ModelMateriales> items);
    public abstract void CargaErronea(mException error);
    public abstract void materialCargaExitosa(String mensaje);

}
