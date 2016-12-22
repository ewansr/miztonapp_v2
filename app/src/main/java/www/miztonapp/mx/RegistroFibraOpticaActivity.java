package www.miztonapp.mx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.models.ModelContratista;
import www.miztonapp.mx.models.ModelTiposOrden;
import www.miztonapp.mx.requests.RequestContratista;
import www.miztonapp.mx.requests.RequestTiposOrden;
import www.miztonapp.mx.utilerias.Utils;

public class RegistroFibraOpticaActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialEditText edtDistrito, edtFolio, edtTelefono, edtTerminal, edtPuerto;
    private MaterialBetterSpinner spTiposOrden, spContratistas;
    private FancyButton btnGuardar;
    private String control_nombre;

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

        btnGuardar = (FancyButton) findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,  ESTATUS);
        MaterialBetterSpinner spEstatus = (MaterialBetterSpinner) findViewById(R.id.sp_estatus);
        spEstatus.setAdapter(adapter);

        spTiposOrden = (MaterialBetterSpinner) findViewById(R.id.sp_tipo_orden);

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
            }

            @Override
            public void tOrdenCargaErronea(mException error) {
                Utils.crear_alerta(RegistroFibraOpticaActivity.this, "Error al obtener el set de datos", error.getMessage()).show();
            }
        }; catalogo_tipos_orden.getTiposOrden("Cobre");

        spContratistas = (MaterialBetterSpinner) findViewById(R.id.sp_contratista);
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
}
