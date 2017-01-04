package www.miztonapp.mx;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.models.LoginModel;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.models.ModelTiposOrden;
import www.miztonapp.mx.requests.RequestOrdenesTrabajo;
import www.miztonapp.mx.requests.RequestTiposOrden;
import www.miztonapp.mx.utilerias.Utils;

public class RegistroCobreActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialEditText edtDistrito, edtFolio, edtTelefono, edtPrincipal, edtSecundario, edtComentarios;
    private MaterialBetterSpinner spEstatus,spTiposOrden;
    private String control_nombre;
    private FancyButton btnGuardar;
    private String idTipoOrden;
    private Bundle datos;
    private Context context;

    private static final String[] ESTATUS = new String[] {
            "Liquidada", "Objetada", "Queja", "Retornada"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cobre);

        context = RegistroCobreActivity.this;

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

        datos = getIntent().getBundleExtra("datos");

        edtFolio = (MaterialEditText) findViewById(R.id.edtFolio_co);
        edtFolio.addValidator(new RegexpValidator("Formato inválido '123456789'", "\\d\\d\\d\\d\\d\\d\\d\\d\\d"));

        edtTelefono = (MaterialEditText) findViewById(R.id.edtTelefono_co);
        edtTelefono.addValidator(new RegexpValidator("Formato inválido '0123456789'", "\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d"));

        edtDistrito = (MaterialEditText) findViewById(R.id.edtDistrito_co);
        edtDistrito.addValidator(new RegexpValidator("Formato inválido 'MOA2001'", "[A-Z^\\s][A-Z^\\s][A-Z^\\s]\\d\\d\\d+"));

        edtPrincipal = (MaterialEditText) findViewById(R.id.edtTerminal_co);
        edtPrincipal.addValidator(new RegexpValidator("Formato inválido '10/10'", "\\d+[/]\\d+"));

        edtSecundario = (MaterialEditText) findViewById(R.id.edtPuerto_co);
        edtSecundario.addValidator(new RegexpValidator("Formato inválido 'A1/10'", "[A-Z^\\s]\\d[/]\\d+"));

        edtComentarios = (MaterialEditText) findViewById(R.id.edtComentarios_co);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,  ESTATUS);
        spEstatus = (MaterialBetterSpinner) findViewById(R.id.sp_estatus_co);
        spEstatus.setAdapter(adapter);
        spEstatus.setText(setItemIndex(0, spEstatus));
        spEstatus.setFocusableInTouchMode(true);

        spTiposOrden = (MaterialBetterSpinner) findViewById(R.id.sp_tipoorden_co);
        spTiposOrden.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                idTipoOrden = ((ModelTiposOrden)spTiposOrden.getAdapter().getItem(position)).Id;
            }
        });
        RequestTiposOrden catalogo_tipos_orden = new RequestTiposOrden() {
            @Override
            public void tOrdenBeforeLoad() {

            }

            @Override
            public void tOrdenCargaExitosa(ArrayList<ModelTiposOrden> items) {
                ArrayAdapter<ModelTiposOrden> adapter_tipo_orden = new ArrayAdapter<ModelTiposOrden>(context,
                        android.R.layout.simple_dropdown_item_1line, items );
                spTiposOrden.setAdapter(adapter_tipo_orden);
                spTiposOrden.setText(setItemIndex(0, spTiposOrden));
                spTiposOrden.setFocusableInTouchMode(true);
                idTipoOrden = ((ModelTiposOrden)spTiposOrden.getAdapter().getItem(0)).Id;
                if(datos != null) {
                    spTiposOrden.setText(datos.getString("tipo"));
                    idTipoOrden = datos.getString("idtipo");
                }
            }

            @Override
            public void tOrdenCargaErronea(mException error) {
                Utils.crear_alerta(RegistroCobreActivity.this, "Error al obtener el set de datos", error.getMessage()).show();
            }
        }; catalogo_tipos_orden.getTiposOrden("Cobre");


        btnGuardar = (FancyButton) findViewById(R.id.btn_guardar_co);
        btnGuardar.setOnClickListener(this);

        // Despues de toda la configuración y desmadre
        if(datos != null) {
            spEstatus.setText(datos.getString("estatus"));
            edtFolio.setText(datos.getString("folio"));
            edtTelefono.setText(datos.getString("telefono"));
            edtDistrito.setText(datos.getString("distrito"));
            edtPrincipal.setText(datos.getString("principal"));
            edtSecundario.setText(datos.getString("secundario"));
            edtComentarios.setText(datos.getString("comentarios"));
        }

    }

    public String setItemIndex(int i, MaterialBetterSpinner spinner){
        return (spinner.getAdapter().getItem(i).toString());
    }

    private Boolean validaCampos(){
        Boolean invalido = false;
        if (!edtFolio.validate()){
            invalido = true;
            control_nombre = control_nombre + edtFolio.getHint().toString() + "\n";
        }

        if (!edtTelefono.validate()){
            invalido = true;
            control_nombre = control_nombre + edtTelefono.getHint().toString() + "\n";
        }

        if (!edtDistrito.validate()){
            invalido = true;
            control_nombre = control_nombre + edtDistrito.getHint().toString() + "\n";
        }

        if (!edtPrincipal.validate()){
            invalido = true;
            control_nombre = control_nombre + edtPrincipal.getHint().toString() + "\n";
        }

        if (!edtSecundario.validate()){
            invalido = true;
            control_nombre = control_nombre + edtSecundario.getHint().toString() + "\n";
        }

        return invalido;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_guardar_co){
            try {
                control_nombre = "\n";
                if (validaCampos()) {
                    throw new Exception("Verifica que no tengas formatos inválidos o campos vacios en los siguientes controles: " + control_nombre);
                }

                if(datos != null) {
                    editarOrden();
                }else {guardarOrden();}
            }catch (Exception e){
                Utils.crear_alerta(this, "Error de captura", e.getMessage()).show();
            }
        }
    }


    public void guardarOrden(){

        LoginModel data_usuario;
        data_usuario = Utils.obtener_usuario(context);

        String folio = edtFolio.getText().toString();
        String FolioTelmex = data_usuario.folio_telmex;
        int IdPersonal = data_usuario.idpersonal;
        String Telefono = edtTelefono.getText().toString();
        String Principal = edtPrincipal.getText().toString();
        String Secundario = edtSecundario.getText().toString();
        String TipoOs = spTiposOrden.getText().toString();
        String Distrito = edtDistrito.getText().toString();
        String Central = edtDistrito.getText().toString().substring(0,3);
        String Comentarios = edtComentarios.getText().toString();
        String Estatus = spEstatus.getText().toString();
        String IdTipo = idTipoOrden;
        String Terminal = null;
        String Puerto = null;
        String IdContratista = null;

        RequestOrdenesTrabajo rOrdenTrabajo = new RequestOrdenesTrabajo() {
            @Override
            public void ordenBeforeLoad() {

            }

            @Override
            public void ordenCargaExitosa(ArrayList<ModelOrdenesTrabajo> items) {

            }

            @Override
            public void ordenCargaExitosa(String mensaje) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(mensaje)
                        .setCancelable(false)
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result", "mrok");
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void ordenCargaErronea(mException error) {
                Utils.crear_alerta(context,"Aviso",error.getMessage()).show();
            }
        }; rOrdenTrabajo.guardar_orden("0",folio,FolioTelmex,Integer.toString(IdPersonal),Telefono,
                Principal,Secundario,TipoOs,Distrito,Central,Comentarios,Estatus,IdTipo,Terminal,
                Puerto,IdContratista);
    }

    public void editarOrden(){

        LoginModel data_usuario;
        data_usuario = Utils.obtener_usuario(context);

        String idfolio = datos.getString("id");
        String folio = edtFolio.getText().toString();
        String FolioTelmex = data_usuario.folio_telmex;
        int IdPersonal = data_usuario.idpersonal;
        String Telefono = edtTelefono.getText().toString();
        String Principal = edtPrincipal.getText().toString();
        String Secundario = edtSecundario.getText().toString();
        String TipoOs = spTiposOrden.getText().toString();
        String Distrito = edtDistrito.getText().toString();
        String Central = edtDistrito.getText().toString().substring(0,3);
        String Comentarios = edtComentarios.getText().toString();
        String Estatus = spEstatus.getText().toString();
        String IdTipo = idTipoOrden;
        String Terminal = null;
        String Puerto = null;
        String IdContratista = null;

        RequestOrdenesTrabajo rOrdenTrabajo = new RequestOrdenesTrabajo() {
            @Override
            public void ordenBeforeLoad() {

            }

            @Override
            public void ordenCargaExitosa(ArrayList<ModelOrdenesTrabajo> items) {

            }

            @Override
            public void ordenCargaExitosa(String mensaje) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(mensaje)
                        .setCancelable(false)
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result", "mrok");
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void ordenCargaErronea(mException error) {
                Utils.crear_alerta(context,"Aviso",error.getMessage()).show();
            }
        }; rOrdenTrabajo.editar_orden(idfolio,folio,FolioTelmex,Integer.toString(IdPersonal),Telefono,
                Principal,Secundario,TipoOs,Distrito,Central,Comentarios,Estatus,IdTipo,Terminal,
                Puerto,IdContratista);
    }

}
