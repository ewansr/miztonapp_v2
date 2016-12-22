package www.miztonapp.mx;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.models.LoginModel;
import www.miztonapp.mx.models.ModelContratista;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.models.ModelTiposOrden;
import www.miztonapp.mx.requests.RequestContratista;
import www.miztonapp.mx.requests.RequestOrdenesTrabajo;
import www.miztonapp.mx.requests.RequestTiposOrden;
import www.miztonapp.mx.utilerias.FTPServerConfig;
import www.miztonapp.mx.utilerias.Utils;

public class RegistroFibraOpticaActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialEditText edtDistrito, edtFolio, edtTelefono, edtTerminal, edtPuerto, edtComentarios;
    private MaterialBetterSpinner spTiposOrden, spContratistas, spEstatus;
    private FancyButton btnGuardar;
    private String control_nombre;
    private String idContratista, idTipoOrden;

    private static final String[] ESTATUS = new String[] {
            "Liquidada", "Objetada", "Queja", "Retornada"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_fibra);

        edtDistrito = (MaterialEditText) findViewById(R.id.edtDistrito);
        edtDistrito.addValidator(new RegexpValidator("Formato inválido 'ABC1234'", "[A-Z^\\s][A-Z^\\s][A-Z^\\s]\\d\\d\\d+"));

        edtFolio = (MaterialEditText) findViewById(R.id.edtFolio);
        edtFolio.addValidator(new RegexpValidator("Formato inválido '123456789'", "\\d\\d\\d\\d\\d\\d\\d\\d\\d"));

        edtTelefono = (MaterialEditText) findViewById(R.id.edtTelefono);
        edtTelefono.addValidator(new RegexpValidator("Formato inválido '0123456789'", "\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d"));

        edtTerminal = (MaterialEditText) findViewById(R.id.edtTerminal);
        edtTerminal.addValidator(new RegexpValidator("Formato inválido 'A1'", "[A-Z^\\s]\\d"));

        edtPuerto = (MaterialEditText) findViewById(R.id.edtPuerto);
        edtPuerto.addValidator(new RegexpValidator("Formato inválido '1-9'", "\\d"));

        edtComentarios = (MaterialEditText) findViewById(R.id.edtComentarios);

        btnGuardar = (FancyButton) findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,  ESTATUS);
        spEstatus = (MaterialBetterSpinner) findViewById(R.id.sp_estatus);
        spEstatus.setAdapter(adapter);

        spTiposOrden = (MaterialBetterSpinner) findViewById(R.id.sp_tipo_orden);
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
                ArrayAdapter<ModelTiposOrden> adapter_tipo_orden = new ArrayAdapter<ModelTiposOrden>(RegistroFibraOpticaActivity.this,
                        android.R.layout.simple_dropdown_item_1line, items );
                spTiposOrden.setAdapter(adapter_tipo_orden);
                spTiposOrden.setText(setItemIndex(0, spTiposOrden));
                idTipoOrden = ((ModelTiposOrden)spTiposOrden.getAdapter().getItem(0)).Id;
            }

            @Override
            public void tOrdenCargaErronea(mException error) {
                Utils.crear_alerta(RegistroFibraOpticaActivity.this, "Error al obtener el set de datos", error.getMessage()).show();
            }
        }; catalogo_tipos_orden.getTiposOrden("Cobre");

        spContratistas = (MaterialBetterSpinner) findViewById(R.id.sp_contratista);

        spContratistas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                idContratista = ((ModelContratista)spContratistas.getAdapter().getItem(position)).Id;
            }
        });

        RequestContratista catalogo_contratista = new RequestContratista() {
            @Override
            public void BeforeLoad() {

            }

            @Override
            public void CargaExitosa(ArrayList<ModelContratista> items) {
                ArrayAdapter<ModelContratista> adapter_contratista = new ArrayAdapter<ModelContratista>(RegistroFibraOpticaActivity.this,
                        android.R.layout.simple_dropdown_item_1line, items);
                spContratistas.setAdapter(adapter_contratista);
                spContratistas.setText(setItemIndex(0, spContratistas));
                idContratista = ((ModelContratista)spContratistas.getAdapter().getItem(0)).Id;
            }

            @Override
            public void CargaErronea(mException error) {
                Utils.crear_alerta(RegistroFibraOpticaActivity.this, "Error al obtener el set de datos", error.getMessage()).show();;
            }
        };catalogo_contratista.getContratistas();

        spEstatus.setText(setItemIndex(0, spEstatus));

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_guardar){
            try {
                control_nombre = "\n";
                if (validaCampos()) {
                    throw new Exception("Verifica que no tengas formatos inválidos o campos vacios en los siguientes controles: " + control_nombre);
                }
                guardarOrden();
            }catch (Exception e){
                Utils.crear_alerta(this, "Error de captura", e.getMessage()).show();
            }
        }
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

        if (!edtTerminal.validate()){
            invalido = true;
            control_nombre = control_nombre + edtTerminal.getHint().toString() + "\n";
        }

        if (!edtPuerto.validate()){
            invalido = true;
            control_nombre = control_nombre + edtPuerto.getHint().toString() + "\n";
        }

        return invalido;
    }

    public String setItemIndex(int i, MaterialBetterSpinner spinner){
        return (spinner.getAdapter().getItem(i).toString());
    }

    public void guardarOrden(){

        LoginModel data_usuario;
        data_usuario = Utils.obtener_usuario(this);

        String folio = edtFolio.getText().toString();
        String FolioTelmex = data_usuario.folio_telmex;
        int IdPersonal = data_usuario.idpersonal;
        String Telefono = edtTelefono.getText().toString();
        String Principal = null;
        String Secundario = null;
        String TipoOs = spTiposOrden.getText().toString();
        String Distrito = edtDistrito.getText().toString();
        String Central = edtDistrito.getText().toString().substring(0,3);
        String Comentarios = edtComentarios.getText().toString();
        String Estatus = spEstatus.getText().toString();
        String IdTipo = idTipoOrden;
        String Terminal = edtTerminal.getText().toString();
        String Puerto = edtPuerto.getText().toString();
        String IdContratista = idContratista;

        RequestOrdenesTrabajo rOrdenTrabajo = new RequestOrdenesTrabajo() {
            @Override
            public void ordenBeforeLoad() {

            }

            @Override
            public void ordenCargaExitosa(ArrayList<ModelOrdenesTrabajo> items) {

            }

            @Override
            public void ordenCargaExitosa(String mensaje) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroFibraOpticaActivity.this);
                builder.setMessage(mensaje)
                        .setCancelable(false)
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void ordenCargaErronea(mException error) {
                Utils.crear_alerta(RegistroFibraOpticaActivity.this,"Aviso",error.getMessage()).show();
            }
        }; rOrdenTrabajo.guardar_orden("0",folio,FolioTelmex,Integer.toString(IdPersonal),Telefono,
                Principal,Secundario,TipoOs,Distrito,Central,Comentarios,Estatus,IdTipo,Terminal,
                Puerto,IdContratista);
    }
}
