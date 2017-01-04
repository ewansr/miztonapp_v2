package www.miztonapp.mx;

import android.app.Activity;
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
import www.miztonapp.mx.models.ModelContratista;
import www.miztonapp.mx.models.ModelOrdenesTrabajo;
import www.miztonapp.mx.models.ModelTiposOrden;
import www.miztonapp.mx.requests.RequestContratista;
import www.miztonapp.mx.requests.RequestOrdenesTrabajo;
import www.miztonapp.mx.requests.RequestTiposOrden;
import www.miztonapp.mx.utilerias.Utils;

public class RegistroCobreActivity extends AppCompatActivity {
    private MaterialEditText edtDistrito, edtFolio, edtTelefono, edtTerminal, edtPuerto, edtComentarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_captura_co);


    }

    edtFolio = (MaterialEditText) findViewById(R.id.edtFolio_co);
    edtFolio.addValidator(new RegexpValidator("Formato inválido '123456789'", "\\d\\d\\d\\d\\d\\d\\d\\d\\d"));

    edtTelefono = (MaterialEditText) findViewById(R.id.edtTelefono_co);
    edtTelefono.addValidator(new RegexpValidator("Formato inválido '0123456789'", "\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d"));

    edtDistrito = (MaterialEditText) findViewById(R.id.edtDistrito_co);
    edtDistrito.addValidator(new RegexpValidator("Formato inválido 'MOA2001'", "[A-Z^\\s][A-Z^\\s][A-Z^\\s]\\d\\d\\d+"));



    edtTerminal = (MaterialEditText) findViewById(R.id.edtTerminal);
    edtTerminal.addValidator(new RegexpValidator("Formato inválido 'A1/10'", "[A-Z^\\s]\\p\\d\\d"));

    edtComentarios = (MaterialEditText) findViewById(R.id.edtComentarios_co);
}
