package www.miztonapp.mx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import www.miztonapp.mx.utilerias.Utils;

public class RegistroFibraOpticaActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialEditText edtDistrito, edtFolio, edtTelefono, edtTerminal, edtPuerto;
    private Button btnGuardar;

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

        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,  ESTATUS);
        MaterialBetterSpinner textView = (MaterialBetterSpinner) findViewById(R.id.sp_estatus);
        textView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnGuardar){
            try {
                if (!validaCampos()) {
                    throw new Exception("Error de captura de datos, verifica que no tengas formatos inválidos o campos vacios");
                }
            }catch (Exception e){
                Utils.crear_alerta(this, "Error", e.getMessage()).show();
            }
        }
    }

    private Boolean validaCampos(){
        edtFolio.validate();
        return edtFolio.isValid("\\d\\d\\d\\d\\d\\d\\d\\d\\d");
    }
}
