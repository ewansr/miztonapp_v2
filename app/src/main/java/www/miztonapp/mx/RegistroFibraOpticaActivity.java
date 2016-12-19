package www.miztonapp.mx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

public class RegistroFibraOpticaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_fibra);

        MaterialEditText edtDistrito = (MaterialEditText) findViewById(R.id.edtDistrito);
        edtDistrito.addValidator(new RegexpValidator("Formato inválido 'ABC1234'", "[A-Z^\\s][A-Z^\\s][A-Z^\\s]\\d\\d\\d+"));

        MaterialEditText edtFolio = (MaterialEditText) findViewById(R.id.edtFolio);
        edtFolio.addValidator(new RegexpValidator("Formato inválido '123456789'", "\\d\\d\\d\\d\\d\\d\\d\\d\\d"));

        MaterialEditText edtTelefono = (MaterialEditText) findViewById(R.id.edtTelefono);
        edtTelefono.addValidator(new RegexpValidator("Formato inválido '0123456789'", "\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d"));

        MaterialEditText edtTerminal = (MaterialEditText) findViewById(R.id.edtTerminal);
        edtTerminal.addValidator(new RegexpValidator("Formato inválido 'A1'", "[A-Z^\\s]\\d"));

        MaterialEditText edtPuerto = (MaterialEditText) findViewById(R.id.edtPuerto);
        edtPuerto.addValidator(new RegexpValidator("Formato inválido '1-9'", "\\d"));

    }
}
