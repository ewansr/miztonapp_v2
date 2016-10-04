package www.miztonapp.mx.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Clase para solicitudes HTTP
 *
 * @author edmsamuel 20/06/16.
 */
public abstract class APIRequest extends AsyncTask<String, Void, JSONObject> {
    private Boolean hasErrors;
    private Exception error;
    private JSONObject datos;
    private JSONObject response;
    public String response_text;
    public APIRequestType tipo;
    public APIRequestHeaders headers;


    /**
     * Constructor.
     *
     */
    public APIRequest() {
        headers = new APIRequestHeaders();
    }




    /**
     * Llamado antes de iniciar el doInBackground
     *
     * @author edmsamuel
     */
    @Override
    public void onPreExecute() {
        RequestBeforeExecute();
    }



    /**
     * Proceso de ejecución en segundo plano.
     *
     * @param params String Lista de urls( solo se toma la primera ).
     * @return JSONObject
     */
    @Override
    protected JSONObject doInBackground(String... params ) {
        JSONObject resultado = new JSONObject();
        response_text = "";
        String requestMethod = this.getRequestMethod();
        Log.i( "Request", "Preparando solicitud `" + requestMethod + "` a `" + params[0] + "`" );

        try {
            InputStream inputStream;
            String line;
            String body;
            StringBuilder responseText = new StringBuilder();
            BufferedReader bufferedReader;
            boolean will_send_data = ( this.tipo != APIRequestType.GET && this.tipo != APIRequestType.DELETE );

            URL url = new URL( params[0] );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod( requestMethod );
            connection.setReadTimeout( 15000 );
            connection.setConnectTimeout( 15000 );
            connection.setDoOutput( will_send_data );

            if ( will_send_data ) {

                int content_length;
                byte[] content_bytes;
                DataOutputStream dataOutputStream;
                APIRequestHeader header;

                // información del body
                body = datos.toString();
                content_bytes = body.getBytes();
                content_length = content_bytes.length;

                // debug
                Log.i( "Request", "Adjuntando a la solicitud `" + body + "`" );

                // establecer headers.
                connection.setRequestProperty( "Content-Type", "application/json" );
                connection.setRequestProperty( "Content-Length", Integer.toString( content_length  ) );
                for ( int header_idx = 0; header_idx < headers.items.size(); header_idx++ ) {
                    header = headers.get( header_idx );
                    connection.setRequestProperty( header.name, header.value );
                }

                // añadir datos al body
                dataOutputStream = new DataOutputStream( connection.getOutputStream() );
                dataOutputStream.writeBytes( body );
                dataOutputStream.flush();
                dataOutputStream.close();
            }

            int responseCode = connection.getResponseCode();
            hasErrors = ( responseCode != HttpURLConnection.HTTP_OK );

            // si la solicitud fue exitosa convertir el resultado en JSONObject
            if ( !hasErrors ) {
                // obtener el resultado de la solicitud
                inputStream = new BufferedInputStream( connection.getInputStream() );
                bufferedReader = new BufferedReader( new InputStreamReader( inputStream ) );
                while ( ( line = bufferedReader.readLine() ) != null ) {
                    responseText.append( line );
                }
                response_text = responseText.toString();

                resultado = new JSONObject( response_text );
                this.response = resultado;
            }else{
                inputStream = new BufferedInputStream( connection.getErrorStream() );
                bufferedReader = new BufferedReader( new InputStreamReader( inputStream ) );
                while ( ( line = bufferedReader.readLine() ) != null ) {
                    responseText.append( line );
                }
                response_text = responseText.toString();

                resultado = new JSONObject( response_text );
                this.response = resultado;
            }

            // terminar la conexión
           connection.disconnect();

        } catch ( Exception error ) {
            hasErrors = true;
            this.error = error;
        }

        return resultado;
    }



    /**
     * Llamado al terminar el proceso en segúndo plano.
     *
     * @param resultado JSONObject Recibido al terminar el proceso en segundo plano.
     */
    @Override
    protected void onPostExecute( JSONObject resultado ) {
        if ( !hasErrors ) {
            Log.i( "Request", "Solicitud completada con resultado `" + resultado.toString() + "`" );
            RequestCompleted( resultado );
        } else {
            Log.e( "RequestS", "Error en la solicitud `" + error.getMessage() + "`" );
            RequestError( error );
        }
    }



    /**
     * Envia una solitud de tipo GET.
     *
     * @param path String Ruta a la que se desea acceder.
     */
    public void get( String path ) {
        String[] urls = { prepareUrl( path ) };
        this.tipo = APIRequestType.GET;
        this.hasErrors = false;
        this.execute( urls );
    }



    /**
     * Envia una solitud de tipo POST.
     *
     * @param path String Ruta a la que se desea acceder.
     */
    public void put(String path, JSONObject datos ) {
        String[] urls = { prepareUrl( path ) };
        this.tipo = APIRequestType.PUT;
        this.hasErrors = false;
        this.setBody( datos );
        this.execute( urls );
    }



    /**
     * Envia una solitud de tipo POST.
     *
     * @param path String Ruta a la que se desea acceder.
     */
    public void delete( String path ) {
        String[] urls = { prepareUrl( path ) };
        this.tipo = APIRequestType.DELETE;
        this.hasErrors = false;
        this.setBody( datos );
        this.execute( urls );
    }



    /**
     * Envia una solitud de tipo POST.
     *
     * @param path String Ruta a la que se desea acceder.
     */
    public void post(String path, JSONObject datos ) {
        String[] urls = { prepareUrl( path ) };
        this.tipo = APIRequestType.POST;
        this.hasErrors = false;
        this.setBody( datos );
        this.execute( urls );
    }



    /**
     * Establece los datos al cuerpo de la solicitud.
     *
     * @param datos JSONObject Datos a enviar en el cuerpo de la solicitud.
     */
    public void setBody( JSONObject datos ) {
        this.datos = datos;
    }



    /**
     * Imprime en la consola el error.
     *
     */
    public void printError() {
        if ( error != null ) {
            Log.e( "Request", error.getMessage() + " : " + response_text );
        } else {
            Log.e( "Request", response_text );
        }
    }



    /**
     * Construye una url completa.
     *
     * @param path String Path ó Ruta en la API.
     * @return String
     */
    private String prepareUrl(String path ) {
        String API_BASE_URL = "http://104.236.201.168/API_mizton/";
        return API_BASE_URL + path;
    }



    /**
     * Retorna el tipo de verbo HTTP.
     *
     * @return String
     */
    private String getRequestMethod() {
        String method = "";
        switch ( this.tipo ) {
            case GET:
                method = "GET";
                break;
            case POST:
                method = "POST";
                break;
            case PUT:
                method = "PUT";
                break;
            case DELETE:
                method = "DELETE";
                break;
        }

        return method;
    }



    /**
     * Llamado antes de iniciar el doInBackground
     *
     * @author edmsamuel
     */
    public abstract void RequestBeforeExecute();



    /**
     * Es invocado al finalizar la tarea en segundo plano cuando no han ocurrido errores en la solicitud HTTP.
     *
     * @param response JSONObject Resultado recibido por el servidor en JSON.
     */
    public abstract void RequestCompleted(JSONObject response );



    /**
     * Es invocado en caso de ocurrir errores durante el envio de la solicutud.
     *
     * @param error Exception Resultado recibido por el servidor en JSON.
     */
    public abstract void RequestError(Exception error );
}